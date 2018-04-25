package net.darkhax.gamestages.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * This class has been deprecated. It should not be used, because it will not work! This class
 * only exists to maintain compatibility with older worlds.
 * 
 * Please move to the new system in net.darkhax.gamestages.data.
 */
@Deprecated
public class GameStageStorage implements Capability.IStorage<IStageData> {
    
    @Override
    public NBTBase writeNBT (Capability<IStageData> capability, IStageData instance, EnumFacing side) {
        
        final NBTTagCompound tag = new NBTTagCompound();
        
        final NBTTagList tagList = new NBTTagList();
        
        for (final String string : instance.getUnlockedStages()) {
            tagList.appendTag(new NBTTagString(string));
        }
        
        tag.setTag("UnlockedStages", tagList);
        
        return tag;
    }
    
    @Override
    public void readNBT (Capability<IStageData> capability, IStageData instance, EnumFacing side, NBTBase nbt) {
        
        final NBTTagCompound tag = (NBTTagCompound) nbt;
        
        final NBTTagList tagList = tag.getTagList("UnlockedStages", NBT.TAG_STRING);
        
        for (int index = 0; index < tagList.tagCount(); index++) {
            instance.unlockStage(tagList.getStringTagAt(index));
        }
    }
}