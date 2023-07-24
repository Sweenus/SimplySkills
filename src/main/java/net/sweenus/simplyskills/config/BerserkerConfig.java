package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "berserker")
public class BerserkerConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableBerserkerSpecialisation = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int passiveBerserkerSwordMasteryFrequency = 20;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int passiveBerserkerSwordMasteryBaseSpeedAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveBerserkerSwordMasterySpeedAmplifierPerTier = 1;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int passiveBerserkerAxeMasteryFrequency = 20;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int passiveBerserkerAxeMasteryBaseStrengthAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveBerserkerAxeMasteryStrengthAmplifierPerTier = 1;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int passiveBerserkerIgnorePainFrequency = 20;
    public double passiveBerserkerIgnorePainHealthThreshold = 0.4;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int passiveBerserkerIgnorePainBaseResistanceAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveBerserkerIgnorePainResistanceAmplifierPerTier = 1;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int passiveBerserkerRecklessnessFrequency = 20;
    public double passiveBerserkerRecklessnessHealthThreshold = 0.7;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int passiveBerserkerRecklessnessWeaknessAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int passiveBerserkerChallengeFrequency = 20;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveBerserkerChallengeRadius = 2;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveBerserkerChallengeMaxAmplifier = 5;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerRampageDuration = 300;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerRampageSubEffectDuration = 200;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int signatureBerserkerRampageSubEffectMaxAmplifier = 3;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerBullrushDuration = 20;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public int signatureBerserkerBullrushVelocity = 2;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int signatureBerserkerBullrushRadius = 3;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public double signatureBerserkerBullrushDamageModifier = 1.8;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerBullrushHitFrequency = 5;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerBullrushImmobilizeDuration = 80;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerBloodthirstyDuration = 400;
    public float signatureBerserkerBloodthirstyHealPercent = 0.25f;

    public float signatureBerserkerBerserkingSacrificeAmount = 0.30f;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int signatureBerserkerBerserkingSecondsPerSacrifice = 1;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerBerserkingSubEffectDuration = 200;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int signatureBerserkerBerserkingSubEffectMaxAmplifier = 3;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerLeapSlamDuration = 62;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    public int signatureBerserkerLeapSlamRadius = 3;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public double signatureBerserkerLeapSlamVelocity = 1.5;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public double signatureBerserkerLeapSlamHeight = 0.9;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public double signatureBerserkerLeapSlamDescentVelocity = 1.0;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public double signatureBerserkerLeapSlamDamageModifier = 2.8;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int signatureBerserkerLeapSlamImmobilizeDuration = 80;




}
