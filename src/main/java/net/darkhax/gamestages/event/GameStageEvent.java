package net.darkhax.gamestages.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GameStageEvent extends Event {
    
    private final EntityPlayer player;
    private String stageName;
    
    public GameStageEvent (EntityPlayer player, String stageName) {
        
        this.player = player;
        this.stageName = stageName;
    }

    public EntityPlayer getPlayer () {
        
        return player;
    }

    public String getStageName () {
        
        return stageName;
    }
    
    public void setStage(String stage) {
        
        this.stageName = stage;
    }
    
    @Cancelable
    public static class Add extends GameStageEvent {

        public Add (EntityPlayer player, String stageName) {
            
            super(player, stageName);
        }
    }
    
    @Cancelable
    public static class Remove extends GameStageEvent {

        public Remove (EntityPlayer player, String stageName) {
            
            super(player, stageName);
        }
    }
    
    @Cancelable
    public static class Check extends GameStageEvent {

        public Check (EntityPlayer player, String stageName) {
            
            super(player, stageName);
        }
    }
}