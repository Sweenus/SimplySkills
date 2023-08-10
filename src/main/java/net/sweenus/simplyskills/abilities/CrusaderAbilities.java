package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.paladins.effect.Effects;
import net.puffish.skillsmod.api.Skill;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Random;

public class CrusaderAbilities {

    // Retribution
    public static void passiveCrusaderRetribution(PlayerEntity player, LivingEntity attacker) {
        int random = new Random().nextInt(100);
        int retributionChance = 15; //SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalChance;
        if (random < retributionChance)
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:paladins_judgement", 32, attacker);
    }


    //Exhaustive Recovery
    public static void passiveCrusaderExhaustiveRecovery(PlayerEntity player, LivingEntity attacker) {
        int random = new Random().nextInt(100);
        int recoveryChance = 15; //SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalChance;
        if (random < recoveryChance) {
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:paladins_flash_heal", 32, player);
            HelperMethods.incrementStatusEffect(player, EffectRegistry.EXHAUSTION, 300, 9, 99);
        }
    }

    //Aegis
    public static void passiveCrusaderAegis(PlayerEntity player) {
        int frequency = 200;
        int stacksRemoved = 25;
        if (player.hasStatusEffect(Effects.DIVINE_PROTECTION)) {
            if (player.age % frequency == 0 && player.getStatusEffect(Effects.DIVINE_PROTECTION).getAmplifier() > stacksRemoved) {
                SignatureAbilities.castSpellEngineIndirectTarget(player, "paladins:divine_protection", 32, player);
                HelperMethods.decrementStatusEffects(player, EffectRegistry.EXHAUSTION, stacksRemoved);
            }
        }
    }


    //------- SIGNATURE ABILITIES --------

    // Heavensmith's Call
    public static void effectDivineAdjudication(PlayerEntity player) {
        int frequency = 20; //SimplySkills.wizardConfig.signatureWizardMeteoricWrathFrequency;

        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.crusaderSpecialisationHeavensmithsCall, player) &&
                player.hasStatusEffect(EffectRegistry.DIVINEADJUDICATION) && player.age % frequency == 0) {
            int chance = 15; //SimplySkills.wizardConfig.signatureWizardMeteoricWrathChance;
            int radius = 10; //SimplySkills.wizardConfig.signatureWizardMeteoricWrathRadius;
            String spellIdentifier = "simplyskills:paladins_judgement";


            if (SignatureAbilities.castSpellEngineAOE(player, spellIdentifier, radius, chance, true)) {
                if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.warriorFrenzy, player))
                    HelperMethods.decrementStatusEffects(player, EffectRegistry.EXHAUSTION, 5);
            }
        }

    }



}
