package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.particle.Particles;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

import java.util.UUID;

public class FocusEffect extends StatusEffect {
    public FocusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    private static boolean hasUsed = false;

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {
            if (livingEntity.getMainHandStack().getItem() instanceof BowItem && livingEntity.isUsingItem()) {
                livingEntity.setVelocity(0, 0, 0);
                livingEntity.setMovementSpeed(0);
                livingEntity.velocityModified = true;
                StatusEffectInstance focusEffectInstance = livingEntity.getStatusEffect(EffectRegistry.FOCUS);
                if (focusEffectInstance != null && focusEffectInstance.getDuration() < 180)
                    hasUsed = true;
                if (livingEntity.age % 10 == 0) {
                    HelperMethods.incrementStatusEffect(livingEntity, EffectRegistry.MARKSMANSHIP, 16, 1, 15);
                    StatusEffectInstance marksmanshipInstance = livingEntity.getStatusEffect(EffectRegistry.MARKSMANSHIP);
                    if (marksmanshipInstance != null)
                        HelperMethods.spawnParticlesInFrontOfPlayer((ServerWorld) livingEntity.getWorld(),
                                livingEntity, Particles.holy_spark_mini.particleType,
                                8, Math.max(-1.0 ,-0.1 * (marksmanshipInstance.getAmplifier())),
                                1+marksmanshipInstance.getAmplifier());
                }
            }
            if (hasUsed && !livingEntity.isUsingItem()) {
                StatusEffectInstance focusEffectInstance = livingEntity.getStatusEffect(EffectRegistry.FOCUS);
                if (focusEffectInstance != null) {
                    livingEntity.getWorld().playSoundFromEntity(null, livingEntity, SoundRegistry.SOUNDEFFECT31,
                            SoundCategory.PLAYERS, 1.4f, 1.0f);
                    livingEntity.removeStatusEffect(EffectRegistry.FOCUS);
                    livingEntity.removeStatusEffect(EffectRegistry.MARKSMANSHIP);
                    hasUsed = false;
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (FabricLoader.getInstance().isModLoaded("prominent") && entity instanceof PlayerEntity player) {
            if (Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:draw_speed")) != null && Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:arrow_velocity")) != null) {
                EntityAttributeInstance attributeInstance = entity.getAttributeInstance(Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:draw_speed")));
                EntityAttributeInstance attributeInstance2 = entity.getAttributeInstance(Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:arrow_velocity")));
                if (attributeInstance != null && attributeInstance2 != null) {
                    EntityAttributeModifier modifier = new EntityAttributeModifier(UUID.fromString("c5d32b45-0d57-4fff-b4ca-6134bc6201a0"), "Focus draw speed", -300, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                    EntityAttributeModifier modifier2 = new EntityAttributeModifier(UUID.fromString("02dea644-d3b1-4bd4-9b3e-5ee96b093692"), "Focus arrow velocity", 2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                    attributeInstance.addTemporaryModifier(modifier);
                    attributeInstance2.addTemporaryModifier(modifier2);
                }
            }
        }
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (FabricLoader.getInstance().isModLoaded("prominent") && entity instanceof PlayerEntity player) {
            if (Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:draw_speed")) != null && Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:arrow_velocity")) != null) {
                EntityAttributeInstance attributeInstance = entity.getAttributeInstance(Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:draw_speed")));
                EntityAttributeInstance attributeInstance2 = entity.getAttributeInstance(Registries.ATTRIBUTE.get(new Identifier("zenith_attributes:arrow_velocity")));
                if (attributeInstance != null && attributeInstance2 != null) {
                    EntityAttributeModifier modifier = new EntityAttributeModifier(UUID.fromString("c5d32b45-0d57-4fff-b4ca-6134bc6201a0"), "Focus draw speed", -300, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                    EntityAttributeModifier modifier2 = new EntityAttributeModifier(UUID.fromString("02dea644-d3b1-4bd4-9b3e-5ee96b093692"), "Focus arrow velocity", 2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                    attributeInstance.removeModifier(modifier);
                    attributeInstance2.removeModifier(modifier2);
                }
            }
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
