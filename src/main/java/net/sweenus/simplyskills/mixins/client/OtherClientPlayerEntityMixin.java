package net.sweenus.simplyskills.mixins.client;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OtherClientPlayerEntity.class, priority = 500)
public abstract class OtherClientPlayerEntityMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void simplyskills$shouldRender(double distance, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        if (player != null) {
            if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                player.setInvisible(true);
                cir.setReturnValue(false);
            }
        }
    }

}
