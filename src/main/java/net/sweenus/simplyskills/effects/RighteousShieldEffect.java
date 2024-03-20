package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class RighteousShieldEffect extends StatusEffect {
    public RighteousShieldEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.RIGHTEOUSSHIELD)) {
                StatusEffectInstance righteousShield = player.getStatusEffect(EffectRegistry.RIGHTEOUSSHIELD);
                if (righteousShield == null)
                    return;

                if (righteousShield.getDuration() == 10) {
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                            SoundCategory.PLAYERS, 1f, 1.1f);
                    StatusEffectInstance effect = player.getStatusEffect(EffectRegistry.GOLDENAEGIS);
                    if (effect != null) {
                        int aegisStacks = effect.getAmplifier();

                        if (aegisStacks > 14) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:righteous_shield_projectile_4", 3, player, null);
                            HelperMethods.decrementStatusEffects(player, EffectRegistry.GOLDENAEGIS, 15);
                        } else if (aegisStacks > 9) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:righteous_shield_projectile_3", 3, player, null);
                            HelperMethods.decrementStatusEffects(player, EffectRegistry.GOLDENAEGIS, 10);
                        } else if (aegisStacks > 4) {
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:righteous_shield_projectile_2", 3, player, null);
                            HelperMethods.decrementStatusEffects(player, EffectRegistry.GOLDENAEGIS, 5);
                        } else {
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:righteous_shield_projectile", 3, player, null);
                            player.removeStatusEffect(EffectRegistry.GOLDENAEGIS);
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
