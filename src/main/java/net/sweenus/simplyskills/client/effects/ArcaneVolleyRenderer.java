package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.sweenus.simplyskills.SimplySkills;

import java.util.List;

public class ArcaneVolleyRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = new Identifier(SimplySkills.MOD_ID, "projectile/arcane_projectile");
    public static final Identifier modelId_overlay = new Identifier(SimplySkills.MOD_ID, "projectile/arcane_projectile");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.GLOW, false);

    @Override
    public void setSpeed(float newSpeed) {
        newSpeed = 8f;
        super.setSpeed(newSpeed);
    }

    public ArcaneVolleyRenderer() {
        super(List.of(
                new Model(GLOWING_RENDER_LAYER, modelId_overlay)),
                0.4F,
                1.55F);
    }

}
