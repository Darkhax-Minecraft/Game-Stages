package net.darkhax.gamestages.packet;

import net.darkhax.gamestages.GameStages;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageStages {
    private final List<String> stages;
    
    public MessageStages(Collection<String> stages) {
        
        this.stages = new ArrayList<>(stages);
    }
    
    public List<String> getStages () {
        
        return this.stages;
    }
}
