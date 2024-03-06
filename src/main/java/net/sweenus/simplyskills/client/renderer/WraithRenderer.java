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
import net.sweenus.simplyskills.client.renderer.model.WraithModel;
import net.sweenus.simplyskills.entities.DreadglareEntity;
import net.sweenus.simplyskills.entities.WraithEntity;


@Environment(value= EnvType.CLIENT)
public class WraithRenderer extends MobEntityRenderer<WraithEntity, WraithModel> {


     private static final Identifier TEXTURE = new Identifier("simplyskills","textures/entity/wraith.png");

    public WraithRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WraithModel(ctx.getPart(SimplySkillsClient.WRAITH_MODEL)), 1.0f);
    }

    @Override
    public Identifier getTexture(WraithEntity entity) {
        return TEXTURE;
    }

}
