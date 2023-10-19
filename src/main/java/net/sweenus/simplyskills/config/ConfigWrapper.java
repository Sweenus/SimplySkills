package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.sweenus.simplyskills.SimplySkills;

@Config(name = SimplySkills.MOD_ID)
@Config.Gui.Background("cloth-config2:transparent")
public class ConfigWrapper extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralConfig client = new GeneralConfig();
    @ConfigEntry.Category("wayfarer")
    @ConfigEntry.Gui.TransitiveObject
    public WayfarerConfig wayfarer = new WayfarerConfig();
    @ConfigEntry.Category("warrior")
    @ConfigEntry.Gui.TransitiveObject
    public WarriorConfig warrior = new WarriorConfig();
    @ConfigEntry.Category("initiate")
    @ConfigEntry.Gui.TransitiveObject
    public InitiateConfig initiate = new InitiateConfig();
    @ConfigEntry.Category("berserker")
    @ConfigEntry.Gui.TransitiveObject
    public BerserkerConfig berserker = new BerserkerConfig();
    @ConfigEntry.Category("wizard")
    @ConfigEntry.Gui.TransitiveObject
    public WizardConfig wizard = new WizardConfig();
    @ConfigEntry.Category("spellblade")
    @ConfigEntry.Gui.TransitiveObject
    public SpellbladeConfig spellblade = new SpellbladeConfig();
    @ConfigEntry.Category("rogue")
    @ConfigEntry.Gui.TransitiveObject
    public RogueConfig rogue = new RogueConfig();
    @ConfigEntry.Category("ranger")
    @ConfigEntry.Gui.TransitiveObject
    public RangerConfig ranger = new RangerConfig();
    @ConfigEntry.Category("crusader")
    @ConfigEntry.Gui.TransitiveObject
    public CrusaderConfig crusader = new CrusaderConfig();
    @ConfigEntry.Category("cleric")
    @ConfigEntry.Gui.TransitiveObject
    public ClericConfig cleric = new ClericConfig();
}
