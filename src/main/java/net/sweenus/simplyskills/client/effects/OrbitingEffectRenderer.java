package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

public class OrbitingEffectRenderer implements CustomModelStatusEffect.Renderer {
    public float speed = 16.25F;
    private final List<Model> models;
    private final float scale;
    private final float horizontalOffset;

    public void setSpeed (float newSpeed) {
        speed = newSpeed;
    }

    public OrbitingEffectRenderer(List<Model> models, float scale, float horizontalOffset) {
        this.models = models;
        this.scale = scale;
        this.horizontalOffset = horizontalOffset;
    }

    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        float time = livingEntity.getWorld().getTime() + delta;
        float initialAngle = time * speed - 45.0F;
        float horizontalOffset = this.horizontalOffset * livingEntity.getScaleFactor();
        float verticalOffset = livingEntity.getHeight() / 2.0F;
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        int stacks = amplifier + 1;
        float turnAngle = 360.0F / stacks;

        for(int i = 0; i < stacks; ++i) {
            float angle = initialAngle + turnAngle * i;
            renderModel(matrixStack, this.scale, verticalOffset, horizontalOffset, angle, itemRenderer, vertexConsumers, light, livingEntity);
        }

        matrixStack.pop();
    }

    private void renderModel(MatrixStack matrixStack, float scale, float verticalOffset, float horizontalOffset, float rotation, ItemRenderer itemRenderer, VertexConsumerProvider vertexConsumers, int light, LivingEntity livingEntity) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        matrixStack.translate(0.0F, verticalOffset, -horizontalOffset);
        matrixStack.scale(scale, scale, scale);

        for(Model model : models) {
            matrixStack.push();
            CustomModels.render(model.layer(), itemRenderer, model.modelId(), matrixStack, vertexConsumers, light, livingEntity.getId());
            matrixStack.pop();
        }

        matrixStack.pop();
    }

    public static record Model(RenderLayer layer, Identifier modelId) {
        public Model(RenderLayer layer, Identifier modelId) {
            this.layer = layer;
            this.modelId = modelId;
        }

        public RenderLayer layer() {
            return this.layer;
        }

        public Identifier modelId() {
            return this.modelId;
        }
    }
}