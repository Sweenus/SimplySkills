package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;
import net.sweenus.simplyskills.SimplySkills;


public class TauntedRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId_overlay = new Identifier(SimplySkills.MOD_ID, "effect/taunted");

    private static final RenderLayer OVERLAY_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.GLOW, false);


    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        float entitySize = livingEntity.getHeight();
        float yOffset = 0F +entitySize - 0.40F;

        float overlayScale = 1.2F;
        matrixStack.push();
        matrixStack.translate(0, yOffset, 0);
        matrixStack.scale(overlayScale, overlayScale, overlayScale);

        CustomModels.render(OVERLAY_RENDER_LAYER, MinecraftClient.getInstance().getItemRenderer(), modelId_overlay,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
