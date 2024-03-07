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
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.network.CooldownPacket;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EntityRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SignatureAbilities {

    public static void signatureAbilityManager(PlayerEntity player, String abilityType) {

        String wizardSkillTree = "simplyskills:wizard";
        String berserkerSkillTree = "simplyskills:berserker";
        String rogueSkillTree = "simplyskills:rogue";
        String rangerSkillTree = "simplyskills:ranger";
        String spellbladeSkillTree = "simplyskills:spellblade";
        String crusaderSkillTree = "simplyskills:crusader";
        String clericSkillTree = "simplyskills:cleric";
        String necromancerSkillTree = "simplyskills:necromancer";
        String baseTree = "simplyskills:tree";
        String ascendancyTree = "simplyskills:ascendancy";
        boolean ability_success = false;
        String ability = "none";



        // - WIZARD -
        if (abilityType.contains("signature")) {
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
            // - Necromancer -
            if (HelperMethods.isUnlocked(necromancerSkillTree, null, player)) {

                // Divine Intervention
                if (HelperMethods.isUnlocked(necromancerSkillTree,
                        SkillReferencePosition.necromancerSpecialisationSummoningRitual, player)) {
                    ability_success = NecromancerAbilities.signatureNecromancerSummoningRitual(necromancerSkillTree, player);
                    ability = "SummoningRitual";
                }
            }
        }
        else if (abilityType.contains("ascendancy")) {
            // --- ASCENDANCY ---
            if (HelperMethods.isUnlocked(ascendancyTree, null, player)) {

                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyRighteousHammers, player)) {
                    ability_success = AscendancyAbilities.righteousHammers(player);
                    ability = "RighteousHammers";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyBoneArmor, player)) {
                    ability_success = AscendancyAbilities.boneArmor(player);
                    ability = "BoneArmor";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyCyclonicCleave, player)) {
                    ability_success = AscendancyAbilities.cyclonicCleave(player);
                    ability = "CyclonicCleave";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyMagicCircle, player)) {
                    ability_success = AscendancyAbilities.magicCircle(player);
                    ability = "MagicCircle";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyArcaneSlash, player)) {
                    ability_success = AscendancyAbilities.arcaneSlash(player);
                    ability = "ArcaneSlash";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyAgony, player)) {
                    ability_success = AscendancyAbilities.agony(player);
                    ability = "Agony";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyTorment, player)) {
                    ability_success = AscendancyAbilities.torment(player);
                    ability = "Torment";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyRapidfire, player)) {
                    ability_success = AscendancyAbilities.rapidfire(player);
                    ability = "Rapidfire";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyCataclysm, player)) {
                    ability_success = AscendancyAbilities.cataclysm(player);
                    ability = "Cataclysm";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyGhostwalk, player)) {
                    ability_success = AscendancyAbilities.ghostwalk(player);
                    ability = "Ghostwalk";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancySkywardSunder, player)) {
                    ability_success = AscendancyAbilities.skywardSunder(player);
                    ability = "SkywardSunder";
                }
                if (HelperMethods.isUnlocked(ascendancyTree,
                        SkillReferencePosition.ascendancyRighteousShield, player)) {
                    ability_success = AscendancyAbilities.righteousShield(player);
                    ability = "RighteousShield";
                }
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
        String cooldownType = "none";

        switch (ability) {
            case "ArcaneBolt" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardArcaneBoltCooldown * 1000;
                type = "magic, arcane";
                cooldownType = "signature";
            }
            case "IceComet" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardIceCometCooldown * 1000;
                type = "magic, elemental, debuff";
                cooldownType = "signature";
            }
            case "MeteorShower" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardMeteorShowerCooldown * 1000;
                type = "magic, elemental";
                cooldownType = "signature";
            }
            case "StaticDischarge" -> {
                cooldown = SimplySkills.wizardConfig.signatureWizardStaticDischargeCooldown * 1000;
                type = "magic, elemental, debuff";
                cooldownType = "signature";
            }
            case "Berserking" -> {
                cooldown = SimplySkills.berserkerConfig.signatureBerserkerBerserkingCooldown * 1000;
                type = "physical, melee, buff, sacrificial";
                cooldownType = "signature";
            }
            case "Bloodthirsty" -> {
                cooldown = SimplySkills.berserkerConfig.signatureBerserkerBloodthirstyCooldown * 1000;
                type = "physical, melee, buff, recovery";
                cooldownType = "signature";
            }
            case "Rampage" -> {
                cooldown = SimplySkills.berserkerConfig.signatureBerserkerRampageCooldown * 1000;
                type = "physical, melee, buff";
                cooldownType = "signature";
            }
            case "SiphoningStrikes" -> {
                cooldown = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesCooldown * 1000;
                type = "physical, melee, buff, debuff, recovery";
                cooldownType = "signature";
            }
            case "Evasion" -> {
                cooldown = SimplySkills.rogueConfig.signatureRogueEvasionCooldown * 1000;
                type = "physical, buff";
                cooldownType = "signature";
            }
            case "Preparation" -> {
                cooldown = SimplySkills.rogueConfig.signatureRoguePreparationCooldown * 1000;
                type = "physical, buff";
                cooldownType = "signature";
            }
            case "ArrowRain" -> {
                cooldown = SimplySkills.rangerConfig.effectRangerArrowRainCooldown * 1000;
                type = "physical, arrow, buff";
                cooldownType = "signature";
            }
            case "Disengage" -> {
                cooldown = SimplySkills.rangerConfig.signatureRangerDisengageCooldown * 1000;
                type = "physical, debuff";
                cooldownType = "signature";
            }
            case "ElementalArrows" -> {
                cooldown = SimplySkills.rangerConfig.effectRangerElementalArrowsCooldown * 1000;
                type = "magic, arrow, elemental, buff";
                cooldownType = "signature";
            }
            case "ElementalImpact" -> {
                cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactCooldown * 1000;
                type = "magic, melee, charge, elemental";
                cooldownType = "signature";
            }
            case "ElementalSurge" -> {
                cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeElementalSurgeCooldown * 1000;
                type = "magic, elemental, buff";
                cooldownType = "signature";
            }
            case "Spellweaver" -> {
                cooldown = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverCooldown * 1000;
                type = "magic, buff, elemental, melee";
                cooldownType = "signature";
            }
            case "HeavensmithsCall" -> {
                cooldown = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallCooldown * 1000;
                type = "physical, debuff";
                cooldownType = "signature";
            }
            case "SacredOnslaught" -> {
                cooldown = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtCooldown * 1000;
                type = "physical, melee, charge, buff, recovery";
                cooldownType = "signature";
            }
            case "Consecration" -> {
                cooldown = SimplySkills.crusaderConfig.signatureCrusaderConsecrationCooldown * 1000;
                type = "magic, buff, recovery";
                cooldownType = "signature";
            }
            case "DivineIntervention" -> {
                cooldown = SimplySkills.clericConfig.signatureClericDivineInterventionCooldown * 1000;
                type = "magic, healing, buff";
                cooldownType = "signature";
            }
            case "SacredOrb" -> {
                cooldown = SimplySkills.clericConfig.signatureClericSacredOrbCooldown * 1000;
                type = "magic, healing, buff";
                cooldownType = "signature";
            }
            case "AnointWeapon" -> {
                cooldown = SimplySkills.clericConfig.signatureClericAnointWeaponCooldown * 1000;
                type = "magic, healing, buff";
                cooldownType = "signature";
            }
            case "SummoningRitual" -> {
                cooldown = SimplySkills.necromancerConfig.signatureNecromancerSummoningRitualCooldown * 1000;
                type = "magic, minion";
                cooldownType = "signature";
            }
            case "RighteousHammers" -> {
                cooldown = 60 * 1000;
                type = "physical, buff";
                cooldownType = "ascendancy";
            }
            case "BoneArmor" -> {
                cooldown = 90 * 1000;
                type = "physical, buff, recovery";
                cooldownType = "ascendancy";
            }
            case "CyclonicCleave" -> {
                cooldown = 25 * 1000;
                type = "physical, magic, melee, channel";
                cooldownType = "ascendancy";
            }
            case "MagicCircle" -> {
                cooldown = 60 * 1000;
                type = "buff, magic";
                cooldownType = "ascendancy";
            }
            case "ArcaneSlash" -> {
                cooldown = 16 * 1000;
                type = "magic, melee,";
                cooldownType = "ascendancy";
            }
            case "Agony" -> {
                cooldown = 60 * 1000;
                type = "magic, debuff, healing";
                cooldownType = "ascendancy";
            }
            case "Torment" -> {
                cooldown = 60 * 1000;
                type = "magic, debuff";
                cooldownType = "ascendancy";
            }
            case "Rapidfire" -> {
                cooldown = 30 * 1000;
                type = "physical, arrow, channel";
                cooldownType = "ascendancy";
            }
            case "Cataclysm" -> {
                cooldown = 70 * 1000;
                type = "magic, channel, elemental";
                cooldownType = "ascendancy";
            }
            case "Ghostwalk" -> {
                cooldown = 45 * 1000;
                type = "magic, channel, soul";
                cooldownType = "ascendancy";
            }
            case "SkywardSunder" -> {
                cooldown = 16 * 1000;
                type = "melee, channel, buff, physical, magic";
                cooldownType = "ascendancy";
            }
            case "RighteousShield" -> {
                cooldown = 6 * 1000;
                type = "physical, magic, buff";
                cooldownType = "ascendancy";
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
        sendCooldownPacket((ServerPlayerEntity) player, (int) sendCooldown, cooldownType);
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

    public static void castSpellEngineIndirectTarget(PlayerEntity player, String spellIdentifier, int range,@Nullable Entity target,@Nullable BlockPos blockpos) {
        if (target == null && blockpos != null) {
            target = EntityRegistry.SPELL_TARGET_ENTITY.spawn( (ServerWorld) player.getWorld(),
                    blockpos,
                    SpawnReason.TRIGGERED);
        } else if (target == null && blockpos == null) {
            blockpos = HelperMethods.getBlockLookingAt(player, range);
            if (blockpos != null) {
                target = EntityRegistry.SPELL_TARGET_ENTITY.spawn((ServerWorld) player.getWorld(),
                        blockpos,
                        SpawnReason.TRIGGERED);
            }
        }

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
    public static void sendKeybindPacket(String type) {

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(type);
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(KeybindPacket.ABILITY1_PACKET, buf);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);

    }

    public static void sendCooldownPacket(ServerPlayerEntity player, int cooldown, String cooldownType) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(cooldown);
        buf.writeString(cooldownType);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(CooldownPacket.COOLDOWN_PACKET, buf);
        ServerPlayNetworking.send(player, CooldownPacket.COOLDOWN_PACKET , packet.getData());

    }


}
