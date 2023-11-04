package net.sweenus.simplyskills.abilities;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.network.CooldownPacket;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;

public class SignatureAbilities {

    public static void signatureAbilityManager(PlayerEntity player) {

        String wizardSkillTree = "simplyskills:wizard";
        String berserkerSkillTree = "simplyskills:berserker";
        String rogueSkillTree = "simplyskills:rogue";
        String rangerSkillTree = "simplyskills:ranger";
        String spellbladeSkillTree = "simplyskills:spellblade";
        String crusaderSkillTree = "simplyskills:crusader";
        String clericSkillTree = "simplyskills:cleric";
        boolean ability_success = false;
        String ability = "none";



        // - WIZARD -
        if (HelperMethods.isUnlocked(wizardSkillTree, null, player)) {

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
        if (HelperMethods.isUnlocked(berserkerSkillTree, null, player)) {

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
        if (HelperMethods.isUnlocked(rogueSkillTree, null, player)) {

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
        if (HelperMethods.isUnlocked(rangerSkillTree, null, player)) {

            // Elemental Arrows
            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationElementalArrows, player)) {
                ability_success = RangerAbilities.signatureRangerElementalArrows(rangerSkillTree, player);
                ability = "ElementalArrows";
            }
            // Disengage
            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationDisengage, player)) {
                ability_success = RangerAbilities.signatureRangerDisengage(rangerSkillTree, player);
                ability = "Disengage";
            }
            // Arrow Rain
            if (HelperMethods.isUnlocked(rangerSkillTree,
                    SkillReferencePosition.rangerSpecialisationArrowRain, player)) {
                ability_success = RangerAbilities.signatureRangerArrowRain(rangerSkillTree, player);
                ability = "ArrowRain";
            }
        }

        // - Spellblade -
        if (HelperMethods.isUnlocked(spellbladeSkillTree, null, player)) {

            // Elemental Surge
            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationElementalSurge, player)) {
                ability_success = SpellbladeAbilities.signatureSpellbladeElementalSurge(spellbladeSkillTree, player);
                ability = "ElementalSurge";
            }
            // Elemental Impact
            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationElementalImpact, player)) {
                ability_success = SpellbladeAbilities.signatureSpellbladeElementalImpact(spellbladeSkillTree, player);
                ability = "ElementalImpact";
            }
            if (HelperMethods.isUnlocked(spellbladeSkillTree,
                    SkillReferencePosition.spellbladeSpecialisationSpellweaver, player)) {
                //Spell Weaver
                ability_success = SpellbladeAbilities.signatureSpellbladeSpellweaver(spellbladeSkillTree, player);
                ability = "Spellweaver";
            }
        }

        // - Crusader -
        if (HelperMethods.isUnlocked(crusaderSkillTree, null, player)
                && FabricLoader.getInstance().isModLoaded("paladins")) {

            // Heavensmith's Call
            if (HelperMethods.isUnlocked(crusaderSkillTree,
                    SkillReferencePosition.crusaderSpecialisationHeavensmithsCall, player)) {
                ability_success = CrusaderAbilities.signatureHeavensmithsCall(crusaderSkillTree, player);
                ability = "HeavensmithsCall";
            }
            // Sacred Onslaught
            if (HelperMethods.isUnlocked(crusaderSkillTree,
                    SkillReferencePosition.crusaderSpecialisationSacredOnslaught, player)) {
                ability_success = CrusaderAbilities.signatureCrusaderSacredOnslaught(crusaderSkillTree, player);
                ability = "SacredOnslaught";
            }
            // Consecration
            if (HelperMethods.isUnlocked(crusaderSkillTree,
                    SkillReferencePosition.crusaderSpecialisationConsecration, player)) {
                ability_success = CrusaderAbilities.signatureCrusaderConsecration(crusaderSkillTree, player);
                ability = "Consecration";
            }

        }

        // - Cleric -
        if (HelperMethods.isUnlocked(clericSkillTree, null, player)
                && FabricLoader.getInstance().isModLoaded("paladins")) {

            // Divine Intervention
            if (HelperMethods.isUnlocked(clericSkillTree,
                    SkillReferencePosition.clericSpecialisationDivineIntervention, player)) {
                ability_success = ClericAbilities.signatureClericDivineIntervention(clericSkillTree, player);
                ability = "DivineIntervention";
            }
            // Sacred Orb
            if (HelperMethods.isUnlocked(clericSkillTree,
                    SkillReferencePosition.clericSpecialisationSacredOrb, player)) {
                ability_success = ClericAbilities.signatureClericSacredOrb(clericSkillTree, player);
                ability = "SacredOrb";
            }
            // Anoint Weapon
            if (HelperMethods.isUnlocked(clericSkillTree,
                    SkillReferencePosition.clericSpecialisationAnointWeapon, player)) {
                ability_success = ClericAbilities.signatureClericAnointWeapon(player);
                ability = "AnointWeapon";
            }

        }


        // Trigger bonus gem effects
        if (ability_success && FabricLoader.getInstance().isModLoaded("simplyswords"))
            SimplySwordsGemEffects.doGenericAbilityGemEffects(player);


        //Return cooldown to client
        if (!player.getWorld().isClient) {
            SignatureAbilities.signatureAbilityCooldownManager(ability, ability_success, player);
            //System.out.println("Using ability: " + ability);
        }


    }

    // COOLDOWN MANAGEMENT

    public static void signatureAbilityCooldownManager(String ability, boolean useSuccess, PlayerEntity player) {
        float spellHasteCDReduce = SimplySkills.generalConfig.spellHasteCooldownReductionModifier;
        int minimumCD = SimplySkills.generalConfig.minimumAchievableCooldown * 1000;
        int useDelay = (int) SimplySkills.generalConfig.minimumTimeBetweenAbilityUse * 1000;
        int cooldown = 500;
        double sendCooldown;
        String type = "";

        switch (ability) {
            case "ArcaneBolt" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardArcaneBoltCooldown * 1000;
                type = "magic, arcane";
            }
            case "IceComet" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardIceCometCooldown * 1000;
                type = "magic, elemental, debuff";
            }
            case "MeteorShower" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardMeteorShowerCooldown * 1000;
                type = "magic, elemental";
            }
            case "StaticDischarge" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardStaticDischargeCooldown * 1000;
                type = "magic, elemental, debuff";
            }
            case "Berserking" -> {
                cooldown = SimplySkills.berserkerConfig.signatureBerserkerBerserkingCooldown * 1000;
                type = "physical, melee, buff, sacrificial";
            }
            case "Bloodthirsty" -> {
                cooldown = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyCooldown * 1000;
                type = "physical, melee, buff, recovery";
            }
            case "Rampage" -> {
                cooldown = SimplySkills.berserkerConfig.signatureBerserkerRampageCooldown * 1000;
                type = "physical, melee, buff";
            }
            case "SiphoningStrikes" -> {
                cooldown = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesCooldown * 1000;
                type = "physical, melee, buff, debuff, recovery";
            }
            case "Evasion" -> {
                cooldown = SimplySkills.rogueConfig.signatureRogueEvasionCooldown * 1000;
                type = "physical, buff";
            }
            case "Preparation" -> {
                cooldown = SimplySkills.rogueConfig.signatureRoguePreparationCooldown * 1000;
                type = "physical, buff";
            }
            case "ArrowRain" -> {
                cooldown = SimplySkills.rangerConfig.effectRangerArrowRainCooldown * 1000;
                type = "physical, arrow, buff";
            }
            case "Disengage" -> {
                cooldown = SimplySkills.rangerConfig.signatureRangerDisengageCooldown * 1000;
                type = "physical, debuff";
            }
            case "ElementalArrows" -> {
                cooldown = SimplySkills.rangerConfig.effectRangerElementalArrowsCooldown * 1000;
                type = "magic, arrow, elemental, buff";
            }
            case "ElementalImpact" -> {
                cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactCooldown * 1000;
                type = "magic, melee, charge, elemental";
            }
            case "ElementalSurge" -> {
                cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalSurgeCooldown * 1000;
                type = "magic, elemental, buff";
            }
            case "Spellweaver" -> {
                cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverCooldown * 1000;
                type = "magic, buff, elemental, melee";
            }
            case "HeavensmithsCall" -> {
                cooldown = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallCooldown * 1000;
                type = "physical, debuff";
            }
            case "SacredOnslaught" -> {
                cooldown = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtCooldown * 1000;
                type = "physical, melee, charge, buff, recovery";
            }
            case "Consecration" -> {
                cooldown = SimplySkills.crusaderConfig.signatureCrusaderConsecrationCooldown * 1000;
                type = "magic, buff, recovery";
            }
            case "DivineIntervention" -> {
                cooldown = SimplySkills.clericConfig.signatureClericDivineInterventionCooldown * 1000;
                type = "magic, healing, buff";
            }
            case "SacredOrb" -> {
                cooldown = SimplySkills.clericConfig.signatureClericSacredOrbCooldown * 1000;
                type = "magic, healing, buff";
            }
            case "AnointWeapon" -> {
                cooldown = SimplySkills.clericConfig.signatureClericAnointWeaponCooldown * 1000;
                type = "magic, healing, buff";
            }
        }

        // Do Gem Effects
        if (FabricLoader.getInstance().isModLoaded("simplyswords")) {
            cooldown = SimplySwordsGemEffects.renewed(player, cooldown, minimumCD);
            cooldown = SimplySwordsGemEffects.accelerant(player, cooldown, minimumCD);
        }


        // Calculations
        double spellHaste = player.getAttributeValue(SpellAttributes.HASTE.attribute);
        sendCooldown = cooldown - (((spellHaste - 100) * spellHasteCDReduce) * 100);

        if (sendCooldown < (minimumCD) && useSuccess) sendCooldown = minimumCD;
        if (!useSuccess) sendCooldown = useDelay;

        //System.out.println("Ability type: " + type);
        sendCooldownPacket((ServerPlayerEntity) player, (int) sendCooldown);
    }



    // -- SPELL CASTING --

    public static void castSpellEngineDumbFire(PlayerEntity player, String spellIdentifier) {

        // -- Cast spell at a target we are looking at --

        //Entity target = HelperMethods.getTargetedEntity(player, range);
        SpellCast.Action action = SpellCast.Action.RELEASE;
        Identifier spellID      = new Identifier(spellIdentifier);
        List<Entity> list       = new ArrayList<Entity>();
        //list.add(target);

        SpellHelper.performSpell(
                player.getWorld(),
                player,
                spellID,
                list,
                action,
                20);
    }

    public static void castSpellEngine(PlayerEntity player, String spellIdentifier) {

        ItemStack itemStack     = player.getMainHandStack();
        Identifier spellID      = new Identifier(spellIdentifier);

        SpellHelper.attemptCasting(player, itemStack, spellID, false);
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
                    player.getWorld(),
                    player,
                    spellID,
                    list,
                    action,
                    1);
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
        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
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
                    player.getWorld(),
                    player,
                    spellID,
                    list,
                    action,
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

    public static void sendCooldownPacket(ServerPlayerEntity player, int cooldown) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(cooldown);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(CooldownPacket.COOLDOWN_PACKET, buf);
        ServerPlayNetworking.send(player, CooldownPacket.COOLDOWN_PACKET , packet.getData());

    }


}
