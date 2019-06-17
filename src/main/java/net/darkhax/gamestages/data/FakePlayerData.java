package net.darkhax.gamestages.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;

public class FakePlayerData implements IStageData {
    
    /**
     * The default fake player data.
     */
    public static final FakePlayerData DEFAULT = new FakePlayerData("DEFAULT", Collections.emptySet());
    
    private final Set<String> stages = new HashSet<>();
    private final String fakePlayerName;
    
    public FakePlayerData(final String fakePlayerName, final Set<String> stages) {
        
        this.stages.addAll(stages);
        this.fakePlayerName = fakePlayerName;
    }
    
    @Override
    public Collection<String> getStages () {
        
        return this.stages;
    }
    
    @Override
    public boolean hasStage (@Nonnull final String stage) {
        
        return this.stages.contains(stage);
    }
    
    @Override
    public void addStage (@Nonnull final String stage) {
        
        // unsupported
    }
    
    @Override
    public void removeStage (@Nonnull final String stage) {
        
        // unsupported
    }
    
    @Override
    public void clear () {
        
        // unsupported
    }
    
    @Override
    public void readFromNBT (CompoundNBT tag) {
        
        // unsupported
    }
    
    @Override
    public CompoundNBT writeToNBT () {
        
        // unsupported
        return null;
    }
    
    public String getFakePlayerName () {
        
        return this.fakePlayerName;
    }
}
