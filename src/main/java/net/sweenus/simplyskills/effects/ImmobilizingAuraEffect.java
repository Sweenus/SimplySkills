package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class ImmobilizingAuraEffect extends StatusEffect {
    public ImmobilizingAuraEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {

            if (livingEntity.age % 20 == 0) {
                int radius = 2;

                Box box = HelperMethods.createBox(livingEntity, radius);
                for (Entity entities : livingEntity.world.getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le)) {
                            if (livingEntity instanceof TameableEntity te) {

                                if (te.getOwner() == null)
                                    break;
                                if (te.getOwner() instanceof PlayerEntity pe) {
                                    if (HelperMethods.checkFriendlyFire(le, pe)) {
                                        le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, 25));
                                    }
                                }
                            }
                            else if (livingEntity instanceof PlayerEntity playerEntity) {

                                if (HelperMethods.checkFriendlyFire(le, playerEntity)) {
                                    le.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE, 25));
                                }
                            }
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
