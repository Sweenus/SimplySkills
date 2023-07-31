package net.sweenus.simplyskills.abilities;

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
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.network.CooldownPacket;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;

public class SignatureAbilities {

    public static void signatureAbilityManager(PlayerEntity player) {

        String wizardSkillTree = "simplyskills_wizard";
        String berserkerSkillTree = "simplyskills_berserker";
        String rogueSkillTree = "simplyskills_rogue";
        String rangerSkillTree = "simplyskills_ranger";
        String spellbladeSkillTree = "simplyskills_spellblade";
        boolean ability_success = false;
        String ability = "";



        // - WIZARD -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(wizardSkillTree)) {

            // Meteor Shower
            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationMeteorShower, player)) {
                ability_success = WizardAbilities.signatureWizardMeteorShower(wizardSkillTree, player);
                ability = "MeteorShower";
            }
            // Ice Comet
            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationIceComet, player)) {
                ability_success = WizardAbilities.signatureWizardIceComet(wizardSkillTree, player);
                ability = "IceComet";
            }
            // Static Discharge
            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationStaticDischarge, player)) {
                ability_success = WizardAbilities.signatureWizardStaticDischarge(wizardSkillTree, player);
                ability = "StaticDischarge";
            }
            // Arcane Bolt
            if (HelperMethods.isUnlocked(wizardSkillTree,
                    SkillReferencePosition.wizardSpecialisationArcaneBolt, player)) {
                ability_success = WizardAbilities.signatureWizardArcaneBolt(wizardSkillTree, player);
                ability = "ArcaneBolt";
            }
        }

        // - BERSERKER -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(berserkerSkillTree)) {

            // Rampage
            if (HelperMethods.isUnlocked(berserkerSkillTree,
                    SkillReferencePosition.berserkerSpecialisationRampage, player)) {
                ability_success = BerserkerAbilities.signatureBerserkerRampage(berserkerSkillTree, player);
                ability = "Rampage";
            }
            // Bloodthirsty
            if (HelperMethods.isUnlocked(berserkerSkillTree,
                    SkillReferencePosition.berserkerSpecialisationBloodthirsty, player)) {
                ability_success = BerserkerAbilities.signatureBerserkerBloodthirsty(berserkerSkillTree, player);
                ability = "Bloodthirsty";
            }
            //Berserking
            if (HelperMethods.isUnlocked(berserkerSkillTree,
                    SkillReferencePosition.berserkerSpecialisationBerserking, player)) {
                ability_success = BerserkerAbilities.signatureBerserkerBerserking(berserkerSkillTree, player);
                ability = "Berserking";
            }
        }

        // - ROGUE -
        if (SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains(rogueSkillTree)) {

            // Evasion
            if (HelperMethods.isUnlocked(rogueSkillTree,
                    SkillReferencePosition.rogueSpecialisationEvasion, player)) {
                ability_success = RogueAbilities.signatureRogueEvasion(rogueSkillTree, player);
                ability = "Evasion";
            }
            // Preparation
            if (HelperMethods.isUnlocked(rogueSkillTree,
                    SkillReferencePosition.rogueSpecialisationPreparation, player)) {
                ability_success = RogueAbilities.signatureRoguePreparation(rogueSkillTree, player);
                ability = "Preparation";
            }
            // Siphoning Strikes
            if (HelperMethods.isUnlocked(rogueSkillTree,
                    SkillReferencePosition.rogueSpecialisationSiphoningStrikes, player)) {
                ability_success = RogueAbilities.signatureRogueSiphoningStrikes(rogueSkillTree, player);
                ability = "SiphoningStrikes";
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
                ability = "ElementalArrows";
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
                ability = "Disengage";
                RangerAbilities.signatureRangerDisengage(player);
            }
            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationArrowRain, player)) {

                int arrowRainDuration = SimplySkills.rangerConfig.effectRangerArrowRainDuration;

                //Arrow Rain
                ability_success = true;
                ability = "ArrowRain";
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
                ability = "ElementalSurge";
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE,
                        elementalSurgeDuration, 0));
            }
            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationElementalImpact, player)) {
                //Elemental Impact
                ability_success = true;
                ability = "ElementalImpact";
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
                ability = "Spellweaver";
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLWEAVER,
                        spellweaverDuration, spellweaverStacks - 1));
            }
        }


        //Return cooldown to client
        if (!player.world.isClient) {
            SignatureAbilities.signatureAbilityCooldownManager(ability, ability_success, player);
            System.out.println("Using ability: " + ability);
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

        if (ability.contains("ArcaneBolt"))
        {cooldown = SimplySkills.wizardConfig.signatureWizardArcaneBoltCooldown * 1000; ; type = "magic";}
        else if (ability.contains("IceComet"))
        {cooldown = SimplySkills.wizardConfig.signatureWizardIceCometCooldown * 1000; type = "magic";}
        else if (ability.contains("MeteorShower"))
        {cooldown = SimplySkills.wizardConfig.signatureWizardMeteorShowerCooldown * 1000; type = "magic";}
        else if (ability.contains("StaticDischarge"))
        {cooldown = SimplySkills.wizardConfig.signatureWizardStaticDischargeCooldown * 1000; type = "magic";}
        else if (ability.contains("Berserking"))
        {cooldown = SimplySkills.berserkerConfig.signatureBerserkerBerserkingCooldown * 1000; type = "physical";}
        else if (ability.contains("Bloodthirsty"))
        {cooldown = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyCooldown * 1000; type = "physical";}
        else if (ability.contains("Rampage"))
        {cooldown = SimplySkills.berserkerConfig.signatureBerserkerRampageCooldown * 1000; type = "physical";}
        else if (ability.contains("SiphoningStrikes"))
        {cooldown = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesCooldown * 1000; type = "physical";}
        else if (ability.contains("Evasion"))
        {cooldown = SimplySkills.rogueConfig.signatureRogueEvasionCooldown * 1000; type = "physical";}
        else if (ability.contains("Preparation"))
        {cooldown = SimplySkills.rogueConfig.signatureRoguePreparationCooldown * 1000; type = "physical";}
        else if (ability.contains("ArrowRain"))
        {cooldown = SimplySkills.rangerConfig.effectRangerArrowRainCooldown * 1000; type = "mixed";}
        else if (ability.contains("Disengage"))
        {cooldown = SimplySkills.rangerConfig.signatureRangerDisengageCooldown * 1000; type = "physical";}
        else if (ability.contains("ElementalArrows"))
        {cooldown = SimplySkills.rangerConfig.effectRangerElementalArrowsCooldown * 1000; type = "magic";}
        else if (ability.contains("ElementalImpact"))
        {cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactCooldown * 1000; type = "mixed";}
        else if (ability.contains("ElementalSurge"))
        {cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalSurgeCooldown * 1000; type = "physical";}
        else if (ability.contains("Spellweaver"))
        {cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverCooldown * 1000; type = "magic";}


        double spellHaste = player.getAttributeValue(SpellAttributes.HASTE.attribute);
        sendCooldown = cooldown - (((spellHaste - 100) * spellHasteCDReduce) * 100);

        if (sendCooldown < (minimumCD) && useSuccess) sendCooldown = minimumCD;
        if (!useSuccess) sendCooldown = useDelay;

        System.out.println("Ability type: " + type);
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
