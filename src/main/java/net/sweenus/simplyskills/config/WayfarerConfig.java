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
    public int passiveWayfarerReflexiveEvasionDuration = 100;
    public int passiveWayfarerReflexiveChance = 75;
    public int passiveWayfarerGuardingBarrierFrequency = 800;
    public int passiveWayfarerGuardingBarrierDuration = 3400;
    public int passiveWayfarerGuardingBarrierStacks = 1;
    public int passiveWayfarerGuardingBarrierMaxStacks = 3;

}
