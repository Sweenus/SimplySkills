package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.sweenus.simplyskills.util.HelperMethods;

import static java.lang.Math.min;

public class ImmobilizeEffect extends StatusEffect {
    private BlockPos blockPos;
    public ImmobilizeEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {
            float damage = min((float)(livingEntity.getMaxHealth() * 0.1), 10);
            if (!livingEntity.getBlockPos().equals(blockPos)) {
                if (livingEntity.age % 5 == 0) {
                    blockPos = livingEntity.getBlockPos();
                    livingEntity.damage(livingEntity.getDamageSources().generic(), damage);
                    HelperMethods.incrementStatusEffect(livingEntity, StatusEffects.SLOWNESS, 80, 1, 9);
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
