package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "general")
public class GeneralConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enablePlayerExCompatibility = false;
    @ConfigEntry.Gui.Tooltip
    public boolean treeResetOnDeath = false;
    @ConfigEntry.Gui.Tooltip
    public boolean disableDefaultPuffishTrees = true;

    @ConfigEntry.Gui.Tooltip
    public boolean removeUnlockRestrictions = false;

    @ConfigEntry.Gui.Tooltip
    public float spellHasteCooldownReductionModifier = 0.4f;

    @ConfigEntry.Gui.Tooltip
    public int minimumAchievableCooldown = 5;

    @ConfigEntry.Gui.Tooltip
    public float minimumTimeBetweenAbilityUse = 0.5f;

}
