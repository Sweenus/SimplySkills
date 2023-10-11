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
                new Identifier(MOD_ID, "projectile/swordfall")
        ));
    }

}
