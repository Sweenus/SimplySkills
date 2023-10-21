package net.sweenus.simplyskills.abilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class BerserkerAbilities {

    public static void passiveBerserkerSwordMastery(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerSwordMasteryFrequency;
        int baseSpeedAmplifier = SimplySkills.berserkerConfig.passiveBerserkerSwordMasteryBaseSpeedAmplifier;
        int speedAmplifierPerTier = SimplySkills.berserkerConfig.passiveBerserkerSwordMasterySpeedAmplifierPerTier;
        if (player.age % frequency == 0) {
            if (player.getMainHandStack() != null) {
                if ((player.getMainHandStack().getItem() instanceof SwordItem)
                        && !HelperMethods.stringContainsAny(player.getMainHandStack().getItem().getName().toString(),
                        new String[] {"Axe", "axe", "molten_edge", "livyatan", "soulpyre"})) {
                    int mastery = baseSpeedAmplifier;

                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerSwordMasterySkilled, player)
                            && player.getOffHandStack().isEmpty())
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MIGHT,
                                frequency + 5, 0, false, false, true));
                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerSwordMasteryProficient, player))
                        mastery = mastery + speedAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                            frequency + 5, mastery, false, false, true));
                }
            }
        }
    }

    public static void passiveBerserkerAxeMastery(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerAxeMasteryFrequency;
        int baseStrengthAmplifier = SimplySkills.berserkerConfig.passiveBerserkerAxeMasteryBaseStrengthAmplifier;
        int strengthAmplifierPerTier = SimplySkills.berserkerConfig.passiveBerserkerAxeMasteryStrengthAmplifierPerTier;
        if (player.age % frequency == 0) {
            if (player.getMainHandStack() != null) {
                if ((player.getMainHandStack().getItem() instanceof AxeItem)
                        || HelperMethods.stringContainsAny(player.getMainHandStack().getItem().getName().toString(),
                        new String[] {"Axe", "axe", "molten_edge", "livyatan", "soulpyre"})) {

                    int mastery = baseStrengthAmplifier;

                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerAxeMasterySkilled, player))
                        mastery = mastery + (strengthAmplifierPerTier * 2);
                    else if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerAxeMasteryProficient, player))
                        mastery = mastery + strengthAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                            frequency + 5, mastery, false, false, true));
                }
            }
        }
    }

    public static void passiveBerserkerIgnorePain(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerIgnorePainFrequency;
        double healthThreshold = SimplySkills.berserkerConfig.passiveBerserkerIgnorePainHealthThreshold;
        int baseResistanceAmplifier = SimplySkills.berserkerConfig.passiveBerserkerIgnorePainBaseResistanceAmplifier;
        int resistanceAmplifierPerTier = SimplySkills.berserkerConfig.passiveBerserkerIgnorePainResistanceAmplifierPerTier;
        if (player.age % frequency == 0) {
            int resistanceStacks = baseResistanceAmplifier;
            if (player.getHealth() <= (healthThreshold * player.getMaxHealth())) {

                if (HelperMethods.isUnlocked("simplyskills:berserker",
                        SkillReferencePosition.berserkerIgnorePainSkilled, player))
                    resistanceStacks = resistanceStacks + (resistanceAmplifierPerTier * 2);
                else if (HelperMethods.isUnlocked("simplyskills:berserker",
                        SkillReferencePosition.berserkerIgnorePainProficient, player))
                    resistanceStacks = resistanceStacks + resistanceAmplifierPerTier;

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                        frequency + 5, resistanceStacks, false, false, true));
            }
        }
    }

    public static void passiveBerserkerRecklessness(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerRecklessnessFrequency;
        double healthThreshold = SimplySkills.berserkerConfig.passiveBerserkerRecklessnessHealthThreshold;
        int weaknessAmplifier = SimplySkills.berserkerConfig.passiveBerserkerRecklessnessWeaknessAmplifier;
        if (player.age % frequency == 0) {
            if (player.getHealth() >= (healthThreshold * player.getMaxHealth())) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                        frequency + 5, weaknessAmplifier, false, false, true));
            }
        }
    }

    public static void passiveBerserkerChallenge(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerChallengeFrequency;
        int radius = SimplySkills.berserkerConfig.passiveBerserkerChallengeRadius;
        int count = 0;
        int countMax = SimplySkills.berserkerConfig.passiveBerserkerChallengeMaxAmplifier;
        if (player.age % frequency == 0) {

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        count++;
                    }
                }
            }
            if (count > countMax)
                count = countMax;
            if (count > 1)
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, frequency + 5, count -1, false, false, true));
        }
    }

    public static void passiveBerserkerExploit(Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.hasStatusEffect(EffectRegistry.IMMOBILIZE)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 1, false, false, true));
            }
        }
    }


    //------- SIGNATURE ABILITIES --------


    // Rampage
    public static boolean signatureBerserkerRampage(String berserkerSkillTree, PlayerEntity player) {

        int rampageDuration = SimplySkills.berserkerConfig.signatureBerserkerRampageDuration;
        int bullrushDuration = SimplySkills.berserkerConfig.signatureBerserkerBullrushDuration;
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RAMPAGE, rampageDuration, 0, false, false, true));
        if (HelperMethods.isUnlocked(berserkerSkillTree,
                SkillReferencePosition.berserkerSpecialisationRampageCharge, player)) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BULLRUSH, bullrushDuration, 0 , false, false, true));
            player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                    SoundCategory.PLAYERS, 0.5f, 1.1f);
        }
        return true;
    }

    // Bloodthirsty
    public static boolean signatureBerserkerBloodthirsty(String berserkerSkillTree, PlayerEntity player) {
        int bloodthirstyDuration = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyDuration;
        int bloodthirstyMightyStacks = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyMightyStacks;
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, bloodthirstyDuration, 0, false, false, true));
        if (HelperMethods.isUnlocked(berserkerSkillTree,
                SkillReferencePosition.berserkerSpecialisationBloodthirstyMighty, player))
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, bloodthirstyDuration,
                    bloodthirstyMightyStacks, 5);
        return true;
    }

    // Berserking
    public static boolean signatureBerserkerBerserking(String berserkerSkillTree, PlayerEntity player) {
        double sacrificeAmountModifier = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSacrificeAmount;
        int secondsPerSacrifice = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSecondsPerSacrifice;
        int leapSlamDuration = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamDuration;
        float sacrificeAmount = (float) (player.getHealth() * sacrificeAmountModifier);
        if (!FabricLoader.getInstance().isModLoaded("simplyswords") || !SimplySwordsGemEffects.doSignatureGemEffects(player, "accelerant")) {
            player.damage(player.getDamageSources().generic(), sacrificeAmount);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BERSERKING,
                    (int) ((sacrificeAmount * secondsPerSacrifice) * 20), 0, false, false, true));
        }
        if (HelperMethods.isUnlocked(berserkerSkillTree,
                SkillReferencePosition.berserkerSpecialisationBerserkingLeap, player)) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LEAPSLAM, leapSlamDuration, 0 , false, false, true));
            player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                    SoundCategory.PLAYERS, 0.5f, 1.1f);
        }
        return true;
    }



}
