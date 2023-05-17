package net.darkhax.gamestages.mixin;

import net.darkhax.gamestages.data.IPlayerDataSaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class PlayerDataSaverMixin implements IPlayerDataSaver {
    private static final String DATA_PATH = "net.darkhax.gamestages.data";
    private CompoundTag persistentData;


    @Override
    public CompoundTag getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new CompoundTag();
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(CompoundTag persistentData) {
        this.persistentData = persistentData;
    }
    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    private void writeNbt(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> ci) {
        if (persistentData != null) {
            nbt.put(DATA_PATH, persistentData);
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void readNbt(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(DATA_PATH, Tag.TAG_COMPOUND)) {
            persistentData = nbt.getCompound(DATA_PATH);
        }
    }
}
