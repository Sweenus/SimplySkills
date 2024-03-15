package net.sweenus.simplyskills.mixins.client;

import net.puffish.skillsmod.client.data.ClientSkillCategoryData;
import net.puffish.skillsmod.client.gui.SkillsScreen;
import net.puffish.skillsmod.utils.Bounds2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(SkillsScreen.class)
public interface SkillsScreenAccessor {
    @Accessor("contentPaddingTop")
    int getContentPaddingTop();
    @Accessor("contentPaddingTop")
    void setContentPaddingTop(int contentPaddingTop);

    @Accessor("contentPaddingBottom")
    int getContentPaddingBottom();
    @Accessor("contentPaddingBottom")
    void setContentPaddingBottom(int contentPaddingBottom);

    @Accessor("contentPaddingRight")
    int getContentPaddingRight();
    @Accessor("contentPaddingRight")
    void setContentPaddingRight(int contentPaddingRight);

    @Accessor("contentPaddingLeft")
    int getContentPaddingLeft();
    @Accessor("contentPaddingLeft")
    void setContentPaddingLeft(int contentPaddingLeft);

    @Accessor("small")
    boolean isSmall();
    @Accessor("small")
    void setSmall(boolean small);

    @Accessor("y")
    int getY();
    @Accessor("y")
    void setY(int y);

    @Accessor("x")
    int getX();
    @Accessor("x")
    void setX(int x);

    @Accessor("bounds")
    Bounds2i getBounds();
    @Accessor("bounds")
    void setBounds(Bounds2i bounds);

    @Accessor("minScale")
    float getMinScale();
    @Accessor("minScale")
    void setMinScale(float minScale);

    @Accessor("maxScale")
    float getMaxScale();
    @Accessor("maxScale")
    void setMaxScale(float maxScale);

    @Accessor("scale")
    float getScale();
    @Accessor("scale")
    void setScale(float scale);

    @Accessor("optActiveCategory")
    Optional<ClientSkillCategoryData> getOptActiveCategory();
    @Accessor("optActiveCategory")
    void setOptActiveCategory(Optional<ClientSkillCategoryData> optActiveCategory);

    // Add other accessors as needed
}