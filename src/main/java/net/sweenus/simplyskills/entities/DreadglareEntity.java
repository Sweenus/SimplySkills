package net.sweenus.simplyskills.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.sweenus.simplyskills.abilities.NecromancerAbilities;
import net.sweenus.simplyskills.entities.ai.DirectionalFlightMoveControl;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DreadglareEntity extends TameableEntity implements Angerable, Flutterer {
    public static int lifespan = 2400;
    public DreadglareEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new DirectionalFlightMoveControl(this, 1, true);
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createDreadglareAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1.6f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }
    @Override
    public void tick() {
        if (!this.getWorld().isClient()) {
            if (this.age > lifespan || (this.age > 120 && this.getOwner() == null))
                this.damage(this.getDamageSources().generic(), this.getMaxHealth());


            if (!this.hasNoGravity()) {
                this.setNoGravity(true);
            }

            this.prevPitch = this.getPitch();
            this.prevYaw = this.getYaw();

            if (this.getTarget() == null && this.getOwner() != null)
                this.setTarget(this.getOwner());
            else if (this.getTarget() != null && !this.getTarget().equals(this.getOwner()) && this.distanceTo(this.getTarget()) > 10)
                this.setTarget(this.getOwner());
        }

        super.tick();
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public boolean tryAttack(Entity target) {
        MoveControl moveControl = this.getMoveControl();
        if (moveControl instanceof DirectionalFlightMoveControl) {
            ((DirectionalFlightMoveControl) moveControl).onAttack();
        }

        // Necromancer Blood Harvest
        if (this.getOwner() != null && this.getOwner() instanceof PlayerEntity player) {

            if (target.equals(player))
                return false;

            float siphonAmount = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.3f;
            if (HelperMethods.isUnlocked("simplyskills:necromancer", SkillReferencePosition.necromancerSpecialisationBloodHarvest, player)) {
                this.heal(siphonAmount);
                player.heal(siphonAmount / 2);
            }
        }

        target.timeUntilRegen = 0;
        return super.tryAttack(target);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, true));
        this.goalSelector.add(2, new AttackWithOwnerGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, HostileEntity.class, false));

    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        Vec3d velocity = this.getVelocity();
        if (!velocity.equals(Vec3d.ZERO)) {
            float yaw = (float) (MathHelper.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)) - 90.0F;
            float pitch = (float) (-(MathHelper.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z)) * (180.0 / Math.PI)));

            this.setYaw(yaw);
            this.bodyYaw = yaw;
            this.setPitch(pitch);

        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        //Necromancer Enrage
        if (this.getOwner() != null && this.getOwner() instanceof PlayerEntity player)
            NecromancerAbilities.effectNecromancerEnrage(this, player);
        //Necromancer Death Essence
        if (this.getOwner() != null && this.getOwner() instanceof PlayerEntity player)
            NecromancerAbilities.effectNecromancerDeathEssence(player);
        super.onDeath(damageSource);
    }

    //I think this is just Entity.getWorld()? What even are mappings
    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Override
    public int getAngerTime() {
        return 0;
    }

    @Override
    public void setAngerTime(int angerTime) {

    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return null;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {

    }

    @Override
    public void chooseRandomAngerTime() {

    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean isInAir() {
        return true;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        // Do not call super to prevent fall damage
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        // Return false to prevent fall damage
        return false;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {

        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(false);
        return birdNavigation;
    }
}


