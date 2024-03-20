package net.sweenus.simplyskills.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.puffish.skillsmod.client.rendering.ConnectionBatchedRenderer;
import net.sweenus.simplyskills.SimplySkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectionBatchedRenderer.class)
public class ConnectionBatchedRendererMixin {

    @Inject(method = "draw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
            ordinal = 0, shift = At.Shift.AFTER))
    private void changeOutlineColor(CallbackInfo ci) {
        // Change the color values as needed for the outline
        RenderSystem.setShaderColor(
                (float) SimplySkills.generalConfig.outerLineR,
                (float) SimplySkills.generalConfig.outerLineG,
                (float) SimplySkills.generalConfig.outerLineB,
                (float) SimplySkills.generalConfig.outerLineA
        );
    }

    @Inject(method = "draw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
            ordinal = 1, shift = At.Shift.AFTER))
    private void changeNormalColor(CallbackInfo ci) {
        // Change the color values as needed for the normal emits
        RenderSystem.setShaderColor(
                (float) SimplySkills.generalConfig.innerLineR,
                (float) SimplySkills.generalConfig.innerLineG,
                (float) SimplySkills.generalConfig.innerLineB,
                (float) SimplySkills.generalConfig.innerLineA
        );
    }

    @Inject(method = "draw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",
            ordinal = 2, shift = At.Shift.AFTER))
    private void changeExclusiveColor(CallbackInfo ci) {
        // Change the color values as needed for the exclusive emits
        RenderSystem.setShaderColor(1.0F, 0.3F, 0.3F, 1.0F); // Example new color
    }
}
