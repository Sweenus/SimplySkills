package net.sweenus.simplyskills.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerEntity;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.client.rendering.ConnectionBatchedRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(ConnectionBatchedRenderer.class)
public class ConnectionBatchedRendererMixin {

    @Inject(method = "draw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
            ordinal = 0, shift = At.Shift.AFTER))
    private void changeOutlineColor(CallbackInfo ci) {
        // Change the color values as needed for the outline
        RenderSystem.setShaderColor(0.156F, 0.148F, 0.132F, 1.0F);
    }

    @Inject(method = "draw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
            ordinal = 1, shift = At.Shift.AFTER))
    private void changeNormalColor(CallbackInfo ci) {
        // Change the color values as needed for the normal emits
        RenderSystem.setShaderColor(0.137F, 0.129F, 0.117F, 1.0F);
    }

    @Inject(method = "draw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
            ordinal = 2, shift = At.Shift.AFTER))
    private void changeExclusiveColor(CallbackInfo ci) {
        // Change the color values as needed for the exclusive emits
        RenderSystem.setShaderColor(1.0F, 0.3F, 0.3F, 1.0F); // Example new color
    }
}
