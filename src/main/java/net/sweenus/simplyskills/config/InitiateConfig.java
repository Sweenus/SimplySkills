package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "initiate")
public class InitiateConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    public int passiveInitiateFrailArmorThreshold = 10;
    public int passiveInitiateFrailSlownessAmplifier = 0;
    public int passiveInitiateFrailAttackThreshold = 6;
    public int passiveInitiateFrailWeaknessAmplifier = 0;
    public int passiveInitiateFrailMiningFatigueAmplifier = 3;
    public int passiveInitiateNullificationFrequency = 80;
    public int passiveInitiateNullificationRadius = 12;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public float passiveInitiateSlowFallDistanceToActivate = 3.0F;
    public int passiveInitiateEmpowerChance = 15;
    public int passiveInitiateEmpowerDuration = 300;
    public int passiveInitiateEmpowerStacks = 1;
    public int passiveInitiateEmpowerMaxStacks = 15;
    public int passiveInitiateAttunedDuration = 150;
    public int passiveInitiateAttunedStackThreshold = 5;
    public int passiveInitiateAttunedStacks = 1;
    public int passiveInitiateAttunedMaxStacks = 15;
    public int passiveInitiateAttunedFrequency = 20;
    public int passiveInitiateLightningRodDuration = 600;
    public int passiveInitiateLightningRodStacks = 1;
    public int passiveInitiateLightningRodMaxStacks = 5;
    public int passiveInitiateLightningRodFrequency = 500;
    public int passiveInitiateHastyStacks = 1;
    public int passiveInitiateHastyDuration = 20;

}
