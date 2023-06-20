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
import net.minecraft.util.math.Box;
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



        // - Wizard -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(wizardSkillTree)) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationMeteorShower)) {
                //Meteor Shower
                SignatureAbilities.castSpellEngineMultiAOE(player,
                        "simplyskills:fire_meteor",
                        12);
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationIceComet)) {
                //Ice Comet
                SignatureAbilities.castSpellEngineAOE(player,
                        "simplyskills:ice_comet",
                        12);
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationStaticDischarge)) {
                //Lightning Beam
                SignatureAbilities.castSpellEngineTargeted(player,
                        "simplyskills:static_discharge",
                        18);
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player, wizardSkillTree).get()
                    .contains(SkillReferencePosition.wizardSpecialisationArcaneBolt)) {
                //Arcane Bolt
                SignatureAbilities.castSpellEngineTargeted(player,
                        "simplyskills:arcane_bolt",
                        32);
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

    public static void castSpellEngineAOE(PlayerEntity player, String spellIdentifier, int radius) {

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

                    list.add(le);

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
        }

    }

    public static void castSpellEngineMultiAOE(PlayerEntity player, String spellIdentifier, int radius) {

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

                    list.add(le);
                    SpellHelper.performSpell(
                            player.world,
                            player,
                            spellID,
                            list,
                            itemStack,
                            action,
                            hand,
                            20);
                    list.remove(le);

                }
            }
        }

    }



    @Environment(EnvType.CLIENT)
    public static void sendKeybindPacket() {

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(KeybindPacket.ABILITY1_PACKET, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);

    }


}
