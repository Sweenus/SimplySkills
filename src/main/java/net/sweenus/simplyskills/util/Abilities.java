package net.sweenus.simplyskills.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.List;
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
                    && HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.initiateAttuned, player)) {
                int stackThreshold = SimplySkills.initiateConfig.passiveInitiateAttunedStackThreshold -1;
                StatusEffectInstance statusInstance = player.getStatusEffect(list.get(0));
                if (statusInstance != null && statusInstance.getAmplifier() > stackThreshold) {
                    Abilities.passiveInitiateAttuned(player, statusInstance);
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
        if (player.age % frequency == 0 && player.world.isThundering()) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.LIGHTNINGATTUNEMENT, duration, stacks, maxStacks);
        }
    }
    public static void passiveInitiateHasty(PlayerEntity player) {
        int duration = SimplySkills.initiateConfig.passiveInitiateHastyDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateHastyStacks;
        HelperMethods.incrementStatusEffect(player, EffectRegistry.IMMOBILIZE, duration, stacks, 1);
    }

    public static void passiveWarriorSpellbreaker(PlayerEntity player) {
        int spellbreakingDuration = SimplySkills.warriorConfig.passiveWarriorSpellbreakerDuration;
        int spellbreakingChance = SimplySkills.warriorConfig.passiveWarriorSpellbreakerChance;
        if (player.getRandom().nextInt(100) < spellbreakingChance) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLBREAKING, spellbreakingDuration));
        }
    }

    public static void passiveWarriorDeathDefy(PlayerEntity player) {
        int deathDefyFrequency = SimplySkills.warriorConfig.passiveWarriorDeathDefyFrequency;
        int deathDefyAmplifierPerTenPercentHealth = SimplySkills.warriorConfig.passiveWarriorDeathDefyAmplifierPerTenPercentHealth;
        int regen = 0;

        int healthThreshold = SimplySkills.warriorConfig.passiveWarriorDeathDefyHealthThreshold;
        if (player.age % deathDefyFrequency == 0) {
            float playerHealthPercent = ((player.getHealth() / player.getMaxHealth()) * 100);
            if (playerHealthPercent < healthThreshold) {
                if (playerHealthPercent < (healthThreshold - 10))
                    regen = regen + deathDefyAmplifierPerTenPercentHealth;
                if (playerHealthPercent < (healthThreshold - 20))
                    regen = regen + (deathDefyAmplifierPerTenPercentHealth * 2);

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,
                        deathDefyFrequency + 5, regen));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,
                        deathDefyFrequency + 5, regen));
            }
        }
    }

    public static void passiveWarriorGoliath(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.EARTHSHAKER, 200));
    }

    public static void passiveAreaCleanse(PlayerEntity player) {
        if (player.age % 80 == 0) {
            int radius = 12;

            Box box = HelperMethods.createBox(player, radius);
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
                            if (statusEffect != null && statusEffect.getEffectType().equals(EffectRegistry.STEALTH)) {
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

    public static void passiveRogueBackstab(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity livingTarget) {
            int weaknessDuration = SimplySkills.rogueConfig.passiveRogueBackstabWeaknessDuration;
            int weaknessAmplifier = SimplySkills.rogueConfig.passiveRogueBackstabWeaknessAmplifier;
            if (livingTarget.getBodyYaw() < (player.getBodyYaw() + 32) &&
                    livingTarget.getBodyYaw() > (player.getBodyYaw() - 32)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                        weaknessDuration, weaknessAmplifier));
            }
        }
    }

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

                if (HelperMethods.isUnlocked("simplyskills",
                        SkillReferencePosition.rogueFleetfooted, player))
                    HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED,
                            speedDuration, speedStacks, speedMaxStacks);

            }

            if (target != null && !brokenByDamage) {

                if (target instanceof LivingEntity livingTarget) {
                    int deathmarkDuration = SimplySkills.rogueConfig.passiveRogueExploitationDeathMarkDuration;
                    int deathmarkStacks = SimplySkills.rogueConfig.passiveRogueExploitationDeathMarkStacks;

                    if (backstabBonus && HelperMethods.isBehindTarget(player, livingTarget)) {
                        if (HelperMethods.isUnlocked("simplyskills",
                                SkillReferencePosition.rogueExploitation, player))
                            HelperMethods.incrementStatusEffect(
                                    livingTarget,
                                    EffectRegistry.DEATHMARK,
                                    deathmarkDuration,
                                    deathmarkStacks,
                                    3);
                        if (HelperMethods.isUnlocked("simplyskills",
                                SkillReferencePosition.rogueOpportunisticMastery, player))
                            Abilities.passiveRogueOpportunisticMastery(livingTarget, player);
                    }
                }
            }
            player.removeStatusEffect(EffectRegistry.STEALTH);
            player.world.playSoundFromEntity(
                    null, player, SoundRegistry.SOUNDEFFECT36,
                    SoundCategory.PLAYERS, 0.7f, 1.4f);
            if (player.hasStatusEffect(StatusEffects.INVISIBILITY))
                player.removeStatusEffect(StatusEffects.INVISIBILITY);
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
            if (player.getArmor() > armorMasteryThreshold
                    && HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.warriorHeavyArmorMastery, player)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                        heavyArmorMasteryDuration, heavyArmorMasteryAmplifier));
            } else if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.warriorMediumArmorMastery, player)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION,
                        mediumArmorMasteryDuration, mediumArmorMasteryAmplifier));
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

                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.berserkerSwordMasterySkilled, player)
                            && player.getOffHandStack().isEmpty())
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MIGHT,
                                frequency + 5, 0));
                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.berserkerSwordMasteryProficient, player))
                        mastery = mastery + speedAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                            frequency + 5, mastery));
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

                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.berserkerAxeMasterySkilled, player))
                        mastery = mastery + (strengthAmplifierPerTier * 2);
                    else if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.berserkerAxeMasteryProficient, player))
                        mastery = mastery + strengthAmplifierPerTier;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                            frequency + 5, mastery));
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

                if (HelperMethods.isUnlocked("simplyskills",
                        SkillReferencePosition.berserkerIgnorePainSkilled, player))
                    resistanceStacks = resistanceStacks + (resistanceAmplifierPerTier * 2);
                else if (HelperMethods.isUnlocked("simplyskills",
                        SkillReferencePosition.berserkerIgnorePainProficient, player))
                    resistanceStacks = resistanceStacks + resistanceAmplifierPerTier;

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                        frequency + 5, resistanceStacks));
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
                        frequency + 5, weaknessAmplifier));
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
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        count++;
                    }
                }
            }
            if (count > countMax)
                count = countMax;
            if (count > 1)
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, frequency + 5, count -1));
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

                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.bulwarkShieldMasterySkilled, player))
                        mastery = mastery + (shieldMasteryResistanceAmplifierPerTier * 2);
                    else if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.bulwarkShieldMasteryProficient, player))
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
            if (player.getArmor() > slenderArmorThreshold && HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.wayfarerSlender, player)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                        25, slenderSlownessAmplifier));
            }
            if (player.getArmor() > frailArmorThreshold && (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.initiateFrail, player))){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                        25, frailSlownessAmplifier));
            }
        }
    }

    public static void passiveRogueSmokeBomb(PlayerEntity player) {
        int radius = SimplySkills.rogueConfig.passiveRogueSmokeBombRadius;
        int chance = SimplySkills.rogueConfig.passiveRogueSmokeBombChance;
        int auraDuration = SimplySkills.rogueConfig.passiveRogueSmokeBombAuraDuration;
        int blindnessDuration = SimplySkills.rogueConfig.passiveRogueSmokeBombBlindnessDuration;
        int blindnessAmplifier = SimplySkills.rogueConfig.passiveRogueSmokeBombBlindnessAmplifier;
        if (player.getRandom().nextInt(100) < chance) {
            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                        le.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,
                                blindnessDuration, blindnessAmplifier));

                    }
                }
            }
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZINGAURA, auraDuration));
            player.world.playSoundFromEntity(
                    null, player, SoundRegistry.SOUNDEFFECT32,
                    SoundCategory.PLAYERS, 0.4f, 1.2f);
        }
    }

    public static boolean passiveRogueEvasionMastery(PlayerEntity player) {

        int baseEvasionChance = SimplySkills.rogueConfig.passiveRogueEvasionMasteryChance;
        int extraEvasionChance = SimplySkills.rogueConfig.passiveRogueDeflectionIncreasedChance;
        int evasionChanceIncreasePerTier = SimplySkills.rogueConfig.passiveRogueEvasionMasteryChanceIncreasePerTier;
        int masteryEvasionMultiplier = SimplySkills.rogueConfig.passiveRogueEvasionMasterySignatureMultiplier;
        int evasionArmorThreshold = SimplySkills.wayfarerConfig.passiveWayfarerSlenderArmorThreshold - 1;
        int iframeDuration = SimplySkills.rogueConfig.passiveRogueEvasionMasteryIframeDuration;
        int mastery = 0;

        if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.rogueDeflection, player)
                && player.getMainHandStack().getItem() instanceof SwordItem
                && player.getOffHandStack().getItem() instanceof SwordItem)
            mastery = baseEvasionChance + extraEvasionChance;
        else
            mastery = baseEvasionChance;

        int evasionMultiplier = 1;

        if (player.hasStatusEffect(EffectRegistry.EVASION))
            evasionMultiplier = masteryEvasionMultiplier;

        if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.rogueEvasionMasterySkilled, player))
            mastery = mastery + (evasionChanceIncreasePerTier * 2);
        else if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.rogueEvasionMasteryProficient, player))
            mastery = mastery + evasionChanceIncreasePerTier;

        if (player.getRandom().nextInt(100) < (mastery * evasionMultiplier)) {
            if (player.getArmor() < evasionArmorThreshold) {

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

        if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.rogueOpportunisticMasterySkilled, player))
            mastery = mastery + (poisonDurationIncreasePerTier * 2);
        else if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.rogueOpportunisticMasteryProficient, player))
            mastery = mastery + poisonDurationIncreasePerTier;

        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, mastery, basePoisonAmplifier));
        }

    }

    public static void passiveRogueBackstabStealth(PlayerEntity player) {
        int stealthChance = SimplySkills.rogueConfig.passiveRogueBackstabStealthChancePerEnemy;
        int stealthDuration = SimplySkills.rogueConfig.passiveRogueBackstabStealthDuration;
        if (player.age % 20 == 0) {
            Box box = HelperMethods.createBox(player, 8);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) &&
                            le.hasStatusEffect(StatusEffects.WEAKNESS)
                            && HelperMethods.checkFriendlyFire(le, player)) {
                        if (player.getRandom().nextInt(100) < stealthChance) {
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, stealthDuration));
                            player.world.playSoundFromEntity(
                                    null, player, SoundRegistry.SOUNDEFFECT39,
                                    SoundCategory.PLAYERS, 0.6f, 1.6f);
                        }
                    }
                }
            }
        }
    }

    public static void passiveInitiateFrail(PlayerEntity player) {
        int attackThreshold = SimplySkills.initiateConfig.passiveInitiateFrailAttackThreshold;
        int weaknessAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailWeaknessAmplifier;
        int miningFatigueAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailMiningFatigueAmplifier;

        if (HelperMethods.getAttackDamage(player.getMainHandStack()) > attackThreshold
                || HelperMethods.getAttackDamage(player.getOffHandStack()) > attackThreshold
                && HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.wayfarerSlender, player)){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    25, weaknessAmplifier));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,
                    25, miningFatigueAmplifier));
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

                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                            slownessDuration, slownessAmplifier));

                }
            }
        }
        player.setVelocity(player.getRotationVector().negate().multiply(+velocity));
        player.setVelocity(player.getVelocity().x, height, player.getVelocity().z); // Prevent player flying to the heavens
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                slowFallDuration, slowFallAmplifier));
        player.velocityModified = true;

        if (HelperMethods.isUnlocked("simplyskills_ranger",
                SkillReferencePosition.rangerSpecialisationDisengageRecuperate, player))
            signatureRangerDisengageRecuperate(player);
        if (HelperMethods.isUnlocked("simplyskills_ranger",
                SkillReferencePosition.rangerSpecialisationDisengageExploitation, player))
            signatureRangerDisengageExploitation(player);
        if (HelperMethods.isUnlocked("simplyskills_ranger",
                SkillReferencePosition.rangerSpecialisationDisengageMarksman, player)) {
            int marksmanDuration = SimplySkills.rangerConfig.signatureRangerDisengageMarksmanDuration;
            int marksmanStacks = SimplySkills.rangerConfig.signatureRangerDisengageMarksmanStacks;
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMAN, marksmanDuration, marksmanStacks, 99);
        }

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
            HelperMethods.incrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS,
                    renewalDuration, renewalStacks, renewalMaxStacks);
    }

    public static void passiveRoguePreparationShadowstrike(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity sPlayerEntity) {
            if (HelperMethods.isUnlocked("simplyskills_rogue",
                    SkillReferencePosition.rogueSpecialisationPreparationShadowstrike, player)) {
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

                            if (HelperMethods.isUnlocked("simplyskills_rogue",
                                    SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeBolt, player))
                                SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        "simplyskills:soul_bolt_lesser", 64, le);
                            if (HelperMethods.isUnlocked("simplyskills_rogue",
                                    SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeVampire, player))
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

    public static boolean skillTreeUnlockManager(PlayerEntity player, String categoryID) {

        if (HelperMethods.stringContainsAny(categoryID, SimplySkills.getSpecialisations())) {

            if (SimplySkills.generalConfig.removeUnlockRestrictions)
                return false;

            //Prevent unlocking multiple specialisations (kinda cursed ngl)
            List<String> specialisationList = SimplySkills.getSpecialisationsAsArray();
            for (String s : specialisationList) {
                if (categoryID.contains(s)) {
                    if (HelperMethods.stringContainsAny(
                            SkillsAPI.getUnlockedCategories((ServerPlayerEntity)player).toString(),
                            SimplySkills.getSpecialisations())) {
                        //System.out.println(player + " attempted to unlock a second specialisation. Denied.");
                        return true;
                    }
                }
            }


            //Process unlock
            if (categoryID.contains("simplyskills_wizard")
                    && !HelperMethods.isUnlocked("simplyskills_wizard", null, player)) {
                if (SimplySkills.wizardConfig.enableWizardSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills_berserker")
                    && !HelperMethods.isUnlocked("simplyskills_berserker", null, player)) {
                if (SimplySkills.berserkerConfig.enableBerserkerSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills_rogue")
                    && !HelperMethods.isUnlocked("simplyskills_rogue", null, player)) {
                if (SimplySkills.rogueConfig.enableRogueSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills_ranger")
                    && !HelperMethods.isUnlocked("simplyskills_ranger", null, player)) {
                if (SimplySkills.rangerConfig.enableRangerSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills_spellblade")
                    && !HelperMethods.isUnlocked("simplyskills_spellblade", null, player)) {
                if (SimplySkills.spellbladeConfig.enableSpellbladeSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            }
        }
        return false;
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
