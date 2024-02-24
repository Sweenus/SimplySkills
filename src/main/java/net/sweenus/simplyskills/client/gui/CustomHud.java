package net.sweenus.simplyskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.client.SimplySkillsClient;

public class CustomHud {

    public static Identifier ICON_TEXTURE = new Identifier(SimplySkills.MOD_ID, "textures/gui/ability1_icon.png");
    public static Identifier ICON_TEXTURE_2 = new Identifier(SimplySkills.MOD_ID, "textures/gui/ability2_icon.png");
    public static Identifier FRAME_TEXTURE = new Identifier("minecraft", "textures/gui/widgets.png");
    public static Identifier COOLDOWN_OVERLAY = new Identifier(SimplySkills.MOD_ID, "textures/gui/cooldown_overlay.png");

    public static void setSprite(Identifier sprite) {
        ICON_TEXTURE = sprite;
    }

    public static void setSprite2(Identifier sprite2) {
        ICON_TEXTURE_2 = sprite2;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.player.isSpectator() || client.isPaused() || client.currentScreen != null) {
            return;
        }

        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();
        int guiAnchorX = ((scaledWidth / 2) + 86) + SimplySkills.generalConfig.signatureHudX;
        int guiAnchorY = (scaledHeight - 29) + SimplySkills.generalConfig.signatureHudY;

        // Render the first ability
        renderAbility(context, client, guiAnchorX, guiAnchorY, SimplySkillsClient.abilityCooldown, SimplySkillsClient.lastUseTime, ICON_TEXTURE, SimplySkillsClient.bindingAbility1);
        // Render the second ability, positioned 22 pixels to the right of the first
        renderAbility(context, client, guiAnchorX + 22, guiAnchorY, SimplySkillsClient.abilityCooldown2, SimplySkillsClient.lastUseTime2, ICON_TEXTURE_2, SimplySkillsClient.bindingAbility2);
    }

    private void renderAbility(DrawContext context, MinecraftClient client, int guiAnchorX, int guiAnchorY, int cooldown, long lastUseTime, Identifier iconTexture, KeyBinding keybind) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastUse = currentTime - lastUseTime;
        long remainingCooldownMillis = Math.max(0, (lastUseTime + cooldown) - currentTime);
        int remainingCooldownSecs = (int) (remainingCooldownMillis / 1000);
        Text remainingCooldownText = Text.of(String.valueOf(remainingCooldownSecs));

        // Set blend & shader to prevent cooldown render alpha issues
        RenderSystem._setShaderTexture(0,0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Calculate the height of the cooldown overlay based on the remaining cooldown time
        int cooldownOverlayHeight = (int) (16 * (remainingCooldownMillis / (float) cooldown));

        Text keybindText = keybind.getBoundKeyLocalizedText();

        if (client.player != null && !iconTexture.toString().contains("cooldown_overlay")) {
            // Draw the frame behind the ability icon
            context.drawTexture(FRAME_TEXTURE, guiAnchorX+5, guiAnchorY+6, 58, 22, 24, 24, 256, 256);
            // Draw the ability icon
            context.drawTexture(iconTexture, guiAnchorX + 10, guiAnchorY + 10, 0, 0, 16, 16, 16, 16);
            // Draw the cooldown overlay if there is a remaining cooldown
            if (remainingCooldownMillis > 0) {
                int overlayY = guiAnchorY + 10 + (16 - cooldownOverlayHeight);
                context.drawTexture(COOLDOWN_OVERLAY, guiAnchorX + 10, overlayY, 0, 16 - cooldownOverlayHeight, 16, cooldownOverlayHeight, 16, 16);
                context.drawCenteredTextWithShadow(client.textRenderer, remainingCooldownText, guiAnchorX + 18, guiAnchorY + 14, 16777215);
            }

            // Draw the keybind text
            context.drawCenteredTextWithShadow(client.textRenderer, keybindText, guiAnchorX + 18, guiAnchorY + 0, 16777215);
        }
    }
}