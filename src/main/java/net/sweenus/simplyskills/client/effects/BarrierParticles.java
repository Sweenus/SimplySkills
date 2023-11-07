package net.sweenus.simplyskills.client.effects;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import net.sweenus.simplyskills.registry.EffectRegistry;

public class BarrierParticles implements CustomParticleStatusEffect.Spawner {

    private final ParticleBatch particles;

    public BarrierParticles(int particleCount) {
        this.particles = new ParticleBatch(
                "spell_engine:arcane_spell",
                ParticleBatch.Shape.PIPE,
                ParticleBatch.Origin.FEET,
                null,
                particleCount,
                0.1F,
                0.4F,
                0);
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHeight() > 1 && !livingEntity.hasStatusEffect(EffectRegistry.STEALTH)) {
            var scaledParticles = new ParticleBatch(particles);
            scaledParticles.count *= (float) ((amplifier * 0.15) + 1);
            ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
        }
    }

}
