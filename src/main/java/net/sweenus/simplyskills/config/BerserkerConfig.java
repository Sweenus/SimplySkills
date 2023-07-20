package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "berserker")
public class BerserkerConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enablePlayerExCompatibility = false;
    @ConfigEntry.Gui.Tooltip
    public boolean enableSomething = false;

}
