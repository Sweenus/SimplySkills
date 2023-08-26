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

import java.util.ArrayList;
import java.util.List;

public class WizardAbilities {

    public static void passiveWizardSpellEcho(PlayerEntity player, List<Entity> targets) {
        //Can we get Spell Identifier from raw Spell in future? This would be better
        Entity target = null;
        //Choose random target from list
        if (!targets.isEmpty())
            target = targets.get(player.getRandom().nextInt(targets.size()));

        int chance = SimplySkills.wizardConfig.passiveWizardSpellEchoChance;
        if (player.getRandom().nextInt(100) < chance) {

            List<String> list = new ArrayList<>();
            list.add("simplyskills:frost_arrow");
            list.add("simplyskills:fire_arrow");
            list.add("simplyskills:lightning_arrow");
            list.add("simplyskills:arcane_bolt_lesser");
            list.add("simplyskills:ice_comet");
            list.add("simplyskills:fire_meteor");
            list.add("simplyskills:static_discharge");
            int spellChoice = player.getRandom().nextInt(list.size());

            if (target != null)
                SignatureAbilities.castSpellEngineIndirectTarget(player, list.get(spellChoice), 120, target);
        }
    }


    //------- SIGNATURE ABILITIES --------


    // Meteor Shower
    public static boolean signatureWizardMeteorShower(String wizardSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int meteoricWrathDuration = SimplySkills.wizardConfig.signatureWizardMeteoricWrathDuration;
        int meteoricWrathStacks = SimplySkills.wizardConfig.signatureWizardMeteoricWrathStacks - 1;
        int meteorShowerRange = SimplySkills.wizardConfig.signatureWizardMeteorShowerRange;

        if (HelperMethods.getTargetedEntity(player, meteorShowerRange) !=null)
            blockpos = HelperMethods.getTargetedEntity(player, meteorShowerRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, meteorShowerRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 8);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;

                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationMeteorShowerWrath, player))
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.METEORICWRATH,
                                    meteoricWrathDuration, meteoricWrathStacks));

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
        }
        return success;
    }

    // Ice Comet
    public static boolean signatureWizardIceComet(String wizardSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int leapVelocity = SimplySkills.wizardConfig.signatureWizardIceCometLeapVelocity;
        double leapHeight = SimplySkills.wizardConfig.signatureWizardIceCometLeapHeight;
        int leapSlowfallDuration = SimplySkills.wizardConfig.signatureWizardIceCometLeapSlowfallDuration;
        int volleyDuration = SimplySkills.wizardConfig.signatureWizardIceCometVolleyDuration;
        int volleyStacks = SimplySkills.wizardConfig.signatureWizardIceCometVolleyStacks;
        int iceCometRange = SimplySkills.wizardConfig.signatureWizardIceCometRange;

        if (HelperMethods.getTargetedEntity(player, iceCometRange) != null)
            blockpos = HelperMethods.getTargetedEntity(player, iceCometRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, iceCometRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;

                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationIceCometLeap, player)) {
                            player.setVelocity(player.getRotationVector().negate().multiply(+leapVelocity));
                            player.setVelocity(player.getVelocity().x, leapHeight, player.getVelocity().z);
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, leapSlowfallDuration));
                            player.velocityModified = true;
                        }
                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationIceCometVolley, player))
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROSTVOLLEY,
                                    volleyDuration, volleyStacks));

                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationIceCometDamageOne, player))
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:ice_comet_large",
                                    3, le);
                        else if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationIceCometDamageTwo, player))
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:ice_comet_large_two",
                                    3, le);
                        else if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationIceCometDamageThree, player))
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:ice_comet_large_three",
                                    3, le);
                        else
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:ice_comet",
                                    3, le);
                    }
                }
            }
        }
        return success;
    }

    // Static Discharge
    public static boolean signatureWizardStaticDischarge(String wizardSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int amplifier = SimplySkills.wizardConfig.signatureWizardStaticDischargeBaseLeaps;
        int speedChance = SimplySkills.wizardConfig.signatureWizardStaticDischargeBaseSpeedChance;
        int speedChancePerTier = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedChancePerTier;
        int leapsPerTier = SimplySkills.wizardConfig.signatureWizardStaticDischargeLeapsPerTier;
        int staticDischargeRange = SimplySkills.wizardConfig.signatureWizardStaticDischargeRange;
        int staticChargeDuration = SimplySkills.wizardConfig.signatureWizardStaticChargeDuration;
        int dischargeSpeedDuration = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedDuration;
        int staticDischargeSpeedStacks = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedStacks;
        int staticDischargeSpeedMaxAmplifier = SimplySkills.wizardConfig.signatureWizardStaticDischargeSpeedMaxAmplifier;

        if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationStaticDischargeLeapTwo, player))
            amplifier = amplifier + leapsPerTier;
        else if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationStaticDischargeLeapThree, player))
            amplifier = amplifier + (leapsPerTier * 2);
        if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedTwo, player))
            speedChance = speedChance + speedChancePerTier;
        else if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedThree, player))
            speedChance = speedChance + (speedChancePerTier * 2);

        if (HelperMethods.getTargetedEntity(player, staticDischargeRange) !=null)
            blockpos = HelperMethods.getTargetedEntity(player, staticDischargeRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, staticDischargeRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;
                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                "simplyskills:static_discharge",
                                3, le);
                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationStaticDischargeLeap, player)) {
                            le.addStatusEffect(new StatusEffectInstance(EffectRegistry.STATICCHARGE,
                                    staticChargeDuration, amplifier));
                        }
                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationStaticDischargeSpeed, player)
                                && player.getRandom().nextInt(100) < speedChance)
                            HelperMethods.incrementStatusEffect(player, StatusEffects.SPEED,
                                    dischargeSpeedDuration,
                                    staticDischargeSpeedStacks,
                                    staticDischargeSpeedMaxAmplifier);
                        break;
                    }
                }
            }
        }
        return success;
    }

    // Arcane Bolt
    public static boolean signatureWizardArcaneBolt(String wizardSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int volleyDuration = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyDuration;
        int volleyStacks = SimplySkills.wizardConfig.signatureWizardArcaneBoltVolleyStacks - 1;
        int arcaneBoltRange = SimplySkills.wizardConfig.signatureWizardArcaneBoltRange;
        int radius = 3;
        if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationArcaneBoltLesser, player)) {
            radius = SimplySkills.wizardConfig.signatureWizardLesserArcaneBoltRadius;
        }

        if (HelperMethods.getTargetedEntity(player, arcaneBoltRange) !=null)
            blockpos = HelperMethods.getTargetedEntity(player, arcaneBoltRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, arcaneBoltRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;

                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationArcaneBoltVolley, player))
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARCANEVOLLEY,
                                    volleyDuration, volleyStacks));

                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationArcaneBoltLesser, player)) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:arcane_bolt_lesser",
                                    radius, le);
                        } else {
                            if (HelperMethods.isUnlocked(wizardSkillTree,
                                    SkillReferencePosition.wizardSpecialisationArcaneBoltGreater, player))
                                SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        "simplyskills:arcane_bolt_greater",
                                        radius, le);
                            else
                                SignatureAbilities.castSpellEngineIndirectTarget(player,
                                        "simplyskills:arcane_bolt",
                                        radius, le);
                            break;
                        }
                    }
                }
            }
        }
        return success;
    }

}
