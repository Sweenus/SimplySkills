package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.particle.Particles;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClericAbilities {

    // ------ PASSIVES ------

    // Healing Ward
    //Chance when casting a healing spell to grant your target a stack of barrier
    public static void passiveClericHealingWard(PlayerEntity player, List<Entity> targets, Identifier spellId) {
        int random = new Random().nextInt(100);
        int chance = 10;
        Spell spell = SpellRegistry.getSpell(spellId);
        MagicSchool healingSchool = MagicSchool.HEALING;
        if (random < chance) {
            targets.forEach(target -> {
                if (target instanceof LivingEntity livingTarget && spell.school == healingSchool) {
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
        if (spellId.toString().contains("holy_beam"))
            chance = 10;
        Spell spell = SpellRegistry.getSpell(spellId);
        MagicSchool healingSchool = MagicSchool.HEALING;
        if (random < chance && !targets.contains(player) && spell.school == healingSchool) {
            if (spellId.toString().contains("holy_beam"))
                SignatureAbilities.castSpellEngineIndirectTarget(player, "paladins:heal", 10, player);
            else SignatureAbilities.castSpellEngineIndirectTarget(player, spellId.toString(), 10, player);
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

                        // Grants recipient Fire Resistance
                        if (HelperMethods.isUnlocked(clericSkillTree, SkillReferencePosition.clericSpecialisationDivineInterventionFireResistance, player))
                            HelperMethods.incrementStatusEffect(le, StatusEffects.FIRE_RESISTANCE, 240, 1, 5);

                        // Grants recipient Might
                        if (HelperMethods.isUnlocked(clericSkillTree, SkillReferencePosition.clericSpecialisationDivineInterventionMight, player))
                            HelperMethods.incrementStatusEffect(le, EffectRegistry.MIGHT, 240, 3, 10);

                        // Grants recipient Spellforged
                        if (HelperMethods.isUnlocked(clericSkillTree, SkillReferencePosition.clericSpecialisationDivineInterventionSpellforged, player))
                            HelperMethods.incrementStatusEffect(le, EffectRegistry.SPELLFORGED, 240, 3, 10);

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
        if (spellProjectile.getSpell() != null && spellId != null && spellId.toString().equals("simplyskills:sacred_orb") && spellProjectile.age > 20 && spellProjectile.getFollowedTarget() == null) {
            Box box = HelperMethods.createBox(spellProjectile, 6);
            for (Entity entities : spellProjectile.getWorld().getOtherEntities(spellProjectile, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities instanceof LivingEntity le && spellProjectile.getOwner() instanceof PlayerEntity playerOwner && !HelperMethods.checkFriendlyFire(le, playerOwner)) {
                    spellProjectile.setFollowedTarget(le);
                    break;
                }
            }
        }
    }
    public static void signatureClericSacredOrbImpact(EntityHitResult entityHitResult, Identifier spellId, Entity ownerEntity, SpellProjectile spellProjectile) {
        if (spellProjectile.getSpell() != null && spellId != null && spellId.toString().equals("simplyskills:sacred_orb") && entityHitResult.getEntity() != null
                && entityHitResult.getEntity() instanceof LivingEntity livingEntity && ownerEntity instanceof LivingEntity livingOwner) {

            SimplyStatusEffectInstance vitalityBond = new SimplyStatusEffectInstance(EffectRegistry.VITALITYBOND, 500, 0, false, false, true);
            SimplyStatusEffectInstance vitalityBond2 = new SimplyStatusEffectInstance(EffectRegistry.VITALITYBOND, 500, 0, false, false, true);
            vitalityBond.setSourceEntity(livingOwner);
            vitalityBond2.setSourceEntity(livingOwner);
            livingEntity.addStatusEffect(vitalityBond);
            livingOwner.addStatusEffect(vitalityBond2);

        }
    }

    // Anoint Weapon
    public static boolean signatureClericAnointWeapon(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ANOINTED, 400, 0, false, false, true));
        return true;
    }
    // Cleanse tick
    public static void signatureClericAnointWeaponCleanse(PlayerEntity player) {
        int frequency = 20;
        if (player.age %frequency == 0) {
            HelperMethods.buffSteal(player, player, true, true, true, true);
        }
    }
    // Undying on damaged
    public static void signatureClericAnointWeaponUndying(PlayerEntity player) {
        float playerHealthPercent = ((player.getHealth() / player.getMaxHealth()) * 100);
        int roll = player.getRandom().nextInt(100);
        int chance = 15;

        if (playerHealthPercent < 30 && roll < chance)
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.UNDYING, 120, 0, false, false, true));

    }

    public static void signatureClericAnointWeaponEffect(PlayerEntity player) {
        int radius = 4;
        float damageMultiplier = 2.2f;

        Box box = HelperMethods.createBox(player, radius);
        List<Entity> targets = new ArrayList<>();
        List<Entity> hostileTargets = new ArrayList<>();
        for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
            if (entities instanceof LivingEntity le) {
                if (!HelperMethods.checkFriendlyFire(le, player))
                    targets.add(le);
                else if (HelperMethods.checkFriendlyFire(le, player))
                    hostileTargets.add(le);
            }
        }
        Identifier spellId = new Identifier("simplyskills:paladins_flash_heal");
        SpellCast.Action action = SpellCast.Action.CHANNEL;
        DamageSource damageSource = player.getDamageSources().indirectMagic(player, player);
        SpellHelper.performSpell(player.getWorld(), player, spellId, targets, action, 20);
        float amount = (float) (player.getAttributeValue(SpellAttributes.
                POWER.get(MagicSchool.HEALING).attribute) * damageMultiplier) / hostileTargets.size();


        hostileTargets.forEach(entity -> {
            entity.timeUntilRegen = 0;
            entity.damage(damageSource, amount);
            entity.timeUntilRegen = 0;

            if (entity instanceof MobEntity mobEntity && mobEntity.isUndead())
                HelperMethods.incrementStatusEffect(mobEntity, StatusEffects.SLOWNESS, 40, 1, 4);

            for (int i = 6; i > 0; i--) {
                HelperMethods.spawnParticle(player.getWorld(), Particles.holy_spark_mini.particleType,
                        entity.getX(), entity.getY(), entity.getZ(), 0.1, 0.1+i, 0.2);
                HelperMethods.spawnParticle(player.getWorld(), Particles.holy_spark_mini.particleType,
                        entity.getX(), entity.getY(), entity.getZ(), 0.2, 0.2+i, 0.1);
                HelperMethods.spawnParticle(player.getWorld(), Particles.holy_hit.particleType,
                        entity.getX(), entity.getY(), entity.getZ(), 0.1, 0.2*i, 0.2);
            }
        });

        // Grants player Resistance
        if (HelperMethods.isUnlocked("simplyskills:cleric", SkillReferencePosition.clericSpecialisationAnointWeaponResistance, player))
            HelperMethods.incrementStatusEffect(player, StatusEffects.RESISTANCE, 40, 1, 2);

    }


}
