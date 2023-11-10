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
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Shadow public abstract int getMaxUseTime(ItemStack stack);

    @Invoker
    public static float callGetPullProgress(int useTicks) {
        throw new AssertionError();
    }

    @Inject(at = @At("HEAD"), method = "onStoppedUsing", cancellable = true)
    public void simplyskills$onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user instanceof PlayerEntity player) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                float requiredPullProgress = 1.0F;
                if (stack.getName().toString().contains("Shortbow") || stack.getName().toString().contains("shortbow")
                        || stack.getName().toString().contains("love"))
                    requiredPullProgress = 0.5F;

                // Calculate the use ticks
                int useTicks = this.getMaxUseTime(stack) - remainingUseTicks;

                // Effect - Elemental Arrows
                if (player.hasStatusEffect(EffectRegistry.ELEMENTALARROWS)) {
                    if (callGetPullProgress(useTicks) >= requiredPullProgress) {

                        if (AbilityEffects.effectRangerElementalArrows(player))
                            ci.cancel();
                    }
                }


                // Effect - Arrow Rain
                else if (player.hasStatusEffect(EffectRegistry.ARROWRAIN)) {
                    if (callGetPullProgress(useTicks) >= requiredPullProgress) {

                        if (AbilityEffects.effectRangerArrowRain(player))
                            ci.cancel();
                    }
                }

                // Effect - Marksman Snipe
                else if (player.hasStatusEffect(EffectRegistry.MARKSMAN)) {
                    if (callGetPullProgress(useTicks) >= requiredPullProgress) {

                        if (AbilityEffects.effectRangerMarksman(player))
                            ci.cancel();
                    }
                }


                //Effect Stealth
                if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                    WayfarerAbilities.passiveWayfarerBreakStealth(null, player, false, false);
                }

                // Use the proxy method to call the actual getPullProgress method
                if (callGetPullProgress(useTicks) >= requiredPullProgress && HelperMethods.isUnlocked("simplyskills:tree",
                        SkillReferencePosition.wayfarerQuickfire, player)) {
                    HelperMethods.incrementStatusEffect(user ,EffectRegistry.MARKSMANSHIP,40, 1, 6);
                }

            }
        }
    }
}