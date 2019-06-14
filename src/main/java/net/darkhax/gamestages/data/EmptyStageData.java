package net.darkhax.gamestages.data;

import java.util.Collection;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;

/**
 * An implementation of IStageData which will always have no stages. This allows for memory
 * usage improvements in some situations.
 */
public class EmptyStageData implements IStageData {

    @Override
    public Collection<String> getStages () {

        return Collections.emptyList();
    }

    @Override
    public boolean hasStage (String stage) {

        return false;
    }

    @Override
    public void addStage (String stage) {

        // Can not add stage.
    }

    @Override
    public void removeStage (String stage) {

        // Can not remove stage
    }

    @Override
    public void clear () {

        // Can not clear stage
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {

        // Can not read stage
    }

    @Override
    public NBTTagCompound writeToNBT () {

        return new NBTTagCompound();
    }
}