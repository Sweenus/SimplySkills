package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "wayfarer")
public class WayfarerConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    public int passiveWayfarerSlenderArmorThreshold = 15;
    public int passiveWayfarerSlenderSlownessAmplifier = 0;
    public int passiveWayfarerSneakSpeedAmplifier = 2;

}
