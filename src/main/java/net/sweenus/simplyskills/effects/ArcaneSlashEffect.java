package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class ArcaneSlashEffect extends StatusEffect {
    public ArcaneSlashEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.ARCANESLASH)) {
                StatusEffectInstance arcaneSlash = player.getStatusEffect(EffectRegistry.ARCANESLASH);
                if (arcaneSlash == null)
                    return;

                if (arcaneSlash.getDuration() == 10 && AscendancyAbilities.getAscendancyPoints(player) < 30) {
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                            SoundCategory.PLAYERS, 1f, 1.1f);
                    SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:arcane_slash_projectile", 3, player, null);
                }
                else if (arcaneSlash.getDuration() == 15 && AscendancyAbilities.getAscendancyPoints(player) > 29) {
                    //player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                    //        SoundCategory.PLAYERS, 1f, 1.0f);
                    SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:arcane_slash_projectile_2", 3, player, null);
                }

            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("simplyswords"))
            SimplySwordsGemEffects.warStandard(player);

        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
