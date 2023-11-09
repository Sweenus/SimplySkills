package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.sweenus.simplyskills.registry.SoundRegistry;

public class MarksmanshipEffect extends StatusEffect {
    public MarksmanshipEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.getWorld().playSoundFromEntity(null, entity, SoundRegistry.ACTIVATE_TOWER_BEACON,
                SoundCategory.PLAYERS, 0.1f, 1 + ((float) amplifier /10));
        super.onApplied(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
