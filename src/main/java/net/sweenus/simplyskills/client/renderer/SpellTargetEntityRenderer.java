package net.sweenus.simplyskills.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.client.renderer.model.SpellTargetEntityModel;
import net.sweenus.simplyskills.entities.SpellTargetEntity;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.client.renderer.model.BattleStandardModel;
import net.sweenus.simplyswords.entity.BattleStandardEntity;

@Environment(value= EnvType.CLIENT)
public class SpellTargetEntityRenderer extends EntityRenderer<SpellTargetEntity> {


     private static final Identifier TEXTURE = new Identifier("simplyswords","textures/entity/battlestandard/battlestandard_texture.png");

    public SpellTargetEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }


    @Override
    public Identifier getTexture(SpellTargetEntity entity) {
        return TEXTURE;
    }
}
