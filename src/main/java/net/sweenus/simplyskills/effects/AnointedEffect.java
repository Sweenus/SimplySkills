package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.particle.Particles;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class AnointedEffect extends StatusEffect {
    public AnointedEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {



                ItemStack stack = livingEntity.getMainHandStack();
                Vec3d itemPosition = livingEntity.getHandPosOffset(stack.getItem());

                HelperMethods.spawnParticle(livingEntity.getWorld(), Particles.holy_ascend.particleType,
                        itemPosition.getX(), itemPosition.getY(), itemPosition.getZ(), 0.02, 1.3, 0.03);
                HelperMethods.spawnParticle(livingEntity.getWorld(), Particles.holy_spark_mini.particleType,
                        itemPosition.getX(), itemPosition.getY(), itemPosition.getZ(), 0.01, 1.5, 0.02);

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
