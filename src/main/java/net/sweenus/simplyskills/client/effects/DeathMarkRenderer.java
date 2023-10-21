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


public class DeathMarkRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId_overlay = new Identifier(SimplySkills.MOD_ID, "effect/death_mark");


    private static final RenderLayer OVERLAY_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.GLOW, false);


    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        float entitySize = livingEntity.getWidth() / 2;
        float yOffset = 0.90F +entitySize;
        float time = livingEntity.getWorld().getTime() + delta;
        float speed = 4F;
        boolean prone = livingEntity.isFallFlying() || livingEntity.isCrawling() || livingEntity.isInSwimmingPose();

        float overlayScale = 1.9F + entitySize;
        matrixStack.push();
        matrixStack.translate(0, yOffset, 0);
        if (prone)
            overlayScale = 0;
        matrixStack.scale(overlayScale, overlayScale, overlayScale);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * speed - 45.0F));

        CustomModels.render(OVERLAY_RENDER_LAYER, MinecraftClient.getInstance().getItemRenderer(), modelId_overlay,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
