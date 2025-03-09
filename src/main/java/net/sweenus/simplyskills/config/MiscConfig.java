package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "misc")
public class MiscConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip
    public double promWarriorsDevotionAttackMulti = 1;
    @ConfigEntry.Gui.Tooltip
    public double promWarriorsDevotionAttackSpeedMulti = 0.9;
    @ConfigEntry.Gui.Tooltip
    public double promSkellaksCallPhysDmgMulti = 0.35;
    @ConfigEntry.Gui.Tooltip
    public int promCorruptionMax = 100;
    @ConfigEntry.Gui.Tooltip
    public double promCorruptionMulti = 1.0;

}
