package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.sound.SoundCategory;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class WayfarerAbilities {

    public static void passiveWayfarerBreakStealth(
            Entity target,
            PlayerEntity player,
            Boolean brokenByDamage,
            Boolean backstabBonus) {

        if (player.hasStatusEffect(EffectRegistry.STEALTH)) {

            if (brokenByDamage) {

                int speedDuration = SimplySkills.rogueConfig.passiveRogueFleetfootedSpeedDuration;
                int speedStacks = SimplySkills.rogueConfig.passiveRogueFleetfootedSpeedStacks;
                int speedMaxStacks = SimplySkills.rogueConfig.passiveRogueFleetfootedSpeedMaxStacks;
                int evasionDuration = SimplySkills.wayfarerConfig.passiveWayfarerReflexiveEvasionDuration;
                int evasionChance = SimplySkills.wayfarerConfig.passiveWayfarerReflexiveChance;

                if (HelperMethods.isUnlocked("simplyskills:rogue",
                        SkillReferencePosition.rogueFleetfooted, player))
                    HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED,
                            speedDuration, speedStacks, speedMaxStacks);
                if (HelperMethods.isUnlocked("simplyskills:tree",
                        SkillReferencePosition.wayfarerReflexive, player)
                        && player.getRandom().nextInt(100) < evasionChance)
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.EVASION,
                            evasionDuration, 1, 1);

            }

            if (target != null && !brokenByDamage) {

                if (target instanceof LivingEntity livingTarget) {
                    int deathmarkDuration = SimplySkills.rogueConfig.passiveRogueExploitationDeathMarkDuration;
                    int deathmarkStacks = SimplySkills.rogueConfig.passiveRogueExploitationDeathMarkStacks;

                    if (backstabBonus && HelperMethods.isBehindTarget(player, livingTarget)) {
                        if (HelperMethods.isUnlocked("simplyskills:rogue",
                                SkillReferencePosition.rogueExploitation, player))
                            HelperMethods.incrementStatusEffect(
                                    livingTarget,
                                    EffectRegistry.DEATHMARK,
                                    deathmarkDuration,
                                    deathmarkStacks,
                                    3);
                        if (HelperMethods.isUnlocked("simplyskills:rogue",
                                SkillReferencePosition.rogueOpportunisticMastery, player))
                            RogueAbilities.passiveRogueOpportunisticMastery(livingTarget, player);
                    }
                }
            }
            player.removeStatusEffect(EffectRegistry.STEALTH);
            player.getWorld().playSoundFromEntity(
                    null, player, SoundRegistry.SOUNDEFFECT36,
                    SoundCategory.PLAYERS, 0.7f, 1.4f);
            if (player.hasStatusEffect(StatusEffects.INVISIBILITY))
                player.removeStatusEffect(StatusEffects.INVISIBILITY);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.REVEALED, 180, 5, false, false, true));
        }
    }

    public static void passiveWayfarerGuarding(PlayerEntity player) {
        int barrierFrequency = SimplySkills.wayfarerConfig.passiveWayfarerGuardingBarrierFrequency;
        int barrierDuration = SimplySkills.wayfarerConfig.passiveWayfarerGuardingBarrierDuration;
        int barrierStacks = SimplySkills.wayfarerConfig.passiveWayfarerGuardingBarrierStacks;
        int barrierMaxStacks = SimplySkills.wayfarerConfig.passiveWayfarerGuardingBarrierMaxStacks;
        if (player.getOffHandStack().getItem() instanceof CrossbowItem
                && player.age % barrierFrequency == 0) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.BARRIER, barrierDuration, barrierStacks, barrierMaxStacks);
        }
    }

    public static void passiveWayfarerSlender(PlayerEntity player) {

        int slenderArmorThreshold = SimplySkills.wayfarerConfig.passiveWayfarerSlenderArmorThreshold - 1;
        int slenderSlownessAmplifier = SimplySkills.wayfarerConfig.passiveWayfarerSlenderSlownessAmplifier;
        int frailArmorThreshold = SimplySkills.initiateConfig.passiveInitiateFrailArmorThreshold - 1;
        int frailSlownessAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailSlownessAmplifier;

        if (player.age % 20 == 0) {
            if (player.getArmor() > slenderArmorThreshold && HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.roguePath, player)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                        25, slenderSlownessAmplifier, false, false, true));
            }
            if (player.getArmor() > slenderArmorThreshold && HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.rangerPath, player)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                        25, slenderSlownessAmplifier, false, false, true));
            }
            if (player.getArmor() > frailArmorThreshold && (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.wizardPath, player))){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                        25, frailSlownessAmplifier, false, false, true));
            }
        }
    }



}
