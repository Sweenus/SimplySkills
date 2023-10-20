package net.sweenus.simplyskills.abilities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.entities.SimplySkillsArrowEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AbilityEffects {


    public static void effectBerserkerBerserking(Entity target, PlayerEntity player) {

        if ((target instanceof LivingEntity livingTarget) && player.hasStatusEffect(EffectRegistry.BERSERKING)) {
            int berserkingSubEffectDuration = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSubEffectDuration;
            int berserkingSubEffectMaxAmplifier = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSubEffectMaxAmplifier;
            HelperMethods.incrementStatusEffect(player, StatusEffects.HASTE, berserkingSubEffectDuration,
                    1, berserkingSubEffectMaxAmplifier);
            HelperMethods.incrementStatusEffect(player, StatusEffects.STRENGTH, berserkingSubEffectDuration,
                    1, berserkingSubEffectMaxAmplifier);
            HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED, berserkingSubEffectDuration,
                    1, berserkingSubEffectMaxAmplifier);
        }
    }

    public static void effectBerserkerBloodthirsty(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY)) {
            float bloodthirstyHealPercent = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyHealPercent;
            float healAmount = player.getMaxHealth() * bloodthirstyHealPercent;
            player.heal(healAmount);
        }
    }

    public static void effectBerserkerBloodthirstyTireless(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY)) {
            int bloodthirstyTirelessChance = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyTirelessChance;
            if (player.getRandom().nextInt(100) < bloodthirstyTirelessChance) {
                HelperMethods.decrementStatusEffect(player, EffectRegistry.EXHAUSTION);
            }
        }
    }

    public static void effectBerserkerBloodthirstyTremor(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY)) {
            int bloodthirstyTremoreChance = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyTremorChance;
            if (player.getRandom().nextInt(100) < bloodthirstyTremoreChance) {
                HelperMethods.incrementStatusEffect(player, EffectRegistry.EARTHSHAKER, 30,
                        1, 1);
            }
        }
    }

    public static void effectBerserkerRampage(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.RAMPAGE)) {
            int rampageSubEffectDuration = SimplySkills.berserkerConfig.signatureBerserkerRampageSubEffectDuration;
            int rampageSubEffectMaxAmplifier = SimplySkills.berserkerConfig.signatureBerserkerRampageSubEffectMaxAmplifier;

            List<StatusEffect> list = new ArrayList<>();
            list.add(StatusEffects.STRENGTH);
            list.add(StatusEffects.SPEED);
            list.add(StatusEffects.RESISTANCE);
            list.add(StatusEffects.HASTE);

            Random rand = new Random();
            StatusEffect randomStatus = list.get(rand.nextInt(list.size()));
            HelperMethods.incrementStatusEffect(player, randomStatus, rampageSubEffectDuration, 1,
                    rampageSubEffectMaxAmplifier);
        }
    }

    public static void effectRogueSiphoningStrikes(Entity target, PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.SIPHONINGSTRIKES)) {
            if (target instanceof LivingEntity livingTarget) {
                double leechMultiplier = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesLeechMultiplier;

                double attackValue = Objects.requireNonNull(
                        player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).getValue();
                float healAmount = (float) (attackValue * leechMultiplier);
                player.heal(healAmount);

                HelperMethods.decrementStatusEffect(player, EffectRegistry.SIPHONINGSTRIKES);

                for (StatusEffectInstance statusEffect : livingTarget.getStatusEffects()) {
                    if (statusEffect != null && statusEffect.getEffectType().isBeneficial()) {
                        livingTarget.removeStatusEffect(statusEffect.getEffectType());
                        break;
                    }
                }

                if (HelperMethods.isUnlocked("simplyskills:rogue",
                        SkillReferencePosition.rogueSpecialisationSiphoningStrikesVanish, player))
                    AbilityEffects.effectRogueSiphoningStrikesVanish(player);

            }
        }
    }

    public static void effectRogueFanOfBlades(PlayerEntity player) {
        int fobFrequency = SimplySkills.rogueConfig.signatureRogueFanOfBladesBaseFrequency;
        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesAssault, player))
            fobFrequency = SimplySkills.rogueConfig.signatureRogueFanOfBladesEnhancedFrequency;
        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades, player) &&
                player.hasStatusEffect(EffectRegistry.FANOFBLADES) && player.age % fobFrequency == 0) {
            int fobRange = SimplySkills.rogueConfig.signatureRogueFanOfBladesRange;
            int fobRadius = SimplySkills.rogueConfig.signatureRogueFanOfBladesRadius;
            int disenchantDuration = SimplySkills.rogueConfig.signatureRogueFanOfBladesDisenchantDuration;

            BlockPos blockPos = player.getBlockPos().offset(player.getMovementDirection(), fobRange);
            BlockState blockstate = player.getWorld().getBlockState(blockPos);
            BlockState blockstateUp = player.getWorld().getBlockState(blockPos.up(1));
            for (int i = fobRange; i > 0; i--) {
                if (blockstate.isAir() && blockstateUp.isAir())
                    break;
                blockPos = player.getBlockPos().offset(player.getMovementDirection(), i);
            }

            Box box = HelperMethods.createBoxBetween(player.getBlockPos(), blockPos, fobRadius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                        if (HelperMethods.isUnlocked("simplyskills:rogue",
                                SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesAssault, player))
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:fan_of_blades_assault", fobRange * 2, le);
                        else
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:fan_of_blades", fobRange * 2, le);

                        if (HelperMethods.isUnlocked("simplyskills:rogue",
                                SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesDisenchantment, player))
                            le.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISENCHANTMENT, disenchantDuration, 0, false ,false));

                    }
                }
            }
            if (HelperMethods.isUnlocked("simplyskills:rogue",
                    SkillReferencePosition.rogueBladestorm, player) && player.getRandom().nextInt(100) < 35 + fobFrequency)
                HelperMethods.incrementStatusEffect(player, EffectRegistry.BLADESTORM, 400, 1, 20);
            HelperMethods.decrementStatusEffect(player, EffectRegistry.FANOFBLADES);
        }
    }

    public static void effectRogueSiphoningStrikesVanish(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.SIPHONINGSTRIKES)) {
            if (player.hasStatusEffect(EffectRegistry.REVEALED))
                player.removeStatusEffect(EffectRegistry.REVEALED);
        }
    }



    public static boolean effectRangerElementalArrows(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.ELEMENTALARROWS)) {


            Vec3d blockpos = null;
            int radius = SimplySkills.rangerConfig.effectRangerElementalArrowsRadius;
            int increase = SimplySkills.rangerConfig.effectRangerElementalArrowsRadiusIncreasePerTier;
            int targetingRange = SimplySkills.rangerConfig.effectRangerElementalArrowsTargetingRange;
            int arrowCount = 1;
            int increasedArrowCount = 6;
            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusOne, player))
                radius = radius + increase;
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusTwo, player))
                radius = radius + (increase * 2);
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusThree, player))
                radius = radius + (increase * 3);

            List<String> list = new ArrayList<>();
            list.add("simplyskills:frost_arrow_rain");
            list.add("simplyskills:fire_arrow_rain");
            list.add("simplyskills:lightning_arrow_rain");

            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsFireAttuned, player)) {
                list.remove("simplyskills:frost_arrow_rain");
                list.remove("simplyskills:lightning_arrow_rain");
            }
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsFrostAttuned, player)) {
                list.remove("simplyskills:fire_arrow_rain");
                list.remove("simplyskills:lightning_arrow_rain");
            }
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsLightningAttuned, player)) {
                list.remove("simplyskills:fire_arrow_rain");
                list.remove("simplyskills:frost_arrow_rain");
            }

            HelperMethods.decrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS);
            
            if (HelperMethods.getTargetedEntity(player, targetingRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, targetingRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, targetingRange);

            if (blockpos != null) {
                int xpos = (int) blockpos.getX();
                int ypos = (int) blockpos.getY();
                int zpos = (int) blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, radius);
                for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY).size() <= 1)
                        arrowCount = increasedArrowCount;

                    for (int i = arrowCount; i > 0; i--) {
                        if (entities != null) {
                            Random rand = new Random();
                            String randomSpell = list.get(rand.nextInt(list.size()));
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                                SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        randomSpell,
                                        512, le);
                            }
                        }
                    }
                }
            }


            return true;
        }
        return false;
    }

    public static boolean effectRangerMarksman(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.MARKSMAN)
                && HelperMethods.getAttackDamage(player.getMainHandStack()) < 1
                && HelperMethods.getAttackDamage(player.getOffHandStack()) < 1) {


            Vec3d blockpos = null;
            int targetingRange = SimplySkills.rangerConfig.effectRangerElementalArrowsTargetingRange;

            if (HelperMethods.getTargetedEntity(player, targetingRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, targetingRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, targetingRange);

            if (blockpos != null) {
                int xpos = (int) blockpos.getX();
                int ypos = (int) blockpos.getY();
                int zpos = (int) blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, 1);
                for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        String spell = "simplyskills:physical_bow_snipe";
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    spell,
                                    512, le);
                            HelperMethods.decrementStatusEffect(player, EffectRegistry.MARKSMAN);
                        }
                    }
                }
            }


            return true;
        }
        return false;
    }


    public static boolean effectRangerArrowRain(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.ARROWRAIN)) {

            int arrowRainRadius = SimplySkills.rangerConfig.effectRangerArrowRainRadius;
            int arrowRainRadiusIncrease = SimplySkills.rangerConfig.effectRangerArrowRainRadiusIncreasePerTier;
            int arrowRainChance = SimplySkills.rangerConfig.effectRangerArrowRainArrowDensity;
            int arrowRainVolleys = SimplySkills.rangerConfig.effectRangerArrowRainVolleys;
            int arrowRainVolleyIncrease = SimplySkills.rangerConfig.effectRangerArrowRainVolleyIncreasePerTier;
            int arrowRainRange = SimplySkills.rangerConfig.effectRangerArrowRainRange;
            boolean preventShotgun = false;
            int projectileLimiter = 0;
            int projectileLimiterCap = 30;

            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationArrowRainRadiusOne, player))
                arrowRainRadius = arrowRainRadius + arrowRainRadiusIncrease;
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationArrowRainRadiusTwo, player))
                arrowRainRadius = arrowRainRadius + (arrowRainRadiusIncrease * 2);
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationArrowRainRadiusThree, player))
                arrowRainRadius = arrowRainRadius + (arrowRainRadiusIncrease * 3);

            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationArrowRainVolleyOne, player))
                arrowRainVolleys = arrowRainVolleys + arrowRainVolleyIncrease;
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationArrowRainVolleyTwo, player))
                arrowRainVolleys = arrowRainVolleys + (arrowRainVolleyIncrease * 2);
            else if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerSpecialisationArrowRainVolleyThree, player))
                arrowRainVolleys = arrowRainVolleys + (arrowRainVolleyIncrease * 3);


            Vec3d blockpos = HelperMethods.getPositionLookingAt(player, arrowRainRange);
            if (blockpos != null) {
                int xpos = (int) blockpos.getX() - arrowRainRadius;
                int ypos = (int) blockpos.getY();
                int zpos = (int) blockpos.getZ() - arrowRainRadius;


                for (int x = arrowRainRadius * 2; x > 0; x--) {
                    for (int z = arrowRainRadius * 2; z > 0; z--) {
                        for (int i = arrowRainVolleys; i > 0; i--) {
                            BlockPos spawnPosition = new BlockPos(xpos + x,
                                    ypos + 25 + (player.getRandom().nextInt(15) * arrowRainVolleys + 1),
                                    zpos + z);

                            if (player.getRandom().nextInt(100) < arrowRainChance
                                    && player.getWorld().getBlockState(spawnPosition).isAir()) {
                                SimplySkillsArrowEntity arrowEntity = new SimplySkillsArrowEntity(EntityType.ARROW,
                                        player.getWorld());
                                arrowEntity.updatePosition(spawnPosition.getX(),
                                        spawnPosition.getY(),
                                        spawnPosition.getZ());
                                arrowEntity.setOwner(player);
                                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                                arrowEntity.setVelocity(0, -0.5, 0);
                                player.getWorld().spawnEntity(arrowEntity);

                                if (HelperMethods.isUnlocked("simplyskills:ranger",
                                        SkillReferencePosition.rangerSpecialisationArrowRainElemental, player)) {

                                    BlockPos blockPos = player.getBlockPos().offset(player.getMovementDirection(), 3);
                                    Box box = HelperMethods.createBoxBetween(player.getBlockPos(), blockPos, 3);
                                    for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                                        if (entities != null) {
                                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                                                preventShotgun = true;
                                                projectileLimiterCap = 4;
                                            }
                                        }
                                    }

                                    arrowEntity.addEffect(new StatusEffectInstance((StatusEffects.SLOWNESS)));
                                    if (!preventShotgun || projectileLimiter < projectileLimiterCap) {
                                        if (player.getRandom().nextInt(100) < 5) {
                                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                    "simplyskills:fire_arrow_rain", 512, arrowEntity);
                                            arrowEntity.setInvisible(true);
                                            projectileLimiter++;
                                        } else if (player.getRandom().nextInt(100) < 15) {
                                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                    "simplyskills:frost_arrow_rain", 512, arrowEntity);
                                            arrowEntity.setInvisible(true);
                                            projectileLimiter++;
                                        } else if (player.getRandom().nextInt(100) < 25) {
                                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                    "simplyskills:lightning_arrow_rain", 512, arrowEntity);
                                            arrowEntity.setInvisible(true);
                                            projectileLimiter++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                HelperMethods.decrementStatusEffect(player, EffectRegistry.ARROWRAIN);
            }
            return true;
        }
        return false;
    }

    public static void effectWizardFrostVolley(PlayerEntity player) {
        int frequency = SimplySkills.wizardConfig.signatureWizardIceCometVolleyFrequency;

        if (HelperMethods.isUnlocked("simplyskills:wizard",
                SkillReferencePosition.wizardSpecialisationIceCometVolley, player) &&
        player.hasStatusEffect(EffectRegistry.FROSTVOLLEY) && player.age % frequency == 0) {
            Vec3d blockpos = null;
            int volleyRange = SimplySkills.wizardConfig.signatureWizardIceCometVolleyRange;

            //Frost Bolt
            if (HelperMethods.getTargetedEntity(player, volleyRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, volleyRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, volleyRange);

            if (blockpos != null) {
                int xpos = (int) blockpos.getX();
                int ypos = (int) blockpos.getY();
                int zpos = (int) blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
                for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:frost_arrow",
                                    3, le);
                            HelperMethods.decrementStatusEffect(player, EffectRegistry.FROSTVOLLEY);
                            break;
                        }
                    }
                }
            }
        }
    }
    public static void effectWizardArcaneVolley(PlayerEntity player) {
        int volleyFrequency = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyFrequency;

        if (HelperMethods.isUnlocked("simplyskills:wizard",
                SkillReferencePosition.wizardSpecialisationArcaneBoltVolley, player) &&
                player.hasStatusEffect(EffectRegistry.ARCANEVOLLEY) && player.age % volleyFrequency == 0) {
            Vec3d blockpos = null;
            int volleyRange = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyRange;

            //Arcane Bolt
            if (HelperMethods.getTargetedEntity(player, volleyRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, volleyRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, volleyRange);

            if (blockpos != null) {
                int xpos = (int) blockpos.getX();
                int ypos = (int) blockpos.getY();
                int zpos = (int) blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
                for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:arcane_bolt_lesser",
                                    3, le);
                            HelperMethods.decrementStatusEffect(player, EffectRegistry.ARCANEVOLLEY);
                            break;
                        }
                    }
                }
            }
        }
    }
    public static void effectWizardMeteoricWrath(PlayerEntity player) {
        int frequency = SimplySkills.wizardConfig.signatureWizardMeteoricWrathFrequency;

        if (HelperMethods.isUnlocked("simplyskills:wizard",
                SkillReferencePosition.wizardSpecialisationMeteorShowerWrath, player) &&
                player.hasStatusEffect(EffectRegistry.METEORICWRATH) && player.age % frequency == 0) {
            int chance = SimplySkills.wizardConfig.signatureWizardMeteoricWrathChance;
            int radius = SimplySkills.wizardConfig.signatureWizardMeteoricWrathRadius;
            int baseRenewalChance = SimplySkills.wizardConfig.signatureWizardMeteoricWrathRenewalBaseChance;
            int renewalChancePerTier = SimplySkills.wizardConfig.signatureWizardMeteoricWrathRenewalChanceIncreasePerTier;
            String spellIdentifier = "simplyskills:fire_meteor_small";


            if (SignatureAbilities.castSpellEngineAOE(player, spellIdentifier, radius, chance, true)) {
                int renewalChance = 0;
                if (HelperMethods.isUnlocked("simplyskills:wizard",
                        SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrath, player))
                    renewalChance = baseRenewalChance;
                else if (HelperMethods.isUnlocked("simplyskills:wizard",
                        SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrathTwo, player))
                    renewalChance = baseRenewalChance + renewalChancePerTier;
                else if (HelperMethods.isUnlocked("simplyskills:wizard",
                        SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrathThree, player))
                    renewalChance = baseRenewalChance + (renewalChancePerTier * 2);
                if (player.getRandom().nextInt(100) > renewalChance)
                    HelperMethods.decrementStatusEffect(player, EffectRegistry.METEORICWRATH);
            }
        }

    }

    public static void effectSpellbladeSpellweaving(Entity target, PlayerEntity player) {
        int chance = SimplySkills.spellbladeConfig.passiveSpellbladeSpellweavingChance;
        int spellweaverHasteDuration = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverHasteDuration;
        int spellweaverHasteStacks = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverHasteStacks;
        int spellweaverHasteMaxStacks = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverHasteMaxStacks;
        int spellweaverRegenerationDuration = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverRegenerationDuration;
        int spellweaverRegenerationStacks = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverRegenerationStacks;
        int spellweaverRegenerationMaxStacks = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverRegenerationMaxStacks;
        int spellweaverRegenerationChance = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverRegenerationChance;

        if (player.hasStatusEffect(EffectRegistry.SPELLWEAVER) &&
                (HelperMethods.isUnlocked("simplyskills:spellblade",
                        SkillReferencePosition.spellbladeSpecialisationSpellweaver, player)))
            chance = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverChance;

        List<String> list = new ArrayList<>();
        list.add("simplyskills:frost_arrow");
        list.add("simplyskills:fire_arrow");
        list.add("simplyskills:lightning_arrow");
        list.add("simplyskills:arcane_bolt");
        list.add("simplyskills:arcane_bolt_lesser");
        list.add("simplyskills:ice_comet");
        list.add("simplyskills:fire_meteor");
        list.add("simplyskills:static_discharge");
        int spellChoice = player.getRandom().nextInt(list.size());

        if ((target instanceof LivingEntity livingTarget) && player.getRandom().nextInt(100) < chance) {
            SignatureAbilities.castSpellEngineIndirectTarget(player,
                    list.get(spellChoice),
                    8, livingTarget);

            if (HelperMethods.isUnlocked("simplyskills:spellblade",
                    SkillReferencePosition.spellbladeSpecialisationSpellweaverHaste, player))
                HelperMethods.incrementStatusEffect(player, StatusEffects.HASTE, spellweaverHasteDuration,
                        spellweaverHasteStacks, spellweaverHasteMaxStacks);
            if (HelperMethods.isUnlocked("simplyskills:spellblade",
                    SkillReferencePosition.spellbladeSpecialisationSpellweaverRegeneration, player) &&
                    player.getRandom().nextInt(100) < spellweaverRegenerationChance)
                HelperMethods.incrementStatusEffect(player, StatusEffects.REGENERATION, spellweaverRegenerationDuration,
                        spellweaverRegenerationStacks, spellweaverRegenerationMaxStacks);
        }
    }




}
