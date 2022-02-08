package net.darkhax.gamestages.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

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
    public void readFromNBT (CompoundTag tag) {
        
        final ListTag list = tag.getList(TAG_STAGES, Tag.TAG_STRING);
        
        for (int tagIndex = 0; tagIndex < list.size(); tagIndex++) {
            
            this.addStage(list.getString(tagIndex));
        }
    }
    
    @Override
    public CompoundTag writeToNBT () {
        
        final CompoundTag tag = new CompoundTag();
        
        final ListTag list = new ListTag();
        
        for (final String stage : this.unlockedStages) {
            
            list.add(StringTag.valueOf(stage));
        }
        
        tag.put(TAG_STAGES, list);
        
        return tag;
    }
    
    @Override
    public String toString () {
        
        return "StageData [unlockedStages=" + this.unlockedStages + "]";
    }
}