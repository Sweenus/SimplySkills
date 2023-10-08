package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ranger")
public class RangerConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableRangerSpecialisation = true;
    public int passiveRangerRevealRadius = 12;
    public int passiveRangerRevealFrequency = 80;
    public int passiveRangerTamerRadius = 12;
    public int passiveRangerTamerFrequency = 80;
    public int passiveRangerTamerResistanceAmplifier = 2;
    public int passiveRangerTamerRegenerationAmplifier = 1;
    public int passiveRangerBondedRadius = 12;
    public int passiveRangerBondedFrequency = 10;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRangerBondedPetMinimumHealthPercent = 30;
    public int passiveRangerBondedHealthTransferAmount = 1;
    public int passiveRangerTrainedRadius = 12;
    public int passiveRangerTrainedFrequency = 80;
    public int passiveRangerTrainedStrengthAmplifier = 1;
    public int passiveRangerTrainedSpeedAmplifier = 1;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRangerTrainedMinimumHealthPercent = 70;
    public int passiveRangerIncognitoRadius = 12;
    public int passiveRangerIncognitoFrequency = 20;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRangerElementalArrowsRenewalChance = 35;
    public int passiveRangerElementalArrowsRenewalDuration = 600;
    public int passiveRangerElementalArrowsRenewalStacks = 1;
    public int passiveRangerElementalArrowsRenewalMaximumStacks = 20;
    public int signatureRangerDisengageRadius = 6;
    public int signatureRangerDisengageVelocity = 3;
    public int signatureRangerDisengageHeight = 1;
    public int signatureRangerDisengageSlownessDuration = 250;
    public int signatureRangerDisengageSlownessAmplifier = 3;
    public int signatureRangerDisengageSlowFallDuration = 80;
    public int signatureRangerDisengageSlowFallAmplifier = 0;
    public int signatureRangerDisengageRecuperateRadius = 18;
    public int signatureRangerDisengageExploitationRadius = 18;
    public int signatureRangerDisengageExploitationDuration = 120;
    public int signatureRangerDisengageMarksmanDuration = 200;
    public int signatureRangerDisengageMarksmanStacks = 1;
    public int signatureRangerDisengageCooldown = 15;
    public int effectRangerElementalArrowsDuration = 600;
    public int effectRangerElementalArrowsStacks = 4;
    public int effectRangerElementalArrowsStacksIncreasePerTier = 1;
    public int effectRangerElementalArrowsRadius = 4;
    public int effectRangerElementalArrowsRadiusIncreasePerTier = 2;
    public int effectRangerElementalArrowsTargetingRange = 120;
    public int effectRangerElementalArrowsCooldown = 40;
    public int effectRangerArrowRainDuration = 600;
    public int effectRangerArrowRainRadius = 3;
    public int effectRangerArrowRainRadiusIncreasePerTier = 1;
    public int effectRangerArrowRainArrowDensity = 25;
    public int effectRangerArrowRainVolleys = 2;
    public int effectRangerArrowRainVolleyIncreasePerTier = 1;
    public int effectRangerArrowRainRange = 64;
    public int effectRangerArrowRainCooldown = 14;

}
