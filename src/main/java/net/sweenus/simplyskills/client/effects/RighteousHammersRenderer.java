package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.sweenus.simplyskills.SimplySkills;

import java.util.List;

public class RighteousHammersRenderer extends OrbitingRotatingEffectRenderer {
    public static final Identifier modelId_base = new Identifier(SimplySkills.MOD_ID, "projectile/righteous_hammers");
    public static final Identifier modelId_overlay = new Identifier(SimplySkills.MOD_ID, "projectile/righteous_hammers");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.GLOW, true);

    @Override
    public void setSpeed(float newSpeed) {
        newSpeed = 9.0f;
        super.setSpeed(newSpeed);
    }

    public RighteousHammersRenderer() {
        super(List.of(
                        new Model(GLOWING_RENDER_LAYER, modelId_overlay)),
                0.9F,
                3.0F);
        setSpeed(9f);
    }

}
