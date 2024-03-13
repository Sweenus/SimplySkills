package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.DynamicDamage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

        float newAmount = DynamicDamage.dynamicDamageReduction(attacker, livingEntity, amount, lastDamageTaken, cir.getReturnValue(), lastDamageTime);
        if (newAmount != amount)
            cir.setReturnValue(newAmount);

    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void simplyskills$readCustomDataFromNbt(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.getWorld().isClient && SimplySkills.generalConfig.enableDAS && !(livingEntity instanceof PlayerEntity)) {
            DynamicDamage.dynamicPlayerCountScaling(livingEntity);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void simplyskills$onEntityTick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.getWorld().isClient && SimplySkills.generalConfig.enableDAS && !(livingEntity instanceof PlayerEntity)
                && (livingEntity.age % (SimplySkills.generalConfig.DASUpdateFrequency * 20) == 0 || livingEntity.age == 20)) {
            DynamicDamage.dynamicPlayerCountScaling(livingEntity);
        }
    }

}
