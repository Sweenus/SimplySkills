package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

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
    public int passiveInitiateEmpowerChance = 15;
    public int passiveInitiateEmpowerDuration = 300;
    public int passiveInitiateEmpowerStacks = 1;
    public int passiveInitiateEmpowerMaxStacks = 15;
    public int passiveInitiateAttunedDuration = 150;
    public int passiveInitiateAttunedStacks = 1;
    public int passiveInitiateAttunedMaxStacks = 15;
    public int passiveInitiateAttunedFrequency = 5;

}
