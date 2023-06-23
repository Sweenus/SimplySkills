package net.sweenus.simplyskills.util;

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
import net.sweenus.simplyskills.entity.SimplySkillsArrowEntity;
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


            Vec3d blockpos = null;
            int radius = 4;
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusOne))
                radius = 6;
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusTwo))
                radius = 8;
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRadiusThree))
                radius = 10;

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
            
            if (HelperMethods.getTargetedEntity(player, 120) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, 120).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, 120);

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

            int arrowRainRadius = 3;
            int arrowRainChance = 25;
            int arrowRainVolleys = 2;

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainRadiusOne))
                arrowRainRadius = 4;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainRadiusTwo))
                arrowRainRadius = 5;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainRadiusThree))
                arrowRainRadius = 6;

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainVolleyOne))
                arrowRainVolleys = 3;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainVolleyTwo))
                arrowRainVolleys = 4;
            else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationArrowRainVolleyThree))
                arrowRainVolleys = 5;


            Vec3d blockpos = HelperMethods.getPositionLookingAt(player, 64);
            if (blockpos != null) {
                //System.out.println(blockpos);
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
                                        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:fire_arrow_rain", 512, arrowEntity);
                                        arrowEntity.setInvisible(true);
                                    } else if (player.getRandom().nextInt(100) < 15) {
                                        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:frost_arrow_rain", 512, arrowEntity);
                                        arrowEntity.setInvisible(true);
                                    } else if (player.getRandom().nextInt(100) < 25) {
                                        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:lightning_arrow_rain", 512, arrowEntity);
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

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                .contains(SkillReferencePosition.wizardSpecialisationIceCometVolley) &&
        player.hasStatusEffect(EffectRegistry.FROSTVOLLEY) && player.age % 20 == 0) {
            Vec3d blockpos = null;

            //Arcane Bolt
            if (HelperMethods.getTargetedEntity(player, 120) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, 120).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, 120);

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

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltVolley) &&
                player.hasStatusEffect(EffectRegistry.ARCANEVOLLEY) && player.age % 10 == 0) {
            Vec3d blockpos = null;

            //Arcane Bolt
            if (HelperMethods.getTargetedEntity(player, 120) !=null)
                blockpos = HelperMethods.getTargetedEntity(player, 120).getPos();

            if (blockpos == null)
                blockpos = HelperMethods.getPositionLookingAt(player, 120);

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

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerWrath) &&
                player.hasStatusEffect(EffectRegistry.METEORICWRATH) && player.age % 15 == 0) {
            int chance = 35;
            int radius = 12;
            String spellIdentifier = "simplyskills:fire_meteor_small";


            if (SignatureAbilities.castSpellEngineAOE(player, spellIdentifier, radius, chance, true)) {
                int renewalChance = 0;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrath))
                    renewalChance = 10;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrathTwo))
                    renewalChance = 30;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerRenewingWrathThree))
                    renewalChance = 50;
                if (player.getRandom().nextInt(100) > renewalChance)
                    HelperMethods.decrementStatusEffect(player, EffectRegistry.METEORICWRATH);
            }
        }

    }




}
