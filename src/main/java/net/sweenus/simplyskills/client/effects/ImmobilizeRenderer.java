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
import net.spell_engine.api.render.CustomModels;
import net.sweenus.simplyskills.SimplySkills;


public class ImmobilizeRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId_base = new Identifier(SimplySkills.MOD_ID, "effect/immobilize");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);


    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        float yOffset = 0.50F;
        float scale = 1.2F;
        float proneOffset = 0.55F; // Things get weird when prone, and render code hurts my head
        boolean prone = livingEntity.isFallFlying() || livingEntity.isCrawling() || livingEntity.isInSwimmingPose();
        matrixStack.push();

        matrixStack.translate(0, yOffset, 0);
        if (prone)
            scale = 0.0F;

        // Apply rotation to match the entity's orientation
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-livingEntity.bodyYaw));
        if (prone)
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(livingEntity.getPitch() + 90));
        matrixStack.scale(scale, scale, scale);

        CustomModels.render(BASE_RENDER_LAYER, MinecraftClient.getInstance().getItemRenderer(), modelId_base,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
