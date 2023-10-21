package net.sweenus.simplyskills.client.effects;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

public class RageParticles implements CustomParticleStatusEffect.Spawner {

    private final ParticleBatch particles;

    public RageParticles(int particleCount) {
        this.particles = new ParticleBatch(
                "minecraft:crimson_spore",
                ParticleBatch.Shape.PIPE,
                ParticleBatch.Origin.FEET,
                null,
                particleCount,
                0.01F,
                0.03F,
                0);
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var scaledParticles = new ParticleBatch(particles);
        scaledParticles.count += Math.max((amplifier / 10), 1);
        ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
    }

}
