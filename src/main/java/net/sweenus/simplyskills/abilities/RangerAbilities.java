package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RangerAbilities {

    public static void passiveRangerReveal(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerRevealFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerRevealRadius;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().equals(EffectRegistry.STEALTH)) {
                                le.removeStatusEffect(statusEffect.getEffectType());
                                le.addStatusEffect(new StatusEffectInstance(EffectRegistry.REVEALED, 180, 1, false, false, true));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRangerTamer(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerTamerFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerTamerRadius;
            int resistanceAmplifier = SimplySkills.rangerConfig.passiveRangerTamerResistanceAmplifier;
            int regenerationAmplifier = SimplySkills.rangerConfig.passiveRangerTamerRegenerationAmplifier;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null && entities instanceof  LivingEntity le) {
                    if ((entities instanceof Tameable te)) {
                        if (te.getOwner() != null) {
                            if (te.getOwnerUuid() == player.getUuid()) {
                                le.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,
                                        frequency + 5, regenerationAmplifier, false, false, true));
                                le.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                                        frequency + 5, resistanceAmplifier, false, false, true));
                            }
                        }
                    }
                }
            }
        }
    }
    public static void passiveRangerBonded(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerBondedFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerBondedRadius;
            int petMinimumHealthPercent = SimplySkills.rangerConfig.passiveRangerBondedPetMinimumHealthPercent;
            int healthTransferAmount = SimplySkills.rangerConfig.passiveRangerBondedHealthTransferAmount;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null && entities instanceof  LivingEntity le) {
                    if ((entities instanceof Tameable te)) {
                        if (te.getOwner() != null) {
                            if (te.getOwnerUuid() == player.getUuid()) {
                                float teHealthPercent = ((le.getHealth() / le.getMaxHealth()) * 100);
                                float playerHealthPercent = ((player.getHealth() / player.getMaxHealth()) * 100);
                                if (teHealthPercent > playerHealthPercent && teHealthPercent > petMinimumHealthPercent) {
                                    le.setHealth(le.getHealth() - healthTransferAmount);
                                    player.heal(healthTransferAmount);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static void passiveRangerTrained(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerTrainedFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerTrainedRadius;
            int strengthAmplifier = SimplySkills.rangerConfig.passiveRangerTrainedStrengthAmplifier;
            int speedAmplifier = SimplySkills.rangerConfig.passiveRangerTrainedSpeedAmplifier;
            int minimumHealthPercent = SimplySkills.rangerConfig.passiveRangerTrainedMinimumHealthPercent;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null && entities instanceof  LivingEntity le) {
                    if ((entities instanceof Tameable te)) {
                        if (te.getOwner() != null) {
                            if (te.getOwnerUuid() == player.getUuid()) {
                                float teHealthPercent = ((le.getHealth() / le.getMaxHealth()) * 100);
                                if (teHealthPercent > minimumHealthPercent) {
                                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                                            frequency + 5, strengthAmplifier, false, false, true));
                                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                                            frequency + 5, speedAmplifier, false, false, true));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRangerIncognito(PlayerEntity player) {
        int frequency = SimplySkills.rangerConfig.passiveRangerIncognitoFrequency;
        if (player.age % frequency == 0) {
            int radius = SimplySkills.rangerConfig.passiveRangerIncognitoRadius;

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null && entities instanceof  LivingEntity le) {
                    if ((entities instanceof Tameable te)) {
                        if (te.getOwner() != null) {
                            if (te.getOwnerUuid() == player.getUuid()) {
                                if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY,
                                            frequency + 5,0, false, false, true));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRangerElementalArrowsRenewal(PlayerEntity player) {
        int random = new Random().nextInt(100);
        int renewalChance = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalChance;
        int renewalDuration = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalDuration;
        int renewalMaxStacks = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalMaximumStacks;
        int renewalStacks = SimplySkills.rangerConfig.passiveRangerElementalArrowsRenewalStacks;
        if (random < renewalChance)
            HelperMethods.incrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS,
                    renewalDuration, renewalStacks, renewalMaxStacks);
    }


    //------- SIGNATURE ABILITIES --------


    // Disengage
    public static boolean signatureRangerDisengage(String rangerSkillTree, PlayerEntity player) {

        int radius = SimplySkills.rangerConfig.signatureRangerDisengageRadius;
        int velocity = SimplySkills.rangerConfig.signatureRangerDisengageVelocity;
        int height = SimplySkills.rangerConfig.signatureRangerDisengageHeight;
        int slownessDuration = SimplySkills.rangerConfig.signatureRangerDisengageSlownessDuration;
        int slownessAmplifier = SimplySkills.rangerConfig.signatureRangerDisengageSlownessAmplifier;
        int slowFallDuration = SimplySkills.rangerConfig.signatureRangerDisengageSlowFallDuration;
        int slowFallAmplifier = SimplySkills.rangerConfig.signatureRangerDisengageSlowFallAmplifier;

        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
            if (entities != null) {
                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                            slownessDuration, slownessAmplifier, false, false, true));

                }
            }
        }

        player.setVelocity(player.getRotationVector().negate().multiply(+velocity));
        player.setVelocity(player.getVelocity().x, height, player.getVelocity().z);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                slowFallDuration, slowFallAmplifier, false, false, true));
        player.velocityModified = true;

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationDisengageRecuperate, player))
            signatureRangerDisengageRecuperate(player);

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationDisengageExploitation, player))
            signatureRangerDisengageExploitation(player);

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationDisengageMarksman, player)) {
            int marksmanDuration = SimplySkills.rangerConfig.signatureRangerDisengageMarksmanDuration;
            int marksmanStacks = SimplySkills.rangerConfig.signatureRangerDisengageMarksmanStacks;
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMAN, marksmanDuration, marksmanStacks, 99);
        }
        return true;
    }

    // Disengage Recuperate
    public static void signatureRangerDisengageRecuperate(PlayerEntity player) {
        int radius = SimplySkills.rangerConfig.signatureRangerDisengageRecuperateRadius;

        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

            if (entities != null && entities instanceof  LivingEntity le) {
                if ((entities instanceof Tameable te)) {
                    if (te.getOwner() != null) {
                        if (te.getOwnerUuid() == player.getUuid()) {
                            le.heal(le.getMaxHealth());
                        }
                    }
                }
            }
        }
    }

    // Disengage Exploitation
    public static void signatureRangerDisengageExploitation(PlayerEntity player) {
        int radius = SimplySkills.rangerConfig.signatureRangerDisengageExploitationRadius;
        int effectDuration = SimplySkills.rangerConfig.signatureRangerDisengageExploitationDuration;

        Box box = HelperMethods.createBox(player, radius);
        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

            if (entities != null && entities instanceof LivingEntity le) {
                if ((entities instanceof Tameable te)) {
                    if (te.getOwner() != null) {
                        if (te.getOwnerUuid() == player.getUuid()) {
                            le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZINGAURA, effectDuration, 0, false, false, true));
                        }
                    }
                }
            }
        }
    }

    // Elemental Arrows
    public static boolean signatureRangerElementalArrows(String rangerSkillTree, PlayerEntity player) {
        int elementalArrowsDuration = SimplySkills.rangerConfig.effectRangerElementalArrowsDuration;
        int elementalArrowsStacks = SimplySkills.rangerConfig.effectRangerElementalArrowsStacks;
        int elementalArrowsStacksIncreasePerTier = SimplySkills.rangerConfig.effectRangerElementalArrowsStacksIncreasePerTier;

        int amplifier =elementalArrowsStacks;

        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationElementalArrowsStacksOne, player))
            amplifier = amplifier + elementalArrowsStacksIncreasePerTier;
        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationElementalArrowsStacksTwo, player))
            amplifier = amplifier + (elementalArrowsStacksIncreasePerTier * 2);
        if (HelperMethods.isUnlocked(rangerSkillTree,
                SkillReferencePosition.rangerSpecialisationElementalArrowsStacksThree, player))
            amplifier = amplifier + (elementalArrowsStacksIncreasePerTier * 3);

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALARROWS,
                elementalArrowsDuration, amplifier, false, false, true));

        return true;
    }

    // Elemental Arrows
    public static boolean signatureRangerArrowRain(String rangerSkillTree, PlayerEntity player) {
        int arrowRainDuration = SimplySkills.rangerConfig.effectRangerArrowRainDuration;
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ARROWRAIN, arrowRainDuration, 0, false, false, true));
        return true;
    }

    // Arrow Rain Elemental Artillery
    public static void signatureRangerElementalArtillery(ServerPlayerEntity player, SpellProjectile spellProjectile, Identifier spellId, SpellHelper.ImpactContext context, Spell .ProjectileData.Perks perks) {
        if (player != null && spellProjectile.age % 12 == 0 && spellProjectile.age > 30) {
            if (HelperMethods.isUnlocked("simplyskills:ranger", SkillReferencePosition.rangerSpecialisationArrowRainElementalArtillery, player)
                    && spellId.toString().contains("arrow_rain")) {

                Vec3d position = spellProjectile.getPos();
                List<String> list = new ArrayList<>();
                list.add("simplyskills:frost_arrow_homing");
                list.add("simplyskills:fire_arrow_homing");
                list.add("simplyskills:lightning_arrow_homing");

                Random rand = new Random();
                Identifier randomSpell = new Identifier(list.get(rand.nextInt(list.size())));

                SpellProjectile projectile = new SpellProjectile(spellProjectile.getWorld(),
                        (LivingEntity) spellProjectile.getOwner(), position.getX(), position.getY(), position.getZ(),
                        spellProjectile.behaviour(), randomSpell, (Entity) null,
                        context, perks.copy());

                projectile.setVelocity(spellProjectile.getVelocity());
                projectile.range = spellProjectile.range;
                ProjectileUtil.setRotationFromVelocity(projectile, 0.2F);

                int radius = 20;
                Box box = new Box(spellProjectile.getX() + radius, spellProjectile.getY() + (float) radius * 3, spellProjectile.getZ() + radius,
                        spellProjectile.getX() - radius, spellProjectile.getY() - (float) radius * 3, spellProjectile.getZ() - radius);
                for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                    if (entities != null && player.getRandom().nextInt(100) < 35) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            projectile.setFollowedTarget(le);
                            break;
                        }
                    }
                }

                spellProjectile.getWorld().spawnEntity(projectile);
            }
        }
    }

}
