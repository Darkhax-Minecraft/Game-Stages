package net.darkhax.gamestages.proxy;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;

public class GameStagesServer {
    
    public IStageData getPlayerData (EntityPlayer player) {
        
        // On server, do basic lookup on player data map.
        return GameStageSaveHandler.getPlayerData(player.getPersistentID());
    }
}