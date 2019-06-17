package net.darkhax.gamestages.packet;

import java.util.Collection;

public class MessageStages {
    
    private final String[] stages;
    
    public MessageStages(String... stages) {
        
        this.stages = stages;
    }
    
    public MessageStages(Collection<String> stages) {
        
        this(stages.toArray(new String[0]));
    }
    
    public String[] getStages () {
        
        return this.stages;
    }
}
