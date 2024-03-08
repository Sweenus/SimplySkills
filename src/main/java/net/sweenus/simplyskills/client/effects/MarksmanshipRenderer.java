package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.sweenus.simplyskills.SimplySkills;

import java.util.List;

public class MarksmanshipRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = new Identifier(SimplySkills.MOD_ID, "projectile/arrow");
    public static final Identifier modelId_overlay = new Identifier(SimplySkills.MOD_ID, "projectile/arrow");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.NONE, false);

    @Override
    public void setSpeed(float newSpeed) {
        newSpeed = 2f;
        super.setSpeed(newSpeed);
    }

    public MarksmanshipRenderer() {
        super(List.of(
                new Model(BASE_RENDER_LAYER, modelId_base)),
                1.2F,
                1.1F);
    }

}
