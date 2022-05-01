package net.darkhax.gamestages.event;

import net.darkhax.gamestages.data.IStageData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;

@OnlyIn(Dist.CLIENT)
public class StagesSyncedEvent extends PlayerEvent {
    
    private final IStageData data;
    
    public StagesSyncedEvent(IStageData data) {
        
        this(data, Minecraft.getInstance().player);
    }
    
    public StagesSyncedEvent(IStageData data, Player player) {
        
        super(player);
        this.data = data;
    }
    
    public IStageData getData () {
        
        return this.data;
    }
}
