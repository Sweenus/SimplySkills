package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "warrior")
public class WarriorConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    public int passiveWarriorArmorMasteryArmorThreshold = 15;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveWarriorArmorMasteryChance = 10;
    public int passiveWarriorHeavyArmorMasteryDuration = 100;
    public int passiveWarriorHeavyArmorMasteryAmplifier = 0;
    public int passiveWarriorMediumArmorMasteryDuration = 100;
    public int passiveWarriorMediumArmorMasteryAmplifier = 0;
    public int passiveWarriorShieldMasteryFrequency = 10;
    public int passiveWarriorShieldMasteryWeaknessAmplifier = 0;
    public int passiveWarriorShieldMasteryResistanceAmplifier = 0;
    public int passiveWarriorShieldMasteryResistanceAmplifierPerTier = 1;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveWarriorRebukeChance = 25;
    public int passiveWarriorRebukeWeaknessDuration = 80;
    public int passiveWarriorRebukeWeaknessAmplifier = 0;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveWarriorSpellbreakerChance = 25;
    public int passiveWarriorSpellbreakerDuration = 100;
    public int passiveWarriorSpellbreakerRadius = 4;
    public int passiveWarriorSpellbreakerFrequency = 5;

    public float passiveWarriorGoliathFallDistance = 3.0f;
    public float passiveWarriorHeavyWeightDamageIncreasePerTick = 0.3f;

    public int passiveWarriorDeathDefyFrequency = 20;
    public int passiveWarriorDeathDefyHealthThreshold = 30;
    public int passiveWarriorDeathDefyAmplifierPerTenPercentHealth = 1;
    public int passiveWarriorFrenzyExhaustionDuration = 400;
    public int passiveWarriorFrenzyExhaustionStacks = 1;
    @ConfigEntry.Gui.Tooltip
    public int passiveWarriorTwinstrikeChance = 15;
    @ConfigEntry.Gui.Tooltip
    public int passiveWarriorSwordfallChance = 8;

}
