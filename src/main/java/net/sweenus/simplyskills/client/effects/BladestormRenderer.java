package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.sweenus.simplyskills.SimplySkills;

import java.util.List;

public class BladestormRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = new Identifier(SimplySkills.MOD_ID, "projectile/swordfall");
    public static final Identifier modelId_overlay = new Identifier(SimplySkills.MOD_ID, "projectile/swordfall");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.RADIATE, false);

    @Override
    public void setSpeed(float newSpeed) {
        newSpeed = 20f;
        super.setSpeed(newSpeed);
    }

    public BladestormRenderer() {
        super(List.of(
                        new Model(GLOWING_RENDER_LAYER, modelId_overlay),
                        new Model(BASE_RENDER_LAYER, modelId_base)),
                0.4F,
                1.55F);
    }

}
