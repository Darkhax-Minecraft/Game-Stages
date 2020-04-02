package net.darkhax.gamestages.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
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
    public void readFromNBT (CompoundNBT tag) {
        
        final ListNBT list = tag.getList(TAG_STAGES, NBT.TAG_STRING);
        
        for (int tagIndex = 0; tagIndex < list.size(); tagIndex++) {
            
            this.addStage(list.getString(tagIndex));
        }
    }
    
    @Override
    public CompoundNBT writeToNBT () {
        
        final CompoundNBT tag = new CompoundNBT();
        
        final ListNBT list = new ListNBT();
        
        for (final String stage : this.unlockedStages) {
            
            list.add(StringNBT.valueOf(stage));
        }
        
        tag.put(TAG_STAGES, list);
        
        return tag;
    }
}