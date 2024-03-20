package net.sweenus.simplyskills.abilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.api.SkillsAPI;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Comparator;

public class AscendancyAbilities {

    public static int getAscendancyPoints(PlayerEntity player) {
        if (player instanceof  ServerPlayerEntity serverPlayer) {

            if (FabricLoader.getInstance().isModLoaded("prominent")) {
                if (Registries.ATTRIBUTE.get(new Identifier("eldritch_end:corruption")) != null) {
                    return (int) player.getAttributeValue(Registries.ATTRIBUTE.get(new Identifier("eldritch_end:corruption")));
                } // Scale abilities with Corruption in Prominence
            }

            Identifier category = new Identifier(SimplySkills.MOD_ID, "ascendancy");
            if (SkillsAPI.getCategory(category).isPresent() && !SkillsAPI.getCategory(category).get().getUnlockedSkills(serverPlayer).isEmpty()) {
                return SkillsAPI.getCategory(category).get().getUnlockedSkills(serverPlayer).size();
            }
        }
        return 0;
    }


    //------- ASCENDANCY ABILITIES --------

    public static boolean righteousHammers(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RIGHTEOUSHAMMERS,
                800, 1 + (getAscendancyPoints(player) / 10), false, false, true));
        return true;
    }

    public static boolean boneArmor(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BONEARMOR,
                800, 3 + (getAscendancyPoints(player) / 10), false, false, true));
        return true;
    }
    public static void boneArmorEffect(ServerPlayerEntity player) {
            if (HelperMethods.isUnlocked("simplyskills:ascendancy", SkillReferencePosition.ascendancyBoneArmor, player) && player.hasStatusEffect(EffectRegistry.BONEARMOR))
                HelperMethods.decrementStatusEffect(player, EffectRegistry.BONEARMOR);
    }

    public static boolean cyclonicCleave(PlayerEntity player) {
        if (FabricLoader.getInstance().isModLoaded("prominent"))
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:cyclonic_cleave_prom", 3, player, null);
        else SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:cyclonic_cleave", 3, player, null);

        return true;
    }

    public static boolean magicCircle(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MAGICCIRCLE,
                120 + (getAscendancyPoints(player)), 0, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.IMMOBILIZE,
                25, 0, false, false, true));
        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SPELL_RADIANT_HIT,
                SoundCategory.PLAYERS, 0.2f, 0.9f);
        return true;
    }
    public static boolean magicCircleEffect(PlayerEntity player) {
        return getAscendancyPoints(player) > 29 && player.hasStatusEffect(EffectRegistry.MAGICCIRCLE);
    }

    public static boolean arcaneSlash(PlayerEntity player) {
        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:arcane_slash", 3, player, null);
        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SPELL_SLASH,
                SoundCategory.PLAYERS, 0.4f, 1.1f);
        if (getAscendancyPoints(player) > 9)
            HelperMethods.incrementStatusEffect(player, EffectRegistry.ARCANEATTUNEMENT, 60, 1+(getAscendancyPoints(player) / 10), 19);

        return true;
    }

    public static boolean agony(PlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        Box box = HelperMethods.createBox(player, 10);
        Entity closestEntity = world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                .min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(player)))
                .orElse(null);

        if (closestEntity != null) {
            if ((closestEntity instanceof LivingEntity ee)) {
                if (HelperMethods.checkFriendlyFire(ee, player)) {
                    SimplyStatusEffectInstance agonyEffect = new SimplyStatusEffectInstance(
                            EffectRegistry.AGONY, 200 + getAscendancyPoints(player), 0, false,
                            false, true);
                    agonyEffect.setSourceEntity(player);
                    ee.addStatusEffect(agonyEffect);
                    HelperMethods.spawnWaistHeightParticles(world, ParticleTypes.SMOKE, player, ee, 20);
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_04,
                            SoundCategory.PLAYERS, 0.2f, 1.0f);

                    return true;
                }
            }
        }
        return false;
    }

    public static void agonyEffect(PlayerEntity playerAttacker, LivingEntity livingEntity) {
        SimplyStatusEffectInstance agonyEffect = (SimplyStatusEffectInstance) livingEntity.getStatusEffect(EffectRegistry.AGONY);
        livingEntity.timeUntilRegen = 0;
        livingEntity.damage(playerAttacker.getDamageSources().indirectMagic(playerAttacker, playerAttacker), (float) (0.1 * HelperMethods.getHighestAttributeValue(playerAttacker)));
        livingEntity.timeUntilRegen = 0;

        if (agonyEffect != null) {
            LivingEntity sourceEntity = agonyEffect.getSourceEntity();
            if (sourceEntity instanceof PlayerEntity sourcePlayer && AscendancyAbilities.getAscendancyPoints(sourcePlayer) > 29)
                playerAttacker.heal((float) (0.1* sourcePlayer.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute)));
        }
    }
    public static boolean torment(PlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        Box box = HelperMethods.createBox(player, 10);
        Entity closestEntity = world.getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                .min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(player)))
                .orElse(null);

        if (closestEntity != null) {
            if ((closestEntity instanceof LivingEntity ee)) {
                if (HelperMethods.checkFriendlyFire(ee, player)) {
                    SimplyStatusEffectInstance tormentEffect = new SimplyStatusEffectInstance(
                            EffectRegistry.TORMENT, 160 + getAscendancyPoints(player), 0, false,
                            false, true);
                    tormentEffect.setSourceEntity(player);
                    ee.addStatusEffect(tormentEffect);
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_04,
                            SoundCategory.PLAYERS, 0.2f, 1.0f);

                    if (getAscendancyPoints(player) > 29) {
                        SimplyStatusEffectInstance tauntedEffect = new SimplyStatusEffectInstance(
                                EffectRegistry.TAUNTED, 160 + getAscendancyPoints(player), 0, false,
                                false, true);
                        tauntedEffect.setSourceEntity(player);
                        ee.addStatusEffect(tauntedEffect);
                    }
                    HelperMethods.spawnWaistHeightParticles(world, ParticleTypes.SMOKE, player, ee, 20);

                    return true;
                }
            }
        }
        return false;
    }

    public static boolean tormentEffect(PlayerEntity player, DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            if (attacker.hasStatusEffect(EffectRegistry.TORMENT)) {
                SimplyStatusEffectInstance tormentEffect = (SimplyStatusEffectInstance) attacker.getStatusEffect(EffectRegistry.TORMENT);
                if (tormentEffect.getSourceEntity() instanceof PlayerEntity sourcePlayer && sourcePlayer == player) {
                    attacker.damage(source, amount);
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean rapidfire(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RAPIDFIRE, 120+getAscendancyPoints(player), 0, false, false, true));
        return true;
    }

    public static boolean cataclysm(PlayerEntity player) {
        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:cataclysm", 3, player, null);
        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.ENERGY_CHARGE,
                SoundCategory.PLAYERS, 0.3f, 1.0f);
        return true;
    }

    public static boolean ghostwalk(PlayerEntity player) {
        SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:ghostwalk", 3, player, null);
        return true;
    }

    public static boolean skywardSunder(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SKYWARDSUNDER, 45, 0, false, false, true));
        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SLASH_02,
                SoundCategory.PLAYERS, 0.6f, 1.0f);
        return true;
    }

    public static void goldenAegis(PlayerEntity player) {
        HelperMethods.incrementStatusEffect(player, EffectRegistry.GOLDENAEGIS, 2400, 1, 15 + (getAscendancyPoints(player) / 10));
    }

    public static boolean righteousShield(PlayerEntity player) {
        if (player.hasStatusEffect(EffectRegistry.GOLDENAEGIS)) {
            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:righteous_shield", 3, player, null);
            return true;
        }
        return false;
    }

    public static boolean chainbreaker(PlayerEntity player) {
        HelperMethods.buffSteal(player, player,true, false,true,true);
        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SPELL_ARCANE_CAST,
                SoundCategory.PLAYERS, 0.3f, 1.1f);
        HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, 120, 1+(getAscendancyPoints(player) / 10), 19);
        HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMANSHIP, 120, 1+(getAscendancyPoints(player) / 10), 19);
        HelperMethods.spawnParticlesPlane(player.getWorld(), ParticleTypes.POOF, player.getBlockPos(), 1, 0, 0.1, 0);
        if (getAscendancyPoints(player) > 29)
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.UNDYING, 120, 0, false, false, true));
        return true;
    }

}
