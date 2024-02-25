package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

import java.util.Map;
import java.util.Objects;

public class BoneArmorEffect extends StatusEffect {
    public BoneArmorEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {

        if (amplifier < 1 && entity instanceof PlayerEntity player) {
            if (AscendancyAbilities.getAscendancyPoints(player) > 29) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.UNDYING, 160, 0, false, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 160, 3, false, false, true));
            }
        }

        super.onRemoved(entity, attributes, amplifier);
    }

}
