package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "crusader")
public class CrusaderConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableCrusaderSpecialisation = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveCrusaderRetributionChance = 15;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int passiveCrusaderExhaustiveRecoveryChance = 15;
    public int passiveCrusaderExhaustiveRecoveryExhaustionStacks = 10;
    public int passiveCrusaderAegisFrequency = 25;
    public int passiveCrusaderAegisStacksRemoved = 35;

    public int signatureCrusaderHeavensmithsCallRange = 20;
    public int signatureCrusaderHeavensmithsCallDADuration = 400;
    public int signatureCrusaderHeavensmithsCallTauntMarkDuration = 350;
    public int signatureCrusaderHeavensmithsCallDAFrequency = 5;
    public int signatureCrusaderHeavensmithsCallDAChance = 10;
    public int signatureCrusaderHeavensmithsCallDARadius = 5;
    public int signatureCrusaderHeavensmithsCallDAExhaustStacks = 5;
    public int signatureCrusaderHeavensmithsCallDAMightDuration = 115;
    public int signatureCrusaderHeavensmithsCallDAMightStacksMax = 3;
    public int signatureCrusaderHeavensmithsCallCooldown = 55;
    public int signatureCrusaderSacredOnslaughtDashDuration = 40;
    public int signatureCrusaderSacredOnslaughtDPDuration = 200;
    public int signatureCrusaderSacredOnslaughtVelocity = 1;
    public int signatureCrusaderSacredOnslaughtRadius = 3;
    public double signatureCrusaderSacredOnslaughtDMGMultiplier = 0.60;
    public int signatureCrusaderSacredOnslaughtStunDuration = 50;
    public int signatureCrusaderSacredOnslaughtCooldown = 15;

    public int signatureCrusaderConsecrationDuration = 250;
    public int signatureCrusaderConsecrationExtendDuration = 250;
    public int signatureCrusaderConsecrationRadius = 3;
    public double signatureCrusaderConsecrationDMGMultiplier = 1.9;
    @ConfigEntry.BoundedDiscrete(min = 16, max = 100)
    public int signatureCrusaderConsecrationHitFrequency = 18;
    public int signatureCrusaderConsecrationMightStacks = 1;
    public int signatureCrusaderConsecrationMightStacksMax = 5;
    public int signatureCrusaderConsecrationSpellforgedStacks = 1;
    public int signatureCrusaderConsecrationSpellforgedStacksMax = 3;
    public int signatureCrusaderConsecrationTauntDuration = 200;
    public int signatureCrusaderConsecrationCooldown = 30;





}
