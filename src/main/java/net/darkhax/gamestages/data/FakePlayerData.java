package net.darkhax.gamestages.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.config.Configuration;
import net.minecraft.nbt.NBTTagCompound;

public class FakePlayerData implements IStageData {
    
    /**
     * The default fake player data.
     */
    public static final FakePlayerData DEFAULT = new FakePlayerData("DEFAULT", Collections.emptySet());
    
    private Set<String> stages = new HashSet<>();
    private String fakePlayerName;
    
    public FakePlayerData (final String fakePlayerName, final Set<String> stages) {
        
        this.stages.addAll(stages);
        this.fakePlayerName = fakePlayerName;
    }
    
    @Override
    public Collection<String> getStages () {
        
        return this.stages;
    }
    
    @Override
    public boolean hasStage (@Nonnull final String stage) {
        
    	final boolean hasStage = this.stages.contains(stage);
    	
    	if (Configuration.debug.logDebug && !hasStage) {
    		
    		GameStages.LOG.info("Fake player {} does not have stage {}. Thay do have {}.", fakePlayerName, stage, this.stages);
    	}
    	
        return hasStage;
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
    public void readFromNBT (NBTTagCompound tag) {
        
        // unsupported
    }
    
    @Override
    public NBTTagCompound writeToNBT () {
        
        // unsupported
        return null;
    }
    
    public String getFakePlayerName () {
        
        return this.fakePlayerName;
    }
}
