package net.sweenus.simplyskills.abilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.items.GraciousManuscript;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.ItemRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbilityLogic {

    // -- Unlock Manager --

    public static boolean skillTreeUnlockManager(PlayerEntity player, String categoryID) {

        if (HelperMethods.stringContainsAny(categoryID, SimplySkills.getSpecialisations())) {

            if (SimplySkills.generalConfig.removeUnlockRestrictions || (player.getMainHandStack().getItem() instanceof GraciousManuscript))
                return false;

            //Prevent unlocking multiple specialisations (kinda cursed ngl)
            List<String> specialisationList = SimplySkills.getSpecialisationsAsArray();
            for (String s : specialisationList) {
                //System.out.println("Comparing " + categoryID + " against " + s);
                if (categoryID.contains(s)) {

                    Collection<Category> categories = SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player);
                    for (Category value : categories) {
                        if (HelperMethods.stringContainsAny(value.getId().toString(), SimplySkills.getSpecialisations())) {
                            //System.out.println(player + " attempted to unlock a second specialisation. Denied.");
                            return true;
                        }
                    }

                }
            }


            //Process unlock
            if (categoryID.contains("simplyskills:wizard")
                    && !HelperMethods.isUnlocked("simplyskills:wizard", null, player)) {
                if (SimplySkills.wizardConfig.enableWizardSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills:berserker")
                    && !HelperMethods.isUnlocked("simplyskills:berserker", null, player)) {
                if (SimplySkills.berserkerConfig.enableBerserkerSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills:rogue")
                    && !HelperMethods.isUnlocked("simplyskills:rogue", null, player)) {
                if (SimplySkills.rogueConfig.enableRogueSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills:ranger")
                    && !HelperMethods.isUnlocked("simplyskills:ranger", null, player)) {
                if (SimplySkills.rangerConfig.enableRangerSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills:spellblade")
                    && !HelperMethods.isUnlocked("simplyskills:spellblade", null, player)) {
                if (SimplySkills.spellbladeConfig.enableSpellbladeSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills:crusader")
                    && !HelperMethods.isUnlocked("simplyskills:crusader", null, player)) {

                if (!FabricLoader.getInstance().isModLoaded("paladins"))
                    return true;

                if (SimplySkills.crusaderConfig.enableCrusaderSpecialisation) {
                    playUnlockSound(player);
                    return false;
                }
            } else if (categoryID.contains("simplyskills:cleric")
                    && !HelperMethods.isUnlocked("simplyskills:cleric", null, player)) {

                if (!FabricLoader.getInstance().isModLoaded("paladins"))
                    return true;

                if (SimplySkills.clericConfig.enableClericSpecialisation) {
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
        ruby.add("123");
        ruby.add("abc");

        for (String s : sapphire) {
            if (skillId.equals(s) && HelperMethods.isUnlocked(categoryId.toString(), s, player)) {
                for (String su : sapphire) {
                    SkillsAPI.getCategory(categoryId).get().getSkill(su).get().unlock(player);
                }
            }
        }
        for (String s : ruby) {
            if (skillId.equals(s) && HelperMethods.isUnlocked(categoryId.toString(), s, player)) {
                for (String su : ruby) {
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

    public static void onSpellCastEffects(PlayerEntity player, List<Entity> targets, Identifier spellId) {

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateEmpower, player))
            InitiateAbilities.passiveInitiateEmpower(player, spellId);

        if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.REVEALED, 180, 5, false, false, true));
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

        if (HelperMethods.isUnlocked("simplyskills:wizard", SkillReferencePosition.wizardSpellEcho, player)) {
            WizardAbilities.passiveWizardSpellEcho(player, targets);
        }

        if (HelperMethods.isUnlocked("simplyskills:spellblade", SkillReferencePosition.spellbladeWeaponExpert, player)) {
            SpellbladeAbilities.effectSpellbladeWeaponExpert(player);
        }

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateOverload, player))
            HelperMethods.incrementStatusEffect(player, EffectRegistry.OVERLOAD, 160, 1, 9);

        if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericMutualMending, player)
                && FabricLoader.getInstance().isModLoaded("paladins")) {
            ClericAbilities.passiveClericMutualMending(player, spellId, targets);
        }
        if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericHealingWard, player)
                && FabricLoader.getInstance().isModLoaded("paladins")) {
            ClericAbilities.passiveClericHealingWard(player, targets, spellId);
        }

    }

}
