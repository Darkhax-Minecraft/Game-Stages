package net.darkhax.gamestages.data;

import net.minecraft.nbt.CompoundTag;

public interface IPlayerDataSaver {
    CompoundTag getPersistentData();
    void setPersistentData(CompoundTag persistentData);
}
