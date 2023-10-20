package net.sweenus.simplyskills.client.effects;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

public class UndyingParticles implements CustomParticleStatusEffect.Spawner {

    private final ParticleBatch particles;

    public UndyingParticles(int particleCount) {
        this.particles = new ParticleBatch(
                "spell_engine:holy_spark_mini",
                ParticleBatch.Shape.PIPE,
                ParticleBatch.Origin.FEET,
                null,
                particleCount,
                0.1F,
                0.7F,
                0);
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var scaledParticles = new ParticleBatch(particles);
        scaledParticles.count *= (amplifier + 1);
        ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
    }

}
