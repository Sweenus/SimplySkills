package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;

public class WizardAbilities {

    public static void passiveWizardSpellEcho(PlayerEntity player, Entity target) {
        //Can we get Spell Identifier from raw Spell in future? This would be better

        int chance = SimplySkills.wizardConfig.passiveWizardSpellEchoChance;
        if (player.getRandom().nextInt(100) < chance) {

            List<String> list = new ArrayList<>();
            list.add("simplyskills:frost_arrow");
            list.add("simplyskills:fire_arrow");
            list.add("simplyskills:lightning_arrow");
            list.add("simplyskills:arcane_bolt");
            list.add("simplyskills:arcane_bolt_lesser");
            list.add("simplyskills:ice_comet");
            list.add("simplyskills:fire_meteor");
            list.add("simplyskills:static_discharge");
            int spellChoice = player.getRandom().nextInt(list.size());

            SignatureAbilities.castSpellEngineIndirectTarget(player, list.get(spellChoice), 120, target);
        }
    }


    //------- SIGNATURE ABILITIES --------


    public static boolean signatureWizardMeteorShower(String wizardSkillTree, PlayerEntity player, Vec3d blockpos) {

        int meteoricWrathDuration = SimplySkills.wizardConfig.signatureWizardMeteoricWrathDuration;
        int meteoricWrathStacks = SimplySkills.wizardConfig.signatureWizardMeteoricWrathStacks - 1;
        int meteorShowerRange = SimplySkills.wizardConfig.signatureWizardMeteorShowerRange;

        //Meteor Shower
        if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationMeteorShowerWrath, player))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.METEORICWRATH,
                    meteoricWrathDuration, meteoricWrathStacks));


        if (HelperMethods.getTargetedEntity(player, meteorShowerRange) !=null)
            blockpos = HelperMethods.getTargetedEntity(player, meteorShowerRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, meteorShowerRange);

        if (blockpos != null) {
            double xpos = blockpos.getX();
            double ypos = blockpos.getY();
            double zpos = blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 8);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationMeteorShowerGreater, player))
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:fire_meteor_large",
                                    8, le);
                        else
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:fire_meteor",
                                    8, le);
                    }
                }
            }
            return true;
        }
        return false;
    }


}
