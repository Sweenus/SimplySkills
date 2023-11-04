package net.sweenus.simplyskills.abilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.SwordItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Random;

public class RogueAbilities {

    public static void passiveRogueBackstab(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity livingTarget) {
            int weaknessDuration = SimplySkills.rogueConfig.passiveRogueBackstabWeaknessDuration;
            int weaknessAmplifier = SimplySkills.rogueConfig.passiveRogueBackstabWeaknessAmplifier;
            if (livingTarget.getBodyYaw() < (player.getBodyYaw() + 32) &&
                    livingTarget.getBodyYaw() > (player.getBodyYaw() - 32)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                        weaknessDuration, weaknessAmplifier, false, false, true));
            }
        }
    }

    public static void passiveRogueSmokeBomb(PlayerEntity player) {
        int radius = SimplySkills.rogueConfig.passiveRogueSmokeBombRadius;
        int chance = SimplySkills.rogueConfig.passiveRogueSmokeBombChance;
        int auraDuration = SimplySkills.rogueConfig.passiveRogueSmokeBombAuraDuration;
        int blindnessDuration = SimplySkills.rogueConfig.passiveRogueSmokeBombBlindnessDuration;
        int blindnessAmplifier = SimplySkills.rogueConfig.passiveRogueSmokeBombBlindnessAmplifier;
        if (player.getRandom().nextInt(100) < chance) {
            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                        le.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,
                                blindnessDuration, blindnessAmplifier, false, false, true));

                    }
                }
            }
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZINGAURA, auraDuration, 0, false, false, true));
            player.getWorld().playSoundFromEntity(
                    null, player, SoundRegistry.SOUNDEFFECT32,
                    SoundCategory.PLAYERS, 0.4f, 1.2f);
        }
    }

    public static boolean passiveRogueEvasionMastery(PlayerEntity player) {

        int baseEvasionChance = SimplySkills.rogueConfig.passiveRogueEvasionMasteryChance;
        int extraEvasionChance = SimplySkills.rogueConfig.passiveRogueDeflectionIncreasedChance;
        int evasionChanceIncreasePerTier = SimplySkills.rogueConfig.passiveRogueEvasionMasteryChanceIncreasePerTier;
        int masteryEvasionMultiplier = SimplySkills.rogueConfig.passiveRogueEvasionMasterySignatureMultiplier;
        int evasionArmorThreshold = SimplySkills.wayfarerConfig.passiveWayfarerSlenderArmorThreshold - 1;
        int mastery = 0;

        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueDeflection, player)
                && player.getMainHandStack().getItem() instanceof SwordItem
                && player.getOffHandStack().getItem() instanceof SwordItem)
            mastery = baseEvasionChance + extraEvasionChance;
        else
            mastery = baseEvasionChance;

        int evasionMultiplier = 1;

        if (player.hasStatusEffect(EffectRegistry.EVASION))
            evasionMultiplier = masteryEvasionMultiplier;

        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueEvasionMasterySkilled, player))
            mastery = mastery + (evasionChanceIncreasePerTier * 2);
        else if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueEvasionMasteryProficient, player))
            mastery = mastery + evasionChanceIncreasePerTier;

        StatusEffectInstance statusEffect = player.getStatusEffect(EffectRegistry.BLADESTORM);
        if (statusEffect != null) {
            mastery = mastery + (statusEffect.getAmplifier()+1);
        }

        if (player.getRandom().nextInt(100) < (mastery * evasionMultiplier)) {
            if (player.getArmor() < evasionArmorThreshold) {

                if (FabricLoader.getInstance().isModLoaded("simplyswords"))
                    SimplySwordsGemEffects.deception(player);
                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.FX_SKILL_BACKSTAB,
                        SoundCategory.PLAYERS, 1, 1);
                return false;
            }
        }
        return true;
    }

    public static void passiveRogueOpportunisticMastery(Entity target, PlayerEntity player) {
        int basePoisonDuration = SimplySkills.rogueConfig.passiveRogueOpportunisticMasteryPoisonDuration;
        int basePoisonAmplifier = SimplySkills.rogueConfig.passiveRogueOpportunisticMasteryPoisonAmplifier;
        int poisonDurationIncreasePerTier = SimplySkills.rogueConfig.passiveRogueOpportunisticMasteryPoisonDurationIncreasePerTier;

        int mastery = basePoisonDuration;

        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueOpportunisticMasterySkilled, player))
            mastery = mastery + (poisonDurationIncreasePerTier * 2);
        else if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueOpportunisticMasteryProficient, player))
            mastery = mastery + poisonDurationIncreasePerTier;

        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, mastery, basePoisonAmplifier, false, false, true));
        }

    }

    public static void passiveRogueBackstabStealth(PlayerEntity player) {
        int stealthChance = SimplySkills.rogueConfig.passiveRogueBackstabStealthChancePerEnemy;
        int stealthDuration = SimplySkills.rogueConfig.passiveRogueBackstabStealthDuration;
        if (player.age % 20 == 0) {
            Box box = HelperMethods.createBox(player, 8);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) &&
                            le.hasStatusEffect(StatusEffects.WEAKNESS)
                            && HelperMethods.checkFriendlyFire(le, player)) {
                        if (player.getRandom().nextInt(100) < stealthChance) {
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, stealthDuration, 0, false, false, true));
                            player.getWorld().playSoundFromEntity(
                                    null, player, SoundRegistry.SOUNDEFFECT39,
                                    SoundCategory.PLAYERS, 0.6f, 1.6f);
                        }
                    }
                }
            }
        }
    }

    public static void passiveRoguePreparationShadowstrike(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            if (HelperMethods.isUnlocked("simplyskills:rogue",
                    SkillReferencePosition.rogueSpecialisationPreparationShadowstrike, player)) {
                int dashRange = SimplySkills.rogueConfig.signatureRoguePreparationShadowstrikeRange;
                int dashRadius = SimplySkills.rogueConfig.signatureRoguePreparationShadowstrikeRadius;
                int dashDamageModifier = SimplySkills.rogueConfig.signatureRoguePreparationShadowstrikeDamageModifier;
                int dashDamage = (int) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                DamageSource dashSource = player.getWorld().getDamageSources().playerAttack(player);
                BlockPos blockPos = player.getBlockPos().offset(player.getMovementDirection(), dashRange);

                Box box = HelperMethods.createBoxBetween(player.getBlockPos(), blockPos, dashRadius);
                for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            le.damage(dashSource, dashDamage * dashDamageModifier);

                            if (HelperMethods.isUnlocked("simplyskills:rogue",
                                    SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeVampire, player)) {
                                HelperMethods.buffSteal(player, le, true, true, false, false);
                                le.addStatusEffect(new StatusEffectInstance(EffectRegistry.DEATHMARK, 120, 0, false, false, true));
                            }

                        }
                    }
                }
                if (!player.isOnGround())
                    dashRange = dashRange /5;
                player.setVelocity(player.getRotationVector().multiply(+dashRange));
                player.setVelocity(player.getVelocity().x, 0, player.getVelocity().z);
                player.velocityModified = true;

                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                        SoundCategory.PLAYERS, 0.6f, 1.3f);

            }
        }
    }


    //------- SIGNATURE ABILITIES --------


    // Evasion
    public static boolean signatureRogueEvasion(String rogueSkillTree, PlayerEntity player) {
        int evasionDuration = SimplySkills.rogueConfig.signatureRogueEvasionDuration;
        int fanOfBladesDuration = SimplySkills.rogueConfig.signatureRogueFanOfBladesDuration;
        int fanOfBladesStacks = SimplySkills.rogueConfig.signatureRogueFanOfBladesStacks - 1;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.EVASION, evasionDuration, 0, false, false, true));

        if (HelperMethods.isUnlocked(rogueSkillTree,
                SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesAssault, player))
            fanOfBladesStacks = fanOfBladesStacks*2;

        if (HelperMethods.isUnlocked(rogueSkillTree,
                SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades, player))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FANOFBLADES,
                    fanOfBladesDuration, fanOfBladesStacks, false, false, true));

        return true;
    }

    // Preparation
    public static boolean signatureRoguePreparation(String rogueSkillTree, PlayerEntity player) {
        int preparationDuration = SimplySkills.rogueConfig.signatureRoguePreparationDuration;
        int speedAmplifier = SimplySkills.rogueConfig.signatureRoguePreparationSpeedAmplifier;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH,
                preparationDuration, 0, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                preparationDuration, speedAmplifier, false, false, true));

        if (HelperMethods.isUnlocked("simplyskills:rogue",
                SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeShield, player)) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.BARRIER, 450, 1, 5);
            if (player.hasStatusEffect(EffectRegistry.REVEALED))
                player.removeStatusEffect(EffectRegistry.REVEALED);
        }

        player.getWorld().playSoundFromEntity(
                null, player, SoundRegistry.SOUNDEFFECT39,
                SoundCategory.PLAYERS, 0.6f, 1.6f);

        if (HelperMethods.isUnlocked(rogueSkillTree,
                SkillReferencePosition.rogueSpecialisationPreparationShadowstrike, player)) {

            RogueAbilities.passiveRoguePreparationShadowstrike(player);
        }

        return true;
    }

    // Siphoning Strikes
    public static boolean signatureRogueSiphoningStrikes(String rogueSkillTree, PlayerEntity player) {

        int siphoningStrikesduration = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesDuration;
        int siphoningStrikesStacks = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesStacks;
        int siphoningStrikesMightyStacks = SimplySkills.rogueConfig.signatureRogueSiphoningStrikesMightyStacks;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SIPHONINGSTRIKES,
                siphoningStrikesduration, siphoningStrikesStacks, false, false, true));

        if (HelperMethods.isUnlocked(rogueSkillTree,
                SkillReferencePosition.rogueSpecialisationSiphoningStrikesMighty, player))
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, siphoningStrikesduration,
                    siphoningStrikesMightyStacks, 5);

        if (HelperMethods.isUnlocked(rogueSkillTree,
                SkillReferencePosition.rogueSpecialisationSiphoningStrikesAura, player))
            HelperMethods.incrementStatusEffect(player, EffectRegistry.IMMOBILIZINGAURA, siphoningStrikesduration,
                    1, 2);

        return true;
    }

    // Rogue Dagger Storm summon UNUSED
    public static void daggerstormSummon(PlayerEntity player) {
        Box box = HelperMethods.createBox(player, 8);
        int count = 12;
        int chance = 95;
        int location = -3;
        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_ENTITY)) {

            if (entities != null) {
                if ((entities instanceof SpellProjectile spe) && spe.getOwner() == player) {
                    count--;
                }

                if ((entities instanceof LivingEntity le) && count > 0) {
                    if (player.getRandom().nextInt(100) < chance && HelperMethods.checkFriendlyFire(le, player)) {
                        //SignatureAbilities.castSpellEngineIndirectTarget(player,
                        //"simplyskills:physical_dagger_homing",
                        //9, player);

                        SpellHelper.ImpactContext context = new SpellHelper.ImpactContext();
                        SpellProjectile projectile = new SpellProjectile(player.getWorld(),
                                player, player.getX() + location, player.getY(), player.getZ() + location,
                                SpellProjectile.Behaviour.FLY, new Identifier("simplyskills:physical_dagger_homing"), (Entity) null,
                                context, null);
                        Random random = new Random();
                        projectile.setVelocity(player.getVelocity().multiply(5).rotateZ(random.nextInt(280)).rotateX(random.nextInt(280)));
                        projectile.range = 356;
                        ProjectileUtil.setRotationFromVelocity(projectile, 0.2F);
                        projectile.setFollowedTarget(le);
                        player.getWorld().spawnEntity(projectile);
                        location ++;
                    }
                }

            }
        }
    }


}
