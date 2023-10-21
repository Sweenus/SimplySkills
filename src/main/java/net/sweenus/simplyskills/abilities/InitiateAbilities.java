package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;

public class InitiateAbilities {

    public static void passiveInitiateNullification(PlayerEntity player) {
        int nullificationFrequency = SimplySkills.initiateConfig.passiveInitiateNullificationFrequency;
        int radius = SimplySkills.initiateConfig.passiveInitiateNullificationRadius;
        if (player.age % nullificationFrequency == 0) {

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().isBeneficial()) {
                                HelperMethods.decrementStatusEffect(le, statusEffect.getEffectType());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    public static void passiveInitiateEmpower(PlayerEntity player, Identifier spellID) {
        int chance = SimplySkills.initiateConfig.passiveInitiateEmpowerChance;
        int duration = SimplySkills.initiateConfig.passiveInitiateEmpowerDuration;
        int amplifier = SimplySkills.initiateConfig.passiveInitiateEmpowerStacks;
        int amplifierMax = SimplySkills.initiateConfig.passiveInitiateEmpowerMaxStacks;
        List<StatusEffect> list = new ArrayList<>();
        if (HelperMethods.stringContainsAny(spellID.toString(), SimplySkills.getArcaneSpells()))
            list.add(EffectRegistry.ARCANEATTUNEMENT);
        if (HelperMethods.stringContainsAny(spellID.toString(), SimplySkills.getSoulSpells()))
            list.add(EffectRegistry.SOULATTUNEMENT);
        if (HelperMethods.stringContainsAny(spellID.toString(), SimplySkills.getHealingSpells()))
            list.add(EffectRegistry.HOLYATTUNEMENT);
        if (HelperMethods.stringContainsAny(spellID.toString(), SimplySkills.getFireSpells()))
            list.add(EffectRegistry.FIREATTUNEMENT);
        if (HelperMethods.stringContainsAny(spellID.toString(), SimplySkills.getFrostSpells()))
            list.add(EffectRegistry.FROSTATTUNEMENT);
        if (HelperMethods.stringContainsAny(spellID.toString(), SimplySkills.getLightningSpells()))
            list.add(EffectRegistry.LIGHTNINGATTUNEMENT);

        if (!list.isEmpty()) {

            int random = player.getRandom().nextInt(100);
            if (random < chance) {
                random = player.getRandom().nextInt(list.size());
                StatusEffect chosenEffect = list.get(random);
                HelperMethods.incrementStatusEffect(player, chosenEffect, duration, amplifier, amplifierMax);
            }

            if (player.hasStatusEffect(list.get(0))
                    && HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.initiateAttuned, player)) {
                int stackThreshold = SimplySkills.initiateConfig.passiveInitiateAttunedStackThreshold -1;
                StatusEffectInstance statusInstance = player.getStatusEffect(list.get(0));
                if (statusInstance != null && statusInstance.getAmplifier() > stackThreshold) {
                    passiveInitiateAttuned(player, statusInstance);
                }
            }
        }
    }

    public static void passiveInitiateAttuned(PlayerEntity player, StatusEffectInstance statusInstance) {
        int duration = SimplySkills.initiateConfig.passiveInitiateAttunedDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateAttunedStacks;
        int maxStacks = SimplySkills.initiateConfig.passiveInitiateAttunedMaxStacks;
        int frequency = SimplySkills.initiateConfig.passiveInitiateAttunedFrequency;
        if (player.age % frequency == 0) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.PRECISION, duration, stacks, maxStacks);
            HelperMethods.decrementStatusEffect(player, statusInstance.getEffectType());
        }
    }

    public static void passiveInitiateLightningRod(PlayerEntity player) {
        int duration = SimplySkills.initiateConfig.passiveInitiateLightningRodDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateLightningRodStacks;
        int maxStacks = SimplySkills.initiateConfig.passiveInitiateLightningRodMaxStacks;
        int frequency = SimplySkills.initiateConfig.passiveInitiateLightningRodFrequency;
        if (player.age % frequency == 0 && player.getWorld().isThundering()) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.LIGHTNINGATTUNEMENT, duration, stacks, maxStacks);
        }
    }

    public static void passiveInitiateHasty(PlayerEntity player) {
        int duration = SimplySkills.initiateConfig.passiveInitiateHastyDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateHastyStacks;
        HelperMethods.incrementStatusEffect(player, EffectRegistry.IMMOBILIZE, duration, stacks, 1);
    }

    public static void passiveInitiateFrail(PlayerEntity player) {
        int attackThreshold = SimplySkills.initiateConfig.passiveInitiateFrailAttackThreshold;
        int weaknessAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailWeaknessAmplifier;
        int miningFatigueAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailMiningFatigueAmplifier;

        if (HelperMethods.getAttackDamage(player.getMainHandStack()) > attackThreshold
                || HelperMethods.getAttackDamage(player.getOffHandStack()) > attackThreshold
                && HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.wayfarerSlender, player)){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    25, weaknessAmplifier, false, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,
                    25, miningFatigueAmplifier, false, false, true));
        }
    }

}
