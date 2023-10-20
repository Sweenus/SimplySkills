package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "cleric")
public class ClericConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.PrefixText
    public boolean enableClericSpecialisation = true;

    public int signatureClericDivineInterventionCooldown = 45;
    public int signatureClericSacredOrbCooldown = 30;
    public int signatureClericAnointWeaponCooldown = 35;

}
