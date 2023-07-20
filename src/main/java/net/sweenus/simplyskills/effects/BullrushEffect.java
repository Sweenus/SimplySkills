package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SignatureAbilities;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BullrushEffect extends StatusEffect {
    public BullrushEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {

                int bullrushVelocity = SimplySkills.berserkerConfig.signatureBerserkerBullrushVelocity;
                int bullrushRadius = SimplySkills.berserkerConfig.signatureBerserkerBullrushRadius;
                double bullrushDamageModifier = SimplySkills.berserkerConfig.signatureBerserkerBullrushDamageModifier;
                int bullrushHitFrequency = SimplySkills.berserkerConfig.signatureBerserkerBullrushHitFrequency;
                int bullrushImmobilizeDuration = SimplySkills.berserkerConfig.signatureBerserkerBullrushImmobilizeDuration;

                player.setVelocity(livingEntity.getRotationVector().multiply(+bullrushVelocity));
                player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                player.velocityModified = true;
                int radius = bullrushRadius;
                double damage_multiplier = bullrushDamageModifier;
                double damage = (HelperMethods.getAttackDamage(livingEntity.getMainHandStack()) * damage_multiplier);

                Box box = HelperMethods.createBox(player, radius*2);
                for (Entity entities : livingEntity.world.getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            le.setVelocity((player.getX() - le.getX()) /4,  (player.getY() - le.getY()) /4, (player.getZ() - le.getZ()) /4);
                            if (player.age % bullrushHitFrequency == 0) {
                                le.damage(DamageSource.player(player), (float) damage);
                                player.world.playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT32,
                                        SoundCategory.PLAYERS, 0.6f, 1.0f);
                            }
                            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                    "simplyskills_berserker").get().contains(
                                            SkillReferencePosition.berserkerSpecialisationRampageChargeImmob))
                                le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, bullrushImmobilizeDuration));
                        }
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
