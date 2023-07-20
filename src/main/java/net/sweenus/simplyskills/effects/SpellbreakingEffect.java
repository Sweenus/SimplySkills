package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.world.explosion.Explosion;
import net.spell_engine.entity.SpellProjectile;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.util.HelperMethods;

public class SpellbreakingEffect extends StatusEffect {
    public SpellbreakingEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {
            int frequency = SimplySkills.warriorConfig.passiveWarriorSpellbreakerFrequency;
            if (livingEntity.age % frequency == 0) {
                int radius = SimplySkills.warriorConfig.passiveWarriorSpellbreakerRadius;

                Box box = HelperMethods.createBox(livingEntity, radius);
                for (Entity entities : livingEntity.world.getOtherEntities(livingEntity, box, EntityPredicates.VALID_ENTITY)) {

                    if (entities != null) {
                        if (entities instanceof SpellProjectile pe) {
                            if (pe.getOwner() instanceof LivingEntity livingOwner) {
                                if (livingEntity instanceof PlayerEntity player) {
                                    if (!HelperMethods.checkFriendlyFire(livingOwner, player))
                                        break;
                                }
                            }
                            pe.world.createExplosion(pe, pe.getX(), pe.getY(), pe.getZ(), 0.2f, false, Explosion.DestructionType.NONE);
                            pe.discard();
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
