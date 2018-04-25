package net.darkhax.gamestages.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class GameStageCapProvider implements ICapabilitySerializable<NBTTagCompound> {
    
    IStageData instance = PlayerDataHandler.CAPABILITY.getDefaultInstance();
    
    public GameStageCapProvider (EntityPlayer player) {
        
        this.instance.setPlayer(player);
    }
    
    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        
        return capability == PlayerDataHandler.CAPABILITY;
    }
    
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        
        return this.hasCapability(capability, facing) ? PlayerDataHandler.CAPABILITY.<T> cast(this.instance) : null;
    }
    
    @Override
    public NBTTagCompound serializeNBT () {
        
        return (NBTTagCompound) PlayerDataHandler.CAPABILITY.getStorage().writeNBT(PlayerDataHandler.CAPABILITY, this.instance, null);
    }
    
    @Override
    public void deserializeNBT (NBTTagCompound nbt) {
        
        PlayerDataHandler.CAPABILITY.getStorage().readNBT(PlayerDataHandler.CAPABILITY, this.instance, null, nbt);
    }
}