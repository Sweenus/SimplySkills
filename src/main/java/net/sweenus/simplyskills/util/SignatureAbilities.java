package net.sweenus.simplyskills.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.puffish.skillsmod.SkillsAPI;
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EffectRegistry;

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
                //Meteor Shower
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationMeteorShowerWrath))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.METEORICWRATH, 800, 9));


                if (HelperMethods.getTargetedEntity(player, 120) !=null)
                    blockpos = HelperMethods.getTargetedEntity(player, 120).getPos();

                if (blockpos == null)
                    blockpos = HelperMethods.getPositionLookingAt(player, 120);

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
                //Ice Comet

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationIceCometLeap)) {
                    player.setVelocity(player.getRotationVector().negate().multiply(+3));
                    player.setVelocity(player.getVelocity().x, 1.3, player.getVelocity().z);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 180, 0));
                    player.velocityModified = true;
                }
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationIceCometVolley))
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROSTVOLLEY, 400, 5));


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
                int amplifier = 8;
                int speedChance = 5;

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeapTwo))
                    amplifier = 16;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeapThree))
                    amplifier = 24;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedTwo))
                    speedChance = 10;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedThree))
                    speedChance = 15;

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
                                        "simplyskills:static_discharge",
                                        3, le);
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeap)) {
                                    le.addStatusEffect(new StatusEffectInstance(EffectRegistry.STATICCHARGE, 900, amplifier));
                                }
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeed) &&
                                player.getRandom().nextInt(100) < speedChance)
                                    HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED, 120, 1, 4);
                                break;
                            }
                        }
                    }
                }
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationArcaneBolt)) {
                //Arcane Bolt
                int radius = 3;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltLesser)) {
                    radius = 12;
                }
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                        .contains(SkillReferencePosition.wizardSpecialisationArcaneBoltVolley))
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARCANEVOLLEY, 400, 9));

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
                //Rampage
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RAMPAGE, 300));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                    .contains(SkillReferencePosition.berserkerSpecialisationBloodthirsty)) {
                //Bloodthirsty
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 400));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, berserkerSkillTree).get()
                    .contains(SkillReferencePosition.berserkerSpecialisationBerserking)) {
                //Berserking
                float sacrificeAmount = (float) (player.getHealth() * 0.30);
                player.damage(DamageSource.GENERIC, sacrificeAmount);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BERSERKING, (int)(sacrificeAmount * 20)));
            }
        }

        // - Rogue -
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rogueSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                    .contains(SkillReferencePosition.rogueSpecialisationEvasion)) {
                //Evasion
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.EVASION, 160));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                    .contains(SkillReferencePosition.rogueSpecialisationPreparation)) {
                //Preparation
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 160));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 160, 2));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rogueSkillTree).get()
                    .contains(SkillReferencePosition.rogueSpecialisationSiphoningStrikes)) {
                //Siphoning Strikes
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SIPHONINGSTRIKES, 600, 9));
            }
        }

        // - Ranger -
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rangerSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rangerSkillTree).get()
                    .contains(SkillReferencePosition.rangerSpecialisationElementalArrows)) {
                //Elemental Arrows
                int amplifier =4;

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsStacksOne))
                    amplifier = 5;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsStacksTwo))
                    amplifier = 6;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_ranger").get().contains(SkillReferencePosition.rangerSpecialisationElementalArrowsStacksThree))
                    amplifier = 7;

                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALARROWS, 600, amplifier));
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rangerSkillTree).get()
                    .contains(SkillReferencePosition.rangerSpecialisationDisengage)) {
                //Disengage
                Abilities.signatureRangerDisengage(player);
            }
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, rangerSkillTree).get()
                    .contains(SkillReferencePosition.rangerSpecialisationArrowRain)) {
                //Arrow Rain
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARROWRAIN, 600, 0));
            }

        }

        // - Spellblade -
        else if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(spellbladeSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, spellbladeSkillTree).get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationElementalSurge)) {
                //Elemental Surge
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE, 300, 0));
            }
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, spellbladeSkillTree).get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationElementalImpact)) {
                //Elemental Impact
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALIMPACT, 20, 0));
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpecialisationElementalImpactResistance))
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 35, 2));
            }
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, spellbladeSkillTree).get()
                    .contains(SkillReferencePosition.spellbladeSpecialisationSpellweaver)) {
                //Spell Weaver
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLWEAVER, 600, 19));
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
