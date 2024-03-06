package net.sweenus.simplyskills.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.entities.ai.DirectionalFlightMoveControl;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.UUID;

public class WraithEntity extends TameableEntity implements Angerable, Flutterer {
    public static int lifespan = 2400;
    public static Entity lookTarget = null;
    public WraithEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new DirectionalFlightMoveControl(this, 1, true);
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createWraithAttributes() {
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

            this.doesNotCollide(0, 0, 0);

            if (!this.hasNoGravity()) {
                this.setNoGravity(true);
            }

            this.prevPitch = this.getPitch();
            this.prevYaw = this.getYaw();

            if (this.getTarget() == null && this.getOwner() != null)
                this.setPositionTarget(this.getOwner().getBlockPos().up(3), 32);
            else if (this.getTarget() != null && this.getOwner() != null && !this.getTarget().equals(this.getOwner()) && this.distanceTo(this.getOwner()) > 10)
                this.setPositionTarget(this.getOwner().getBlockPos().up(3), 32);

            Box box = HelperMethods.createBoxHeight(this, 16);
            int frequency = (20+ this.getRandom().nextInt(30));
            if (this.age % frequency == 0 && this.getOwner() != null && this.getOwner().isAlive() && this.getOwner() instanceof PlayerEntity player) {
                World world = this.getWorld();
                Entity closestEntity = world.getOtherEntities(this, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                        .filter(entity -> !(entity instanceof TameableEntity tameableEntity &&
                                tameableEntity.isTamed() &&
                                tameableEntity.getOwnerUuid() != null &&
                                tameableEntity.getOwnerUuid().equals(this.getOwnerUuid())))
                        .filter(entity -> !(entity instanceof PlayerEntity playerEntity &&
                                playerEntity.getUuid().equals(this.getOwnerUuid())))
                        .min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(this)))
                        .orElse(null);

                if (closestEntity != null) {
                    if ((closestEntity instanceof LivingEntity ee)) {
                        if (HelperMethods.checkFriendlyFire(ee, player)) {

                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:minion_soul_spell", 32, ee, null);
                            HelperMethods.spawnWaistHeightParticles((ServerWorld) world, ParticleTypes.SMOKE, this, ee, 20);
                            lookTarget = ee;

                            return;
                        }
                    }
                }
            }
            if (lookTarget != null) {
                this.lookAtEntity(lookTarget, 90f, 10f);
                Vec3d direction = new Vec3d(lookTarget.getX() - this.getX(), 0, lookTarget.getZ() - this.getZ());
                // Calculate the yaw angle towards the look target (in degrees)
                float targetYaw = (float)(MathHelper.atan2(direction.z, direction.x) * (180 / Math.PI)) - 90.0F;
                this.setBodyYaw(targetYaw);
                this.headYaw = targetYaw;
            }
        }

        super.tick();
    }

    @Override
    public boolean tryAttack(Entity target) {
        MoveControl moveControl = this.getMoveControl();
        if (moveControl instanceof DirectionalFlightMoveControl) {
            ((DirectionalFlightMoveControl) moveControl).onAttack();
        }
        target.timeUntilRegen = 0;
        return super.tryAttack(target);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new FollowOwnerGoal(this, 1.0D, 8.0F, 12.0F, true));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        //this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        //this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        //this.targetSelector.add(3, new RevengeGoal(this));
        //this.targetSelector.add(4, new ActiveTargetGoal<>(this, HostileEntity.class, false));

    }

    @Override
    public void tickMovement() {
        super.tickMovement();
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


