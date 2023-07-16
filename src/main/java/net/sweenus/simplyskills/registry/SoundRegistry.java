package net.sweenus.simplyskills.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sweenus.simplyskills.SimplySkills;

public class SoundRegistry {

    public static void init() {
        SoundEvent soundEvent = SOUNDEFFECT6;
        soundEvent = SOUNDEFFECT7;
        soundEvent = SOUNDEFFECT8;
        soundEvent = SOUNDEFFECT9;
        soundEvent = SOUNDEFFECT10;
        soundEvent = SOUNDEFFECT11;
        soundEvent = SOUNDEFFECT12;
        soundEvent = SOUNDEFFECT13;
        soundEvent = SOUNDEFFECT14;
        soundEvent = SOUNDEFFECT15;
        soundEvent = SOUNDEFFECT16;
        soundEvent = SOUNDEFFECT17;
        soundEvent = SOUNDEFFECT18;
        soundEvent = SOUNDEFFECT19;
        soundEvent = SOUNDEFFECT20;
        soundEvent = SOUNDEFFECT21;
        soundEvent = SOUNDEFFECT22;
        soundEvent = SOUNDEFFECT23;
        soundEvent = SOUNDEFFECT24;
        soundEvent = SOUNDEFFECT25;
        soundEvent = SOUNDEFFECT26;
        soundEvent = SOUNDEFFECT27;
        soundEvent = SOUNDEFFECT28;
        soundEvent = SOUNDEFFECT29;
        soundEvent = SOUNDEFFECT30;
        soundEvent = SOUNDEFFECT31;
        soundEvent = SOUNDEFFECT32;
        soundEvent = SOUNDEFFECT33;
        soundEvent = SOUNDEFFECT34;
        soundEvent = SOUNDEFFECT35;
        soundEvent = SOUNDEFFECT36;
        soundEvent = SOUNDEFFECT37;
        soundEvent = SOUNDEFFECT38;
        soundEvent = SOUNDEFFECT39;
        soundEvent = SOUNDEFFECT40;
    }

    public static SoundEvent FX_SKILL_BACKSTAB = register("fx_skill_backstab");
    public static SoundEvent FX_UI_UNLOCK = register("fx_ui_unlock");
    public static SoundEvent FX_UI_UNLOCK2 = register("fx_ui_unlock2");
    public static SoundEvent FX_UI_UNLOCK3 = register("fx_ui_unlock3");
    public static SoundEvent SOUNDEFFECT6 = register("soundeffect_6");
    public static SoundEvent SOUNDEFFECT7 = register("soundeffect_7");
    public static SoundEvent SOUNDEFFECT8 = register("soundeffect_8");
    public static SoundEvent SOUNDEFFECT9 = register("soundeffect_9");
    public static SoundEvent SOUNDEFFECT10 = register("soundeffect_10");
    public static SoundEvent SOUNDEFFECT11 = register("soundeffect_11");
    public static SoundEvent SOUNDEFFECT12 = register("soundeffect_12");
    public static SoundEvent SOUNDEFFECT13 = register("soundeffect_13");
    public static SoundEvent SOUNDEFFECT14 = register("soundeffect_14");
    public static SoundEvent SOUNDEFFECT15 = register("soundeffect_15");
    public static SoundEvent SOUNDEFFECT16 = register("soundeffect_16");
    public static SoundEvent SOUNDEFFECT17 = register("soundeffect_17");
    public static SoundEvent SOUNDEFFECT18 = register("soundeffect_18");
    public static SoundEvent SOUNDEFFECT19 = register("soundeffect_19");
    public static SoundEvent SOUNDEFFECT20 = register("soundeffect_20");
    public static SoundEvent SOUNDEFFECT21 = register("soundeffect_21");
    public static SoundEvent SOUNDEFFECT22 = register("soundeffect_22");
    public static SoundEvent SOUNDEFFECT23 = register("soundeffect_23");
    public static SoundEvent SOUNDEFFECT24 = register("soundeffect_24");
    public static SoundEvent SOUNDEFFECT25 = register("soundeffect_25");
    public static SoundEvent SOUNDEFFECT26 = register("soundeffect_26");
    public static SoundEvent SOUNDEFFECT27 = register("soundeffect_27");
    public static SoundEvent SOUNDEFFECT28 = register("soundeffect_28");
    public static SoundEvent SOUNDEFFECT29 = register("soundeffect_29");
    public static SoundEvent SOUNDEFFECT30 = register("soundeffect_30");
    public static SoundEvent SOUNDEFFECT31 = register("soundeffect_31");
    public static SoundEvent SOUNDEFFECT32 = register("soundeffect_32");
    public static SoundEvent SOUNDEFFECT33 = register("soundeffect_33");
    public static SoundEvent SOUNDEFFECT34 = register("soundeffect_34");
    public static SoundEvent SOUNDEFFECT35 = register("soundeffect_35");
    public static SoundEvent SOUNDEFFECT36 = register("soundeffect_36");
    public static SoundEvent SOUNDEFFECT37 = register("soundeffect_37");
    public static SoundEvent SOUNDEFFECT38 = register("soundeffect_38");
    public static SoundEvent SOUNDEFFECT39 = register("soundeffect_39");
    public static SoundEvent SOUNDEFFECT40 = register("soundeffect_40");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(SimplySkills.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }


}
