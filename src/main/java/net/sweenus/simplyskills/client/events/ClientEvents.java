package net.sweenus.simplyskills.client.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.sweenus.simplyskills.client.gui.CustomHud;

public class ClientEvents {

    private static final CustomHud customHud = new CustomHud();

    public static void registerClientEvents() {
        HudRenderCallback.EVENT.register((DrawContext context, float tickDelta) -> {
            double mouseX = MinecraftClient.getInstance().mouse.getX();
            double mouseY = MinecraftClient.getInstance().mouse.getY();
            customHud.render(context, (int)mouseX, (int)mouseY, tickDelta);
        });
    }
}
