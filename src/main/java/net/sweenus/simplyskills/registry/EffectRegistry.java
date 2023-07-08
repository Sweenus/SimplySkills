package net.sweenus.simplyskills.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.*;

public class EffectRegistry {

    public static StatusEffect BERSERKING = new BerserkingEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect BLOODTHIRSTY= new BloodthirstyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect RAMPAGE= new RampageEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect EVASION= new EvasionEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect SIPHONINGSTRIKES= new SiphoningStrikesEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ELEMENTALARROWS= new ElementalArrowsEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ARROWRAIN= new ArrowRainEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect FROSTVOLLEY= new FrostVolleyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ARCANEVOLLEY= new ArcaneVolleyEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect METEORICWRATH= new MeteoricWrathEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect STATICCHARGE= new StaticChargeEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ELEMENTALIMPACT= new ElementalImpactEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ELEMENTALSURGE= new ElementalSurgeEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect SPELLWEAVER= new SpellweaverEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect FANOFBLADES= new FanOfBladesEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect DISENCHANTMENT= new DisenchantmentEffect(StatusEffectCategory.HARMFUL, 3124687);
    public static StatusEffect BULLRUSH= new BullrushEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect IMMOBILIZE= new ImmobilizeEffect(StatusEffectCategory.HARMFUL, 3124687);


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
        ARROWRAIN = registerStatusEffect("arrow_rain", ARROWRAIN);
        FROSTVOLLEY = registerStatusEffect("frost_volley", FROSTVOLLEY);
        ARCANEVOLLEY = registerStatusEffect("arcane_volley", ARCANEVOLLEY);
        METEORICWRATH = registerStatusEffect("meteoric_wrath", METEORICWRATH);
        STATICCHARGE = registerStatusEffect("static_charge", STATICCHARGE);
        ELEMENTALSURGE = registerStatusEffect("elemental_surge", ELEMENTALSURGE);
        ELEMENTALIMPACT = registerStatusEffect("elemental_impact", ELEMENTALIMPACT);
        SPELLWEAVER = registerStatusEffect("spellweaver", SPELLWEAVER);
        FANOFBLADES = registerStatusEffect("fanofblades", FANOFBLADES);
        DISENCHANTMENT = registerStatusEffect("disenchantment", DISENCHANTMENT);
        BULLRUSH = registerStatusEffect("bullrush", BULLRUSH);
        IMMOBILIZE = registerStatusEffect("immobilize", IMMOBILIZE);
    }


}
