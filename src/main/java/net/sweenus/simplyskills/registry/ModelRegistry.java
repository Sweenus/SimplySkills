package net.sweenus.simplyskills.registry;

import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static net.sweenus.simplyskills.SimplySkills.MOD_ID;

public class ModelRegistry {

    public static void registerModels() {
        // For projectiles '(N) North' in blockbench is 'down' in-game with default rotations
        // For projectiles 'Up' in blockbench is 'forward' in-game with default rotations
        CustomModels.registerModelIds(List.of(
                new Identifier(MOD_ID, "projectile/swordfall"),
                new Identifier(MOD_ID, "projectile/sword"),
                new Identifier(MOD_ID, "projectile/ice_projectile"),
                new Identifier(MOD_ID, "projectile/fire_projectile"),
                new Identifier(MOD_ID, "projectile/lightning_projectile"),
                new Identifier(MOD_ID, "projectile/arcane_projectile"),
                new Identifier(MOD_ID, "projectile/meteor_projectile"),
                new Identifier(MOD_ID, "projectile/comet_projectile")
        ));
    }

}
