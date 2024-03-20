package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class RapidFireEffect extends StatusEffect {
    public RapidFireEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private int arrowCount = 0;

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.RAPIDFIRE)) {
                if (player.getMainHandStack().getItem() instanceof BowItem || player.getMainHandStack().getItem() instanceof CrossbowItem) {

                    StatusEffectInstance rapidFire = player.getStatusEffect(EffectRegistry.RAPIDFIRE);
                    if (rapidFire == null)
                        return;

                    if (rapidFire.getDuration() % 4 == 0) {
                        player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                                SoundCategory.PLAYERS, 0.6f, 1.4f);
                        if (player.getMainHandStack().getItem() instanceof BowItem)
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:rapidfire", 3, player, null);
                        else if (player.getMainHandStack().getItem() instanceof CrossbowItem)
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:rapidfire_crossbow", 3, player, null);
                    } else if (rapidFire.getDuration() % 5 == 0) {
                        arrowCount++;
                        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:rapidfire_projectile", 3, player, null);
                    }
                    if (arrowCount > 2 && AscendancyAbilities.getAscendancyPoints(player) > 29) {
                        arrowCount = 0;
                        HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMANSHIP, 60, 1, 12);
                    }

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
