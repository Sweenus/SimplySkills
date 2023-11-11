package net.sweenus.simplyskills.entities;

import com.google.common.base.Suppliers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SpellTargetEntity extends Entity {
    public SpellTargetEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public static final Supplier<EntityType<SpellTargetEntity>> TYPE = Suppliers.memoize(() ->
            EntityType.Builder.create(SpellTargetEntity::new, SpawnGroup.MISC).build("spell_target_entity"));
    public static int lifetime = 120;

    @Override
    public void baseTick() {
        this.setNoGravity(true);
        if (this.age > lifetime)
            this.discard();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }
    @Override
    public boolean shouldRender(double distance) {
        return false;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
