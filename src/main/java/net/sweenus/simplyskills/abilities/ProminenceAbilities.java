package net.sweenus.simplyskills.abilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ProminenceAbilities {

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

    public static boolean boneArmor(PlayerEntity player) {

        ServerWorld world = (ServerWorld) player.getWorld();
        Box box = HelperMethods.createBoxHeight(player, 12);
        AtomicInteger count = new AtomicInteger();
        player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> player instanceof ServerPlayerEntity)
                .forEach(entity -> {
                    LivingEntity le = (LivingEntity) entity;
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) player;
                    if (HelperMethods.checkFriendlyFire(le, playerEntity)) {
                        SimplyStatusEffectInstance tauntedEffect = new SimplyStatusEffectInstance(
                                EffectRegistry.TAUNTED, 160 + getAscendancyPoints(player), 0, false,
                                false, true);
                        if (getAscendancyPoints(player) > 29)
                            le.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                                    300 + getAscendancyPoints(player), 1,
                                    false, false, true));
                        tauntedEffect.setSourceEntity(player);
                        le.addStatusEffect(tauntedEffect);
                        count.getAndIncrement();
                    HelperMethods.spawnWaistHeightParticles(world, ParticleTypes.SMOKE, player, le, 20);
                }
            });

        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_04,
                SoundCategory.PLAYERS, 0.2f, 1.0f);

        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BONEARMOR,
                400, Math.min(6, count.get()), false, false, true));
        player.setAbsorptionAmount(Math.min(20, 1 + count.get() * 2));
        return true;
    }
    public static void boneArmorEffect(ServerPlayerEntity player) {
        if (HelperMethods.isUnlocked("puffish_skills:prom", SkillReferencePosition.ascendancyBoneArmor, player) && player.hasStatusEffect(EffectRegistry.BONEARMOR)) {
            StatusEffectInstance boneArmorEffect = player.getStatusEffect(EffectRegistry.BONEARMOR);
            if (boneArmorEffect != null) {
                HelperMethods.decrementStatusEffect(player, EffectRegistry.BONEARMOR);
                player.setAbsorptionAmount(Math.min(20, 2 + boneArmorEffect.getAmplifier() * 2));
            }
        }
    }

    public static void focusEffect(PlayerEntity player, Identifier spellId) {
        if (HelperMethods.isUnlocked("puffish_skills:prom", SkillReferencePosition.promFocus, player)) {
            if (spellId.toString().contains("archers:barrage")) {
                if (player.getMainHandStack().getItem() instanceof BowItem) {
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FOCUS, 220, 0, false, false, true));
                } else if (player.getMainHandStack().getItem() instanceof CrossbowItem) {
                    player.removeStatusEffect(EffectRegistry.REVEALED);
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.MARKSMANSHIP, 80, 8, 15);
                    player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 80, 0, false, false, true));
                }
            }
        }
    }

    public static void promTwinstrike(PlayerEntity player, LivingEntity target) {
        int effectChance = SimplySkills.warriorConfig.passiveWarriorTwinstrikeChance;
        int effectDamage = (int) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        DamageSource damageSource = player.getDamageSources().playerAttack(player);
        if (HelperMethods.isDualWielding(player))
            effectChance = effectChance * 2;
        if (player.getRandom().nextInt(100) < effectChance) {
            target.damage(damageSource, effectDamage);
            target.timeUntilRegen = 0;
        }
    }

    public static void warriorsDevotion(PlayerEntity player) {
        if (player.age % 20 == 0 && HelperMethods.isUnlocked("puffish_skills:prom", SkillReferencePosition.promWarriorsDevotion, player)) {
            if (player.getMainHandStack().isEmpty() || player.getOffHandStack().isEmpty()) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.WARRIORSDEVOTION, 30, 0, false, false, true));
            }
       }
    }

}
