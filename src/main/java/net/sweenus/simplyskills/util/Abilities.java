package net.sweenus.simplyskills.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;

import java.util.Random;

public class Abilities {


    //Ability methods for our Mixins


    public static void passiveInitiateNullification(PlayerEntity player) {
        if (player.age % 80 == 0) {
            int radius = 12;

            Box box = new Box(player.getX() + radius, player.getY() + (float) radius / 3, player.getZ() + radius,
                    player.getX() - radius, player.getY() - (float) radius / 3, player.getZ() - radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().isBeneficial()) {
                                le.removeStatusEffect(statusEffect.getEffectType());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveAreaCleanse(PlayerEntity player) {
        if (player.age % 80 == 0) {
            int radius = 12;

            Box box = new Box(player.getX() + radius, player.getY() + (float) radius / 3, player.getZ() + radius,
                    player.getX() - radius, player.getY() - (float) radius / 3, player.getZ() - radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && !HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && !statusEffect.getEffectType().isBeneficial()) {
                                le.removeStatusEffect(statusEffect.getEffectType());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveSelfCleanse(PlayerEntity player) {
        if (player.age % 120 == 0) {
            for (StatusEffectInstance statusEffect : player.getStatusEffects()) {
                if (statusEffect != null && !statusEffect.getEffectType().isBeneficial()) {
                    player.removeStatusEffect(statusEffect.getEffectType());
                    break;
                }
            }
        }
    }

    public static void passiveRangerReveal(PlayerEntity player) {
        if (player.age % 80 == 0) {
            int radius = 12;

            Box box = new Box(player.getX() + radius, player.getY() + (float) radius / 3, player.getZ() + radius,
                    player.getX() - radius, player.getY() - (float) radius / 3, player.getZ() - radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().equals(StatusEffects.INVISIBILITY)) {
                                le.removeStatusEffect(statusEffect.getEffectType());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void passiveRogueBackstab(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.getBodyYaw() < (player.getBodyYaw() + 32) &&
                    livingTarget.getBodyYaw() > (player.getBodyYaw() - 32)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60));
            }
        }
    }

    public static void passiveWarriorArmorMastery(PlayerEntity player) {
        if (player.getRandom().nextInt(100) < 25) {
            if (player.getArmor() > 9 && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.warriorHeavyArmorMastery)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100));
            } else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.warriorMediumArmorMastery)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100));
            }
        }
    }

    public static void passiveBerserkerSwordMastery(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getMainHandStack() != null) {
                if (player.getMainHandStack().getItem() instanceof SwordItem) {
                    int mastery = 0;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerSwordMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerSwordMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 25, mastery));
                }
            }
        }
    }

    public static void passiveBerserkerAxeMastery(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getMainHandStack() != null) {
                if (player.getMainHandStack().getItem() instanceof AxeItem) {

                    int mastery = 0;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerAxeMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.berserkerAxeMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 25, mastery));
                }
            }
        }
    }

    public static void passiveBerserkerIgnorePain(PlayerEntity player) {
        if (player.age % 20 == 0) {
            int resistanceStacks = 0;
            if (player.getHealth() <= (0.4 * player.getMaxHealth())) {

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills").get().contains(SkillReferencePosition.berserkerIgnorePainSkilled))
                    resistanceStacks = 2;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills").get().contains(SkillReferencePosition.berserkerIgnorePainProficient))
                    resistanceStacks = 1;

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 25, resistanceStacks));
            }
        }
    }

    public static void passiveBerserkerRecklessness(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getHealth() >= (0.7 * player.getMaxHealth())) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
            }
        }
    }

    public static void passiveBerserkerChallenge(PlayerEntity player) {
        if (player.age % 20 == 0) {
            int radius = 2;

            Box box = new Box(player.getX() + radius, player.getY() + radius, player.getZ() + radius,
                    player.getX() - radius, player.getY() - radius, player.getZ() - radius);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        if (player.hasStatusEffect(StatusEffects.HASTE)) {
                            int amplify = (player.getStatusEffect(StatusEffects.HASTE).getAmplifier() + 1);
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, amplify));
                        } else {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE));
                        }
                    }
                }
            }
        }
    }

    public static void passiveBulwarkShieldMastery(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getOffHandStack() != null) {
                if (player.getOffHandStack().getItem() instanceof ShieldItem) {

                    int mastery = 0;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.bulwarkShieldMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills").get().contains(SkillReferencePosition.bulwarkShieldMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 25, mastery));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
                }
            }
        }
    }

    public static void passiveBulwarkRebuke(PlayerEntity player, LivingEntity attacker) {
        if (player.getRandom().nextInt(100) < 25) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80));
        }
    }

    public static void passiveWayfarerSlender(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getArmor() > 14 && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.wayfarerSlender)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25));
            }
            if (player.getArmor() > 9 && (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.initiateFrail))){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25));
            }
        }
    }

    public static void passiveRogueSmokeBomb(PlayerEntity player) {
        if (player.getRandom().nextInt(100) < 25) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 100));
            Box box = HelperMethods.createBox((LivingEntity) player, 6);
            for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                        le.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60));

                    }
                }
            }
        }
    }

    public static boolean passiveRogueEvasionMastery(PlayerEntity player) {

        int mastery = 15;
        int evasionMultiplier = 1;

        if (player.hasStatusEffect(EffectRegistry.EVASION))
            evasionMultiplier = 2;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueEvasionMasterySkilled))
            mastery = 35;
        else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueEvasionMasteryProficient))
            mastery = 25;

        if (player.getRandom().nextInt(100) < (mastery * evasionMultiplier)) {
            if (player.getEquippedStack(EquipmentSlot.HEAD).isEmpty()
                    && player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()
                    && player.getEquippedStack(EquipmentSlot.LEGS).isEmpty()
                    && player.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {

                //player.timeUntilRegen = 15;
                player.world.playSoundFromEntity(null, player, SoundRegistry.FX_SKILL_BACKSTAB,
                        SoundCategory.PLAYERS, 1, 1);
                return true;


            }
        }
        return false;
    }

    public static void passiveRogueOpportunisticMastery(Entity target, PlayerEntity player) {

        int mastery = 40;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueOpportunisticMasterySkilled))
            mastery = 120;
        else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "simplyskills").get().contains(SkillReferencePosition.rogueOpportunisticMasteryProficient))
            mastery = 80;

        if ((target instanceof LivingEntity livingTarget) && player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, mastery));
            player.removeStatusEffect(StatusEffects.INVISIBILITY);
        }

    }

    public static void passiveInitiateFrail(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (HelperMethods.getAttackDamage(player.getMainHandStack()) > 6
                    || HelperMethods.getAttackDamage(player.getOffHandStack()) > 6
                    && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.wayfarerSlender)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 25, 1));
            }
        }
    }

    public static void signatureRangerDisengage(PlayerEntity player) {
        Box box = HelperMethods.createBox((LivingEntity) player, 6);
        for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
            if (entities != null) {
                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 250, 3));

                }
            }
        }
        player.setVelocity(player.getRotationVector().negate().multiply(+3));
        player.setVelocity(player.getVelocity().x, 1, player.getVelocity().z); // Prevent player flying to the heavens
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 80, 0));
        player.velocityModified = true;

    }

    public static void passiveRangerElementalArrowsRenewal(PlayerEntity player) {
        int random = new Random().nextInt(100);
        int renewalChance = 35;
        if (random < renewalChance)
            HelperMethods.incrementStatusEffect(player, EffectRegistry.ELEMENTALARROWS, 600, 1, 20);
    }

    public static void passiveRoguePreparationShadowstrike(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity sPlayerEntity) {
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "simplyskills_rogue").get()
                    .contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrike)) {
                int dashRange = 6;
                int dashRadius = 2;
                int dashDamageModifier = 3;
                int dashDamage = (int) HelperMethods.getAttackDamage(player.getMainHandStack());
                DamageSource dashSource = DamageSource.player(player);

                BlockPos blockPos = player.getBlockPos().offset(player.getMovementDirection(), dashRange);
                BlockState blockstate = player.world.getBlockState(blockPos);
                BlockState blockstateUp = player.world.getBlockState(blockPos.up(1));
                for (int i = dashRange; i > 0; i--) {
                    if (blockstate.isAir() && blockstateUp.isAir())
                        break;
                    blockPos = player.getBlockPos().offset(player.getMovementDirection(), i);
                }
                player.teleport(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                Box box = HelperMethods.createBoxBetween(player.getBlockPos(), blockPos, dashRadius);
                for (Entity entities : player.world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            le.damage(dashSource, dashDamage * dashDamageModifier);

                            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                    "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeBolt))
                                SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:soul_bolt_lesser", 64, le);
                            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                    "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationPreparationShadowstrikeVampire))
                                HelperMethods.buffSteal(player, le, true, true);

                        }
                    }
                }
            }

        }
    }





    // -- Unlock Manager --

    public static void skillTreeUnlockManager(PlayerEntity player, String skillID) {

        if (!skillID.contains(SkillReferencePosition.wizardPath) &&
                !skillID.contains(SkillReferencePosition.berserkerPath) &&
                !skillID.contains(SkillReferencePosition.crusaderPath) &&
                !skillID.contains(SkillReferencePosition.frostguardPath) &&
                !skillID.contains(SkillReferencePosition.roguePath) &&
                !skillID.contains(SkillReferencePosition.rangerPath) &&
                !skillID.contains(SkillReferencePosition.spellbladePath)) {
            return;
        }

        if (skillID.contains(SkillReferencePosition.wizardPath)
        && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_wizard")){
            SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_wizard");
            playUnlockSound(player);
        } else if (skillID.contains(SkillReferencePosition.berserkerPath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_berserker")){
            SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_berserker");
            playUnlockSound(player);
        } else if (skillID.contains(SkillReferencePosition.roguePath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_rogue")){
            SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_rogue");
            playUnlockSound(player);
        } else if (skillID.contains(SkillReferencePosition.rangerPath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_ranger")){
            SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_ranger");
            playUnlockSound(player);
        } else if (skillID.contains(SkillReferencePosition.spellbladePath)
                && !SkillsAPI.getUnlockedCategories((ServerPlayerEntity) player).contains("simplyskills_spellblade")){
            SkillsAPI.unlockCategory((ServerPlayerEntity) player, "simplyskills_spellblade");
            playUnlockSound(player);
        }


    }
    static void playUnlockSound(PlayerEntity player) {
        player.world.playSoundFromEntity(null, player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                SoundCategory.PLAYERS, 1, 1);
    }



    // -- DEBUG --
    public static void debugPrintAttributes(PlayerEntity player) {
        //
        // For checking Spell Power attribute values
        //
        if (player.age % 20 == 0 && player.isSneaking()) {
            String attributeArcane       = SpellPower.getSpellPower(MagicSchool.ARCANE, player).toString();
            String attributeFire         = SpellPower.getSpellPower(MagicSchool.FIRE, player).toString();
            String attributeFrost         = SpellPower.getSpellPower(MagicSchool.FROST, player).toString();
            String attributeHealing      = SpellPower.getSpellPower(MagicSchool.HEALING, player).toString();
            String attributeLightning    = SpellPower.getSpellPower(MagicSchool.LIGHTNING, player).toString();
            String attributeSoul         = SpellPower.getSpellPower(MagicSchool.SOUL, player).toString();

            System.out.println("Arcane: "    + attributeArcane);
            System.out.println("Fire: "      + attributeFire);
            System.out.println("Frost: "     + attributeFrost);
            System.out.println("Healing: "   + attributeHealing);
            System.out.println("Lightning: " + attributeLightning);
            System.out.println("Soul: "      + attributeSoul);
        }
    }

    public static void getSpellCooldown(LivingEntity livingEntity, String spellID) {
        //Identifier spell = new Identifier(spellID);
        //SpellHelper.getCooldownDuration(livingEntity);

    }



}
