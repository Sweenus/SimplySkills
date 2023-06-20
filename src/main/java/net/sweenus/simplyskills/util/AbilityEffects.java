package net.sweenus.simplyskills.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AbilityEffects {


    public static void effectBerserkerBerserking(Entity target, PlayerEntity player) {

        if ((target instanceof LivingEntity livingTarget) && player.hasStatusEffect(EffectRegistry.BERSERKING)) {
            int berserkingSubEffectDuration = 200;
            HelperMethods.incrementStatusEffect(player, StatusEffects.HASTE, berserkingSubEffectDuration, 1, 3);
            HelperMethods.incrementStatusEffect(player, StatusEffects.STRENGTH, berserkingSubEffectDuration, 1, 3);
            HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED, berserkingSubEffectDuration, 1, 3);
        }
    }

    public static void effectBerserkerBloodthirsty(PlayerEntity player, LivingEntity livingTarget) {

        if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY)) {
            float bloodthirstyHealPercent = 0.25f;
            float healAmount = player.getMaxHealth() * bloodthirstyHealPercent;
            player.heal(healAmount);
        }
    }

    public static void effectBerserkerRampage(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.RAMPAGE)) {
            int rampageSubEffectDuration = 200;

            List<StatusEffect> list = new ArrayList<>();
            list.add(StatusEffects.STRENGTH);
            list.add(StatusEffects.SPEED);
            list.add(StatusEffects.RESISTANCE);
            list.add(StatusEffects.HASTE);

            Random rand = new Random();
            StatusEffect randomStatus = list.get(rand.nextInt(list.size()));
            HelperMethods.incrementStatusEffect(player, randomStatus, rampageSubEffectDuration, 1, 2);
        }
    }

    public static void effectRogueSiphoningStrikes(Entity target, PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.SIPHONINGSTRIKES)) {
            if (target instanceof LivingEntity livingTarget) {

                double attackValue = Objects.requireNonNull(
                        player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).getValue();
                float healAmount = (float) (attackValue * 0.15);
                player.heal(healAmount);

                HelperMethods.decrementStatusEffect(player, EffectRegistry.SIPHONINGSTRIKES);

                for (StatusEffectInstance statusEffect : livingTarget.getStatusEffects()) {
                    if (statusEffect != null && statusEffect.getEffectType().isBeneficial()) {
                        livingTarget.removeStatusEffect(statusEffect.getEffectType());
                        break;
                    }
                }
            }
        }
    }

    public static boolean effectRangerElementalArrows(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.ELEMENTALARROWS)) {

            List<String> list = new ArrayList<>();
            list.add("simplyskills:frost_arrow");
            list.add("simplyskills:fire_arrow");
            list.add("simplyskills:lightning_arrow");
            Random rand = new Random();
            String randomSpell = list.get(rand.nextInt(list.size()));

            HelperMethods.decrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS);

            SignatureAbilities.castSpellEngineTargeted(player,
                    randomSpell,
                    96);
            return true;
        }
        return false;
    }




}
