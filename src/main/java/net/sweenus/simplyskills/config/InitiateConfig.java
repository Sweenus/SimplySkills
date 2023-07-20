package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "initiate")
public class InitiateConfig implements ConfigData {

    public int passiveInitiateFrailArmorThreshold = 10;
    public int passiveInitiateFrailSlownessAmplifier = 0;
    public int passiveInitiateFrailAttackThreshold = 6;
    public int passiveInitiateFrailWeaknessAmplifier = 0;
    public int passiveInitiateFrailMiningFatigueAmplifier = 1;
    public int passiveInitiateNullificationFrequency = 80;
    public int passiveInitiateNullificationRadius = 12;
    public float passiveInitiateSlowFallDistanceToActivate = 3.0F;

}
