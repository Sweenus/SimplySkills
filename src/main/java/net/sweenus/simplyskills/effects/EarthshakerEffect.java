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
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class EarthshakerEffect extends StatusEffect {
    public EarthshakerEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    private float fallDistance;


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {

            int radius = 3;
            float damageIncrease = SimplySkills.warriorConfig.passiveWarriorHeavyWeightDamageIncreasePerTick;
            double damage_multiplier = 0.5;
            double damage = 1 + (livingEntity.getArmor() * damage_multiplier);
            DamageSource damageSource = DamageSource.GENERIC;
            fallDistance += damageIncrease;

            if (livingEntity.isOnGround()) {

                Box box = HelperMethods.createBox(livingEntity, radius);
                for (Entity entities : livingEntity.world.getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && !livingEntity.hasStatusEffect(StatusEffects.SLOW_FALLING)){
                            if (livingEntity instanceof PlayerEntity player) {
                                damageSource = DamageSource.player(player);
                                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                                        "simplyskills").get().contains(SkillReferencePosition.warriorHeavyWeight))
                                    damage +=fallDistance;
                                if (!HelperMethods.checkFriendlyFire(le, player))
                                    break;
                            }

                            le.setVelocity((le.getX() - livingEntity.getX()) /4,  (le.getY() - livingEntity.getY()) /4, (le.getZ() - livingEntity.getZ()) /4);
                            le.damage(damageSource, (float) damage);
                        }
                    }
                }
                livingEntity.world.playSoundFromEntity(null, livingEntity, SoundRegistry.SOUNDEFFECT14,
                        SoundCategory.PLAYERS, 0.3f, 1.1f);
                fallDistance = 0;
                livingEntity.removeStatusEffect(EffectRegistry.EARTHSHAKER);
            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
