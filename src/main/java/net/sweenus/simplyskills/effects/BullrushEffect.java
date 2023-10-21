package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class BullrushEffect extends StatusEffect {
    public BullrushEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {

                int bullrushVelocity = SimplySkills.berserkerConfig.signatureBerserkerBullrushVelocity;
                int bullrushRadius = SimplySkills.berserkerConfig.signatureBerserkerBullrushRadius;
                double bullrushDamageModifier = SimplySkills.berserkerConfig.signatureBerserkerBullrushDamageModifier;
                int bullrushHitFrequency = SimplySkills.berserkerConfig.signatureBerserkerBullrushHitFrequency;
                int bullrushImmobilizeDuration = SimplySkills.berserkerConfig.signatureBerserkerBullrushImmobilizeDuration;
                int bullrushStrengthDuration = SimplySkills.berserkerConfig.signatureBerserkerBullrushRelentlessDuration;
                int bullrushExhaustionPerStrength = SimplySkills.berserkerConfig.signatureBerserkerBullrushRelentlessExhaustPerStrength;

                player.setVelocity(livingEntity.getRotationVector().multiply(+bullrushVelocity));
                player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                player.velocityModified = true;
                int radius = bullrushRadius;
                double damage_multiplier = bullrushDamageModifier;
                double damage = (HelperMethods.getAttackDamage(livingEntity.getMainHandStack()) * damage_multiplier);

                Box box = HelperMethods.createBox(player, radius*2);
                for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            le.setVelocity((player.getX() - le.getX()) /4,  (player.getY() - le.getY()) /4, (player.getZ() - le.getZ()) /4);
                            if (player.age % bullrushHitFrequency == 0) {
                                le.damage(player.getDamageSources().playerAttack(player), (float) damage);
                                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT32,
                                        SoundCategory.PLAYERS, 0.6f, 1.0f);
                                if (HelperMethods.isUnlocked("simplyskills:berserker",
                                        SkillReferencePosition.berserkerSpecialisationRampageChargeImmob, player))
                                    le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, bullrushImmobilizeDuration, 0, false, false, true));

                                if (HelperMethods.isUnlocked("simplyskills:berserker",
                                        SkillReferencePosition.berserkerSignatureRampageChargeRelentless, player)
                                        &&player.hasStatusEffect(EffectRegistry.EXHAUSTION)) {
                                    int stacks = player.getStatusEffect(EffectRegistry.EXHAUSTION).getAmplifier();
                                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                                            bullrushStrengthDuration, stacks / bullrushExhaustionPerStrength, false, false, true));
                                    player.removeStatusEffect(EffectRegistry.EXHAUSTION);
                                }
                                HelperMethods.spawnParticlesPlane(
                                        player.getWorld(),
                                        ParticleTypes.CLOUD,
                                        player.getBlockPos(),
                                        radius-2, 0, 1, 0 );
                            }
                        }
                    }
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("simplyswords"))
            SimplySwordsGemEffects.warStandard(player);

        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
