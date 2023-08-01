package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.puffish.skillsmod.server.PlayerAttributes;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class ExhaustionEffect extends StatusEffect {
    public ExhaustionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {
            if (livingEntity instanceof PlayerEntity player) {
                double exhaustResist = player.getAttributeValue(PlayerAttributes.STAMINA) * 2;
                int frequency = 10;
                if (player.age % frequency == 0) {
                    if (player.getRandom().nextInt(100) < exhaustResist)
                        HelperMethods.decrementStatusEffect(player, EffectRegistry.EXHAUSTION);
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
