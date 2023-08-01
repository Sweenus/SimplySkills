package net.sweenus.simplyskills.mixins.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.WorldView;
import net.spell_engine.api.effect.Synchronized;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    //Prevents shadow flickering visible when effects are reapplied

    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void simplyskills$renderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            if (Synchronized.effectsOf(livingEntity).toString().contains("StealthEffect")) {
                ci.cancel();
            }
        }
    }
}