package net.sweenus.simplyskills.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.sweenus.simplyskills.entities.ai.DirectionalFlightMoveControl;
import net.sweenus.simplyskills.util.HelperMethods;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DreadglareEntity extends TameableEntity implements Angerable, Flutterer {
    public static int lifespan = 2400;
    private float targetYaw;
    private float targetPitch;
    private static final float TURN_SPEED = 5.0F; // Adjust this value for faster or slower turning
    public DreadglareEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new DirectionalFlightMoveControl(this, 1, true);
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createDreadglareAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1.6f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }
    @Override
    public void tick() {
        if (this.age > lifespan || (this.age > 120 && this.getOwner() == null))
            this.discard();

        double groundDistance = getGroundDistance();

        //if (!this.getWorld().isClient && groundDistance <= 5.0) {
        //    this.setVelocity(this.getVelocity().add(0, 0.1, 0)); // Adjust the upward velocity as needed
        //    this.velocityModified = true;
        //}

        if (!this.hasNoGravity()) {
            this.setNoGravity(true);
        }

        this.prevPitch = this.getPitch();
        this.prevYaw = this.getYaw();

        super.tick();
    }

    private double getGroundDistance() {
        BlockPos pos = this.getBlockPos();
        while (pos.getY() > 0 && !this.getWorld().getBlockState(pos).isSolidBlock(this.getWorld(), pos)) {
            pos = pos.down();
        }
        return this.getY() - pos.getY();
    }

    private float smoothRotation(float from, float to, float turnSpeed) {
        float f = MathHelper.wrapDegrees(to - from);
        if (f > turnSpeed) {
            f = turnSpeed;
        }
        if (f < -turnSpeed) {
            f = -turnSpeed;
        }
        return from + f;
    }

    // Call this method to set a new target rotation for the entity
    public void setTargetRotation(float yaw, float pitch) {
        this.targetYaw = yaw;
        this.targetPitch = pitch;
    }

    @Override
    public boolean tryAttack(Entity target) {
        target.timeUntilRegen = 0;
        MoveControl moveControl = this.getMoveControl();
        if (moveControl instanceof DirectionalFlightMoveControl) {
            ((DirectionalFlightMoveControl) moveControl).onAttack();
        }
        return super.tryAttack(target);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));

        // Goals for attacking hostile entities
        this.goalSelector.add(4, new AttackWithOwnerGoal(this));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 0.6D, true));

        // Goal for wandering around if the entity has no tasks
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));

        // Target selector for hostile entities
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
            // Override methods as necessary for your entity
        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }
}


