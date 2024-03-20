package net.sweenus.simplyskills.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.sweenus.simplyskills.client.SimplySkillsClient;
import net.sweenus.simplyskills.client.renderer.model.DreadglareModel;
import net.sweenus.simplyskills.entities.DreadglareEntity;


@Environment(value= EnvType.CLIENT)
public class DreadglareRenderer extends MobEntityRenderer<DreadglareEntity, DreadglareModel> {


     private static final Identifier TEXTURE = new Identifier("simplyskills","textures/entity/dreadglare.png");

    public DreadglareRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DreadglareModel(ctx.getPart(SimplySkillsClient.DREADGLARE_MODEL)), 1.0f);
    }

    @Override
    public Identifier getTexture(DreadglareEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(DreadglareEntity dreadglareEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLightIn) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(dreadglareEntity.getPitch(partialTicks)));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(dreadglareEntity.getYaw(partialTicks)));

        super.render(dreadglareEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLightIn);

        matrixStack.pop();
    }
}
