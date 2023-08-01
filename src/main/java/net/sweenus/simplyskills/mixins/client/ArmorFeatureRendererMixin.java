package net.sweenus.simplyskills.mixins.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.Synchronized;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {



    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public void simplyskills$renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                         LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i,
                                         BipedEntityModel<LivingEntity> bipedEntityModel, CallbackInfo ci) {

        // Status Effects are not synchronised correctly between clients in a server environment.
        // We piggyback off Daedelus' SpellEngine synchronisation technique to get around this issue.

        if(livingEntity.isInvisible()) {
            //System.out.println("List of synchronised effects: " + Synchronized.effectsOf(livingEntity));
            if (Synchronized.effectsOf(livingEntity).toString().contains("StealthEffect")) {
                ci.cancel();
            }
        }
    }

}
