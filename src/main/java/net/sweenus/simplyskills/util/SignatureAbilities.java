package net.sweenus.simplyskills.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
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
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.puffish.skillsmod.SkillsAPI;
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.client.SimplySkillsClient;
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



        // - Wizard -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(wizardSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationMeteorShower)) {

                int meteoricWrathDuration = SimplySkillsClient.wizardConfig.signatureWizardMeteoricWrathDuration;
                int meteoricWrathStacks = SimplySkillsClient.wizardConfig.signatureWizardMeteoricWrathStacks - 1;
                int meteorShowerRange = SimplySkillsClient.wizardConfig.signatureWizardMeteorShowerRange;

                //Meteor Shower
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerWrath))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.METEORICWRATH, meteoricWrathDuration, meteoricWrathStacks));


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
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, "simplyskills_wizard").get()
                                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerGreater))
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
                }
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationIceComet)) {

                int leapVelocity = SimplySkillsClient.wizardConfig.signatureWizardIceCometLeapVelocity;
                double leapHeight = SimplySkillsClient.wizardConfig.signatureWizardIceCometLeapHeight;
                int leapSlowfallDuration = SimplySkillsClient.wizardConfig.signatureWizardIceCometLeapSlowfallDuration;
                int volleyDuration = SimplySkillsClient.wizardConfig.signatureWizardIceCometVolleyDuration;
                int volleyStacks = SimplySkillsClient.wizardConfig.signatureWizardIceCometVolleyStacks;
                int iceCometRange = SimplySkillsClient.wizardConfig.signatureWizardIceCometRange;

                //Ice Comet

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationIceCometLeap)) {
                    player.setVelocity(player.getRotationVector().negate().multiply(+leapVelocity));
                    player.setVelocity(player.getVelocity().x, leapHeight, player.getVelocity().z);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, leapSlowfallDuration));
                    player.velocityModified = true;
                }
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationIceCometVolley))
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROSTVOLLEY, volleyDuration, volleyStacks));


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
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationIceCometDamageOne))
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        "simplyskills:ice_comet_large",
                                        3, le);
                                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationIceCometDamageTwo))
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:ice_comet_large_two",
                                            3, le);
                                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationIceCometDamageThree))
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
                }
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationStaticDischarge)) {
                //Static Discharge
                int amplifier = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeBaseLeaps;
                int speedChance = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeBaseSpeedChance;
                int speedChancePerTier = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeSpeedChancePerTier;
                int leapsPerTier = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeLeapsPerTier;
                int staticDischargeRange = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeRange;
                int staticChargeDuration = SimplySkillsClient.wizardConfig.signatureWizardStaticChargeDuration;
                int dischargeSpeedDuration = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeSpeedDuration;
                int staticDischargeSpeedStacks = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeSpeedStacks;
                int staticDischargeSpeedMaxAmplifier = SimplySkillsClient.wizardConfig.signatureWizardStaticDischargeSpeedMaxAmplifier;

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeapTwo))
                    amplifier = amplifier + leapsPerTier;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeapThree))
                    amplifier = amplifier + (leapsPerTier * 2);
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedTwo))
                    speedChance = speedChance + speedChancePerTier;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedThree))
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
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeap)) {
                                    le.addStatusEffect(new StatusEffectInstance(EffectRegistry.STATICCHARGE, staticChargeDuration, amplifier));
                                }
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeed) &&
                                player.getRandom().nextInt(100) < speedChance)
                                    HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED, dischargeSpeedDuration, staticDischargeSpeedStacks, staticDischargeSpeedMaxAmplifier);
                                break;
                            }
                        }
                    }
                }
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationArcaneBolt)) {
                //Arcane Bolt
                int volleyDuration = SimplySkillsClient.wizardConfig.signatureWizardArcaneBoltVolleyDuration;
                int volleyStacks = SimplySkillsClient.wizardConfig.signatureWizardArcaneBoltVolleyStacks - 1;
                int arcaneBoltRange = SimplySkillsClient.wizardConfig.signatureWizardArcaneBoltRange;
                int radius = 3;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltLesser)) {
                    radius = SimplySkillsClient.wizardConfig.signatureWizardLesserArcaneBoltRadius;
                }
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltVolley))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARCANEVOLLEY, volleyDuration, volleyStacks));

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
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltLesser)) {
                                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                                            "simplyskills:arcane_bolt_lesser",
                                            radius, le);
                                } else {
                                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                            .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltGreater))
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
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(berserkerSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                    .contains(SkillReferencePosition.berserkerSpecialisationRampage)) {
                int rampageDuration = SimplySkillsClient.berserkerConfig.signatureBerserkerRampageDuration;
                int bullrushDuration = SimplySkillsClient.berserkerConfig.signatureBerserkerBullrushDuration;
                //Rampage
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RAMPAGE, rampageDuration));
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                        .contains(SkillReferencePosition.berserkerSpecialisationRampageCharge)) {
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BULLRUSH, bullrushDuration));
                    player.world.playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                            SoundCategory.PLAYERS, 0.5f, 1.1f);
                }
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                    .contains(SkillReferencePosition.berserkerSpecialisationBloodthirsty)) {
                int bloodthirstyDuration = SimplySkillsClient.berserkerConfig.signatureBerserkerBloodthirstyDuration;
                //Bloodthirsty
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, bloodthirstyDuration));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                    .contains(SkillReferencePosition.berserkerSpecialisationBerserking)) {
                //Berserking
                double sacrificeAmountModifier = SimplySkillsClient.berserkerConfig.signatureBerserkerBerserkingSacrificeAmount;
                int secondsPerSacrifice = SimplySkillsClient.berserkerConfig.signatureBerserkerBerserkingSecondsPerSacrifice;
                int leapSlamDuration = SimplySkillsClient.berserkerConfig.signatureBerserkerLeapSlamDuration;
                float sacrificeAmount = (float) (player.getHealth() * sacrificeAmountModifier);
                player.damage(DamageSource.GENERIC, sacrificeAmount);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BERSERKING, (int)((sacrificeAmount * secondsPerSacrifice) * 20)));
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                        .contains(SkillReferencePosition.berserkerSpecialisationBerserkingLeap)) {
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LEAPSLAM, leapSlamDuration));
                    player.world.playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                            SoundCategory.PLAYERS, 0.5f, 1.1f);
                }
            }
        }

        // - Rogue -
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rogueSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                    .contains(SkillReferencePosition.rogueSpecialisationEvasion)) {

                int evasionDuration = SimplySkillsClient.rogueConfig.signatureRogueEvasionDuration;
                int fanOfBladesDuration = SimplySkillsClient.rogueConfig.signatureRogueFanOfBladesDuration;
                int fanOfBladesStacks = SimplySkillsClient.rogueConfig.signatureRogueFanOfBladesStacks - 1;

                //Evasion
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.EVASION, evasionDuration));
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                        .contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FANOFBLADES, fanOfBladesDuration, fanOfBladesStacks));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                    .contains(SkillReferencePosition.rogueSpecialisationPreparation)) {

                int preparationDuration = SimplySkillsClient.rogueConfig.signatureRoguePreparationDuration;
                int speedAmplifier = SimplySkillsClient.rogueConfig.signatureRoguePreparationSpeedAmplifier;

                //Preparation
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, preparationDuration));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, preparationDuration, speedAmplifier));
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrike))
                    Abilities.passiveRoguePreparationShadowstrike(player);
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                    .contains(SkillReferencePosition.rogueSpecialisationSiphoningStrikes)) {

                int siphoningStrikesduration = SimplySkillsClient.rogueConfig.signatureRogueSiphoningStrikesDuration;
                int siphoningStrikesStacks = SimplySkillsClient.rogueConfig.signatureRogueSiphoningStrikesStacks - 1;

                //Siphoning Strikes
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SIPHONINGSTRIKES, siphoningStrikesduration, siphoningStrikesStacks));
            }
        }

        // - Ranger -
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rangerSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rangerSkillTree).get()
                    .contains(SkillReferencePosition.rangerSpecialisationElementalArrows)) {

                int elementalArrowsDuration = SimplySkillsClient.rangerConfig.effectRangerElementalArrowsDuration;
                int elementalArrowsStacks = SimplySkillsClient.rangerConfig.effectRangerElementalArrowsStacks;
                int elementalArrowsStacksIncreasePerTier = SimplySkillsClient.rangerConfig.effectRangerElementalArrowsStacksIncreasePerTier;

                //Elemental Arrows
                int amplifier =elementalArrowsStacks;

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsStacksOne))
                    amplifier = amplifier + elementalArrowsStacksIncreasePerTier;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsStacksTwo))
                    amplifier = amplifier + (elementalArrowsStacksIncreasePerTier * 2);
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsStacksThree))
                    amplifier = amplifier + (elementalArrowsStacksIncreasePerTier * 3);

                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALARROWS, elementalArrowsDuration, amplifier));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rangerSkillTree).get()
                    .contains(SkillReferencePosition.rangerSpecialisationDisengage)) {
                //Disengage
                Abilities.signatureRangerDisengage(player);
            }
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rangerSkillTree).get()
                    .contains(SkillReferencePosition.rangerSpecialisationArrowRain)) {

                int arrowRainDuration = SimplySkillsClient.rangerConfig.effectRangerArrowRainDuration;

                //Arrow Rain
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARROWRAIN, arrowRainDuration, 0));
            }

        }

        // - Spellblade -
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(spellbladeSkillTree)) {

            int elementalSurgeDuration = SimplySkillsClient.spellbladeConfig.signatureSpellbladeElementalSurgeDuration;
            int elementalImpactDuration = SimplySkillsClient.spellbladeConfig.signatureSpellbladeElementalImpactDuration;
            int elementalImpactResistanceAmplifier = SimplySkillsClient.spellbladeConfig.signatureSpellbladeElementalImpactResistanceAmplifier;
            int spellweaverDuration = SimplySkillsClient.spellbladeConfig.signatureSpellbladeSpellweaverDuration;
            int spellweaverStacks = SimplySkillsClient.spellbladeConfig.signatureSpellbladeSpellweaverStacks;

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, spellbladeSkillTree).get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationElementalSurge)) {
                //Elemental Surge
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE, elementalSurgeDuration, 0));
            }
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, spellbladeSkillTree).get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationElementalImpact)) {
                //Elemental Impact
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALIMPACT, elementalImpactDuration, 0));
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpecialisationElementalImpactResistance))
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, elementalImpactDuration + 15, elementalImpactResistanceAmplifier));
            }
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, spellbladeSkillTree).get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationSpellweaver)) {
                //Spell Weaver
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLWEAVER, spellweaverDuration, spellweaverStacks - 1));
            }
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



    @Environment(EnvType.CLIENT)
    public static void sendKeybindPacket() {

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(KeybindPacket.ABILITY1_PACKET, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);

    }


}
