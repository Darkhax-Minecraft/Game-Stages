package net.darkhax.gamestages.proxy;

import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GameStagesClient extends GameStagesServer {
    
    @Override
    public IStageData getPlayerData (EntityPlayer player) {
        
        final EntityPlayerSP clientPlayer = PlayerUtils.getClientPlayerSP();
        
        if (clientPlayer != null && clientPlayer.getUniqueID().equals(player.getUniqueID())) {
            
            return GameStageSaveHandler.clientData;
        }
        
        // A client can not have stage data for other players at this time.
        return GameStageSaveHandler.EMPTY_STAGE_DATA;
    }
}