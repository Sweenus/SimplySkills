package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.api.spell.SpellPool;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.List;
import java.util.Random;

public class ClericAbilities {

    // ------ PASSIVES ------

    // Healing Ward
    //Chance when casting a healing spell to grant your target a stack of barrier
    public static void passiveClericHealingWard(PlayerEntity player, List<Entity> targets) {
        int random = new Random().nextInt(100);
        int chance = 10;
        if (random < chance) {
            targets.forEach(target -> {
                if (target instanceof LivingEntity livingTarget) {
                    HelperMethods.incrementStatusEffect(livingTarget, EffectRegistry.BARRIER, 100, 1, 20);
                }
            });
        }
    }

    // Mutual Mending
    // Chance when casting a healing spell to also cast the spell on yourself
    public static void passiveClericMutualMending(PlayerEntity player, Identifier spellId, List<Entity> targets) {
        int random = new Random().nextInt(100);
        int chance = 20;
        Spell spell = SpellRegistry.getSpell(spellId);
        MagicSchool healingSchool = MagicSchool.HEALING;
        if (random < chance && !targets.contains(player) && spell.school == healingSchool) {
            SignatureAbilities.castSpellEngineIndirectTarget(player, spellId.toString(), 10, player);
        }
    }

    // Altruism
    // When wearing less than 10 points of armor, you periodically generate Spellforged stacks
    public static void passiveClericAltruism(PlayerEntity player) {
        int frequency = 600;
        if (player.getArmor() <= 10 && player.age %frequency == 0) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.SPELLFORGED, frequency+5, 1, 2);
        }
    }

    // Shared Vitality
    // Fire a bolt of holy energy at your target, healing them and granting both of you Vitality Bond - Meld lifeforce, causing you and your targets healthbars to equalise.
    public static void passiveClericSharedVitality(PlayerEntity player, LivingEntity target) {
        int random = new Random().nextInt(100);
        int chance = 10;
        if (random < chance)
            HelperMethods.incrementStatusEffect(target, EffectRegistry.BARRIER, 100, 1, 20);
    }


    //------- SIGNATURE ABILITIES --------

    // Divine Intervention
    //Call down celestial energy on a ally in the targeted area, granting them Undying for 12s
    public static boolean signatureClericDivineIntervention(String clericSkillTree, PlayerEntity player) {
        Vec3d blockpos = null;
        boolean success = false;
        int divineInterventionRange = 25;
        if (HelperMethods.getTargetedEntity(player, divineInterventionRange) != null)
            blockpos = HelperMethods.getTargetedEntity(player, divineInterventionRange).getPos();

        if (blockpos == null)
            blockpos = HelperMethods.getPositionLookingAt(player, divineInterventionRange);

        if (blockpos != null) {
            int xpos = (int) blockpos.getX();
            int ypos = (int) blockpos.getY();
            int zpos = (int) blockpos.getZ();
            BlockPos searchArea = new BlockPos(xpos, ypos, zpos);
            Box box = HelperMethods.createBoxAtBlock(searchArea, 3);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof LivingEntity le) && !HelperMethods.checkFriendlyFire(le, player)) {
                        success = true;

                        SignatureAbilities.castSpellEngineIndirectTarget(player,
                                "simplyskills:divine_intervention",
                                15, le);
                        break;
                    }
                }
            }
        }
        return success;
    }
    // Sacred Orb
    public static boolean signatureClericSacredOrb(String clericSkillTree, PlayerEntity player) {
        SignatureAbilities.castSpellEngineDumbFire(player, "simplyskills:sacred_orb");
        return true;
    }
    public static void signatureClericSacredOrbHoming(SpellProjectile spellProjectile, Identifier spellId) {
        if (spellId != null && spellId.toString().equals("simplyskills:sacred_orb") && spellProjectile.age > 20 && spellProjectile.getFollowedTarget() == null) {
            Box box = HelperMethods.createBox(spellProjectile, 3);
            for (Entity entities : spellProjectile.getWorld().getOtherEntities(spellProjectile, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities instanceof LivingEntity le && spellProjectile.getOwner() instanceof PlayerEntity playerOwner && !HelperMethods.checkFriendlyFire(le, playerOwner)) {
                    spellProjectile.setFollowedTarget(le);
                    break;
                }
            }
        }
    }


}
