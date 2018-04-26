package net.darkhax.gamestages.data;

import java.io.File;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameStageSaveHandler {
    
    @SubscribeEvent
    public void onPlayerLoad (PlayerEvent.LoadFromFile event) {
        
    }
    
    @SubscribeEvent
    public void onPlayerSave (PlayerEvent.SaveToFile event) {
        
    }
    
    private File getPlayerFile (File playerDir, String uuid) {
        
        final File saveDir = new File(playerDir, "gamestages");
        
        if (!saveDir.exists()) {
            
            saveDir.mkdirs();
        }
        
        return new File(saveDir, uuid + ".dat");
    }
}