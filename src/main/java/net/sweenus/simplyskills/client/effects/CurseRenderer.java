package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.sweenus.simplyskills.SimplySkills;

import java.util.List;

public class CurseRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = new Identifier("minecraft", "ender_eye");
    public static final Identifier modelId_overlay = new Identifier("minecraft", "ender_eye");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.GLOW, false);

    @Override
    public void setSpeed(float newSpeed) {
        newSpeed = 6.0f;
        super.setSpeed(newSpeed);
    }

    public CurseRenderer() {
        super(List.of(
                        new Model(BASE_RENDER_LAYER, modelId_overlay)),
                0.9F,
                1.2F);
        setSpeed(6f);
    }

}
