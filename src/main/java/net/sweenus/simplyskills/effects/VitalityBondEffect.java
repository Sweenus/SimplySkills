package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.spell_engine.particle.Particles;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class VitalityBondEffect extends StatusEffect {

    public LivingEntity target;

    public VitalityBondEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public void setTarget(LivingEntity livingEntity) {
        target = livingEntity;
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.getStatusEffect(EffectRegistry.VITALITYBOND) instanceof SimplyStatusEffectInstance statusEffect) {
                target = statusEffect.getSourceEntity();
            }


            //Target isn't null & isn't us
            float incrementFrequency = 10;
            if (target != null && target != livingEntity && livingEntity.age %incrementFrequency == 0) {
                LivingEntity healingEntity = null;
                LivingEntity sacrificeEntity = null;
                float incrementAmount = 1;
                float livingEntityHealthPercent = ((livingEntity.getHealth() / livingEntity.getMaxHealth()) * 100);
                float targetHealthPercent = ((target.getHealth() / target.getMaxHealth()) * 100);

                if (amplifier > 0)
                    amplifier = 0;

                //Sacrifice attack speed to grant recipient attack & movespeed
                if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericSpecialisationSacredOrbSpeed, target)) {
                    HelperMethods.incrementStatusEffect(livingEntity, StatusEffects.HASTE, (int) incrementFrequency+5, 1, 6);
                    HelperMethods.incrementStatusEffect(livingEntity, StatusEffects.SPEED, (int) incrementFrequency+5, 1, 2);
                    HelperMethods.incrementStatusEffect(target, StatusEffects.MINING_FATIGUE, (int) incrementFrequency+5, 1, 3);
                }

                // Take debuffs from recipient
                if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericSpecialisationSacredOrbDebuffs, target)) {
                    HelperMethods.buffSteal(target, livingEntity, true, true, true, false);
                }

                // Copy buffs to recipient
                if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericSpecialisationSacredOrbBuffs, target)) {
                    HelperMethods.buffSteal(livingEntity, target, false, false, false, false);
                }

                if ( Math.abs(livingEntityHealthPercent - targetHealthPercent) > 15 && (livingEntityHealthPercent < 85 || targetHealthPercent < 85)) {
                    if (livingEntityHealthPercent > targetHealthPercent) {
                        healingEntity = target;
                        sacrificeEntity = livingEntity;
                    } else if (livingEntityHealthPercent < targetHealthPercent) {
                        healingEntity = livingEntity;
                        sacrificeEntity = target;
                    }

                    HelperMethods.spawnParticlesPlane(livingEntity.getWorld(), Particles.healing_ascend.particleType,
                            healingEntity.getBlockPos(), 1, 0.01, 0.9, 0.03);
                    HelperMethods.spawnParticlesPlane(livingEntity.getWorld(), Particles.holy_hit.particleType,
                            sacrificeEntity.getBlockPos(), 1, 0.01, 0.9, 0.03);

                    sacrificeEntity.getWorld().playSoundFromEntity(null, sacrificeEntity, SoundRegistry.SOUNDEFFECT28,
                            SoundCategory.PLAYERS, 0.1f, 1.1f);
                    healingEntity.getWorld().playSoundFromEntity(null, healingEntity, SoundRegistry.SOUNDEFFECT25,
                            SoundCategory.PLAYERS, 0.1f, 1.0f);

                    if (sacrificeEntity != null && healingEntity != null && sacrificeEntity.getHealth() > 4 + incrementAmount) {
                        sacrificeEntity.setHealth(sacrificeEntity.getHealth() - incrementAmount);
                        healingEntity.heal(incrementAmount);
                    }
                }


            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.getWorld().playSoundFromEntity(null, entity, SoundRegistry.SPELL_RADIANT_HIT,
                SoundCategory.PLAYERS, 0.1f, 1.5f);
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.getWorld().playSoundFromEntity(null, entity, SoundRegistry.SPELL_RADIANT_EXPIRE,
                SoundCategory.PLAYERS, 0.4f, 1);
        super.onRemoved(entity, attributes, amplifier);
    }

}
