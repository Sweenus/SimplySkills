package net.sweenus.simplyskills.mixins;

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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spell_power.api.attributes.SpellAttributeEntry;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "onKilledOther")
    public void simplyskills$onKilledOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            // fervour
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.fervour)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 100));
            }


        }
    }

    @Inject(at = @At("HEAD"), method = "damage")
    public void simplyskills$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.warriorHeavyArmorMastery) ||
            SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.warriorMediumArmorMastery)) {
                passiveWarriorArmorMastery(player);
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
            "combat").get().contains(SkillReferencePosition.rogueSmokeBomb)) {
                passiveRogueSmokeBomb(player);
            }

            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.rogueEvasionMastery)) {
                passiveRogueEvasionMastery(player);
            }


        }
    }


    @Inject(at = @At("HEAD"), method = "takeShieldHit")
    public void simplyskills$takeShieldHit(LivingEntity attacker, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.bulwarkRebuke)) {
                passiveBulwarkRebuke(player, attacker);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void simplyskills$tickFallStartPos(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            //Print attributes to log for debug purposes (don't forget to disable this before shipping, or it's gonna be real awkward.)
            debugPrintAttributes(player);

            //Passive Rogue Stealth
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.rogueStealth)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 15));
            }

            //Passive Wayfarer Sneak
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.wayfarerSneak)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 15, 2));
            }

            //Passive Area Strip
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.initiateNullification)) {
                passiveInitiateNullification(player);
            }

            //Passive Area Cleanse
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.cleric)) {
                passiveAreaCleanse(player);
            }

            //Passive Self Cleanse
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.cleansing)) {
                passiveSelfCleanse(player);
            }

            //Passive Area Reveal
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.rangerReveal)) {
                passiveRangerReveal(player);
            }

            //Passive Berserker Sword Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.berserkerSwordMastery)) {
                passiveBerserkerSwordMastery(player);
            }
            //Passive Berserker Axe Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.berserkerAxeMastery)) {
                passiveBerserkerAxeMastery(player);
            }
            //Passive Berserker Ignore Pain
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.berserkerIgnorePain)) {
                passiveBerserkerIgnorePain(player);
            }
            //Passive Berserker Recklessness
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.berserkerRecklessness)) {
                passiveBerserkerRecklessness(player);
            }
            //Passive Berserker Challenge
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.berserkerChallenge)) {
                passiveBerserkerChallenge(player);
            }
            //Passive Bulwark Shield Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.bulwarkShieldMastery)) {
                passiveBulwarkShieldMastery(player);
            }
            //Passive Wayfarer Slender && Initiate Frail
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.wayfarerSlender)
            || SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.initiateFrail)) {
                passiveWayfarerSlender(player);
            }
            //Initiate Frail (weapon element)
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.initiateFrail)) {
                passiveInitiateFrail(player);
            }


        }
    }


    @Inject(at = @At("HEAD"), method = "attack")
    public void simplyskills$attack(Entity target,CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (target.isAttackable()) {
                if (!target.handleAttack(player)) {

                    //Passive Rogue Backstab
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.rogueBackstab)) {
                        passiveRogueBackstab(target, player);
                    }

                    //Passive Rogue Opportunistic Mastery
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.rogueOpportunisticMastery)) {
                        passiveRogueOpportunisticMastery(target, player);
                    }

                }
            }
        }
    }




    //Bulky abilities go below

    private static void passiveInitiateNullification(PlayerEntity player) {
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

    private static void passiveAreaCleanse(PlayerEntity player) {
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

    private static void passiveSelfCleanse(PlayerEntity player) {
        if (player.age % 120 == 0) {
            for (StatusEffectInstance statusEffect : player.getStatusEffects()) {
                if (statusEffect != null && !statusEffect.getEffectType().isBeneficial()) {
                    player.removeStatusEffect(statusEffect.getEffectType());
                    break;
                }
            }
        }
    }

    private static void passiveRangerReveal(PlayerEntity player) {
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

    private static void passiveRogueBackstab(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.getBodyYaw() < (player.getBodyYaw() + 32) &&
                    livingTarget.getBodyYaw() > (player.getBodyYaw() - 32)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60));
            }
        }
    }

    private static void passiveWarriorArmorMastery(PlayerEntity player) {
        if (player.getRandom().nextInt(100) < 25) {
            if (player.getArmor() > 9 && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.warriorHeavyArmorMastery)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100));
            } else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.warriorMediumArmorMastery)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100));
            }
        }
    }

    private static void passiveBerserkerSwordMastery(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getMainHandStack() != null) {
                if (player.getMainHandStack().getItem() instanceof SwordItem) {
                    int mastery = 0;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.berserkerSwordMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.berserkerSwordMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 25, mastery));
                }
            }
        }
    }

    private static void passiveBerserkerAxeMastery(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getMainHandStack() != null) {
                if (player.getMainHandStack().getItem() instanceof AxeItem) {

                    int mastery = 0;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.berserkerAxeMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.berserkerAxeMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 25, mastery));
                }
            }
        }
    }

    private static void passiveBerserkerIgnorePain(PlayerEntity player) {
        if (player.age % 20 == 0) {
            int resistanceStacks = 0;
            if (player.getHealth() <= (0.4 * player.getMaxHealth())) {

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "combat").get().contains(SkillReferencePosition.berserkerIgnorePainSkilled))
                    resistanceStacks = 2;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "combat").get().contains(SkillReferencePosition.berserkerIgnorePainProficient))
                    resistanceStacks = 1;

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 25, resistanceStacks));
            }
        }
    }

    private static void passiveBerserkerRecklessness(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getHealth() >= (0.7 * player.getMaxHealth())) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
            }
        }
    }

    private static void passiveBerserkerChallenge(PlayerEntity player) {
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

    private static void passiveBulwarkShieldMastery(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getOffHandStack() != null) {
                if (player.getOffHandStack().getItem() instanceof ShieldItem) {

                    int mastery = 0;

                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.bulwarkShieldMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.bulwarkShieldMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 25, mastery));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
                }
            }
        }
    }

    private static void passiveBulwarkRebuke(PlayerEntity player, LivingEntity attacker) {
        if (player.getRandom().nextInt(100) < 25) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80));
        }
    }

    private static void passiveWayfarerSlender(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (player.getArmor() > 14 && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.wayfarerSlender)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25));
            }
            if (player.getArmor() > 9 && (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.initiateFrail))){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25));
            }
        }
    }

    private static void passiveRogueSmokeBomb(PlayerEntity player) {
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

    private static void passiveRogueEvasionMastery(PlayerEntity player) {

        int mastery = 15;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "combat").get().contains(SkillReferencePosition.rogueEvasionMasterySkilled))
            mastery = 35;
        else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "combat").get().contains(SkillReferencePosition.rogueEvasionMasteryProficient))
            mastery = 25;

        if (player.getRandom().nextInt(100) < mastery) {
            if (player.getEquippedStack(EquipmentSlot.HEAD) == null
            && player.getEquippedStack(EquipmentSlot.CHEST) == null
            && player.getEquippedStack(EquipmentSlot.LEGS) == null
            && player.getEquippedStack(EquipmentSlot.FEET) == null) {

                player.timeUntilRegen = 20;

            }
        }
    }

    private static void passiveRogueOpportunisticMastery(Entity target, PlayerEntity player) {

        int mastery = 40;

        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "combat").get().contains(SkillReferencePosition.rogueOpportunisticMasterySkilled))
            mastery = 120;
        else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                "combat").get().contains(SkillReferencePosition.rogueOpportunisticMasteryProficient))
            mastery = 80;

        if (target instanceof LivingEntity livingTarget)
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, mastery));
        if (player.hasStatusEffect(StatusEffects.INVISIBILITY))
            player.removeStatusEffect(StatusEffects.INVISIBILITY);

    }

    private static void passiveInitiateFrail(PlayerEntity player) {
        if (player.age % 20 == 0) {
            if (HelperMethods.getAttackDamage(player.getMainHandStack()) > 6
                    || HelperMethods.getAttackDamage(player.getOffHandStack()) > 6
                    && SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "combat").get().contains(SkillReferencePosition.wayfarerSlender)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 25, 1));
            }
        }
    }







    //DEBUG
    private static void debugPrintAttributes(PlayerEntity player) {
        if (player.age % 20 == 0 && player.isSneaking()) {
            String attributeHealing = SpellPower.getSpellPower(MagicSchool.HEALING, player).toString();

            System.out.println("Healing: " + attributeHealing);
        }
    }

}