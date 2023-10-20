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


public class BarrierRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId_base = new Identifier(SimplySkills.MOD_ID, "effect/barrier");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);


    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        float yOffset = 0.68F;
        float proneOffset = 0.30F; // Things get weird when prone, and render code hurts my head
        boolean prone = livingEntity.isFallFlying() || livingEntity.isCrawling() || livingEntity.isInSwimmingPose();
        matrixStack.push();

        matrixStack.translate(0, yOffset, 0);
        if (prone)
            matrixStack.translate(0, -proneOffset, 0);

        // Apply rotation to match the entity's orientation
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-livingEntity.bodyYaw));
        if (prone)
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(livingEntity.getPitch() + 90));

        CustomModels.render(BASE_RENDER_LAYER, MinecraftClient.getInstance().getItemRenderer(), modelId_base,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
