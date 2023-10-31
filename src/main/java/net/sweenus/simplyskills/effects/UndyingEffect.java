package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class UndyingEffect extends StatusEffect {
    public UndyingEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.hasStatusEffect(EffectRegistry.UNDYING)) {
                int durationRemaining = livingEntity.getStatusEffect(EffectRegistry.UNDYING).getDuration();
                if (durationRemaining == 35 && ((livingEntity.getHealth() / livingEntity.getMaxHealth()) * 100) < 60)
                    livingEntity.getWorld().playSoundFromEntity(null, livingEntity, SoundRegistry.SOUNDEFFECT11,
                            SoundCategory.PLAYERS, 0.3f, 1f);
            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (((entity.getHealth() / entity.getMaxHealth()) * 100) < 60) {
            entity.damage(entity.getDamageSources().magic(), entity.getMaxHealth());
            entity.getWorld().playSoundFromEntity(null, entity, SoundRegistry.SOUNDEFFECT36,
                    SoundCategory.PLAYERS, 0.4f, 1.3f);
            HelperMethods.spawnParticlesPlane(
                    entity.getWorld(),
                    ParticleTypes.SOUL,
                    entity.getBlockPos(),
                    2, 0, 0.4, 0);
            HelperMethods.spawnParticlesPlane(
                    entity.getWorld(),
                    ParticleTypes.SCULK_SOUL,
                    entity.getBlockPos(),
                    2, 0, 0.6, 0);
        } else {
            entity.getWorld().playSoundFromEntity(null, entity, SoundRegistry.SPELL_RADIANT_EXPIRE,
                    SoundCategory.PLAYERS, 0.4f, 1);
        }

        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.getWorld().playSoundFromEntity(null, entity, SoundRegistry.SPELL_CELESTIAL_HIT,
                SoundCategory.PLAYERS, 0.1f, 1.4f);
        super.onApplied(entity, attributes, amplifier);
    }

}
