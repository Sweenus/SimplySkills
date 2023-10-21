package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.paladins.effect.Effects;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Random;

public class CrusaderAbilities {

    // Retribution
    public static void passiveCrusaderRetribution(PlayerEntity player, LivingEntity attacker) {
        int random = new Random().nextInt(100);
        int retributionChance = SimplySkills.crusaderConfig.passiveCrusaderRetributionChance;
        if (random < retributionChance)
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:paladins_judgement", 32, attacker);
    }


    //Exhaustive Recovery
    public static void passiveCrusaderExhaustiveRecovery(PlayerEntity player, LivingEntity attacker) {
        int random = new Random().nextInt(100);
        int recoveryChance = SimplySkills.crusaderConfig.passiveCrusaderExhaustiveRecoveryChance;
        int exhaustStacks = SimplySkills.crusaderConfig.passiveCrusaderExhaustiveRecoveryExhaustionStacks - 1;
        if (random < recoveryChance) {
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:paladins_flash_heal", 32, player);
            HelperMethods.incrementStatusEffect(player, EffectRegistry.EXHAUSTION, 300, exhaustStacks, 99);
        }
    }

    //Aegis
    public static void passiveCrusaderAegis(PlayerEntity player) {
        int frequency = SimplySkills.crusaderConfig.passiveCrusaderAegisFrequency;
        int stacksRemoved = SimplySkills.crusaderConfig.passiveCrusaderAegisStacksRemoved;
        if (player.hasStatusEffect(EffectRegistry.EXHAUSTION)) {
            if (player.age % frequency == 0 && player.getStatusEffect(EffectRegistry.EXHAUSTION).getAmplifier() > stacksRemoved) {
                HelperMethods.incrementStatusEffect(player, Effects.DIVINE_PROTECTION, 200, 1, 5);
                HelperMethods.decrementStatusEffects(player, EffectRegistry.EXHAUSTION, stacksRemoved);
            }
        }
    }


    //------- SIGNATURE ABILITIES --------

    // Heavensmith's Call
    public static boolean signatureHeavensmithsCall(String crusaderSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int heavensmithsCallRange = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallRange;
        int duration = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDADuration;
        int tauntDuration = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallTauntMarkDuration;

        if (HelperMethods.getTargetedEntity(player, heavensmithsCallRange) != null)
            blockpos = HelperMethods.getTargetedEntity(player, heavensmithsCallRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, heavensmithsCallRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;

                        if (HelperMethods.isUnlocked(crusaderSkillTree,
                                SkillReferencePosition.crusaderSpecialisationDivineAdjudication, player))
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.DIVINEADJUDICATION, duration, 0, false, false, true));

                        if (HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationHeavensmithsCallMark, player))
                            le.addStatusEffect(new StatusEffectInstance(EffectRegistry.DEATHMARK, tauntDuration));

                        if ((le instanceof MobEntity me) && HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationHeavensmithsCallTaunt, player)) {
                            SimplyStatusEffectInstance tauntEffect = new SimplyStatusEffectInstance(
                                    EffectRegistry.TAUNTED, tauntDuration, 0, false,
                                    false, true);
                            tauntEffect.setSourceEntity(player);
                            me.addStatusEffect(tauntEffect);
                        }


                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                "simplyskills:physical_heavensmiths_call",
                                3, le);
                        break;
                    }
                }
            }
        }
        return success;
    }

    // Sacred Onslaught
    public static boolean signatureCrusaderSacredOnslaught(String crusaderSkillTree, PlayerEntity player) {

        int divineProtectionDuration = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtDPDuration;
        int dashDuration = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtDashDuration;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SACREDONSLAUGHT, dashDuration, 0, false, false, true));

        if (HelperMethods.isUnlocked(crusaderSkillTree,
                SkillReferencePosition.crusaderSpecialisationSacredOnslaughtDefend, player)) {
            player.addStatusEffect(new StatusEffectInstance(Effects.DIVINE_PROTECTION, divineProtectionDuration, 0 , false, false, true));
            player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT15,
                    SoundCategory.PLAYERS, 0.5f, 1.1f);
        }
        if (HelperMethods.isUnlocked(crusaderSkillTree,
                SkillReferencePosition.crusaderSpecialisationSacredOnslaughtMighty, player)) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, divineProtectionDuration, 3, 5);
        }
        return true;
    }

    // Consecration
    public static boolean signatureCrusaderConsecration(String crusaderSkillTree, PlayerEntity player) {

        int consecrationExtendDuration = SimplySkills.crusaderConfig.signatureCrusaderConsecrationExtendDuration;
        int consecrationDuration = SimplySkills.crusaderConfig.signatureCrusaderConsecrationDuration;

        if (HelperMethods.isUnlocked(crusaderSkillTree, SkillReferencePosition.crusaderSpecialisationConsecrationDuration, player))
            consecrationDuration = SimplySkills.crusaderConfig.signatureCrusaderConsecrationDuration + consecrationExtendDuration;

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.CONSECRATION, consecrationDuration, 0 , false, false, true));

        return true;
    }

















    // ------- EFFECTS --------

    // Heavensmith's Call - Divine Adjudication
    public static void effectDivineAdjudication(PlayerEntity player) {
        int frequency = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDAFrequency;

        if (HelperMethods.isUnlocked("simplyskills:crusader",
                SkillReferencePosition.crusaderSpecialisationHeavensmithsCall, player) &&
                player.hasStatusEffect(EffectRegistry.DIVINEADJUDICATION) && player.age % frequency == 0) {
            int chance = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDAChance;
            int radius = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDARadius;
            int exhaustStacksRemoved = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDAExhaustStacks;
            int mightDuration = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDAMightDuration;
            int mightStacksMax = SimplySkills.crusaderConfig.signatureCrusaderHeavensmithsCallDAMightStacksMax - 1;
            String spellIdentifier = "simplyskills:paladins_judgement";


            if (SignatureAbilities.castSpellEngineAOE(player, spellIdentifier, radius, chance, true)) {
                if (HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationHeavensmithsCallExhaust, player))
                    HelperMethods.decrementStatusEffects(player, EffectRegistry.EXHAUSTION, exhaustStacksRemoved);
                if (HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationHeavensmithsCallMighty, player))
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, mightDuration, 1, mightStacksMax);
            }
        }

    }



}
