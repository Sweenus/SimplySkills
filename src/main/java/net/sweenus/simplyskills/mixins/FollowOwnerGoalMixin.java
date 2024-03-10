package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.sweenus.simplyskills.entities.DreadglareEntity;
import net.sweenus.simplyskills.entities.GreaterDreadglareEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FollowOwnerGoal.class)
public abstract class FollowOwnerGoalMixin {

    @Shadow @Nullable private TameableEntity tameable;

    protected FollowOwnerGoalMixin(@Nullable TameableEntity tameable) {
        this.tameable = tameable;
    }

    @Inject(at = @At("HEAD"), method = "tryTeleport", cancellable = true)
    public void simplyskills$canTarget(CallbackInfo ci) {
        FollowOwnerGoal followOwnerGoal = (FollowOwnerGoal) (Object)this;
        if (!tameable.getWorld().isClient()) {
            if (tameable instanceof DreadglareEntity || tameable instanceof GreaterDreadglareEntity) {
                if (tameable.getOwner() !=null && tameable.squaredDistanceTo(tameable.getOwner()) < 576)
                    ci.cancel();
            }
        }
    }
}
