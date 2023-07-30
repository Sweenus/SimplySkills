package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "spellblade")
public class SpellbladeConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableSpellbladeSpecialisation = true;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int passiveSpellbladeSpellweavingChance = 15;

    public int signatureSpellbladeElementalSurgeDuration = 300;
    public int signatureSpellbladeElementalSurgeFrequency = 20;
    public int signatureSpellbladeElementalSurgeRadius = 3;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureSpellbladeElementalSurgeChance = 100;
    public int signatureSpellbladeElementalSurgeCooldown = 30;
    public int signatureSpellbladeElementalImpactDuration = 20;
    public int signatureSpellbladeElementalImpactResistanceAmplifier = 2;
    public int signatureSpellbladeElementalImpactRadius = 3;
    public int signatureSpellbladeElementalImpactVelocity = 2;
    public int signatureSpellbladeElementalImpactSlownessDuration = 60;
    public int signatureSpellbladeElementalImpactSlownessAmplifier = 2;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureSpellbladeElementalImpactChance = 100;
    public int signatureSpellbladeElementalImpactCooldown = 25;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureSpellbladeSpellweaverChance = 30;
    public int signatureSpellbladeSpellweaverDuration = 600;
    public int signatureSpellbladeSpellweaverStacks = 20;
    public int signatureSpellbladeSpellweaverHasteDuration = 100;
    public int signatureSpellbladeSpellweaverHasteStacks = 1;
    public int signatureSpellbladeSpellweaverHasteMaxStacks = 5;
    public int signatureSpellbladeSpellweaverRegenerationDuration = 140;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int signatureSpellbladeSpellweaverRegenerationChance = 50;
    public int signatureSpellbladeSpellweaverRegenerationStacks = 1;
    public int signatureSpellbladeSpellweaverRegenerationMaxStacks = 2;
    public int signatureSpellbladeSpellweaverCooldown = 40;

}
