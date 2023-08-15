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
    public static boolean signatureDivineIntervention(String crusaderSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int heavensmithsCallRange = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallRange;
        int duration = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDADuration;

        if (HelperMethods.getTargetedEntity(player, heavensmithsCallRange) != null)
            blockpos = HelperMethods.getTargetedEntity(player, heavensmithsCallRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, heavensmithsCallRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 1);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && !HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;

                        if (HelperMethods.isUnlocked(crusaderSkillTree,
                                SkillReferencePosition.clericSpecialisationDivineInterventionMighty, player))
                            HelperMethods.incrementStatusEffect(le, EffectRegistry.MIGHT, duration, 4, 20);

                        if (HelperMethods.isUnlocked("simplyskills:crusader",
                                SkillReferencePosition.clericSpecialisationDivineInterventionSpellforged, player))
                            HelperMethods.incrementStatusEffect(le, EffectRegistry.SPELLFORGED, duration, 4, 20);

                        le.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, duration));

                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                "simplyskills:physical_heavensmiths_call",
                                3, le);
                        break;
                    }
                }
            }
        }
        return success;
    }


}
