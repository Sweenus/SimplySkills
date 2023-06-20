package net.sweenus.simplyskills.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effect.BerserkingEffect;
import net.sweenus.simplyskills.effect.BloodthirstyEffect;

public class EffectRegistry {

    public static StatusEffect BERSERKING = new BerserkingEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect BLOODTHIRSTY= new BloodthirstyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect RAMPAGE= new BloodthirstyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect EVASION= new BloodthirstyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect SIPHONINGSTRIKES= new BloodthirstyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ELEMENTALARROWS= new BloodthirstyEffect(StatusEffectCategory.BENEFICIAL, 3124687);

    public static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(SimplySkills.MOD_ID, name), statusEffect);
    }
    
    public static void registerEffects() {
        BERSERKING = registerStatusEffect("berserking", BERSERKING);
        BLOODTHIRSTY = registerStatusEffect("bloodthirsty", BLOODTHIRSTY);
        RAMPAGE = registerStatusEffect("rampage", RAMPAGE);
        EVASION = registerStatusEffect("evasion", EVASION);
        SIPHONINGSTRIKES = registerStatusEffect("siphoning_strikes", SIPHONINGSTRIKES);
        ELEMENTALARROWS = registerStatusEffect("elemental_arrows", ELEMENTALARROWS);
    }


}
