package net.sweenus.simplyskills.mixins.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.render.SpellProjectileRenderer;
import net.spell_engine.entity.SpellProjectile;
import net.sweenus.simplyskills.util.HelperMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellProjectileRenderer.class)
public class SpellProjectileRendererMixin <T extends Entity & FlyingItemEntity> extends EntityRenderer<T> {

    protected SpellProjectileRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void simplyskills$render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof SpellProjectile projectile) {
            if (projectile.renderData() != null) {
                Spell.ProjectileModel renderData = projectile.renderData();
                String modelId = renderData.model_id;
                String[] modelList =  new String[] {"swordfall", "sword"};

                if (HelperMethods.stringContainsAny(modelId, modelList)) {
                    renderData.rotate_degrees_per_tick = 0;
                }

            }
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
