package net.sweenus.simplyskills.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.NecromancerAbilities;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.entities.ai.DirectionalFlightMoveControl;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
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
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10.0);
    }
    @Override
    public void tick() {
        if (!this.getWorld().isClient()) {
            boolean ownerNotInWorld = true;

            if (this.getOwnerUuid() != null) {
                PlayerEntity owner = this.getWorld().getPlayerByUuid(this.getOwnerUuid());
                ownerNotInWorld = (owner == null || !owner.isAlive());
            }

            if (this.age > lifespan || (this.age > 120 && (this.getOwner() == null || ownerNotInWorld))) {
                this.damage(this.getDamageSources().generic(), this.getMaxHealth());
                this.remove(RemovalReason.UNLOADED_WITH_PLAYER);
            }

            this.doesNotCollide(0, 0, 0);

            if (!this.hasNoGravity()) {
                this.setNoGravity(true);
            }

            this.prevPitch = this.getPitch();
            this.prevYaw = this.getYaw();

            if (this.getTarget() == null && this.getOwner() != null)
                this.setTarget(this.getOwner());
            else if (this.getTarget() != null && !this.getTarget().equals(this.getOwner()) && this.distanceTo(this.getTarget()) > 20)
                this.setTarget(this.getOwner());

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
                    if ((closestEntity instanceof LivingEntity ee) && !(closestEntity instanceof PassiveEntity)) {
                        if (HelperMethods.checkFriendlyFire(ee, player)) {

                            if (HelperMethods.isUnlocked("simplyskills:necromancer", SkillReferencePosition.necromancerSpecialisationWitherWraiths, player))
                                SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:minion_soul_spell_wither", 32, ee, null);
                            else if (HelperMethods.isUnlocked("simplyskills:necromancer", SkillReferencePosition.necromancerSpecialisationFrostWraiths, player))
                                SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:minion_soul_spell_frost", 32, ee, null);
                            else
                                SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:minion_soul_spell", 32, ee, null);

                            HelperMethods.spawnWaistHeightParticles((ServerWorld) world, ParticleTypes.SMOKE, this, ee, 20);
                            lookTarget = ee;

                            int chance = this.getRandom().nextInt(100);
                            int chanceCheck = HelperMethods.countHarmfulStatusEffects(this) * 5;

                            if (chance < chanceCheck && HelperMethods.isUnlocked("simplyskills:necromancer", SkillReferencePosition.necromancerSpecialisationWraithLegion, player)) {
                                SimplyStatusEffectInstance agonyEffect = new SimplyStatusEffectInstance(
                                        EffectRegistry.AGONY, 200 + AscendancyAbilities.getAscendancyPoints(player), 0, false,
                                        false, true);
                                agonyEffect.setSourceEntity(player);
                                ee.addStatusEffect(agonyEffect);
                                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_04,
                                        SoundCategory.PLAYERS, 0.1f, 1.0f);
                            }

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
    public boolean damage(DamageSource source, float amount) {
        if (Objects.equals(source.getAttacker(), this.getOwner()))
            return false;
        return super.damage(source, amount);
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
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }
    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.getWorld().isClient() && this.getOwner() != null && this.getOwner() instanceof PlayerEntity player) {
            NecromancerAbilities.effectNecromancerEnrage(this, player);
            NecromancerAbilities.effectNecromancerDeathEssence(player);
            NecromancerAbilities.effectShadowCombust(player, this);
            NecromancerAbilities.effectEndlessServitude(player, this);
        }
        super.onDeath(damageSource);
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


