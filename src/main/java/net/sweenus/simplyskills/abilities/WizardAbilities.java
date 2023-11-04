package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.StaticChargeEffect;
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
                                    meteoricWrathDuration, meteoricWrathStacks, false, false, true));

                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationMeteorShowerGreater, player)) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:fire_meteor_large",
                                    8, le);
                            break;
                        }
                        else
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:fire_meteor",
                                    8, le);
                        break;
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
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, leapSlowfallDuration, 0, false, false, true));
                            player.velocityModified = true;
                        }
                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationIceCometVolley, player))
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROSTVOLLEY,
                                    volleyDuration, volleyStacks, false, false, true));

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
                        else {
                            //SignatureAbilities.castSpellEngineIndirectTarget(player,
                            //        "simplyskills:ice_comet",
                            //        3, le);
                            SignatureAbilities.castSpellEngine(player, "simplyskills:ice_comet");
                        }
                        break;
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
        int leapsPerTier = SimplySkills.wizardConfig.signatureWizardStaticDischargeLeapsPerTier;
        int staticDischargeRange = SimplySkills.wizardConfig.signatureWizardStaticDischargeRange;
        int staticChargeDuration = SimplySkills.wizardConfig.signatureWizardStaticChargeDuration;

        if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationStaticDischargeLeapTwo, player))
            amplifier = amplifier + leapsPerTier;
        else if (HelperMethods.isUnlocked(wizardSkillTree,
                SkillReferencePosition.wizardSpecialisationStaticDischargeLeapThree, player))
            amplifier = amplifier + (leapsPerTier * 2);

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
                        if (HelperMethods.isUnlocked(wizardSkillTree,
                                SkillReferencePosition.wizardSpecialisationStaticDischargeLightningBall, player)) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:lightning_ball",
                                    3, le);
                        } else {
                            SignatureAbilities.castSpellEngineIndirectTarget(player,
                                    "simplyskills:static_discharge",
                                    3, le);
                            if (HelperMethods.isUnlocked(wizardSkillTree,
                                    SkillReferencePosition.wizardSpecialisationStaticDischargeLeap, player)) {
                                le.addStatusEffect(new StatusEffectInstance(EffectRegistry.STATICCHARGE,
                                        staticChargeDuration, amplifier, false, false, true));
                            }
                            StaticChargeEffect.onHitEffects(player, StaticChargeEffect.calculateSpeedChance(player), le);
                        }

                        break;
                    }
                }
            }
        }

        return success;
    }

    // Static Discharge Lightning Ball
    public static void signatureWizardStaticDischargeBall(ServerPlayerEntity player, SpellProjectile spellProjectile,
                                                          Identifier spellId, SpellHelper.ImpactContext context,
                                                          Spell.ProjectileData.Perks perks) {

        if (player != null && spellProjectile.age % 5 == 0 && spellProjectile.age > 5) {
            if (spellId.toString().contains("lightning_ball") || spellId.toString().contains("lightning_lesser")) {

                if (HelperMethods.isUnlocked("simplyskills:wizard",
                        SkillReferencePosition.wizardSpecialisationStaticDischargeLightningBall, player)) {

                    Vec3d position = spellProjectile.getPos();
                    if (!spellId.toString().contains("ball_homing")) {
                        perks.pierce = 132;
                        SpellProjectile projectile = new SpellProjectile(spellProjectile.getWorld(),
                                (LivingEntity) spellProjectile.getOwner(), position.getX(), position.getY(), position.getZ(),
                                spellProjectile.behaviour(), new Identifier("simplyskills:lightning_lesser"), (Entity) null,
                                context, perks.copy());

                        projectile.setVelocity(spellProjectile.getVelocity().multiply(5));
                        projectile.range = spellProjectile.range;
                        ProjectileUtil.setRotationFromVelocity(projectile, 0.2F);

                        int radius = 5;
                        List<Entity> targets = new ArrayList<Entity>();
                        Box box = new Box(spellProjectile.getX() + radius, spellProjectile.getY() + (float) radius / 2, spellProjectile.getZ() + radius,
                                spellProjectile.getX() - radius, spellProjectile.getY() - (float) radius / 2, spellProjectile.getZ() - radius);

                        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                            if (entities != null && player.getRandom().nextInt(100) < 5) {
                                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                                    projectile.setFollowedTarget(le);
                                    spellProjectile.getWorld().spawnEntity(projectile);
                                    targets.add(le);
                                    AbilityLogic.onSpellCastEffects(player, targets, spellId);
                                    StaticChargeEffect.onHitEffects(player, StaticChargeEffect.calculateSpeedChance(player), le);
                                    targets.clear();
                                    break;

                                }
                            }
                        }
                    }
                }

                if (HelperMethods.isUnlocked("simplyskills:wizard",
                        SkillReferencePosition.wizardSpecialisationStaticDischargeLightningOrb, player)
                        && spellId.toString().contains("lightning_ball")) {

                    spellProjectile.setFollowedTarget(player);
                    spellProjectile.range = 512;
                    spellProjectile.setPitch(90);
                }

            }
        }
    }

    public static void signatureWizardLightningOrb(SpellProjectile spellProjectile, Entity followedTarget, Identifier spellId) {

        if (spellId != null) {
            if (spellId.toString().equals("simplyskills:lightning_ball_homing") && spellProjectile.age % 20 == 0 && followedTarget !=null) {
                if (spellProjectile.distanceTo(followedTarget) > 10) {
                    spellProjectile.teleport(followedTarget.getX(), followedTarget.getY(), followedTarget.getZ());
                    spellProjectile.setPitch(90);
                    spellProjectile.velocityModified = true;
                }
            }
        }

    }

    public static void signatureWizardLightningOrbBuff(PlayerEntity player) {
        int radius = SimplySkills.wizardConfig.signatureWizardLightningOrbBuffRadius;
        int frequency = SimplySkills.wizardConfig.signatureWizardLightningOrbBuffFrequency;
        int count = 0;
        Box box = new Box(player.getX() + radius, player.getY() + (float) radius * 3, player.getZ() + radius,
                player.getX() - radius, player.getY() - (float) radius * 3, player.getZ() - radius);
        if (player.age % frequency == 0) {
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_ENTITY)) {
                if (entities != null && player.getRandom().nextInt(100) < SimplySkills.wizardConfig.signatureWizardLightningOrbBuffChance) {
                    if ((entities instanceof SpellProjectile spe) && spe.getOwner() != null) {
                        if (spe.getOwner() == player)
                            count ++;
                    }
                }
            }
            if (count > 0)
                HelperMethods.incrementStatusEffect(player, EffectRegistry.SOULSHOCK,
                        frequency + 5, count, count);
        }
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
                                    volleyDuration, volleyStacks, false, false, true));

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
