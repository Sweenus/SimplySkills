package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;

public class MagicCircleEffect extends StatusEffect {
    public MagicCircleEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {
            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.MAGICCIRCLE)) {
                StatusEffectInstance magicCircle = player.getStatusEffect(EffectRegistry.MAGICCIRCLE);
                if (magicCircle == null)
                    return;

                if (magicCircle.getDuration() % 20 == 0) {
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, 25, 0, false, false, true));
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
