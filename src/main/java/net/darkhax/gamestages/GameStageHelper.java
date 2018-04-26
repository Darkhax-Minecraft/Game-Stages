package net.darkhax.gamestages;

import java.util.Arrays;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;

public class GameStageHelper {
    
    public static boolean hasStage(EntityPlayer player, String stage) {
        
        return hasStage(getPlayerData(player), stage);
    }
    
    public static boolean hasStage(IStageData data, String stage) {
        
        return data.hasStage(stage);
    }
    
    public static void addStage(EntityPlayer player, String stage) {
        
        getPlayerData(player).addStage(stage);
    }
    
    public static void removeStage(EntityPlayer player, String stage) {
        
        getPlayerData(player).removeStage(stage);
    }
    
    public static boolean hasAnyOf(EntityPlayer player, String... stages) {
        
        final IStageData playerData = getPlayerData(player);
        return Arrays.stream(stages).anyMatch(stage -> hasStage(playerData, stage));
    }
    
    public static boolean hasAllOf(EntityPlayer player, String... stages) {
        
        final IStageData playerData = getPlayerData(player);
        return Arrays.stream(stages).allMatch(stage -> hasStage(playerData, stage));
    }
    
    public static IStageData getPlayerData (EntityPlayer player) {

        if (player instanceof FakePlayer) {

            // TODO
        }

        return GameStageSaveHandler.getPlayerData(player.getPersistentID().toString());
    }
}