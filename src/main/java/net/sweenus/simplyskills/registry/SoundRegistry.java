package net.sweenus.simplyskills.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;

import java.util.Arrays;
import java.util.List;

public class SoundRegistry {

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
    public static SoundEvent SOUNDEFFECT41 = register("soundeffect_41");
    public static SoundEvent SOUNDEFFECT42 = register("soundeffect_42");
    public static SoundEvent SOUNDEFFECT43 = register("soundeffect_43");
    public static SoundEvent SOUNDEFFECT44 = register("soundeffect_44");
    public static SoundEvent SOUNDEFFECT45 = register("soundeffect_45");
    public static SoundEvent SOUNDEFFECT46 = register("soundeffect_46");
    public static SoundEvent SOUNDEFFECT47 = register("soundeffect_47");
    public static SoundEvent SOUNDEFFECT48 = register("soundeffect_48");
    public static SoundEvent SPELL_ENERGY = register("spell_energy");
    public static SoundEvent SPELL_EARTH_PUNCH = register("spell_earth_punch");
    public static SoundEvent SPELL_MISC_01 = register("spell_misc_01");
    public static SoundEvent SPELL_FIREBALL = register("spell_fireball");
    public static SoundEvent SPELL_SLASH = register("spell_slash");
    public static SoundEvent SPELL_SLASH_02 = register("spell_slash_02");
    public static SoundEvent SPELL_ARCANE_HIT = register("spell_arcane_hit");
    public static SoundEvent SPELL_ARCANE_CAST = register("spell_arcane_cast");
    public static SoundEvent SPELL_GAIN_BARRIER = register("spell_gain_barrier");
    public static SoundEvent SPELL_ARCANE_NOVA = register("spell_arcane_nova");
    public static SoundEvent SPELL_RADIANT_CAST = register("spell_radiant_cast");
    public static SoundEvent SPELL_RADIANT_HIT = register("spell_radiant_hit");
    public static SoundEvent SPELL_RADIANT_EXPIRE = register("spell_radiant_expire");
    public static SoundEvent SPELL_CELESTIAL_CAST = register("spell_celestial_cast");
    public static SoundEvent SPELL_CELESTIAL_HIT = register("spell_celestial_hit");
    public static SoundEvent SPELL_THUNDER_CAST = register("spell_thunder_cast");
    public static SoundEvent SPELL_FIRE_CAST = register("spell_fire_cast");
    public static SoundEvent SPELL_LIGHTNING_CAST = register("spell_lightning_cast");

    public static void registerSounds() {
    }

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(SimplySkills.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


}
