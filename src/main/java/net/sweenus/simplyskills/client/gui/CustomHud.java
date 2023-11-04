package net.sweenus.simplyskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;

public class CustomHud {

    public static Identifier ICON_TEXTURE = new Identifier(SimplySkills.MOD_ID, "textures/icons/wizard_signature_ice_comet.png");
    public static Identifier FRAME_TEXTURE = new Identifier("minecraft", "textures/gui/widgets.png");

    public static void setSprite(Identifier sprite) {
        ICON_TEXTURE = sprite;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Ensure the game isn't paused and the player exists
        if (client.player != null && client.currentScreen == null) {
            int scaledWidth = client.getWindow().getScaledWidth();
            int scaledHeight = client.getWindow().getScaledHeight();

            RenderSystem.getShaderTexture(0);

            // Reset the OpenGL state
            RenderSystem._setShaderTexture(0,0);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // Draw the icon at the top left of the screen
            client.getTextureManager().bindTexture(ICON_TEXTURE);
            RenderSystem.setShaderTexture(0, ICON_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            context.drawTexture(FRAME_TEXTURE, 5, 6, 58, 22, 24, 24, 256, 256);
            context.drawTexture(ICON_TEXTURE, 10, 10, 0, 0, 16, 16, 16, 16);
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