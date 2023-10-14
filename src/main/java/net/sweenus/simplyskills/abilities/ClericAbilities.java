package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Random;

public class ClericAbilities {

    // Retribution
    public static void passiveClericAbility(PlayerEntity player, LivingEntity attacker) {
        int random = new Random().nextInt(100);
        int retributionChance = SimplySkills.crusaderConfig.passiveCrusaderRetributionChance;
        if (random < retributionChance)
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:paladins_judgement", 32, attacker);
    }


    //------- SIGNATURE ABILITIES --------

    // Divine Intervention
    //Your next healing spell cast grants the target Undying for X seconds


}
