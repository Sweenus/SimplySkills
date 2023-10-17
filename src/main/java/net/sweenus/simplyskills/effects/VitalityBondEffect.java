package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;

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
                LivingEntity healingEntity;
                LivingEntity sacrificeEntity;
                float incrementAmount = 1;
                float livingEntityHealthPercent = ((livingEntity.getHealth() / livingEntity.getMaxHealth()) * 100);
                float targetHealthPercent = ((target.getHealth() / target.getMaxHealth()) * 100);

                if (livingEntityHealthPercent > targetHealthPercent) {
                    healingEntity = target;
                    sacrificeEntity = livingEntity;
                } else {
                    healingEntity = livingEntity;
                    sacrificeEntity = target;
                }

                if (sacrificeEntity.getHealth() > 4 + incrementAmount) {
                    sacrificeEntity.setHealth(sacrificeEntity.getHealth() - incrementAmount);
                    healingEntity.heal(incrementAmount);
                }


            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
