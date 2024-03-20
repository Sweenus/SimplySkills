package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.abilities.NecromancerAbilities;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShadowAuraEffect extends StatusEffect {
    public ShadowAuraEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {
            HelperMethods.spawnOrbitParticles((ServerWorld) livingEntity.getWorld(), livingEntity.getPos(), ParticleTypes.SMOKE, 0.5, 3);
            if (livingEntity.age % Math.max((22 - (amplifier * 2)), 1) == 0) {
                int radius = 2;
                Box box = HelperMethods.createBox(livingEntity, radius);
                PlayerEntity effectivePlayer = getPlayerEntity(livingEntity);

                livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                        .filter(Objects::nonNull)
                        .filter(entity -> entity instanceof LivingEntity)
                        .forEach(entity -> {
                            LivingEntity le = (LivingEntity) entity;

                            if (effectivePlayer != null && HelperMethods.checkFriendlyFire(le, effectivePlayer)) {
                                le.timeUntilRegen = 0;
                                le.damage(effectivePlayer.getWorld().getDamageSources().indirectMagic(effectivePlayer, effectivePlayer),
                                        (float) effectivePlayer.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute) * ((float) amplifier / 5));
                                HelperMethods.spawnWaistHeightParticles((ServerWorld) livingEntity.getWorld(), ParticleTypes.SMOKE, livingEntity, le, 5);
                                le.timeUntilRegen = 0;
                            }
                        });
                if (effectivePlayer != null) {
                    float damage = (1 + (float) effectivePlayer.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute) * ((float) amplifier / 10));
                    if ((livingEntity.getHealth() - (damage * 2)) < 0 && livingEntity instanceof TameableEntity minion) {
                        NecromancerAbilities.effectShadowCombust(effectivePlayer, minion);
                        minion.removeStatusEffect(EffectRegistry.SHADOWAURA);
                    } else livingEntity.setHealth((livingEntity.getHealth() - damage));
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Nullable
    private static PlayerEntity getPlayerEntity(LivingEntity livingEntity) {
        PlayerEntity effectivePlayer = null;

        if (livingEntity instanceof ServerPlayerEntity) {
            effectivePlayer = (ServerPlayerEntity) livingEntity;
        } else if (livingEntity instanceof TameableEntity) {
            Entity owner = ((TameableEntity) livingEntity).getOwner();
            if (owner instanceof ServerPlayerEntity) {
                effectivePlayer = (ServerPlayerEntity) owner;
            }
        }
        return effectivePlayer;
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
