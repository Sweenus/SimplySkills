package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Objects;

public class LeapSlamEffect extends StatusEffect {
    public LeapSlamEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {

            if (livingEntity instanceof PlayerEntity player) {
                int ability_timer = Objects.requireNonNull(player.getStatusEffect(EffectRegistry.LEAPSLAM)).getDuration();
                int radius = 3;

                if (ability_timer >= 60) {
                    player.setVelocity(livingEntity.getRotationVector().multiply(+1.5));
                player.setVelocity(livingEntity.getVelocity().x, 0.9, livingEntity.getVelocity().z);
                player.velocityModified = true;
                }
                else if (ability_timer <= 50) {
                    //player.setVelocity(livingEntity.getRotationVector().multiply(+1.01));
                    player.setVelocity(livingEntity.getVelocity().x, -1, livingEntity.getVelocity().z);
                    player.velocityModified = true;

                    if (player.isOnGround()) {

                        player.world.playSoundFromEntity(null, player, SoundRegistry.FX_UI_UNLOCK3,
                                SoundCategory.PLAYERS, 1, 0.8f);

                        Box box = HelperMethods.createBox(player, radius*2);
                        for (Entity entities : livingEntity.world.getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                            if (entities != null) {
                                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                            "simplyskills_berserker").get().contains(
                                            SkillReferencePosition.berserkerSpecialisationBerserkingLeapPull))
                                        le.setVelocity((player.getX() - le.getX()) /4,  (player.getY() - le.getY()) /4, (player.getZ() - le.getZ()) /4);
                                    else
                                        le.setVelocity((le.getX() - player.getX()) /4,  (le.getY() - player.getY()) /4, (le.getZ() - player.getZ()) /4);

                                    if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                            "simplyskills_berserker").get().contains(
                                            SkillReferencePosition.berserkerSpecialisationBerserkingLeapImmob))
                                        le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, 80));
                                }
                            }
                        }
                        player.removeStatusEffect(EffectRegistry.LEAPSLAM);
                    }
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
