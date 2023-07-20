package net.sweenus.simplyskills.util;

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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.entities.SimplySkillsArrowEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AbilityEffects {


    public static void effectBerserkerBerserking(Entity target, PlayerEntity player) {

        if ((target instanceof LivingEntity livingTarget) && player.hasStatusEffect(EffectRegistry.BERSERKING)) {
            int berserkingSubEffectDuration = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSubEffectDuration;
            int berserkingSubEffectMaxAmplifier = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSubEffectMaxAmplifier;
            HelperMethods.incrementStatusEffect(player, StatusEffects.HASTE, berserkingSubEffectDuration, 1, berserkingSubEffectMaxAmplifier);
            HelperMethods.incrementStatusEffect(player, StatusEffects.STRENGTH, berserkingSubEffectDuration, 1, berserkingSubEffectMaxAmplifier);
            HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED, berserkingSubEffectDuration, 1, berserkingSubEffectMaxAmplifier);
        }
    }

    public static void effectBerserkerBloodthirsty(PlayerEntity player, LivingEntity livingTarget) {

        if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY)) {
            float bloodthirstyHealPercent = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyHealPercent;
            float healAmount = player.getMaxHealth() * bloodthirstyHealPercent;
            player.heal(healAmount);
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
            }
        }
    }

    public static void effectRogueFanOfBlades(PlayerEntity player) {
        int fobFrequency = SimplySkills.rogueConfig.signatureRogueFanOfBladesBaseFrequency;
        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesAssault))
            fobFrequency = SimplySkills.rogueConfig.signatureRogueFanOfBladesEnhancedFrequency;
        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_rogue").get()
                .contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades) &&
                player.hasStatusEffect(EffectRegistry.FANOFBLADES) && player.age % fobFrequency == 0) {
            int fobRange = SimplySkills.rogueConfig.signatureRogueFanOfBladesRange;
            int fobRadius = SimplySkills.rogueConfig.signatureRogueFanOfBladesRadius;
            int disenchantDuration = SimplySkills.rogueConfig.signatureRogueFanOfBladesDisenchantDuration;

            BlockPos blockPos = player.getBlockPos().offset(player.getMovementDirection(), fobRange);
            BlockState blockstate = player.world.getBlockState(blockPos);
            BlockState blockstateUp = player.world.getBlockState(blockPos.up(1));
            for (int i = fobRange; i > 0; i--) {
                if (blockstate.isAir() && blockstateUp.isAir())
                    break;
                blockPos = player.getBlockPos().offset(player.getMovementDirection(), i);
            }

            Box box = HelperMethods.createBoxBetween(player.getBlockPos(), blockPos, fobRadius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesAssault))
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:fan_of_blades_assault", fobRange * 2, le);
                        else
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:fan_of_blades", fobRange * 2, le);

                        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesDisenchantment))
                            le.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISENCHANTMENT, disenchantDuration));

                    }
                }
            }
            HelperMethods.decrementStatusEffect(player, EffectRegistry.FANOFBLADES);
        }
    }



    public static boolean effectRangerElementalArrows(PlayerEntity player) {

        if (player.hasStatusEffect(EffectRegistry.ELEMENTALARROWS)) {


            Vec3d blockpos = null;
            int radius = SimplySkills.rangerConfig.effectRangerElementalArrowsRadius;
            int increase = SimplySkills.rangerConfig.effectRangerElementalArrowsRadiusIncreasePerTier;
            int targetingRange = SimplySkills.rangerConfig.effectRangerElementalArrowsTargetingRange;
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusOne))
                radius = radius + increase;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusTwo))
                radius = radius + (increase * 2);
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusThree))
                radius = radius + (increase * 3);

            List<String> list = new ArrayList<>();
            list.add("simplyskills:frost_arrow_rain");
            list.add("simplyskills:fire_arrow_rain");
            list.add("simplyskills:lightning_arrow_rain");

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsFireAttuned)) {
                list.remove("simplyskills:frost_arrow_rain");
                list.remove("simplyskills:lightning_arrow_rain");
            }
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsFrostAttuned)) {
                list.remove("simplyskills:fire_arrow_rain");
                list.remove("simplyskills:lightning_arrow_rain");
            }
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsLightningAttuned)) {
                list.remove("simplyskills:fire_arrow_rain");
                list.remove("simplyskills:frost_arrow_rain");
            }


            HelperMethods.decrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS);
            
            if (HelperMethods.getTargetedEntity(player, targetingRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, targetingRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, targetingRange);

            if (blockpos != null) {
                double xpos = blockpos.getX();
                double ypos = blockpos.getY();
                double zpos = blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, radius);
                for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

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

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainRadiusOne))
                arrowRainRadius = arrowRainRadius + arrowRainRadiusIncrease;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainRadiusTwo))
                arrowRainRadius = arrowRainRadius + (arrowRainRadiusIncrease * 2);
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainRadiusThree))
                arrowRainRadius = arrowRainRadius + (arrowRainRadiusIncrease * 3);

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainVolleyOne))
                arrowRainVolleys = arrowRainVolleys + arrowRainVolleyIncrease;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainVolleyTwo))
                arrowRainVolleys = arrowRainVolleys + (arrowRainVolleyIncrease * 2);
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainVolleyThree))
                arrowRainVolleys = arrowRainVolleys + (arrowRainVolleyIncrease * 3);


            Vec3d blockpos = HelperMethods.getPositionLookingAt(player, arrowRainRange);
            if (blockpos != null) {
                double xpos = blockpos.getX() - arrowRainRadius;
                double ypos = blockpos.getY();
                double zpos = blockpos.getZ() - arrowRainRadius;


                for (int x = arrowRainRadius * 2; x > 0; x--) {
                    for (int z = arrowRainRadius * 2; z > 0; z--) {
                        for (int i = arrowRainVolleys; i > 0; i--) {
                            BlockPos spawnPosition = new BlockPos(xpos + x, ypos + 25 + (player.getRandom().nextInt(15) * arrowRainVolleys + 1), zpos + z);

                            if (player.getRandom().nextInt(100) < arrowRainChance
                                    && player.world.getBlockState(spawnPosition).isAir()) {
                                SimplySkillsArrowEntity arrowEntity = new SimplySkillsArrowEntity(EntityType.ARROW, player.world);
                                arrowEntity.updatePosition(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
                                arrowEntity.setOwner(player);
                                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                                arrowEntity.setVelocity(0, -0.5, 0);
                                player.world.spawnEntity(arrowEntity);

                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainElemental)) {
                                    arrowEntity.addEffect(new StatusEffectInstance((StatusEffects.SLOWNESS)));
                                    if (player.getRandom().nextInt(100) < 5) {
                                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                "simplyskills:fire_arrow_rain", 512, arrowEntity);
                                        arrowEntity.setInvisible(true);
                                    } else if (player.getRandom().nextInt(100) < 15) {
                                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                "simplyskills:frost_arrow_rain", 512, arrowEntity);
                                        arrowEntity.setInvisible(true);
                                    } else if (player.getRandom().nextInt(100) < 25) {
                                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                "simplyskills:lightning_arrow_rain", 512, arrowEntity);
                                        arrowEntity.setInvisible(true);
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

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                .contains(SkillReferencePosition.wizardSpecialisationIceCometVolley) &&
        player.hasStatusEffect(EffectRegistry.FROSTVOLLEY) && player.age % frequency == 0) {
            Vec3d blockpos = null;
            int volleyRange = SimplySkills.wizardConfig.signatureWizardIceCometVolleyRange;

            //Frost Bolt
            if (HelperMethods.getTargetedEntity(player, volleyRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, volleyRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, volleyRange);

            if (blockpos != null) {
                double xpos = blockpos.getX();
                double ypos = blockpos.getY();
                double zpos = blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
                for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

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

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltVolley) &&
                player.hasStatusEffect(EffectRegistry.ARCANEVOLLEY) && player.age % volleyFrequency == 0) {
            Vec3d blockpos = null;
            int volleyRange = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyRange;

            //Arcane Bolt
            if (HelperMethods.getTargetedEntity(player, volleyRange) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, volleyRange).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, volleyRange);

            if (blockpos != null) {
                double xpos = blockpos.getX();
                double ypos = blockpos.getY();
                double zpos = blockpos.getZ();
                BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
                for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

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

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerWrath) &&
                player.hasStatusEffect(EffectRegistry.METEORICWRATH) && player.age % frequency == 0) {
            int chance = SimplySkills.wizardConfig.signatureWizardMeteoricWrathChance;
            int radius = SimplySkills.wizardConfig.signatureWizardMeteoricWrathRadius;
            int baseRenewalChance = SimplySkills.wizardConfig.signatureWizardMeteoricWrathRenewalBaseChance;
            int renewalChancePerTier = SimplySkills.wizardConfig.signatureWizardMeteoricWrathRenewalChanceIncreasePerTier;
            String spellIdentifier = "simplyskills:fire_meteor_small";


            if (SignatureAbilities.castSpellEngineAOE(player, spellIdentifier, radius, chance, true)) {
                int renewalChance = 0;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrath))
                    renewalChance = baseRenewalChance;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrathTwo))
                    renewalChance = baseRenewalChance + renewalChancePerTier;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrathThree))
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
                (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_spellblade").get()
                        .contains(SkillReferencePosition.spellbladeSpecialisationSpellweaver)))
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

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_spellblade").get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationSpellweaverHaste))
                HelperMethods.incrementStatusEffect(player, StatusEffects.HASTE, spellweaverHasteDuration,
                        spellweaverHasteStacks, spellweaverHasteMaxStacks);
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_spellblade").get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationSpellweaverRegeneration) &&
                    player.getRandom().nextInt(100) < spellweaverRegenerationChance)
                HelperMethods.incrementStatusEffect(player, StatusEffects.REGENERATION, spellweaverRegenerationDuration,
                        spellweaverRegenerationStacks, spellweaverRegenerationMaxStacks);
        }
    }




}
