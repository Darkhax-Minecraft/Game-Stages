package net.darkhax.gamestages.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.darkhax.bookshelf.util.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public class StageData implements IStageData {

    public static final String TAG_STAGES = "Stages";
    private final Set<String> unlockedStages = new HashSet<>();

    @Override
    public Collection<String> getStages () {

        return Collections.unmodifiableCollection(this.unlockedStages);
    }

    @Override
    public boolean hasStage (String stage) {

        return this.unlockedStages.contains(stage.toLowerCase());
    }

    @Override
    public void addStage (String stage) {

        this.unlockedStages.add(stage.toLowerCase());
    }

    @Override
    public void removeStage (String stage) {

        this.unlockedStages.remove(stage.toLowerCase());
    }

    @Override
    public void clear () {

        this.unlockedStages.clear();
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {

        final Collection<String> stages = NBTUtils.readCollection(new ArrayList<>(), tag.getTagList(TAG_STAGES, NBT.TAG_STRING), stage -> stage);

        for (final String stage : stages) {

            this.addStage(stage);
        }
    }

    @Override
    public NBTTagCompound writeToNBT () {

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(TAG_STAGES, NBTUtils.writeCollection(this.getStages(), stage -> stage));
        return tag;
    }
}