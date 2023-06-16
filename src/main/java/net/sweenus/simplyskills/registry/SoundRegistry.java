package net.sweenus.simplyskills.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sweenus.simplyskills.SimplySkills;

public class SoundRegistry {

    public static void init() {

    }

    public static SoundEvent FX_SKILL_BACKSTAB = register("fx_skill_backstab");
    public static SoundEvent FX_UI_UNLOCK = register("fx_ui_unlock");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(SimplySkills.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }


}
