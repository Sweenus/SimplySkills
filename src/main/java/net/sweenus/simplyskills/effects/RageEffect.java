package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class RageEffect extends StatusEffect {
    public RageEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {
            if (livingEntity.hasStatusEffect(EffectRegistry.EXHAUSTION)
                    && livingEntity.age % 10 == 0) {
                if (livingEntity.getStatusEffect(EffectRegistry.RAGE).getAmplifier() > 25)
                    HelperMethods.decrementStatusEffects(livingEntity, EffectRegistry.EXHAUSTION, 1);
            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
