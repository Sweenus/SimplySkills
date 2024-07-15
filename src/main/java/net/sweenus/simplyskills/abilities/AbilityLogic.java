package net.sweenus.simplyskills.abilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import net.spell_engine.internals.SpellRegistry;
import net.spell_power.api.SpellSchool;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.items.GraciousManuscript;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.ItemRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AbilityLogic {

    // -- Unlock Manager --

    public static boolean skillTreeUnlockManager(PlayerEntity player, String categoryID) {

        if (net.sweenus.simplyskills.util.HelperMethods.stringContainsAny(categoryID, SimplySkills.getSpecialisations())) {

            if (SimplySkills.generalConfig.removeUnlockRestrictions || (player.getMainHandStack().getItem() instanceof GraciousManuscript))
                return false;

            //Prevent unlocking multiple specialisations (kinda cursed ngl)
            List<String> specialisationList = SimplySkills.getSpecialisationsAsArray();
            for (String s : specialisationList) {
                //System.out.println("Comparing " + categoryID + " against " + s);
                if (categoryID.contains(s)) {

                    for (Category value : (Iterable<Category>) SkillsAPI.streamUnlockedCategories((ServerPlayerEntity) player)::iterator) {
                        if (net.sweenus.simplyskills.util.HelperMethods.stringContainsAny(value.getId().toString(), SimplySkills.getSpecialisations())) {
                            //System.out.println(player + " attempted to unlock a second specialisation. Denied.");
                            return true;
                        }
                    }

                }
            }


            //Process unlock
            if (categoryID.contains("simplyskills:wizard")
                    && !net.sweenus.simplyskills.util.HelperMethods.isUnlocked("simplyskills:wizard", null, player)) {
                if (SimplySkills.wizardConfig.enableWizardSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:berserker")
                    && !HelperMethods.isUnlocked("simplyskills:berserker", null, player)) {
                if (SimplySkills.berserkerConfig.enableBerserkerSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:rogue")
                    && !HelperMethods.isUnlocked("simplyskills:rogue", null, player)) {
                if (SimplySkills.rogueConfig.enableRogueSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:ranger")
                    && !HelperMethods.isUnlocked("simplyskills:ranger", null, player)) {
                if (SimplySkills.rangerConfig.enableRangerSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:spellblade")
                    && !HelperMethods.isUnlocked("simplyskills:spellblade", null, player)) {
                if (SimplySkills.spellbladeConfig.enableSpellbladeSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:crusader")
                    && !HelperMethods.isUnlocked("simplyskills:crusader", null, player)) {

                if (!FabricLoader.getInstance().isModLoaded("paladins"))
                    return true;

                if (SimplySkills.crusaderConfig.enableCrusaderSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:cleric")
                    && !HelperMethods.isUnlocked("simplyskills:cleric", null, player)) {

                if (!FabricLoader.getInstance().isModLoaded("paladins"))
                    return true;

                if (SimplySkills.clericConfig.enableClericSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:necromancer")
                    && !HelperMethods.isUnlocked("simplyskills:necromancer", null, player)) {
                if (SimplySkills.necromancerConfig.enableNecromancerSpecialisation) {
                    playUnlockSound(player);
                    player.sendMessage(Text.translatable("system.simplyskills.unlock"));
                    return false;
                }
            } else if (categoryID.contains("simplyskills:ascendancy")
                    && !HelperMethods.isUnlocked("simplyskills:ascendancy", null, player)) {

                if (SimplySkills.generalConfig.enableAscendancy) {
                    playUnlockSound(player);
                    return false;
                }
            }

        }
        return false;
    }

    static void playUnlockSound(PlayerEntity player) {
        if (player.getMainHandStack().getItem() != ItemRegistry.GRACIOUSMANUSCRIPT)
            player.getWorld().playSoundFromEntity(null, player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                    SoundCategory.PLAYERS, 1, 1);
    }

    // Unlocks junction nodes of the corresponding type
    public static void performJunctionLogic(ServerPlayerEntity player, String skillId, Identifier categoryId) {
        List<String> sapphire = new ArrayList<>();
        sapphire.add(SkillReferencePosition.sapphire_portal_1);
        sapphire.add(SkillReferencePosition.sapphire_portal_2);
        List<String> ruby = new ArrayList<>();
        ruby.add(SkillReferencePosition.ruby_portal_1);
        ruby.add(SkillReferencePosition.ruby_portal_2);

        for (String s : sapphire) {
            if (skillId.equals(s) && HelperMethods.isUnlocked(categoryId.toString(), s, player)) {
                for (String su : sapphire) {
                    if (!HelperMethods.isUnlocked(categoryId.toString(), su, player))
                        SkillsAPI.getCategory(categoryId).get().getSkill(su).get().unlock(player);
                }
            }
        }
        for (String s : ruby) {
            if (skillId.equals(s) && HelperMethods.isUnlocked(categoryId.toString(), s, player)) {
                for (String su : ruby) {
                    if (!HelperMethods.isUnlocked(categoryId.toString(), su, player))
                        SkillsAPI.getCategory(categoryId).get().getSkill(su).get().unlock(player);
                }
            }
        }
    }

    public static void performTagEffects(PlayerEntity player, String tags) {

        if (tags.contains("magic")) {

        }
        if (tags.contains("physical")) {

        }
        if (tags.contains("arrow")) {

        }
        if (tags.contains("arcane")) {

        }

    }

    public static void onSpellCastEffects(PlayerEntity player, @Nullable List<Entity> targets,@Nullable Identifier spellId, @Nullable Set<? extends SpellSchool> schools) {
        SpellSchool school = null;
        if (spellId !=null)
            school = SpellRegistry.getSpell(spellId).school;

        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.initiateEmpower, player)
                || (FabricLoader.getInstance().isModLoaded("prominent")
                && HelperMethods.isUnlocked("puffish_skills:prom",
                SkillReferencePosition.initiateEmpower, player)))
            InitiateAbilities.passiveInitiateEmpower(player, school, schools);

        if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
            WayfarerAbilities.passiveWayfarerBreakStealth(null, player, false, false);
            if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateWhisperedWizardry, player))
                HelperMethods.incrementStatusEffect(player, EffectRegistry.SPELLFORGED, 80, 1, 5);
        } else if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateSpellcloak, player)
                && !player.hasStatusEffect(EffectRegistry.REVEALED)) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 40, 0 , false, false, true));
        }

        if (FabricLoader.getInstance().isModLoaded("simplyswords")) {
            SimplySwordsGemEffects.spellshield(player);
            SimplySwordsGemEffects.spellStandard(player);
        }

        if ((HelperMethods.isUnlocked("simplyskills:wizard", SkillReferencePosition.wizardSpellEcho, player) || AscendancyAbilities.magicCircleEffect(player)) && targets != null) {
            WizardAbilities.passiveWizardSpellEcho(player, targets);
        }

        if (HelperMethods.isUnlocked("simplyskills:spellblade", SkillReferencePosition.spellbladeWeaponExpert, player)) {
            SpellbladeAbilities.effectSpellbladeWeaponExpert(player);
        }

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateOverload, player))
            HelperMethods.incrementStatusEffect(player, EffectRegistry.OVERLOAD, 160, 1, 9);

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateEldritchEnfeeblement, player) && targets != null) {
            InitiateAbilities.passiveInitiateEldritchEnfeeblement(player, targets);
        }

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiatePerilousPrecision, player) && targets != null) {
            InitiateAbilities.passiveInitiatePerilousPrecision(player, targets);
        }

        NecromancerAbilities.effectDelightfulSuffering(player);

        // Not Amethyst Imbuement safe (Anything that requires Spell Engine spellId)
        if (spellId !=null) {
            if (FabricLoader.getInstance().isModLoaded("prominent")) {
                ProminenceAbilities.focusEffect(player, spellId);
            }

            if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericMutualMending, player)
                    && FabricLoader.getInstance().isModLoaded("paladins")) {
                ClericAbilities.passiveClericMutualMending(player, spellId, targets);
            }
            if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericHealingWard, player)
                    && FabricLoader.getInstance().isModLoaded("paladins")) {
                ClericAbilities.passiveClericHealingWard(player, targets, spellId);
            }

            if (school.id.toString().contains("physical_ranged")) {
                if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.wayfarerQuickfire, player))
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMANSHIP, 40, 1, 6);
                AbilityEffects.effectRangerElementalArrows(player);
            }

            if (school.id.toString().contains("physical_melee") && targets != null) {
                for (Entity target : targets) {
                    if (target instanceof LivingEntity) {
                        doMeleeOnHit((ServerPlayerEntity) player, target);
                    }
                }
            }

            CrusaderAbilities.signatureHeavensmithsCallImpact("simplyskills:crusader", targets, spellId, player);
        }
    }

    public static void doMeleeOnHit(ServerPlayerEntity player, Entity target) {
        //Passive Rogue Backstab
        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueBackstab, player)) {
            RogueAbilities.passiveRogueBackstab(target, player);
        }

        //Passive Rogue Opportunistic Mastery
        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueOpportunisticMastery, player)) {
            RogueAbilities.passiveRogueOpportunisticMastery(target, player);
        }

        //Effect Warrior Frenzy
        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.warriorFrenzy, player)) {
            WarriorAbilities.passiveWarriorFrenzy(player);
        }

        //Initiate Frail (weapon element)
        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.initiateFrail, player)
                && !HelperMethods.isUnlocked("simplyskills:spellblade",
                SkillReferencePosition.spellbladeWeaponExpert, player)) {
            InitiateAbilities.passiveInitiateFrail(player);
        }

        //Signature Passive Elemental Surge Renewal
        if (HelperMethods.isUnlocked("simplyskills:spellblade",
                SkillReferencePosition.spellbladeSpecialisationElementalSurgeRenewal, player)) {
            if (player.hasStatusEffect(EffectRegistry.ELEMENTALSURGE)) {
                int surgeDuration = player.getStatusEffect(EffectRegistry.ELEMENTALSURGE).getDuration();
                player.removeStatusEffect(EffectRegistry.ELEMENTALSURGE);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE, surgeDuration+3, 0, false, false, true));
            }
        }

        //Effect Bloodthirsty Tremor
        if (HelperMethods.isUnlocked("simplyskills:berserker",
                SkillReferencePosition.berserkerSpecialisationBloodthirstyTremor, player)) {
            AbilityEffects.effectBerserkerBloodthirstyTremor(player);
        }

        //Effect Bloodthirsty Tireless
        if (HelperMethods.isUnlocked("simplyskills:berserker",
                SkillReferencePosition.berserkerSpecialisationBloodthirstyTireless, player)) {
            AbilityEffects.effectBerserkerBloodthirstyTireless(player);
        }

        //Effect Berserking
        if (HelperMethods.isUnlocked("simplyskills:berserker",
                SkillReferencePosition.berserkerSpecialisationBerserking, player)) {
            AbilityEffects.effectBerserkerBerserking(target, player);
        }

        //Effect Siphoning Strikes
        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueSpecialisationSiphoningStrikes, player)) {
            AbilityEffects.effectRogueSiphoningStrikes(target, player);
        }

        //Effect Rogue Vanish
        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueSpecialisationSiphoningStrikesVanish, player)) {
            AbilityEffects.effectRogueSiphoningStrikesVanish(player);
        }

        //Effect Spell Weaving
        if (HelperMethods.isUnlocked("simplyskills:spellblade",
                SkillReferencePosition.spellbladeSpellweaving, player)) {
            AbilityEffects.effectSpellbladeSpellweaving(target, player);
        }

        //Effect Stealth
        if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
            WayfarerAbilities.passiveWayfarerBreakStealth(target, player, false, true);
        }

        //Passive Rage
        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.berserkerPath, player)) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.RAGE, 300, 1, 99);
        }

        //Passive Berserker Exploit
        if (HelperMethods.isUnlocked("simplyskills:berserker",
                SkillReferencePosition.berserkerExploit, player)) {
            BerserkerAbilities.passiveBerserkerExploit(target);
        }

        //Passive Warrior Twinstrike
        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.warriorTwinstrike, player)
                && target instanceof LivingEntity) {
            WarriorAbilities.passiveWarriorTwinstrike(player, (LivingEntity)target);
        }
        //Passive Prom Twinstrike
        if (FabricLoader.getInstance().isModLoaded("prominence")
                && HelperMethods.isUnlocked("puffish_skills:prom",
                SkillReferencePosition.warriorTwinstrike, player)
                && target instanceof LivingEntity) {
            ProminenceAbilities.promTwinstrike(player, (LivingEntity)target);
        }

        //Passive Warrior Swordfall
        if (target instanceof LivingEntity) {
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorSwordfall, player)
                    || (FabricLoader.getInstance().isModLoaded("prominent")
                    && HelperMethods.isUnlocked("puffish_skills:prom",
                    SkillReferencePosition.warriorSwordfall, player)))
                WarriorAbilities.passiveWarriorSwordfall(player, (LivingEntity)target);
        }

        //Signature Cleric Anoint Weapon
        if (player.hasStatusEffect(EffectRegistry.ANOINTED)
                && FabricLoader.getInstance().isModLoaded("paladins")) {
            ClericAbilities.signatureClericAnointWeaponEffect(player);
        }

        //Passive Warrior Spellbreaker
        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.warriorSpellbreaker, player)) {
            WarriorAbilities.passiveWarriorSpellbreakerOnHit(player);
        }
    }

}
