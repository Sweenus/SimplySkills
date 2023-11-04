package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class OverloadEffect extends StatusEffect {
    public OverloadEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            int radius = 3;
            double damage = (livingEntity.getMaxHealth() - 6) * (1 + livingEntity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute));
            DamageSource damageSource = livingEntity.getDamageSources().generic();
            DamageSource damageSourceMagic = livingEntity.getDamageSources().indirectMagic(livingEntity, livingEntity);

            if (livingEntity.getStatusEffect(EffectRegistry.OVERLOAD).getAmplifier() >= 5) {

                Box box = HelperMethods.createBox(livingEntity, radius);
                for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if (entities instanceof LivingEntity le){
                            if (livingEntity instanceof PlayerEntity player) {
                                damageSource = player.getDamageSources().playerAttack(player);
                                if (!HelperMethods.checkFriendlyFire(le, player))
                                    break;
                            }

                            le.setVelocity((le.getX() - livingEntity.getX()) /4,  (le.getY() - livingEntity.getY()) /4, (le.getZ() - livingEntity.getZ()) /4);
                            le.timeUntilRegen = 0;
                            le.damage(damageSourceMagic, (float) damage);
                            le.timeUntilRegen = 0;
                        }
                    }
                }
                livingEntity.damage(damageSourceMagic, (livingEntity.getMaxHealth() - 2));
                livingEntity.getWorld().playSoundFromEntity(null, livingEntity, SoundRegistry.SOUNDEFFECT14,
                        SoundCategory.PLAYERS, 0.8f, 0.9f);
                HelperMethods.spawnParticlesPlane(
                        livingEntity.getWorld(),
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        livingEntity.getBlockPos(),
                        radius, 0, 1, 0 );
                livingEntity.removeStatusEffect(EffectRegistry.OVERLOAD);
            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
