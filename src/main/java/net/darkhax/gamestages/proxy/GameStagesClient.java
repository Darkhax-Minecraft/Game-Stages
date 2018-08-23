package net.darkhax.gamestages.proxy;

import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GameStagesClient extends GameStagesServer {
    
    @Override
    public IStageData getPlayerData (EntityPlayer player) {
        
        // If player is the client player return the client data. Otherwise give empty data.
        return player == PlayerUtils.getClientPlayer() ? GameStageSaveHandler.clientData : GameStageSaveHandler.EMPTY_STAGE_DATA;
    }
}