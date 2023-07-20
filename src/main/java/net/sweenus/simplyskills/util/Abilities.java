package net.sweenus.simplyskills.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;

import java.util.Collection;
import java.util.Random;

public class Abilities {


    //Ability methods for our Mixins


    public static void passiveInitiateNullification(PlayerEntity player) {
        int nullificationFrequency = SimplySkills.initiateConfig.passiveInitiateNullificationFrequency;
        int radius = SimplySkills.initiateConfig.passiveInitiateNullificationRadius;
        if (player.age % nullificationFrequency == 0) {

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

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

    public static void passiveAreaCleanse(PlayerEntity player) {
        if (player.age % 80 == 0) {
            int radius = 12;

            Box box = new Box(player.getX() + radius, player.getY() + (float) radius / 3, player.getZ() + radius,
                    player.getX() - radius, player.getY() - (float) radius / 3, player.getZ() - radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && !HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && !statusEffect.getEffectType().isBeneficial()) {
                                le.removeStatusEffect(statusEffect.getEffectType());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveSelfCleanse(PlayerEntity player) {
        if (player.age % 120 == 0) {
            for (StatusEffectInstance statusEffect : player.getStatusEffects()) {
                if (statusEffect != null && !statusEffect.getEffectType().isBeneficial()) {
                    player.removeStatusEffect(statusEffect.getEffectType());
                    break;
                }
            }
        }
    }

    public static void passiveRangerReveal(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerRevealFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerRevealRadius;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().equals(StatusEffects.INVISIBILITY)) {
                                le.removeStatusEffect(statusEffect.getEffectType());
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
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, frequency + 5, regenerationAmplifier));
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, frequency + 5, resistanceAmplifier));
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
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, frequency + 5, strengthAmplifier));
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, frequency + 5, speedAmplifier));
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
                            if (player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
                                te.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, frequency + 5));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRogueBackstab(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity livingTarget) {
            int weaknessDuration = SimplySkills.rogueConfig.passiveRogueBackstabWeaknessDuration;
            int weaknessAmplifier = SimplySkills.rogueConfig.passiveRogueBackstabWeaknessAmplifier;
            if (livingTarget.getBodyYaw() < (player.getBodyYaw() + 32) &&
                    livingTarget.getBodyYaw() > (player.getBodyYaw() - 32)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, weaknessDuration, weaknessAmplifier));
            }
        }
    }

    public static void passiveWarriorArmorMastery(PlayerEntity player) {
        int armorMasteryThreshold = SimplySkills.warriorConfig.passiveWarriorArmorMasteryArmorThreshold - 1;
        int armorMasteryChance = SimplySkills.warriorConfig.passiveWarriorArmorMasteryChance;
        int heavyArmorMasteryDuration = SimplySkills.warriorConfig.passiveWarriorHeavyArmorMasteryDuration;
        int heavyArmorMasteryAmplifier = SimplySkills.warriorConfig.passiveWarriorHeavyArmorMasteryAmplifier;
        int mediumArmorMasteryDuration = SimplySkills.warriorConfig.passiveWarriorMediumArmorMasteryDuration;
        int mediumArmorMasteryAmplifier = SimplySkills.warriorConfig.passiveWarriorMediumArmorMasteryAmplifier;

        if (player.getRandom().nextInt(100) < armorMasteryChance) {
            if (player.getArmor() > armorMasteryThreshold && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.warriorHeavyArmorMastery)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, heavyArmorMasteryDuration, heavyArmorMasteryAmplifier));
            } else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.warriorMediumArmorMastery)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, mediumArmorMasteryDuration, mediumArmorMasteryAmplifier));
            }
        }
    }

    public static void passiveBerserkerSwordMastery(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerSwordMasteryFrequency;
        int baseSpeedAmplifier = SimplySkills.berserkerConfig.passiveBerserkerSwordMasteryBaseSpeedAmplifier;
        int speedAmplifierPerTier = SimplySkills.berserkerConfig.passiveBerserkerSwordMasterySpeedAmplifierPerTier;
        if (player.age % frequency == 0) {
            if (player.getMainHandStack() != null) {
                if (player.getMainHandStack().getItem() instanceof SwordItem) {
                    int mastery = baseSpeedAmplifier;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerSwordMasterySkilled))
                        mastery = mastery + (speedAmplifierPerTier * 2);
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerSwordMasteryProficient))
                        mastery = mastery + speedAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, frequency + 5, mastery));
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
                if (player.getMainHandStack().getItem() instanceof AxeItem) {

                    int mastery = baseStrengthAmplifier;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerAxeMasterySkilled))
                        mastery = mastery + (strengthAmplifierPerTier * 2);
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerAxeMasteryProficient))
                        mastery = mastery + strengthAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, frequency + 5, mastery));
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

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills").get().contains(SkillReferencePosition.berserkerIgnorePainSkilled))
                    resistanceStacks = resistanceStacks + (resistanceAmplifierPerTier * 2);
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills").get().contains(SkillReferencePosition.berserkerIgnorePainProficient))
                    resistanceStacks = resistanceStacks + resistanceAmplifierPerTier;

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, frequency + 5, resistanceStacks));
            }
        }
    }

    public static void passiveBerserkerRecklessness(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerRecklessnessFrequency;
        double healthThreshold = SimplySkills.berserkerConfig.passiveBerserkerRecklessnessHealthThreshold;
        int weaknessAmplifier = SimplySkills.berserkerConfig.passiveBerserkerRecklessnessWeaknessAmplifier;
        if (player.age % frequency == 0) {
            if (player.getHealth() >= (healthThreshold * player.getMaxHealth())) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, frequency + 5, weaknessAmplifier));
            }
        }
    }

    public static void passiveBerserkerChallenge(PlayerEntity player) {
        int frequency = SimplySkills.berserkerConfig.passiveBerserkerChallengeFrequency;
        int radius = SimplySkills.berserkerConfig.passiveBerserkerChallengeRadius;
        if (player.age % frequency == 0) {

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        if (player.hasStatusEffect(StatusEffects.HASTE)) {
                            int amplify = (player.getStatusEffect(StatusEffects.HASTE).getAmplifier() + 1);
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, frequency + 5, amplify));
                        } else {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, frequency + 5));
                        }
                    }
                }
            }
        }
    }

    public static void passiveBulwarkShieldMastery(PlayerEntity player) {
        int shieldMasteryFrequency = SimplySkills.warriorConfig.passiveWarriorShieldMasteryFrequency;
        int shieldMasteryWeaknessAmplifier = SimplySkills.warriorConfig.passiveWarriorShieldMasteryWeaknessAmplifier;
        int shieldMasteryResistanceAmplifier = SimplySkills.warriorConfig.passiveWarriorShieldMasteryResistanceAmplifier;
        int shieldMasteryResistanceAmplifierPerTier = SimplySkills.warriorConfig.passiveWarriorShieldMasteryResistanceAmplifierPerTier;


        if (player.age % shieldMasteryFrequency == 0) {
            if (player.getOffHandStack() != null) {
                if (player.getOffHandStack().getItem() instanceof ShieldItem) {

                    int mastery = shieldMasteryResistanceAmplifier;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.bulwarkShieldMasterySkilled))
                        mastery = mastery + (shieldMasteryResistanceAmplifierPerTier * 2);
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.bulwarkShieldMasteryProficient))
                        mastery = mastery + shieldMasteryResistanceAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                            shieldMasteryFrequency + 5, mastery));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                            shieldMasteryFrequency + 5, shieldMasteryWeaknessAmplifier));
                }
            }
        }
    }

    public static void passiveBulwarkRebuke(PlayerEntity player, LivingEntity attacker) {
        int rebukeChance = SimplySkills.warriorConfig.passiveWarriorRebukeChance;
        int rebukeWeaknessDuration = SimplySkills.warriorConfig.passiveWarriorRebukeWeaknessDuration;
        int rebukeWeaknessAmplifier = SimplySkills.warriorConfig.passiveWarriorRebukeWeaknessAmplifier;
        if (player.getRandom().nextInt(100) < rebukeChance) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    rebukeWeaknessDuration, rebukeWeaknessAmplifier));
        }
    }

    public static void passiveWayfarerSlender(PlayerEntity player) {

        int slenderArmorThreshold = SimplySkills.wayfarerConfig.passiveWayfarerSlenderArmorThreshold - 1;
        int slenderSlownessAmplifier = SimplySkills.wayfarerConfig.passiveWayfarerSlenderSlownessAmplifier;
        int frailArmorThreshold = SimplySkills.initiateConfig.passiveInitiateFrailArmorThreshold - 1;
        int frailSlownessAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailSlownessAmplifier;

        if (player.age % 20 == 0) {
            if (player.getArmor() > slenderArmorThreshold && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.wayfarerSlender)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25, slenderSlownessAmplifier));
            }
            if (player.getArmor() > frailArmorThreshold && (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.initiateFrail))){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25, frailSlownessAmplifier));
            }
        }
    }

    public static void passiveRogueSmokeBomb(PlayerEntity player) {
        int radius = SimplySkills.rogueConfig.passiveRogueSmokeBombRadius;
        int chance = SimplySkills.rogueConfig.passiveRogueSmokeBombChance;
        int invisibilityDuration = SimplySkills.rogueConfig.passiveRogueSmokeBombInvisibilityDuration;
        int blindnessDuration = SimplySkills.rogueConfig.passiveRogueSmokeBombBlindnessDuration;
        int blindnessAmplifier = SimplySkills.rogueConfig.passiveRogueSmokeBombBlindnessAmplifier;
        if (player.getRandom().nextInt(100) < chance) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, invisibilityDuration));
            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                        le.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, blindnessDuration, blindnessAmplifier));

                    }
                }
            }
        }
    }

    public static boolean passiveRogueEvasionMastery(PlayerEntity player) {

        int baseEvasionChance = SimplySkills.rogueConfig.passiveRogueEvasionMasteryChance;
        int evasionChanceIncreasePerTier = SimplySkills.rogueConfig.passiveRogueEvasionMasteryChanceIncreasePerTier;
        int masteryEvasionMultiplier = SimplySkills.rogueConfig.passiveRogueEvasionMasterySignatureMultiplier;
        int iframeDuration = SimplySkills.rogueConfig.passiveRogueEvasionMasteryIframeDuration;

        int mastery = baseEvasionChance;
        int evasionMultiplier = 1;

        if (player.hasStatusEffect(EffectRegistry.EVASION))
            evasionMultiplier = masteryEvasionMultiplier;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueEvasionMasterySkilled))
            mastery = mastery + (evasionChanceIncreasePerTier * 2);
        else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueEvasionMasteryProficient))
            mastery = mastery + evasionChanceIncreasePerTier;

        if (player.getRandom().nextInt(100) < (mastery * evasionMultiplier)) {
            if (player.getEquippedStack(EquipmentSlot.HEAD).isEmpty()
                    && player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()
                    && player.getEquippedStack(EquipmentSlot.LEGS).isEmpty()
                    && player.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {

                player.timeUntilRegen = iframeDuration;
                player.world.playSoundFromEntity(null, player, SoundRegistry.FX_SKILL_BACKSTAB,
                        SoundCategory.PLAYERS, 1, 1);
                return true;


            }
        }
        return false;
    }

    public static void passiveRogueOpportunisticMastery(Entity target, PlayerEntity player) {
        int basePoisonDuration = SimplySkills.rogueConfig.passiveRogueOpportunisticMasteryPoisonDuration;
        int basePoisonAmplifier = SimplySkills.rogueConfig.passiveRogueOpportunisticMasteryPoisonAmplifier;
        int poisonDurationIncreasePerTier = SimplySkills.rogueConfig.passiveRogueOpportunisticMasteryPoisonDurationIncreasePerTier;

        int mastery = basePoisonDuration;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueOpportunisticMasterySkilled))
            mastery = mastery + (poisonDurationIncreasePerTier * 2);
        else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueOpportunisticMasteryProficient))
            mastery = mastery + poisonDurationIncreasePerTier;

        if ((target instanceof LivingEntity livingTarget) && player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, mastery, basePoisonAmplifier));
            player.removeStatusEffect(StatusEffects.INVISIBILITY);
        }

    }

    public static void passiveInitiateFrail(PlayerEntity player) {
        int attackThreshold = SimplySkills.initiateConfig.passiveInitiateFrailAttackThreshold;
        int weaknessAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailWeaknessAmplifier;
        int miningFatigueAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailMiningFatigueAmplifier;
        if (player.age % 20 == 0) {
            if (HelperMethods.getAttackDamage(player.getMainHandStack()) > attackThreshold
                    || HelperMethods.getAttackDamage(player.getOffHandStack()) > attackThreshold
                    && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.wayfarerSlender)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25, weaknessAmplifier));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 25, miningFatigueAmplifier));
            }
        }
    }

    public static void signatureRangerDisengage(PlayerEntity player) {
        int radius = SimplySkills.rangerConfig.signatureRangerDisengageRadius;
        int velocity = SimplySkills.rangerConfig.signatureRangerDisengageVelocity;
        int height = SimplySkills.rangerConfig.signatureRangerDisengageHeight;
        int slownessDuration = SimplySkills.rangerConfig.signatureRangerDisengageSlownessDuration;
        int slownessAmplifier = SimplySkills.rangerConfig.signatureRangerDisengageSlownessAmplifier;
        int slowFallDuration = SimplySkills.rangerConfig.signatureRangerDisengageSlowFallDuration;
        int slowFallAmplifier = SimplySkills.rangerConfig.signatureRangerDisengageSlowFallAmplifier;
        Box box = HelperMethods.createBox((LivingEntity) player, radius);
        for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
            if (entities != null) {
                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, slownessDuration, slownessAmplifier));

                }
            }
        }
        player.setVelocity(player.getRotationVector().negate().multiply(+velocity));
        player.setVelocity(player.getVelocity().x, height, player.getVelocity().z); // Prevent player flying to the heavens
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, slowFallDuration, slowFallAmplifier));
        player.velocityModified = true;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationDisengageRecuperate))
            signatureRangerDisengageRecuperate(player);
        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationDisengageExploitation))
            signatureRangerDisengageExploitation(player);

    }
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

    public static void passiveRangerElementalArrowsRenewal(PlayerEntity player) {
        int random = new Random().nextInt(100);
        int renewalChance = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalChance;
        int renewalDuration = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalDuration;
        int renewalMaxStacks = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalMaximumStacks;
        int renewalStacks = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalStacks;
        if (random < renewalChance)
            HelperMethods.incrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS, renewalDuration, renewalStacks, renewalMaxStacks);
    }

    public static void passiveRoguePreparationShadowstrike(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity sPlayerEntity) {
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills_rogue").get()
                    .contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrike)) {
                int dashRange = SimplySkills.rogueConfig.signatureRoguePreparationShadowstrikeRange;
                int dashRadius = SimplySkills.rogueConfig.signatureRoguePreparationShadowstrikeRadius;
                int dashDamageModifier = SimplySkills.rogueConfig.signatureRoguePreparationShadowstrikeDamageModifier;
                int dashDamage = (int) HelperMethods.getAttackDamage(player.getMainHandStack());
                DamageSource dashSource = DamageSource.player(player);
                BlockPos blockPos = player.getBlockPos().offset(player.getMovementDirection(), dashRange);

                Box box = HelperMethods.createBoxBetween(player.getBlockPos(), blockPos, dashRadius);
                for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            le.damage(dashSource, dashDamage * dashDamageModifier);

                            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                    "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeBolt))
                                SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:soul_bolt_lesser", 64, le);
                            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                    "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeVampire))
                                HelperMethods.buffSteal(player, le, true, true);

                        }
                    }
                }

                BlockState blockstate = player.world.getBlockState(blockPos);
                BlockState blockstateUp = player.world.getBlockState(blockPos.up(1));
                for (int i = dashRange; i > 0; i--) {
                    if (blockstate.isAir() && blockstateUp.isAir())
                        break;
                    blockPos = player.getBlockPos().offset(player.getMovementDirection(), i);
                }
                player.teleport(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                player.world.playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                        SoundCategory.PLAYERS, 0.6f, 1.3f);

            }
        }
    }





    // -- Unlock Manager --

    public static void skillTreeUnlockManager(PlayerEntity player, String skillID) {

        //Check if current skill selection is a specialisation path
        if (!skillID.contains(SkillReferencePosition.wizardPath) &&
                !skillID.contains(SkillReferencePosition.berserkerPath) &&
                !skillID.contains(SkillReferencePosition.crusaderPath) &&
                !skillID.contains(SkillReferencePosition.frostguardPath) &&
                !skillID.contains(SkillReferencePosition.roguePath) &&
                !skillID.contains(SkillReferencePosition.rangerPath) &&
                !skillID.contains(SkillReferencePosition.spellbladePath)) {
            return;
        }

        //Prevent unlocking multiple specialisations (This could be configurable in future)
        Collection<String> collection = SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player);
        if (collection.contains("simplyskills_wizard") ||
                collection.contains("simplyskills_spellblade") ||
                collection.contains("simplyskills_ranger") ||
                collection.contains("simplyskills_rogue") ||
                collection.contains("simplyskills_berserker")) {
            return;
        }

        //Process unlock
        if (skillID.contains(SkillReferencePosition.wizardPath)
        && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_wizard")){
            if (SimplySkills.wizardConfig.enableWizardSpecialisation) {
                SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_wizard");
                playUnlockSound(player);
            }
        } else if (skillID.contains(SkillReferencePosition.berserkerPath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_berserker")){
            if (SimplySkills.berserkerConfig.enableBerserkerSpecialisation) {
                SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_berserker");
                playUnlockSound(player);
            }
        } else if (skillID.contains(SkillReferencePosition.roguePath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_rogue")){
            if (SimplySkills.rogueConfig.enableRogueSpecialisation) {
                SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_rogue");
                playUnlockSound(player);
            }
        } else if (skillID.contains(SkillReferencePosition.rangerPath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_ranger")){
            if (SimplySkills.rangerConfig.enableRangerSpecialisation) {
                SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_ranger");
                playUnlockSound(player);
            }
        } else if (skillID.contains(SkillReferencePosition.spellbladePath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_spellblade")){
            if (SimplySkills.spellbladeConfig.enableSpellbladeSpecialisation) {
                SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_spellblade");
                playUnlockSound(player);
            }
        }


    }
    static void playUnlockSound(PlayerEntity player) {
        player.world.playSoundFromEntity(null, player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                SoundCategory.PLAYERS, 1, 1);
    }



    // -- DEBUG --
    public static void debugPrintAttributes(PlayerEntity player) {
        //
        // For checking Spell Power attribute values
        //
        if (player.age % 20 == 0 && player.isSneaking()) {
            String attributeArcane       = SpellPower.getSpellPower(MagicSchool.ARCANE, player).toString();
            String attributeFire         = SpellPower.getSpellPower(MagicSchool.FIRE, player).toString();
            String attributeFrost         = SpellPower.getSpellPower(MagicSchool.FROST, player).toString();
            String attributeHealing      = SpellPower.getSpellPower(MagicSchool.HEALING, player).toString();
            String attributeLightning    = SpellPower.getSpellPower(MagicSchool.LIGHTNING, player).toString();
            String attributeSoul         = SpellPower.getSpellPower(MagicSchool.SOUL, player).toString();

            System.out.println("Arcane: "    + attributeArcane);
            System.out.println("Fire: "      + attributeFire);
            System.out.println("Frost: "     + attributeFrost);
            System.out.println("Healing: "   + attributeHealing);
            System.out.println("Lightning: " + attributeLightning);
            System.out.println("Soul: "      + attributeSoul);
        }
    }

    public static void getSpellCooldown(LivingEntity livingEntity, String spellID) {
        //Identifier spell = new Identifier(spellID);
        //SpellHelper.getCooldownDuration(livingEntity);

    }



}
