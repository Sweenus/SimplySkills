package net.sweenus.simplyskills.client.renderer.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sweenus.simplyskills.entities.WraithEntity;

// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class WraithModel extends EntityModel<WraithEntity> {
	private final ModelPart all;
	private final ModelPart hands;
	private final ModelPart spine;
	public WraithModel(ModelPart root) {
		this.all = root.getChild("all");
		this.hands = root.getChild("hands");
		this.spine = root.getChild("spine");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData weapon = modelPartData.addChild("weapon", ModelPartBuilder.create().uv(46, 25).cuboid(4.0F, -18.0F, -10.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 37).cuboid(5.0F, -15.0F, -9.0F, 1.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create().uv(19, 21).cuboid(-3.0F, -16.0F, -4.0F, 6.0F, 6.0F, 5.0F, new Dilation(0.0F))
				.uv(6, 44).cuboid(-1.0F, -10.0F, 1.0F, 2.0F, 9.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 13).cuboid(-3.0F, -10.0F, 1.0F, 2.0F, 8.0F, 1.0F, new Dilation(0.0F))
				.uv(44, 44).cuboid(1.0F, -10.0F, 1.0F, 2.0F, 7.0F, 1.0F, new Dilation(0.0F))
				.uv(47, 13).cuboid(-4.0F, -10.0F, 1.0F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F))
				.uv(12, 44).cuboid(3.0F, -10.0F, 1.0F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
				.uv(38, 38).cuboid(3.0F, -10.0F, -4.0F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 13).cuboid(4.0F, -10.0F, -8.0F, 3.0F, 4.0F, 9.0F, new Dilation(0.0F))
				.uv(0, 29).cuboid(6.0F, -6.0F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(34, 14).cuboid(6.0F, -6.0F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(16, 32).cuboid(4.0F, -6.0F, -8.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 26).cuboid(5.0F, -6.0F, -7.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-7.0F, -10.0F, -8.0F, 3.0F, 4.0F, 9.0F, new Dilation(0.0F))
				.uv(15, 13).cuboid(-6.0F, -6.0F, -7.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 16).cuboid(-6.0F, -6.0F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 18).cuboid(-6.0F, -6.0F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(19, 7).cuboid(-5.0F, -6.0F, -6.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(41, 14).cuboid(3.0F, -10.0F, -2.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
				.uv(16, 44).cuboid(3.0F, -10.0F, 0.0F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F))
				.uv(47, 23).cuboid(-4.0F, -10.0F, 0.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 38).cuboid(-4.0F, -10.0F, -2.0F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
				.uv(32, 40).cuboid(-4.0F, -10.0F, -4.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
				.uv(44, 34).cuboid(-4.0F, -10.0F, -5.0F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F))
				.uv(41, 24).cuboid(-3.0F, -10.0F, -5.0F, 2.0F, 9.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-1.0F, -10.0F, -5.0F, 2.0F, 8.0F, 1.0F, new Dilation(0.0F))
				.uv(26, 40).cuboid(1.0F, -10.0F, -5.0F, 2.0F, 10.0F, 1.0F, new Dilation(0.0F))
				.uv(48, 0).cuboid(3.0F, -10.0F, -5.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
				.uv(22, 32).cuboid(-4.0F, -17.0F, 1.0F, 8.0F, 7.0F, 1.0F, new Dilation(0.0F))
				.uv(34, 7).cuboid(-3.0F, -18.0F, 2.0F, 6.0F, 6.0F, 1.0F, new Dilation(0.0F))
				.uv(8, 26).cuboid(-2.0F, -18.0F, 3.0F, 4.0F, 5.0F, 1.0F, new Dilation(0.0F))
				.uv(24, 7).cuboid(-2.0F, -17.0F, 4.0F, 4.0F, 5.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 0).cuboid(-1.0F, -16.0F, 5.0F, 2.0F, 5.0F, 1.0F, new Dilation(0.0F))
				.uv(8, 32).cuboid(-4.0F, -16.0F, -5.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
				.uv(0, 26).cuboid(3.0F, -16.0F, -5.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
				.uv(15, 0).cuboid(-4.0F, -17.0F, -5.0F, 8.0F, 1.0F, 6.0F, new Dilation(0.0F))
				.uv(15, 13).cuboid(-3.0F, -18.0F, -5.0F, 6.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData hands = modelPartData.addChild("hands", ModelPartBuilder.create().uv(18, 15).cuboid(-7.0F, -9.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-7.0F, -9.0F, -10.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-5.0F, -9.0F, -10.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-7.0F, -9.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-5.0F, -9.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-7.0F, -8.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-5.0F, -8.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-5.0F, -10.0F, -11.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-7.0F, -10.0F, -11.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-6.0F, -9.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-6.0F, -8.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(-5.0F, -9.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(4.0F, -9.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(4.0F, -9.0F, -10.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(6.0F, -9.0F, -10.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(4.0F, -9.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(6.0F, -9.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(4.0F, -8.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(6.0F, -8.0F, -12.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(6.0F, -10.0F, -11.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(4.0F, -10.0F, -11.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(5.0F, -9.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(5.0F, -8.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 15).cuboid(6.0F, -9.0F, -9.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData spine = modelPartData.addChild("spine", ModelPartBuilder.create().uv(22, 40).cuboid(-0.5F, -10.0F, -1.0F, 1.0F, 12.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(-1.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(-1.5F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(0.5F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(0.5F, -5.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(-1.5F, -6.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(-1.5F, -4.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(15, 7).cuboid(0.5F, -7.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(WraithEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		all.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		hands.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		spine.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}