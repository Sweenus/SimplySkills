package net.sweenus.simplyskills.mixins.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.client.gui.SkillsScreen;
import net.puffish.skillsmod.utils.Bounds2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(SkillsScreen.class)
public abstract class SkillsScreenMixin {

    /* Scaling Variant - not aligned
    @Redirect(method = "drawContentWithCategory(Lnet/minecraft/client/gui/DrawContext;DDLnet/puffish/skillsmod/client/data/ClientSkillCategoryData;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void redirectDrawTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        int originalTextureWidth = 5120;
        int originalTextureHeight = 2880;

        float aspectRatio = (float) originalTextureWidth / (float) originalTextureHeight;

        int newTextureWidth = bounds.width();
        int newTextureHeight = (int) (newTextureWidth / aspectRatio);

        int centerX = bounds.min().x + bounds.width() / 2;
        int centerY = bounds.min().y + bounds.height() / 2;

        int newX = centerX - newTextureWidth / 2;
        int newY = centerY - newTextureHeight / 2;

        context.drawTexture(texture, newX, newY, u, v, newTextureWidth, newTextureHeight, newTextureWidth, newTextureHeight);
    }
     */
    // Maintains image position in 16:9
    /*
    @Redirect(method = "drawContentWithCategory(Lnet/minecraft/client/gui/DrawContext;DDLnet/puffish/skillsmod/client/data/ClientSkillCategoryData;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void redirectDrawTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        SkillsScreen screen = (SkillsScreen) (Object) this;
        SkillsScreenAccessor accessor = (SkillsScreenAccessor) screen;
        Bounds2i bounds = accessor.getBounds();
        int originalTextureWidth = 1920;
        int originalTextureHeight = 1080;

        float aspectRatio = (float) originalTextureWidth / (float) originalTextureHeight;

        int newTextureWidth = bounds.width(); // new width
        int newTextureHeight = (int) (newTextureWidth / aspectRatio);

        int centerX = bounds.min().x + bounds.width() / 2;
        int centerY = bounds.min().y + bounds.height() / 2;

        int newX = centerX - newTextureWidth / 2;
        int newY = centerY - newTextureHeight / 2;

        context.drawTexture(texture, newX, newY, u, v, newTextureWidth, newTextureHeight, newTextureWidth, newTextureHeight);
    }*/

    @Redirect(method = "drawContentWithCategory(Lnet/minecraft/client/gui/DrawContext;DDLnet/puffish/skillsmod/client/data/ClientSkillCategoryData;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void redirectDrawTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        SkillsScreen screen = (SkillsScreen) (Object) this;
        SkillsScreenAccessor accessor = (SkillsScreenAccessor) screen;
        Bounds2i bounds = accessor.getBounds();
        // Original texture dimensions
        int originalTextureWidth = 5120;
        int originalTextureHeight = 1440;

        // Calculate the aspect ratio of the texture
        float aspectRatio = (float) originalTextureWidth / (float) originalTextureHeight;

        // New width and height based on bounds height while maintaining aspect ratio
        int newTextureHeight = bounds.height(); // new height
        int newTextureWidth = (int) (newTextureHeight * aspectRatio);

        // Calculate the center position of the bounds
        int centerX = bounds.min().x + bounds.width() / 2;
        int centerY = bounds.min().y + bounds.height() / 2;

        // Adjust x and y to make the image's center align with the center of the bounds
        int newX = centerX - newTextureWidth / 2;
        int newY = centerY - newTextureHeight / 2;

        // Draw a black rectangle behind the texture
        int colorBlack = 0xFF000000; // ARGB format for opaque black
        context.fill(x, y, x + bounds.width(), y + bounds.height(), colorBlack);

        // Draw the texture centered at the specified point
        context.drawTexture(texture, newX, newY, u, v, newTextureWidth, newTextureHeight, newTextureWidth, newTextureHeight);
    }
}