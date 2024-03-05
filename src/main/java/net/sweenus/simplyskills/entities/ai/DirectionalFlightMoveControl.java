package net.sweenus.simplyskills.entities.ai;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.sweenus.simplyskills.util.HelperMethods;

public class DirectionalFlightMoveControl extends MoveControl {
    private final int maxPitchChange;
    private final boolean noGravity;
    private final MobEntity entity;
    private final double minAltitudeAboveGround = 4.0; // Minimum altitude above the ground
    private long lastAttackTime = 0; // Timestamp of the last attack
    private static final long ATTACK_COOLDOWN = 5000; // 2 seconds cooldown after attack
    private double yOffset = 0;

    public DirectionalFlightMoveControl(MobEntity entity, int maxPitchChange, boolean noGravity) {
        super(entity);
        this.maxPitchChange = maxPitchChange;
        this.noGravity = noGravity;
        this.entity = entity;
    }

    @Override
    public void tick() {
        if (this.noGravity) {
            this.entity.setNoGravity(true);
        }

        if (this.entity.getTarget() != null)
            yOffset = 0;
        else yOffset = 4;

        if (this.state == MoveControl.State.MOVE_TO) {
            this.state = MoveControl.State.WAIT;

            if (this.entity.getTarget() == null) {
                // Choose a random nearby position to move to
                this.targetX = this.entity.getX() + this.entity.getRandom().nextGaussian() * 5;
                this.targetY = this.entity.getY() + this.entity.getRandom().nextGaussian() * 5;
                this.targetZ = this.entity.getZ() + this.entity.getRandom().nextGaussian() * 5;
            } else {
                this.targetX = this.entity.getTarget().getX();
                this.targetY = this.entity.getTarget().getY();
                this.targetZ = this.entity.getTarget().getZ();
            }
            this.targetY += Math.sin(this.entity.age * 0.3) * 0.5;

            double d = this.targetX - this.entity.getX();
            double e = (this.targetY+yOffset) - this.entity.getY();
            double f = this.targetZ - this.entity.getZ();
            double g = d * d + e * e + f * f;

            if (g < 2.500000277905201E-7) {
                this.entity.setUpwardSpeed(0.0f);
                this.entity.setForwardSpeed(0.0f);
                // Choose a random nearby position to move to
                this.targetX = this.entity.getX() + this.entity.getRandom().nextGaussian() * 5;
                this.targetY = this.entity.getY() + this.entity.getRandom().nextGaussian() * 5;
                this.targetZ = this.entity.getZ() + this.entity.getRandom().nextGaussian() * 5;
                return;
            }

            float h = (float)(MathHelper.atan2(f, d) * (180.0 / Math.PI)) - 90.0f;
            this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), h, 90.0f));

            float i = this.entity.isOnGround() ? (float)(this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)) : (float)(this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED));
            this.entity.setMovementSpeed(i);

            double j = Math.sqrt(d * d + f * f);

            // Check if the entity has an attack target or is within the cooldown period after an attack
            boolean recentlyAttacked = this.entity.getTarget() != null && (System.currentTimeMillis() - lastAttackTime) > ATTACK_COOLDOWN;
            double groundDistance = HelperMethods.getGroundDistance(this.entity);

            // Check if the entity is too close to the ground
            if (groundDistance < this.minAltitudeAboveGround && !recentlyAttacked) {
                // Adjust targetY to move upwards smoothly
                this.targetY = this.entity.getY() + (this.minAltitudeAboveGround - groundDistance);
                e = this.targetY - this.entity.getY();
            }

            if (Math.abs(e) > 1.0E-5f || Math.abs(j) > 1.0E-5f) {
                float k = (float)(-(MathHelper.atan2(e, j) * (180.0 / Math.PI)));
                this.entity.setPitch(this.wrapDegrees(this.entity.getPitch(), k, this.maxPitchChange));
                this.entity.setUpwardSpeed(e > 0.0 ? i : -i);
            }
        } else {
            this.entity.setUpwardSpeed(0.1f);
            Vec3d velocity = this.entity.getVelocity();
            this.entity.setPitch((float) (-(MathHelper.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z)) * (180.0 / Math.PI))));
            this.entity.setForwardSpeed(0.1f);
            this.entity.setYaw((float) (MathHelper.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)) - 90.0F);
        }
    }
    // Call this method when the entity hits an attack target
    public void onAttack() {
        this.lastAttackTime = System.currentTimeMillis();
    }
}