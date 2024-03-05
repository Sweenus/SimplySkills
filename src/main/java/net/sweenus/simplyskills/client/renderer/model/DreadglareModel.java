package net.sweenus.simplyskills.client.renderer.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sweenus.simplyskills.entities.DreadglareEntity;

public class DreadglareModel extends EntityModel<DreadglareEntity> {
	private final ModelPart bb_main;
	public DreadglareModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -8.0F, -4.0F, 6.0F, 6.0F, 5.0F, new Dilation(0.0F))
		.uv(10, 11).cuboid(-2.0F, -7.0F, 1.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 11).cuboid(1.0F, -7.0F, 2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(8, 16).cuboid(-1.0F, -7.0F, 2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 2).cuboid(-1.0F, -6.0F, 2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(17, 0).cuboid(0.0F, -4.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 11).cuboid(1.0F, -5.0F, 2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(13, 16).cuboid(-1.0F, -5.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 16).cuboid(-2.0F, -4.0F, 2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-2.0F, -7.0F, 2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(5, 16).cuboid(-2.0F, -6.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(DreadglareEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}