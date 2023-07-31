package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Random;

public class RangerAbilities {

    public static void passiveRangerReveal(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerRevealFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerRevealRadius;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().equals(EffectRegistry.STEALTH)) {
                                le.removeStatusEffect(statusEffect.getEffectType());
                                le.addStatusEffect(new StatusEffectInstance(EffectRegistry.REVEALED, 180, 1));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRangerTamer(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerTamerFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerTamerRadius;
            int resistanceAmplifier = SimplySkills.rangerConfig.passiveRangerTamerResistanceAmplifier;
            int regenerationAmplifier = SimplySkills.rangerConfig.passiveRangerTamerRegenerationAmplifier;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof TameableEntity te)) {
                        if (te.isOwner(player)) {
                            te.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,
                                    frequency + 5, regenerationAmplifier));
                            te.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                                    frequency + 5, resistanceAmplifier));
                        }
                    }
                }
            }
        }
    }
    public static void passiveRangerBonded(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerBondedFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerBondedRadius;
            int petMinimumHealthPercent = SimplySkills.rangerConfig.passiveRangerBondedPetMinimumHealthPercent;
            int healthTransferAmount = SimplySkills.rangerConfig.passiveRangerBondedHealthTransferAmount;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof TameableEntity te)) {
                        if (te.isOwner(player)) {
                            float teHealthPercent = ((te.getHealth() / te.getMaxHealth()) * 100);
                            float playerHealthPercent = ((player.getHealth() / player.getMaxHealth()) * 100);
                            if (teHealthPercent > playerHealthPercent && teHealthPercent > petMinimumHealthPercent) {
                                te.setHealth(te.getHealth() - healthTransferAmount);
                                player.heal(healthTransferAmount);
                            }
                        }
                    }
                }
            }
        }
    }
    public static void passiveRangerTrained(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerTrainedFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerTrainedRadius;
            int strengthAmplifier = SimplySkills.rangerConfig.passiveRangerTrainedStrengthAmplifier;
            int speedAmplifier = SimplySkills.rangerConfig.passiveRangerTrainedSpeedAmplifier;
            int minimumHealthPercent = SimplySkills.rangerConfig.passiveRangerTrainedMinimumHealthPercent;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof TameableEntity te)) {
                        if (te.isOwner(player)) {
                            float teHealthPercent = ((te.getHealth() / te.getMaxHealth()) * 100);
                            if (teHealthPercent > minimumHealthPercent) {
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                                        frequency + 5, strengthAmplifier));
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                                        frequency + 5, speedAmplifier));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRangerIncognito(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerIncognitoFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerIncognitoRadius;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof TameableEntity te)) {
                        if (te.isOwner(player)) {
                            if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                                te.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH,
                                        frequency + 5));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRangerElementalArrowsRenewal(PlayerEntity player) {
        int random = new Random().nextInt(100);
        int renewalChance = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalChance;
        int renewalDuration = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalDuration;
        int renewalMaxStacks = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalMaximumStacks;
        int renewalStacks = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalStacks;
        if (random < renewalChance)
            HelperMethods.incrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS,
                    renewalDuration, renewalStacks, renewalMaxStacks);
    }


    //------- SIGNATURE ABILITIES --------


    //Disengage
    public static boolean signatureRangerDisengage(String rangerSkillTree, PlayerEntity player) {

        int radius = SimplySkills.rangerConfig.signatureRangerDisengageRadius;
        int velocity = SimplySkills.rangerConfig.signatureRangerDisengageVelocity;
        int height = SimplySkills.rangerConfig.signatureRangerDisengageHeight;
        int slownessDuration = SimplySkills.rangerConfig.signatureRangerDisengageSlownessDuration;
        int slownessAmplifier = SimplySkills.rangerConfig.signatureRangerDisengageSlownessAmplifier;
        int slowFallDuration = SimplySkills.rangerConfig.signatureRangerDisengageSlowFallDuration;
        int slowFallAmplifier = SimplySkills.rangerConfig.signatureRangerDisengageSlowFallAmplifier;

        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
            if (entities != null) {
                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                            slownessDuration, slownessAmplifier));

                }
            }
        }

        player.setVelocity(player.getRotationVector().negate().multiply(+velocity));
        player.setVelocity(player.getVelocity().x, height, player.getVelocity().z);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                slowFallDuration, slowFallAmplifier));
        player.velocityModified = true;

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationDisengageRecuperate, player))
            signatureRangerDisengageRecuperate(player);

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationDisengageExploitation, player))
            signatureRangerDisengageExploitation(player);

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationDisengageMarksman, player)) {
            int marksmanDuration = SimplySkills.rangerConfig.signatureRangerDisengageMarksmanDuration;
            int marksmanStacks = SimplySkills.rangerConfig.signatureRangerDisengageMarksmanStacks;
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMAN, marksmanDuration, marksmanStacks, 99);
        }
        return true;
    }

    // Disengage Recuperate
    public static void signatureRangerDisengageRecuperate(PlayerEntity player) {
        int radius = SimplySkills.rangerConfig.signatureRangerDisengageRecuperateRadius;

        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

            if (entities != null) {
                if ((entities instanceof TameableEntity te)) {
                    if (te.isOwner(player)) {
                        te.heal(te.getMaxHealth());
                    }
                }
            }
        }
    }

    // Disengage Exploitation
    public static void signatureRangerDisengageExploitation(PlayerEntity player) {
        int radius = SimplySkills.rangerConfig.signatureRangerDisengageExploitationRadius;
        int effectDuration = SimplySkills.rangerConfig.signatureRangerDisengageExploitationDuration;

        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

            if (entities != null) {
                if ((entities instanceof TameableEntity te)) {
                    if (te.isOwner(player)) {
                        te.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZINGAURA, effectDuration));
                    }
                }
            }
        }
    }

    //Disengage
    public static boolean signatureRangerElementalArrows(String rangerSkillTree, PlayerEntity player) {

        return true;
    }

}
