package net.sweenus.simplyskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.client.keybinding.KeyBindingHandler;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.client.SimplySkillsClient;

public class CustomHud {

    public static Identifier ICON_TEXTURE = new Identifier(SimplySkills.MOD_ID, "textures/gui/cooldown_overlay.png");
    public static Identifier FRAME_TEXTURE = new Identifier("minecraft", "textures/gui/widgets.png");
    public static Identifier COOLDOWN_OVERLAY = new Identifier(SimplySkills.MOD_ID, "textures/gui/cooldown_overlay.png");

    public static void setSprite(Identifier sprite) {
        ICON_TEXTURE = sprite;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        // Anchor to the bottom right corner of the screen
        int guiAnchorX = ((scaledWidth / 2) + 86) + SimplySkills.generalConfig.signatureHudX;
        int guiAnchorY = (scaledHeight - 29) + SimplySkills.generalConfig.signatureHudY;

        int cooldown = SimplySkillsClient.abilityCooldown;
        long lastUseTime = SimplySkillsClient.lastUseTime;
        long remainingCooldown = 1;
        if (lastUseTime + cooldown > 0)
            remainingCooldown = (((lastUseTime + cooldown) - System.currentTimeMillis()) / 1000);
        Text remainingCooldownText = Text.of(String.valueOf(remainingCooldown));
        int cooldownOverlay = 1;
        if (remainingCooldown != 0) {
            int maxCooldown = 16;
            cooldownOverlay = (int) ((remainingCooldown / (double) maxCooldown) * 10);
        } else {
            cooldownOverlay = 0;
        }

        // Fetch the keybind from SimplySkillsClient
        KeyBinding keybind = SimplySkillsClient.bindingAbility1;

        // Get the localized name of the bound key
        Text keybindText = keybind.getBoundKeyLocalizedText();

        // Ensure the game isn't paused and the player exists
        if (client.player != null && client.currentScreen == null && !ICON_TEXTURE.toString().contains("cooldown_overlay")) {

            RenderSystem.getShaderTexture(0);

            // Reset the OpenGL state
            RenderSystem._setShaderTexture(0,0);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // Draw the icon at the top left of the screen
            client.getTextureManager().bindTexture(ICON_TEXTURE);
            RenderSystem.setShaderTexture(0, ICON_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            context.drawTexture(FRAME_TEXTURE, guiAnchorX + 5, guiAnchorY + 6, 58, 22, 24, 24, 256, 256);
            context.drawTexture(ICON_TEXTURE, guiAnchorX + 10, guiAnchorY + 10, 0, 0, 16, 16, 16, 16);
            if (remainingCooldown > 0) {
                context.drawTexture(COOLDOWN_OVERLAY, guiAnchorX + 10, guiAnchorY + 10, 0, 0, 16, cooldownOverlay, 16, 16);
                context.drawTexture(COOLDOWN_OVERLAY, guiAnchorX + 10, guiAnchorY + 10, 0, 0, 16, 16, 16, 16);
                context.drawCenteredTextWithShadow(client.textRenderer, remainingCooldownText, guiAnchorX + 18, guiAnchorY + 14, 16777215);
            }
            context.drawCenteredTextWithShadow(client.textRenderer, keybindText, guiAnchorX + 18, guiAnchorY + 0, 16777215);
            //drawTexture(matrices, 10, 10, 0, 0, 16, 16);

            // Reset the OpenGL state again
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        float textureWidth = 16.0F; // The width of your texture in pixels
        float textureHeight = 16.0F; // The height of your texture in pixels

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, ICON_TEXTURE);
        int z = 0;
        RenderSystem.setShader(RenderSystem::getShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(x, y + height, z).texture(u / textureWidth, (v + height) / textureHeight).next();
        bufferBuilder.vertex(x + width, y + height, z).texture((u + width) / textureWidth, (v + height) / textureHeight).next();
        bufferBuilder.vertex(x + width, y, z).texture((u + width) / textureWidth, v / textureHeight).next();
        bufferBuilder.vertex(x, y, z).texture(u / textureWidth, v / textureHeight).next();
        tessellator.draw();
        RenderSystem.disableBlend();
    }
}