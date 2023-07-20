package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rogue")
public class RogueConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enableRogueSpecialisation = true;
    public int passiveRogueBackstabWeaknessDuration = 60;
    public int passiveRogueBackstabWeaknessAmplifier = 0;
    public int passiveRogueSmokeBombRadius = 6;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueSmokeBombChance = 25;
    public int passiveRogueSmokeBombInvisibilityDuration = 100;
    public int passiveRogueSmokeBombBlindnessDuration = 60;
    public int passiveRogueSmokeBombBlindnessAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueEvasionMasteryChance = 15;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueEvasionMasteryChanceIncreasePerTier = 10;
    public int passiveRogueEvasionMasterySignatureMultiplier = 2;
    public int passiveRogueEvasionMasteryIframeDuration = 15;
    public int passiveRogueOpportunisticMasteryPoisonDuration = 40;
    public int passiveRogueOpportunisticMasteryPoisonAmplifier = 0;
    public int passiveRogueOpportunisticMasteryPoisonDurationIncreasePerTier = 40;

    public int signatureRoguePreparationShadowstrikeRange = 12;
    public int signatureRoguePreparationShadowstrikeRadius = 3;
    public int signatureRoguePreparationShadowstrikeDamageModifier = 3;
    public double signatureRogueSiphoningStrikesLeechMultiplier = 0.15;
    public int signatureRogueFanOfBladesBaseFrequency = 20;
    public int signatureRogueFanOfBladesEnhancedFrequency = 5;
    public int signatureRogueFanOfBladesRange = 8;
    public int signatureRogueFanOfBladesRadius = 6;
    public int signatureRogueFanOfBladesDisenchantDuration = 160;

}
