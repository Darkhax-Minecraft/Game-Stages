package net.darkhax.gamestages.addons.crt;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.openzen.zencode.java.ZenCodeType;

import net.darkhax.gamestages.GameStageHelper;

@ZenRegister
@ZenCodeType.Expansion("crafttweaker.api.entity.type.player.Player")
public class ExpandPlayer {
    
    @ZenCodeType.Method
    public static void addGameStage (Player player, String... stages) {

        LivingEvent
        if (player instanceof ServerPlayer) {
            
            GameStageHelper.addStage((ServerPlayer) player, stages);
        }
    }
    
    @ZenCodeType.Method
    public static void removeGameStage (Player player, String... stages) {
        
        if (player instanceof ServerPlayer) {
            
            GameStageHelper.removeStage((ServerPlayer) player, stages);
        }
    }
    
    @ZenCodeType.Method
    public static void clearGameStages (Player player) {
        
        if (player instanceof ServerPlayer) {
            
            GameStageHelper.clearStages((ServerPlayer) player);
        }
    }
    
    @ZenCodeType.Method
    public static boolean hasGameStage (Player player, String stage) {
        
        return GameStageHelper.hasStage(player, stage);
    }
    
    @ZenCodeType.Method
    public static boolean hasAnyGameStages (Player player, String... stage) {
        
        return GameStageHelper.hasAnyOf(player, stage);
    }
    
    @ZenCodeType.Method
    public static boolean hasAllGameStages (Player player, String... stage) {
        
        return GameStageHelper.hasAllOf(player, stage);
    }
}