package net.sweenus.simplyskills.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.items.MalevolentManuscript;

public class ItemRegistry {

    public static final Item MALEVOLENTMANUSCRIPT = registerItem( "malevolent_manuscript",
            new MalevolentManuscript( new FabricItemSettings()
                    .group(ItemGroup.MISC)
                    .rarity(Rarity.EPIC)
                    .maxCount(1)
                    .fireproof()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SimplySkills.MOD_ID, name), item);
    }


    public static void registerItems() {
        SimplySkills.LOGGER.info("Registering Items for " + SimplySkills.MOD_ID);
    }


}
