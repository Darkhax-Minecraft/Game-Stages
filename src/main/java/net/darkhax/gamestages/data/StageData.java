package net.darkhax.gamestages.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StageData implements IStageData {
    
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
}