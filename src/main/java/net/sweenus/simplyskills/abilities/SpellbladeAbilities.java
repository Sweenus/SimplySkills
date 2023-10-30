package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class SpellbladeAbilities {

    public static void effectSpellbladeWeaponExpert(PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:spellblade",
                SkillReferencePosition.spellbladeWeaponExpert, player)) {
            int chance = 5;
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, 60, 1, 3);
            if (player.getRandom().nextInt(100) > chance)
                HelperMethods.incrementStatusEffect(player, EffectRegistry.SPELLFORGED, 80, 1, 3);
        }
    }

    //------- SIGNATURE ABILITIES --------


    // Elemental Surge
    public static boolean signatureSpellbladeElementalSurge(String spellbladeSkillTree, PlayerEntity player) {
        int elementalSurgeDuration = SimplySkills.spellbladeConfig.signatureSpellbladeElementalSurgeDuration;
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE,
                elementalSurgeDuration, 0, false, false, true));
        return true;
    }
    // Elemental Impact
    public static boolean signatureSpellbladeElementalImpact(String spellbladeSkillTree, PlayerEntity player) {
        int elementalImpactDuration = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactDuration;
        int elementalImpactResistanceAmplifier = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactResistanceAmplifier;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALIMPACT,
                elementalImpactDuration, 0, false, false, true));

        if (HelperMethods.isUnlocked(spellbladeSkillTree,
                SkillReferencePosition.spellbladeSpecialisationElementalImpactResistance, player))
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                    elementalImpactDuration + 15, elementalImpactResistanceAmplifier, false, false, true));

        return true;
    }
    // Spellweaver
    public static boolean signatureSpellbladeSpellweaver(String spellbladeSkillTree, PlayerEntity player) {
        int spellweaverDuration = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverDuration;
        int spellweaverStacks = SimplySkills.spellbladeConfig.signatureSpellbladeSpellweaverStacks;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLWEAVER,
                spellweaverDuration, spellweaverStacks - 1, false, false, true));

        return true;
    }

}
