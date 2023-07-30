package net.sweenus.simplyskills.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.server.PlayerAttributes;
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.network.CooldownPacket;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.List;

public class SignatureAbilities {

    public static void signatureAbilityManager(PlayerEntity player) {

        String wizardSkillTree = "simplyskills_wizard";
        String berserkerSkillTree = "simplyskills_berserker";
        String rogueSkillTree = "simplyskills_rogue";
        String rangerSkillTree = "simplyskills_ranger";
        String spellbladeSkillTree = "simplyskills_spellblade";
        Vec3d blockpos = null;
        boolean ability_success = false;
        String ability = "";



        // - Wizard -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(wizardSkillTree)) {

            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationMeteorShower, player)) {

                int meteoricWrathDuration = SimplySkills.wizardConfig.signatureWizardMeteoricWrathDuration;
                int meteoricWrathStacks = SimplySkills.wizardConfig.signatureWizardMeteoricWrathStacks - 1;
                int meteorShowerRange = SimplySkills.wizardConfig.signatureWizardMeteorShowerRange;

                //Meteor Shower
                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationMeteorShowerWrath, player))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.METEORICWRATH,
                            meteoricWrathDuration, meteoricWrathStacks));


                if (HelperMethods.getTargetedEntity(player, meteorShowerRange) !=null)
                    blockpos = HelperMethods.getTargetedEntity(player, meteorShowerRange).getPos();

                if (blockpos == null)
                    blockpos = HelperMethods.getPositionLookingAt(player, meteorShowerRange);

                if (blockpos != null) {
                    double xpos = blockpos.getX();
                    double ypos = blockpos.getY();
                    double zpos = blockpos.getZ();
                    BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                    Box box = HelperMethods.createBoxAtBlock(searchArea, 8);
                    for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                                if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationMeteorShowerGreater, player))
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:fire_meteor_large",
                                            8, le);
                                else
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        "simplyskills:fire_meteor",
                                        8, le);
                            }
                        }
                    }
                    ability_success = true;
                    ability = SkillReferencePosition.wizardSpecialisationMeteorShower;
                }
            }

            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationIceComet, player)) {

                int leapVelocity = SimplySkills.wizardConfig.signatureWizardIceCometLeapVelocity;
                double leapHeight = SimplySkills.wizardConfig.signatureWizardIceCometLeapHeight;
                int leapSlowfallDuration = SimplySkills.wizardConfig.signatureWizardIceCometLeapSlowfallDuration;
                int volleyDuration = SimplySkills.wizardConfig.signatureWizardIceCometVolleyDuration;
                int volleyStacks = SimplySkills.wizardConfig.signatureWizardIceCometVolleyStacks;
                int iceCometRange = SimplySkills.wizardConfig.signatureWizardIceCometRange;

                //Ice Comet

                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationIceCometLeap, player)) {
                    player.setVelocity(player.getRotationVector().negate().multiply(+leapVelocity));
                    player.setVelocity(player.getVelocity().x, leapHeight, player.getVelocity().z);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,leapSlowfallDuration));
                    player.velocityModified = true;
                }
                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationIceCometVolley, player))
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROSTVOLLEY,
                                volleyDuration, volleyStacks));


                if (HelperMethods.getTargetedEntity(player, iceCometRange) !=null)
                    blockpos = HelperMethods.getTargetedEntity(player, iceCometRange).getPos();

                if (blockpos == null)
                    blockpos = HelperMethods.getPositionLookingAt(player, iceCometRange);

                if (blockpos != null) {
                    double xpos = blockpos.getX();
                    double ypos = blockpos.getY();
                    double zpos = blockpos.getZ();
                    BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                    Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
                    for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                                if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationIceCometDamageOne, player))
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        "simplyskills:ice_comet_large",
                                        3, le);
                                else if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationIceCometDamageTwo, player))
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:ice_comet_large_two",
                                            3, le);
                                else if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationIceCometDamageThree, player))
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:ice_comet_large_three",
                                            3, le);
                                else
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:ice_comet",
                                            3, le);
                            }
                        }
                    }
                    ability_success = true;
                    ability = SkillReferencePosition.wizardSpecialisationIceComet;
                }
            }

            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationStaticDischarge, player)) {
                //Static Discharge
                int amplifier = SimplySkills.wizardConfig.signatureWizardStaticDischargeBaseLeaps;
                int speedChance = SimplySkills.wizardConfig.signatureWizardStaticDischargeBaseSpeedChance;
                int speedChancePerTier = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedChancePerTier;
                int leapsPerTier = SimplySkills.wizardConfig.signatureWizardStaticDischargeLeapsPerTier;
                int staticDischargeRange = SimplySkills.wizardConfig.signatureWizardStaticDischargeRange;
                int staticChargeDuration = SimplySkills.wizardConfig.signatureWizardStaticChargeDuration;
                int dischargeSpeedDuration = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedDuration;
                int staticDischargeSpeedStacks = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedStacks;
                int staticDischargeSpeedMaxAmplifier = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedMaxAmplifier;

                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationStaticDischargeLeapTwo, player))
                    amplifier = amplifier + leapsPerTier;
                else if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationStaticDischargeLeapThree, player))
                    amplifier = amplifier + (leapsPerTier * 2);
                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedTwo, player))
                    speedChance = speedChance + speedChancePerTier;
                else if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedThree, player))
                    speedChance = speedChance + (speedChancePerTier * 2);

                if (HelperMethods.getTargetedEntity(player, staticDischargeRange) !=null)
                    blockpos = HelperMethods.getTargetedEntity(player, staticDischargeRange).getPos();

                if (blockpos == null)
                    blockpos = HelperMethods.getPositionLookingAt(player, staticDischargeRange);

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
                                        "simplyskills:static_discharge",
                                        3, le);
                                if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationStaticDischargeLeap, player)) {
                                    le.addStatusEffect(new StatusEffectInstance(EffectRegistry.STATICCHARGE,
                                            staticChargeDuration, amplifier));
                                }
                                if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationStaticDischargeSpeed, player)
                                        && player.getRandom().nextInt(100) < speedChance)
                                    HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED,
                                            dischargeSpeedDuration,
                                            staticDischargeSpeedStacks,
                                            staticDischargeSpeedMaxAmplifier);
                                break;
                            }
                        }
                    }
                    ability_success = true;
                    ability = SkillReferencePosition.wizardSpecialisationStaticDischarge;
                }
            }

            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationArcaneBolt, player)) {
                //Arcane Bolt
                int volleyDuration = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyDuration;
                int volleyStacks = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyStacks - 1;
                int arcaneBoltRange = SimplySkills.wizardConfig.signatureWizardArcaneBoltRange;
                int radius = 3;
                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationArcaneBoltLesser, player)) {
                    radius = SimplySkills.wizardConfig.signatureWizardLesserArcaneBoltRadius;
                }
                if (HelperMethods.isUnlocked(wizardSkillTree,
                        SkillReferencePosition.wizardSpecialisationArcaneBoltVolley, player))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARCANEVOLLEY,
                            volleyDuration, volleyStacks));

                if (HelperMethods.getTargetedEntity(player, arcaneBoltRange) !=null)
                    blockpos = HelperMethods.getTargetedEntity(player, arcaneBoltRange).getPos();

                if (blockpos == null)
                    blockpos = HelperMethods.getPositionLookingAt(player, arcaneBoltRange);

                if (blockpos != null) {
                    double xpos = blockpos.getX();
                    double ypos = blockpos.getY();
                    double zpos = blockpos.getZ();
                    BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
                    Box box = HelperMethods.createBoxAtBlock(searchArea, radius);
                    for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                                ability_success = true;
                                ability = SkillReferencePosition.wizardSpecialisationArcaneBolt;
                                if (HelperMethods.isUnlocked(wizardSkillTree,
                                        SkillReferencePosition.wizardSpecialisationArcaneBoltLesser, player)) {
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:arcane_bolt_lesser",
                                            radius, le);
                                } else {
                                    if (HelperMethods.isUnlocked(wizardSkillTree,
                                            SkillReferencePosition.wizardSpecialisationArcaneBoltGreater, player))
                                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:arcane_bolt_greater",
                                            radius, le);
                                    else
                                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                                "simplyskills:arcane_bolt",
                                                radius, le);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        // - Berserker -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(berserkerSkillTree)) {

            if (HelperMethods.isUnlocked(berserkerSkillTree,
                    SkillReferencePosition.berserkerSpecialisationRampage, player)) {
                int rampageDuration = SimplySkills.berserkerConfig.signatureBerserkerRampageDuration;
                int bullrushDuration = SimplySkills.berserkerConfig.signatureBerserkerBullrushDuration;
                //Rampage
                ability_success = true;
                ability = SkillReferencePosition.berserkerSpecialisationRampage;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RAMPAGE, rampageDuration));
                if (HelperMethods.isUnlocked(berserkerSkillTree,
                        SkillReferencePosition.berserkerSpecialisationRampageCharge, player)) {
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BULLRUSH, bullrushDuration));
                    player.world.playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                            SoundCategory.PLAYERS, 0.5f, 1.1f);
                }
            }

            if (HelperMethods.isUnlocked(berserkerSkillTree,
                    SkillReferencePosition.berserkerSpecialisationBloodthirsty, player)) {
                int bloodthirstyDuration = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyDuration;
                int bloodthirstyMightyStacks = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyMightyStacks;
                //Bloodthirsty
                ability_success = true;
                ability = SkillReferencePosition.berserkerSpecialisationBloodthirsty;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, bloodthirstyDuration));
                if (HelperMethods.isUnlocked("simplyskills_berserker",
                        SkillReferencePosition.berserkerSpecialisationBloodthirstyMighty, player))
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, bloodthirstyDuration,
                            bloodthirstyMightyStacks, 5);
            }

            if (HelperMethods.isUnlocked(berserkerSkillTree,
                    SkillReferencePosition.berserkerSpecialisationBerserking, player)) {
                //Berserking
                ability_success = true;
                ability = SkillReferencePosition.berserkerSpecialisationBerserking;
                double sacrificeAmountModifier = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSacrificeAmount;
                int secondsPerSacrifice = SimplySkills.berserkerConfig.signatureBerserkerBerserkingSecondsPerSacrifice;
                int leapSlamDuration = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamDuration;
                float sacrificeAmount = (float) (player.getHealth() * sacrificeAmountModifier);
                player.damage(DamageSource.GENERIC, sacrificeAmount);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BERSERKING,
                        (int)((sacrificeAmount * secondsPerSacrifice) * 20)));
                if (HelperMethods.isUnlocked(berserkerSkillTree,
                        SkillReferencePosition.berserkerSpecialisationBerserkingLeap, player)) {
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LEAPSLAM, leapSlamDuration));
                    player.world.playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                            SoundCategory.PLAYERS, 0.5f, 1.1f);
                }
            }
        }

        // - Rogue -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rogueSkillTree)) {

            if (HelperMethods.isUnlocked(rogueSkillTree,
                    SkillReferencePosition.rogueSpecialisationEvasion, player)) {

                int evasionDuration = SimplySkills.rogueConfig.signatureRogueEvasionDuration;
                int fanOfBladesDuration = SimplySkills.rogueConfig.signatureRogueFanOfBladesDuration;
                int fanOfBladesStacks = SimplySkills.rogueConfig.signatureRogueFanOfBladesStacks - 1;

                //Evasion
                ability_success = true;
                ability = SkillReferencePosition.rogueSpecialisationEvasion;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.EVASION, evasionDuration));
                if (HelperMethods.isUnlocked(rogueSkillTree,
                        SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades, player))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FANOFBLADES,
                            fanOfBladesDuration, fanOfBladesStacks));
            }

            if (HelperMethods.isUnlocked(rogueSkillTree,
                    SkillReferencePosition.rogueSpecialisationPreparation, player)) {

                int preparationDuration = SimplySkills.rogueConfig.signatureRoguePreparationDuration;
                int speedAmplifier = SimplySkills.rogueConfig.signatureRoguePreparationSpeedAmplifier;

                //Preparation
                ability_success = true;
                ability = SkillReferencePosition.rogueSpecialisationPreparation;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, preparationDuration));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                        preparationDuration, speedAmplifier));
                player.world.playSoundFromEntity(
                        null, player, SoundRegistry.SOUNDEFFECT39,
                        SoundCategory.PLAYERS, 0.6f, 1.6f);
                if (HelperMethods.isUnlocked(rogueSkillTree,
                        SkillReferencePosition.rogueSpecialisationPreparationShadowstrike, player))
                    Abilities.passiveRoguePreparationShadowstrike(player);
            }

            if (HelperMethods.isUnlocked(rogueSkillTree,
                    SkillReferencePosition.rogueSpecialisationSiphoningStrikes, player)) {

                int siphoningStrikesduration = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesDuration;
                int siphoningStrikesStacks = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesStacks;
                int siphoningStrikesMightyStacks = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesMightyStacks;

                //Siphoning Strikes
                ability_success = true;
                ability = SkillReferencePosition.rogueSpecialisationSiphoningStrikes;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SIPHONINGSTRIKES,
                        siphoningStrikesduration, siphoningStrikesStacks));

                if (HelperMethods.isUnlocked("simplyskills_rogue",
                        SkillReferencePosition.rogueSpecialisationSiphoningStrikesMighty, player))
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, siphoningStrikesduration,
                            siphoningStrikesMightyStacks, 5);
                if (HelperMethods.isUnlocked("simplyskills_rogue",
                        SkillReferencePosition.rogueSpecialisationSiphoningStrikesAura, player))
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.IMMOBILIZINGAURA, siphoningStrikesduration,
                            1, 2);

            }
        }

        // - Ranger -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rangerSkillTree)) {

            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationElementalArrows, player)) {

                int elementalArrowsDuration = SimplySkills.rangerConfig.effectRangerElementalArrowsDuration;
                int elementalArrowsStacks = SimplySkills.rangerConfig.effectRangerElementalArrowsStacks;
                int elementalArrowsStacksIncreasePerTier = SimplySkills.rangerConfig.effectRangerElementalArrowsStacksIncreasePerTier;

                //Elemental Arrows
                ability_success = true;
                ability = SkillReferencePosition.rangerSpecialisationElementalArrows;
                int amplifier =elementalArrowsStacks;

                if (HelperMethods.isUnlocked(rangerSkillTree,
                        SkillReferencePosition.rangerSpecialisationElementalArrowsStacksOne, player))
                    amplifier = amplifier + elementalArrowsStacksIncreasePerTier;
                if (HelperMethods.isUnlocked(rangerSkillTree,
                        SkillReferencePosition.rangerSpecialisationElementalArrowsStacksTwo, player))
                    amplifier = amplifier + (elementalArrowsStacksIncreasePerTier * 2);
                if (HelperMethods.isUnlocked(rangerSkillTree,
                        SkillReferencePosition.rangerSpecialisationElementalArrowsStacksThree, player))
                    amplifier = amplifier + (elementalArrowsStacksIncreasePerTier * 3);

                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALARROWS,
                        elementalArrowsDuration, amplifier));
            }

            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationDisengage, player)) {
                //Disengage
                ability_success = true;
                ability = SkillReferencePosition.rangerSpecialisationDisengage;
                Abilities.signatureRangerDisengage(player);
            }
            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationArrowRain, player)) {

                int arrowRainDuration = SimplySkills.rangerConfig.effectRangerArrowRainDuration;

                //Arrow Rain
                ability_success = true;
                ability = SkillReferencePosition.rangerSpecialisationArrowRain;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARROWRAIN, arrowRainDuration, 0));
            }
        }

        // - Spellblade -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(spellbladeSkillTree)) {

            int elementalSurgeDuration = SimplySkills.spellbladeConfig.signatureSpellbladeElementalSurgeDuration;
            int elementalImpactDuration = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactDuration;
            int elementalImpactResistanceAmplifier = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactResistanceAmplifier;
            int spellweaverDuration = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverDuration;
            int spellweaverStacks = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverStacks;

            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationElementalSurge, player)) {
                //Elemental Surge
                ability_success = true;
                ability = SkillReferencePosition.spellbladeSpecialisationElementalSurge;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE,
                        elementalSurgeDuration, 0));
            }
            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationElementalImpact, player)) {
                //Elemental Impact
                ability_success = true;
                ability = SkillReferencePosition.spellbladeSpecialisationElementalImpact;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALIMPACT,
                        elementalImpactDuration, 0));
                if (HelperMethods.isUnlocked(spellbladeSkillTree,
                        SkillReferencePosition.spellbladeSpecialisationElementalImpactResistance, player))
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                            elementalImpactDuration + 15, elementalImpactResistanceAmplifier));
            }
            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationSpellweaver, player)) {
                //Spell Weaver
                ability_success = true;
                ability = SkillReferencePosition.spellbladeSpecialisationSpellweaver;
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLWEAVER,
                        spellweaverDuration, spellweaverStacks - 1));
            }
        }


        //Return cooldown to client
        if (!player.world.isClient) {
            SignatureAbilities.signatureAbilityCooldownManager(ability, ability_success, player);
        }


    }



    // -- SPELL CASTING --

    public static void castSpellEngineTargeted(PlayerEntity player, String spellIdentifier, int range) {

        // -- Cast spell at a target we are looking at --

        Entity target = HelperMethods.getTargetedEntity(player, range);
        if (target != null) {
            ItemStack itemStack     = player.getMainHandStack();
            Hand hand               = player.getActiveHand();
            SpellCast.Action action = SpellCast.Action.RELEASE;
            Identifier spellID      = new Identifier(spellIdentifier);
            List<Entity> list       = new ArrayList<Entity>();
            list.add(target);

            SpellHelper.performSpell(
                    player.world,
                    player,
                    spellID,
                    list,
                    itemStack,
                    action,
                    hand,
                    20);


        }

    }

    public static void castSpellEngineIndirectTarget(PlayerEntity player, String spellIdentifier, int range, Entity target) {

        // -- Cast spell at specified target --
        if (target != null) {
            ItemStack itemStack     = player.getMainHandStack();
            Hand hand               = player.getActiveHand();
            SpellCast.Action action = SpellCast.Action.RELEASE;
            Identifier spellID      = new Identifier(spellIdentifier);
            List<Entity> list       = new ArrayList<Entity>();
            list.add(target);

            SpellHelper.performSpell(
                    player.world,
                    player,
                    spellID,
                    list,
                    itemStack,
                    action,
                    hand,
                    20);

        }

    }

    public static boolean castSpellEngineAOE(PlayerEntity player, String spellIdentifier, int radius, int chance, boolean singleTarget) {

        // -- Cast spell at nearby targets --

        ItemStack itemStack     = player.getMainHandStack();
        Hand hand               = player.getActiveHand();
        SpellCast.Action action = SpellCast.Action.RELEASE;
        Identifier spellID      = new Identifier(spellIdentifier);
        List<Entity> list       = new ArrayList<Entity>();


        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
            if (entities != null) {
                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                    if (player.getRandom().nextInt(100) < chance)
                        list.add(le);
                    if (singleTarget)
                        break;

                }
            }
        }

        if (!list.isEmpty()) {
            SpellHelper.performSpell(
                    player.world,
                    player,
                    spellID,
                    list,
                    itemStack,
                    action,
                    hand,
                    20);

            return true;
        }
        return false;
    }

    public static void signatureAbilityCooldownManager(String ability, boolean useSuccess, PlayerEntity player) {
        float spellHasteCDReduce = SimplySkills.generalConfig.spellHasteCooldownReductionModifier;
        int minimumCD = SimplySkills.generalConfig.minimumAchievableCooldown * 1000;
        int useDelay = (int) SimplySkills.generalConfig.minimumTimeBetweenAbilityUse * 1000;
        int cooldown = 500;
        double sendCooldown;
        String type = "";

        if (ability.equals(SkillReferencePosition.wizardSpecialisationArcaneBolt))
        {cooldown = SimplySkills.wizardConfig.signatureWizardArcaneBoltCooldown * 1000; ; type = "magic";}
        else if (ability.equals(SkillReferencePosition.wizardSpecialisationIceComet))
        {cooldown = SimplySkills.wizardConfig.signatureWizardIceCometCooldown * 1000; type = "magic";}
        else if (ability.equals(SkillReferencePosition.wizardSpecialisationMeteorShower))
        {cooldown = SimplySkills.wizardConfig.signatureWizardMeteorShowerCooldown * 1000; type = "magic";}
        else if (ability.equals(SkillReferencePosition.wizardSpecialisationStaticDischarge))
        {cooldown = SimplySkills.wizardConfig.signatureWizardStaticDischargeCooldown * 1000; type = "magic";}

        else if (ability.equals(SkillReferencePosition.berserkerSpecialisationBerserking))
        {cooldown = SimplySkills.berserkerConfig.signatureBerserkerBerserkingCooldown * 1000; type = "physical";}
        else if (ability.equals(SkillReferencePosition.berserkerSpecialisationBloodthirsty))
        {cooldown = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyCooldown * 1000; type = "physical";}
        else if (ability.equals(SkillReferencePosition.berserkerSpecialisationRampage))
        {cooldown = SimplySkills.berserkerConfig.signatureBerserkerRampageCooldown * 1000; type = "physical";}

        else if (ability.equals(SkillReferencePosition.rogueSpecialisationSiphoningStrikes))
        {cooldown = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesCooldown * 1000; type = "physical";}
        else if (ability.equals(SkillReferencePosition.rogueSpecialisationEvasion))
        {cooldown = SimplySkills.rogueConfig.signatureRogueEvasionCooldown * 1000; type = "physical";}
        else if (ability.equals(SkillReferencePosition.rogueSpecialisationPreparation))
        {cooldown = SimplySkills.rogueConfig.signatureRoguePreparationCooldown * 1000; type = "physical";}

        else if (ability.equals(SkillReferencePosition.rangerSpecialisationArrowRain))
        {cooldown = SimplySkills.rangerConfig.effectRangerArrowRainCooldown * 1000; type = "mixed";}
        else if (ability.equals(SkillReferencePosition.rangerSpecialisationDisengage))
        {cooldown = SimplySkills.rangerConfig.signatureRangerDisengageCooldown * 1000; type = "physical";}
        else if (ability.equals(SkillReferencePosition.rangerSpecialisationElementalArrows))
        {cooldown = SimplySkills.rangerConfig.effectRangerElementalArrowsCooldown * 1000; type = "magic";}

        else if (ability.equals(SkillReferencePosition.spellbladeSpecialisationElementalImpact))
        {cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactCooldown * 1000; type = "mixed";}
        else if (ability.equals(SkillReferencePosition.spellbladeSpecialisationElementalSurge))
        {cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalSurgeCooldown * 1000; type = "physical";}
        else if (ability.equals(SkillReferencePosition.spellbladeSpecialisationSpellweaver))
        {cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverCooldown * 1000; type = "magic";}


        double spellHaste = player.getAttributeValue(SpellAttributes.HASTE.attribute);
        sendCooldown = cooldown - ((spellHaste * spellHasteCDReduce) * 100);

        if (sendCooldown < (minimumCD) && useSuccess) sendCooldown = minimumCD;
        if (!useSuccess) sendCooldown = useDelay;

        sendCooldownPacket((ServerPlayerEntity) player, (int) sendCooldown);
    }



    @Environment(EnvType.CLIENT)
    public static void sendKeybindPacket() {

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(KeybindPacket.ABILITY1_PACKET, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);

    }

    //@Environment(EnvType.SERVER)
    public static void sendCooldownPacket(ServerPlayerEntity player, int cooldown) {

        //PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(cooldown);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(CooldownPacket.COOLDOWN_PACKET, buf);
        ServerPlayNetworking.send(player, CooldownPacket.COOLDOWN_PACKET , packet.getData());
        //player.networkHandler.sendPacket(packet);

    }


}
