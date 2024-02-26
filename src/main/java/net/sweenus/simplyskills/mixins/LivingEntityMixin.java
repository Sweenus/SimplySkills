package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.DynamicDamage;
import net.sweenus.simplyskills.util.HelperMethods;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {


    @Shadow protected float lastDamageTaken;

    @Shadow private long lastDamageTime;

    @Shadow public abstract float getMaxHealth();

    @Shadow @Nullable public abstract LivingEntity getAttacker();

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    //Prevent detection when stealthed
    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    public void simplyskills$canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target.hasStatusEffect(EffectRegistry.STEALTH))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "isDead", cancellable = true)
    public void simplyskills$tick(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (livingEntity.getHealth() <= 0.0F && livingEntity.hasStatusEffect(EffectRegistry.UNDYING)) {
            livingEntity.setHealth(1.0F);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    protected void simplyskills$modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity attacker = this.getAttacker();
        LivingEntity livingEntity = (LivingEntity) (Object)this;

        // DAS Dynamic Attribute Scaling (scale attributes depending on skill points spent)
        if (SimplySkills.generalConfig.enableDAS && !(livingEntity instanceof PlayerEntity)) {
            if (SimplySkills.generalConfig.DASHealth > 0)
                DynamicDamage.DynamicAttributeScaling(livingEntity, EntityAttributes.GENERIC_MAX_HEALTH, "SimplySkills health DAS",
                        SimplySkills.generalConfig.DASHealth, UUID.fromString("631937f6-bc47-486b-b07a-542823d668a6"));
            if (SimplySkills.generalConfig.DASAttack > 0)
                DynamicDamage.DynamicAttributeScaling(livingEntity, EntityAttributes.GENERIC_ATTACK_DAMAGE, "SimplySkills attack damage DAS",
                        SimplySkills.generalConfig.DASAttack, UUID.fromString("8097403f-ed25-4534-b7f9-854e16ef2fbb"));
            if (SimplySkills.generalConfig.DASArmor > 0)
                DynamicDamage.DynamicAttributeScaling(livingEntity, EntityAttributes.GENERIC_ARMOR, "SimplySkills armor DAS",
                        SimplySkills.generalConfig.DASArmor, UUID.fromString("eae99963-45c4-4651-b501-4b2e16879705"));
            if (SimplySkills.generalConfig.DASArmorToughness > 0)
                DynamicDamage.DynamicAttributeScaling(livingEntity, EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "SimplySkills armor toughness DAS",
                        SimplySkills.generalConfig.DASArmorToughness, UUID.fromString("0794ab19-7dbd-421f-90c7-6b65e3aee495"));
            if (SimplySkills.generalConfig.DASSpeed / 100 > 0)
                DynamicDamage.DynamicAttributeScaling(livingEntity, EntityAttributes.GENERIC_MOVEMENT_SPEED, "SimplySkills movement speed DAS",
                        SimplySkills.generalConfig.DASSpeed / 100, UUID.fromString("426d5ed8-020b-484f-93d9-4327a3c05c97"));
            if (SimplySkills.generalConfig.DASKnockbackResist / 100 > 0)
                DynamicDamage.DynamicAttributeScaling(livingEntity, EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "SimplySkills knockback resistance DAS",
                        SimplySkills.generalConfig.DASKnockbackResist / 100, UUID.fromString("9ffef649-025d-4c30-98ed-d4378cd07d36"));
        }

        float newAmount = DynamicDamage.DynamicDamageReduction(attacker, livingEntity, amount, lastDamageTaken, cir.getReturnValue(), lastDamageTime);
        if (newAmount != amount)
            cir.setReturnValue(newAmount);

    }
}
