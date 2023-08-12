package net.sweenus.simplyskills.effects.instance;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public class SimplyStatusEffectInstance extends StatusEffectInstance {

    public LivingEntity sourceEntity;

    public SimplyStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
        super(type, duration, amplifier, ambient, showParticles, showIcon);
    }

    public LivingEntity getSourceEntity() {
        if (sourceEntity != null)
            return sourceEntity;

        return null;
    }

    public void setSourceEntity(LivingEntity entity) {
        if (entity != null)
            sourceEntity = entity;
    }


}
