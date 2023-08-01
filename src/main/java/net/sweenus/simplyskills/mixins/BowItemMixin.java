package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.sweenus.simplyskills.abilities.AbilityEffects;
import net.sweenus.simplyskills.abilities.WayfarerAbilities;
import net.sweenus.simplyskills.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Inject(at = @At("HEAD"), method = "onStoppedUsing", cancellable = true)
    public void simplyskills$onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user instanceof PlayerEntity player) {
            if (player instanceof ServerPlayerEntity serverPlayer) {

                // Effect - Elemental Arrows
                if (player.hasStatusEffect(EffectRegistry.ELEMENTALARROWS)) {
                    if (player.getItemUseTime() > 20) {

                        if (AbilityEffects.effectRangerElementalArrows(player))
                            ci.cancel();
                    }
                }


                // Effect - Arrow Rain
                else if (player.hasStatusEffect(EffectRegistry.ARROWRAIN)) {
                    if (player.getItemUseTime() > 20) {

                        if (AbilityEffects.effectRangerArrowRain(player))
                            ci.cancel();
                    }
                }

                // Effect - Marksman Snipe
                else if (player.hasStatusEffect(EffectRegistry.MARKSMAN)) {
                    if (player.getItemUseTime() > 20) {

                        if (AbilityEffects.effectRangerMarksman(player))
                            ci.cancel();
                    }
                }


                //Effect Stealth
                if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                    WayfarerAbilities.passiveWayfarerBreakStealth(null, player, false, false);
                }

            }
        }
    }


}