package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

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
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.hardy)) {
                passiveWarriorArmour(player);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void simplyskills$tickFallStartPos(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            //Stealth Sneak
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.stealth)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 15));
            }

            //Speed Sneak
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.sneak)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 15));
            }

            //Passive Area Strip
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.nullification)) {
                passiveAreaStrip(player);
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
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.reveal)) {
                passiveAreaReveal(player);
            }

            //Passive Berserker Sword Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.BerserkerSwordMastery)) {
                passiveBerserkerSwordMastery(player);
            }
            //Passive Berserker Axe Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.BerserkerAxeMastery)) {
                passiveBerserkerAxeMastery(player);
            }
            //Passive Berserker Ignore Pain
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.BerserkerIgnorePain)) {
                passiveBerserkerIgnorePain(player);
            }
            //Passive Berserker Recklessness
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.BerserkerRecklessness)) {
                passiveBerserkerRecklessness(player);
            }
            //Passive Berserker Challenge
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.BerserkerChallenge)) {
                passiveBerserkerChallenge(player);
            }
            //Passive Bulwark Shield Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.BulwarkShieldMastery)) {
                passiveBulwarkShieldMastery(player);
            }


        }
    }


    @Inject(at = @At("HEAD"), method = "attack")
    public void simplyskills$attack(Entity target,CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (target.isAttackable()) {
                if (!target.handleAttack(player)) {

                    //Passive Backstab
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "combat").get().contains(SkillReferencePosition.backstab)) {
                        passiveBackstab(target, player);


                    }
                }
            }
        }
    }




    //Bulky abilities go below

    private static void passiveAreaStrip(PlayerEntity player) {
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

    private static void passiveAreaReveal(PlayerEntity player) {
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

    private static void passiveBackstab(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.getBodyYaw() < (player.getBodyYaw() + 32) &&
                    livingTarget.getBodyYaw() > (player.getBodyYaw() - 32)) {
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60));
            }
        }
    }

    private static void passiveWarriorArmour(PlayerEntity player) {
        if (player.getRandom().nextInt(100) < 25) {
            if (player.getArmor() > 9) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100));
            } else {
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
                            "combat").get().contains(SkillReferencePosition.BerserkerSwordMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.BerserkerSwordMasteryProficient))
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
                            "combat").get().contains(SkillReferencePosition.BerserkerAxeMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.BerserkerAxeMasteryProficient))
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
                        "combat").get().contains(SkillReferencePosition.BerserkerIgnorePainSkilled))
                    resistanceStacks = 2;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "combat").get().contains(SkillReferencePosition.BerserkerIgnorePainProficient))
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
                            "combat").get().contains(SkillReferencePosition.BulwarkShieldMasterySkilled))
                        mastery = 2;
                    else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                            "combat").get().contains(SkillReferencePosition.BulwarkShieldMasteryProficient))
                        mastery = 1;

                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 25, mastery));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25));
                }
            }
        }
    }

}