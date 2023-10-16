package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.SimplySkills;

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
