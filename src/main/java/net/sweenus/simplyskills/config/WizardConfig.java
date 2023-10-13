package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "wizard")
public class WizardConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableWizardSpecialisation = true;

    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveWizardSpellEchoChance = 15;

    public int signatureWizardMeteoricWrathDuration = 800;
    public int signatureWizardMeteoricWrathStacks = 10;
    public int signatureWizardMeteoricWrathFrequency = 15;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureWizardMeteoricWrathChance = 35;
    public int signatureWizardMeteoricWrathRadius = 12;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureWizardMeteoricWrathRenewalBaseChance = 10;
    public int signatureWizardMeteoricWrathRenewalChanceIncreasePerTier = 15;
    public int signatureWizardMeteorShowerRange = 120;
    public int signatureWizardMeteorShowerCooldown = 40;
    public int signatureWizardIceCometLeapVelocity = 3;
    public double signatureWizardIceCometLeapHeight = 1.3;
    public int signatureWizardIceCometLeapSlowfallDuration = 180;
    public int signatureWizardIceCometVolleyDuration = 400;
    public int signatureWizardIceCometVolleyStacks = 5;
    public int signatureWizardIceCometVolleyFrequency = 20;
    public int signatureWizardIceCometVolleyRange = 120;
    public int signatureWizardIceCometRange = 120;
    public int signatureWizardIceCometCooldown = 30;
    public int signatureWizardStaticDischargeBaseLeaps = 12;
    public int signatureWizardStaticDischargeLeapsPerTier = 20;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureWizardStaticDischargeBaseSpeedChance = 5;
    public int signatureWizardStaticDischargeSpeedChancePerTier = 5;
    public int signatureWizardStaticDischargeSpeedDuration = 400;
    public int signatureWizardStaticDischargeSpeedStacks = 1;
    public int signatureWizardStaticDischargeSpeedMaxAmplifier = 3;
    public int signatureWizardStaticChargeDuration = 1600;
    public int signatureWizardStaticChargeLeapFrequency = 5;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureWizardStaticChargeLeapChance = 30;
    public int signatureWizardStaticChargeWeaknessDuration = 80;
    public int signatureWizardStaticChargeWeaknessAmplifier = 0;
    public int signatureWizardStaticDischargeRange = 120;
    public int signatureWizardStaticDischargeCooldown = 35;
    @ConfigEntry.Gui.Tooltip
    public int signatureWizardLightningOrbBuffRadius = 15;
    @ConfigEntry.Gui.Tooltip
    public int signatureWizardLightningOrbBuffFrequency = 40;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureWizardLightningOrbBuffChance = 35;
    public int signatureWizardArcaneBoltRange = 120;
    public int signatureWizardLesserArcaneBoltRadius = 12;
    public int signatureWizardArcaneBoltVolleyDuration = 400;
    public int signatureWizardArcaneBoltVolleyStacks = 10;
    public int signatureWizardArcaneBoltVolleyFrequency = 10;
    public int signatureWizardArcaneBoltVolleyRange = 120;
    public int signatureWizardArcaneBoltCooldown = 35;

}
