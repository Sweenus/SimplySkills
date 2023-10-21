package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsRequiredMethods;
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
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof PlayerEntity player) {
                int ability_timer = Objects.requireNonNull(player.getStatusEffect(EffectRegistry.LEAPSLAM)).getDuration();
                int radius = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamRadius;
                int immobilizeDuration = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamImmobilizeDuration;
                double leapVelocity = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamVelocity;
                double height = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamHeight;
                double descentVelocity = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamDescentVelocity;
                double damage_multiplier = SimplySkills.berserkerConfig.signatureBerserkerLeapSlamDamageModifier;
                double damage = (HelperMethods.getAttackDamage(livingEntity.getMainHandStack()) * damage_multiplier);

                if (ability_timer >= 60) {
                    player.setVelocity(livingEntity.getRotationVector().multiply(+leapVelocity));
                player.setVelocity(livingEntity.getVelocity().x, height, livingEntity.getVelocity().z);
                player.velocityModified = true;
                }
                else if (ability_timer <= 50) {
                    //player.setVelocity(livingEntity.getRotationVector().multiply(+1.01));
                    player.setVelocity(livingEntity.getVelocity().x, -descentVelocity, livingEntity.getVelocity().z);
                    player.velocityModified = true;

                    if (player.isOnGround()) {

                        Box box = HelperMethods.createBox(player, radius * 2);
                        for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                            if (entities != null) {
                                if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                                            SkillReferencePosition.berserkerSpecialisationBerserkingLeapPull, player))
                                        le.setVelocity((player.getX() - le.getX()) / 4, (player.getY() - le.getY()) / 4, (player.getZ() - le.getZ()) / 4);
                                    else
                                        le.setVelocity((le.getX() - player.getX()) / 4, (le.getY() - player.getY()) / 4, (le.getZ() - player.getZ()) / 4);

                                    le.damage(player.getDamageSources().playerAttack(player), (float) damage);
                                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT14,
                                            SoundCategory.PLAYERS, 0.3f, 1.1f);
                                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                                            SkillReferencePosition.berserkerSpecialisationBerserkingLeapImmob, player))
                                        le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, immobilizeDuration, 0, false, false, true));
                                }
                            }
                        }
                        HelperMethods.spawnParticlesPlane(
                                player.getWorld(),
                                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                player.getBlockPos(),
                                radius, 0, 1, 0);
                        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT14,
                                SoundCategory.PLAYERS, 0.5f, 0.9f);
                        if (FabricLoader.getInstance().isModLoaded("simplyswords")
                                && SimplySwordsGemEffects.passVersionCheck()) {
                            int resetChance = SimplySwordsRequiredMethods.leapingChance;
                            if (SimplySwordsGemEffects.doSignatureGemEffects(player, "leaping")
                                    && player.getRandom().nextInt(100) < resetChance) {
                                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                                        SoundCategory.PLAYERS, 0.5f, 1.1f);
                                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LEAPSLAM,
                                        SimplySkills.berserkerConfig.signatureBerserkerLeapSlamDuration, 0, false, false, true));
                            }
                            else player.removeStatusEffect(EffectRegistry.LEAPSLAM);
                        }
                        else player.removeStatusEffect(EffectRegistry.LEAPSLAM);
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
