package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    //Prevent detection when stealthed
    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    public void simplyskills$canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target.hasStatusEffect(EffectRegistry.STEALTH))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "isDead", cancellable = true)
    public void simplyskills$tick(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (livingEntity.getHealth() <= 0.0F && livingEntity.hasStatusEffect(EffectRegistry.UNDYING)) {
            livingEntity.setHealth(1.0F);
            cir.setReturnValue(false);
        }

    }
}