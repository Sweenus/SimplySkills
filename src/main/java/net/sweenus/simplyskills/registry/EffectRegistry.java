package net.sweenus.simplyskills.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.server.PlayerAttributes;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
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
    public static StatusEffect LEAPSLAM= new LeapSlamEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect IMMOBILIZINGAURA= new ImmobilizingAuraEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect SPELLBREAKING= new SpellbreakingEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect EARTHSHAKER= new EarthshakerEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ARCANEATTUNEMENT= new ArcaneAttunementEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute,
                    "8b724548-dbd9-4dbf-8ad5-9c0b7757dec5",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect FIREATTUNEMENT= new FireAttunementEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute,
                    "5835e9c2-4182-4098-b9ef-23670c46cb4d",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect FROSTATTUNEMENT= new FrostAttunementEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.FROST).attribute,
                    "caa82c97-9874-4f5e-84e4-37380bf756ec",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect LIGHTNINGATTUNEMENT= new LightningAttunementEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.LIGHTNING).attribute,
                    "7edc1ac1-c6c5-4a46-92e1-baf28abea256",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect SOULATTUNEMENT= new SoulAttunementEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute,
                    "45da701e-e40a-4041-bd54-f06e283ad7cb",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect HOLYATTUNEMENT= new HolyAttunementEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute,
                    "60125c3e-4980-4cc8-b54e-037b47185e2b",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect PRECISION= new PrecisionEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.CRITICAL_DAMAGE.attribute,
                    "32a5a129-51a6-4a38-b78e-e7afb69f9e17",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(SpellAttributes.CRITICAL_CHANCE.attribute,
                    "bb6233b1-4759-47d0-9044-d509b4bc6695",
                    0.02,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect DEATHMARK= new DeathMarkEffect(StatusEffectCategory.HARMFUL, 3124687)
            .addAttributeModifier(PlayerAttributes.RESISTANCE,
                    "325dbaa9-84c5-4cea-aca1-88b8dc585c3e",
                    -0.25,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(PlayerAttributes.HEALING,
                    "5e2ff54c-9698-4ba0-8320-9ba6e9f2b394",
                    -0.25,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect MARKSMAN= new MarksmanEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect STEALTH= new StealthEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    "0e7a848f-46db-4e12-9d4a-40a5f24683c3",
                    -0.40,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect MIGHT= new MightEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    "4a4233a0-3299-4755-8213-9f10cfb7e795",
                    0.25,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect EXHAUSTION= new ExhaustionEffect(StatusEffectCategory.HARMFUL, 3124687)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    "1ce35aec-6a41-44ff-a537-f03b76f01664",
                    -0.01,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED,
                    "9410035d-5838-4f51-a48e-c896e7a7570f",
                            -0.01,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    "1d715996-97d0-41d7-ab3c-96c5302c9d98",
                    -0.01,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect REVEALED= new RevealedEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect BARRIER= new BarrierEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect SOULSHOCK= new SoulshockEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute,
                    "f4f57190-f82f-4283-a4b9-a898382bcea7",
                    2,
                    EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.LIGHTNING).attribute,
                    "f811acad-2542-4e5a-837f-a869251162ee",
                    2,
                    EntityAttributeModifier.Operation.ADDITION);
    public static StatusEffect SPELLFORGED= new SoulshockEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute,
                    "5d6e7b01-e11b-46ac-8a97-0ec497616982",
                    1,
                    EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.LIGHTNING).attribute,
                    "dc0cea79-ffa6-4209-b851-952f60147b2c",
                    1,
                    EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute,
                    "fa5f66cd-ca9b-4da6-a4a8-3444a77156b7",
                    1,
                    EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute,
                    "d7b58e45-df85-40d9-a78b-943dc8765f2a",
                    1,
                    EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute,
                    "3d183b4b-9bfb-4d4e-b7f0-bd8cf65a34fc",
                    1,
                    EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.FROST).attribute,
                    "5951bed4-b058-4320-8512-75c1be44bc33",
                    1,
                    EntityAttributeModifier.Operation.ADDITION);
    public static StatusEffect DIVINEADJUDICATION= new DivineAdjudicationEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect SACREDONSLAUGHT= new SacredOnslaughtEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect CONSECRATION= new ConsecrateEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect TAUNTED= new TauntedEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect UNDYING= new UndyingEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect RAGE= new RageEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    "2489075c-b5ce-40b5-8a1d-3787ad4f4d8b",
                    +0.005,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED,
                    "29d236f5-e3e1-4612-898b-39b916fd771c",
                    +0.005,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public static StatusEffect OVERLOAD= new OverloadEffect(StatusEffectCategory.BENEFICIAL, 3124687)
            .addAttributeModifier(SpellAttributes.CRITICAL_DAMAGE.attribute,
                    "c937f985-c571-46e5-8339-b4ccf4c15442",
                    0.45,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(SpellAttributes.CRITICAL_CHANCE.attribute,
                    "ad26be8b-db35-4d04-98db-d8943e4ac8be",
                    0.10,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect BLADESTORM= new BladestormEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect VITALITYBOND= new VitalityBondEffect(StatusEffectCategory.BENEFICIAL, 3124687);
    public static StatusEffect ANOINTED= new AnointedEffect(StatusEffectCategory.BENEFICIAL, 3124687);


    public static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(SimplySkills.MOD_ID, name), statusEffect);
    }
    
    public static void registerEffects() {

        Synchronized.configure(STEALTH, true);
        Synchronized.configure(BLADESTORM, true);
        Synchronized.configure(VITALITYBOND, true);
        Synchronized.configure(UNDYING, true);
        Synchronized.configure(ARCANEVOLLEY, true);
        Synchronized.configure(FROSTVOLLEY, true);
        Synchronized.configure(BARRIER, true);
        Synchronized.configure(RAGE, true);
        Synchronized.configure(EVASION, true);
        Synchronized.configure(IMMOBILIZE, true);
        Synchronized.configure(DEATHMARK, true);
        Synchronized.configure(TAUNTED, true);

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
        LEAPSLAM = registerStatusEffect("leapslam", LEAPSLAM);
        IMMOBILIZINGAURA = registerStatusEffect("immobilizing_aura", IMMOBILIZINGAURA);
        SPELLBREAKING = registerStatusEffect("spellbreaking", SPELLBREAKING);
        EARTHSHAKER = registerStatusEffect("earthshaker", EARTHSHAKER);
        ARCANEATTUNEMENT = registerStatusEffect("arcane_attunement", ARCANEATTUNEMENT);
        HOLYATTUNEMENT = registerStatusEffect("holy_attunement", HOLYATTUNEMENT);
        SOULATTUNEMENT = registerStatusEffect("soul_attunement", SOULATTUNEMENT);
        FIREATTUNEMENT = registerStatusEffect("fire_attunement", FIREATTUNEMENT);
        FROSTATTUNEMENT = registerStatusEffect("frost_attunement", FROSTATTUNEMENT);
        LIGHTNINGATTUNEMENT = registerStatusEffect("lightning_attunement", LIGHTNINGATTUNEMENT);
        PRECISION = registerStatusEffect("precision", PRECISION);
        DEATHMARK = registerStatusEffect("death_mark", DEATHMARK);
        MARKSMAN = registerStatusEffect("marksman", MARKSMAN);
        STEALTH = registerStatusEffect("stealth", STEALTH);
        MIGHT = registerStatusEffect("might", MIGHT);
        EXHAUSTION = registerStatusEffect("exhaustion", EXHAUSTION);
        REVEALED = registerStatusEffect("revealed", REVEALED);
        BARRIER = registerStatusEffect("barrier", BARRIER);
        SOULSHOCK = registerStatusEffect("soulshock", SOULSHOCK);
        SPELLFORGED = registerStatusEffect("spellforged", SPELLFORGED);
        DIVINEADJUDICATION = registerStatusEffect("divine_adjudication", DIVINEADJUDICATION);
        TAUNTED = registerStatusEffect("taunted", TAUNTED);
        UNDYING = registerStatusEffect("undying", UNDYING);
        RAGE = registerStatusEffect("rage", RAGE);
        OVERLOAD = registerStatusEffect("overload", OVERLOAD);
        BLADESTORM = registerStatusEffect("bladestorm", BLADESTORM);
        VITALITYBOND = registerStatusEffect("vitality_bond", VITALITYBOND);
        ANOINTED = registerStatusEffect("anointed", ANOINTED);

        if (FabricLoader.getInstance().isModLoaded("paladins")) {
            CONSECRATION = registerStatusEffect("consecration", CONSECRATION);
            SACREDONSLAUGHT = registerStatusEffect("sacred_onslaught", SACREDONSLAUGHT);
        }

    }


}
