package net.sweenus.simplyskills.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.entities.DreadglareEntity;
import net.sweenus.simplyskills.entities.SpellTargetEntity;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.entity.SimplySwordsBeeEntity;

public class EntityRegistry {

    public static final EntityType<SpellTargetEntity> SPELL_TARGET_ENTITY;
    public static final EntityType<DreadglareEntity> DREADGLARE;

    static {
        SPELL_TARGET_ENTITY = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(SimplySkills.MOD_ID, "custom_entity_1"),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SpellTargetEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
        );

        DREADGLARE = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(SimplySkills.MOD_ID, "dreadglare"),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DreadglareEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
        );

    }


    public static void registerEntities() {
        SimplySkills.LOGGER.info("Registering Entities for " + SimplySkills.MOD_ID);
    }


}
