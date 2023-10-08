package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rogue")
public class RogueConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableRogueSpecialisation = true;
    public int passiveRogueBackstabWeaknessDuration = 60;
    public int passiveRogueBackstabWeaknessAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueBackstabStealthChancePerEnemy = 3;
    public int passiveRogueBackstabStealthDuration = 200;

    public int passiveRogueSmokeBombRadius = 6;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueSmokeBombChance = 10;
    public int passiveRogueSmokeBombAuraDuration = 40;
    public int passiveRogueSmokeBombBlindnessDuration = 40;
    public int passiveRogueSmokeBombBlindnessAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueEvasionMasteryChance = 15;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueEvasionMasteryChanceIncreasePerTier = 5;
    public int passiveRogueEvasionMasterySignatureMultiplier = 2;
    public int passiveRogueOpportunisticMasteryPoisonDuration = 40;
    public int passiveRogueOpportunisticMasteryPoisonAmplifier = 0;
    public int passiveRogueOpportunisticMasteryPoisonDurationIncreasePerTier = 40;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveRogueDeflectionIncreasedChance = 10;
    public int passiveRogueRecoveryRegenerationFrequency = 42;
    public int passiveRogueRecoveryRegenerationAmplifier = 1;
    public int passiveRogueShadowVeilResistanceFrequency = 60;
    public int passiveRogueShadowVeilResistanceStacks = 1;
    public int passiveRogueShadowVeilResistanceMaxStacks = 3;
    public int passiveRogueExploitationDeathMarkDuration = 80;
    public int passiveRogueExploitationDeathMarkStacks = 1;
    public int passiveRogueFleetfootedSpeedDuration = 40;
    public int passiveRogueFleetfootedSpeedStacks = 1;
    public int passiveRogueFleetfootedSpeedMaxStacks = 6;

    public int signatureRoguePreparationDuration = 160;
    public int signatureRoguePreparationSpeedAmplifier = 2;
    public int signatureRoguePreparationShadowstrikeRange = 12;
    public int signatureRoguePreparationShadowstrikeRadius = 3;
    public int signatureRoguePreparationShadowstrikeDamageModifier = 3;
    public int signatureRoguePreparationCooldown = 10;
    public double signatureRogueSiphoningStrikesLeechMultiplier = 0.15;
    public int signatureRogueSiphoningStrikesDuration = 600;
    public int signatureRogueSiphoningStrikesStacks = 10;
    public int signatureRogueSiphoningStrikesMightyStacks = 4;
    public int signatureRogueSiphoningStrikesCooldown = 25;
    public int signatureRogueEvasionDuration = 160;
    public int signatureRogueEvasionCooldown = 25;
    public int signatureRogueFanOfBladesDuration = 500;
    public int signatureRogueFanOfBladesStacks = 10;
    public int signatureRogueFanOfBladesBaseFrequency = 20;
    public int signatureRogueFanOfBladesEnhancedFrequency = 5;
    public int signatureRogueFanOfBladesRange = 8;
    public int signatureRogueFanOfBladesRadius = 6;
    public int signatureRogueFanOfBladesDisenchantDuration = 160;

}
