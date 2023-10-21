package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.sweenus.simplyskills.abilities.WayfarerAbilities;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Shadow private boolean charged;

    @Inject(at = @At("HEAD"), method = "onStoppedUsing", cancellable = true)
    public void simplyskills$onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user instanceof PlayerEntity player) {
            if (player instanceof ServerPlayerEntity serverPlayer) {

                //Break Stealth
                if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                    WayfarerAbilities.passiveWayfarerBreakStealth(null, player, false, false);
                }



            }
        }
    }

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void simplyskills$use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient && user != null) {
            if (user instanceof ServerPlayerEntity) {

                //Gain Stealth
                if (HelperMethods.isUnlocked("simplyskills:tree",
                        SkillReferencePosition.wayfarerUnseen, user)
                        && !user.hasStatusEffect(EffectRegistry.STEALTH)
                        && !this.charged) {

                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 45, 0, false, false, true));
                }

            }
        }
    }


}