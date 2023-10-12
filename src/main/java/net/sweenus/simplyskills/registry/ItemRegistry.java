package net.sweenus.simplyskills.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.items.GraciousManuscript;
import net.sweenus.simplyskills.items.MalevolentManuscript;
import net.sweenus.simplyskills.items.SkillChronicle;

public class ItemRegistry {

    public static final Item MALEVOLENTMANUSCRIPT = registerItem( "malevolent_manuscript",
            new MalevolentManuscript( new FabricItemSettings()
                    .rarity(Rarity.EPIC)
                    .maxCount(1)
                    .fireproof()));
    public static final Item GRACIOUSMANUSCRIPT = registerItem( "gracious_manuscript",
            new GraciousManuscript( new FabricItemSettings()
                    .rarity(Rarity.EPIC)
                    .maxCount(1)
                    .fireproof()));
    public static final Item SKILLCHRONICLE = registerItem( "skill_chronicle",
            new SkillChronicle( new FabricItemSettings()
                    .rarity(Rarity.RARE)
                    .maxCount(1)
                    .fireproof()));

    private static Item registerItem(String name, Item item) {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(item));
        return Registry.register(Registries.ITEM, new Identifier(SimplySkills.MOD_ID, name), item);
    }


    public static void registerItems() {
        SimplySkills.LOGGER.info("Registering Items for " + SimplySkills.MOD_ID);
    }


}
