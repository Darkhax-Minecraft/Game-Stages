package net.darkhax.gamestages.addons.crt;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

@ZenRegister
@ZenCodeType.Expansion("crafttweaker.api.player.MCPlayerEntity")
public class ExpandPlayerEntity {
    
    @ZenCodeType.Method
    public static void addGameStage(PlayerEntity player, String stage) {
    
        if (player instanceof ServerPlayerEntity) {
            
            GameStageHelper.addStage((ServerPlayerEntity) player, stage);
        }
    }
    
    @ZenCodeType.Method
    public static void removeGameStage(PlayerEntity player, String stage) {
    
        if (player instanceof ServerPlayerEntity) {
            
            GameStageHelper.removeStage((ServerPlayerEntity) player, stage);
        }
    }
    
    @ZenCodeType.Method
    public static void clearGameStages(PlayerEntity player) {
    
        if (player instanceof ServerPlayerEntity) {
            
            GameStageHelper.clearStages((ServerPlayerEntity) player);
        }
    }
    
    @ZenCodeType.Method
    public static boolean hasGameStage(PlayerEntity player, String stage) {
    
        return GameStageHelper.hasStage(player, stage);
    }
    
    @ZenCodeType.Method
    public static boolean hasAnyGameStages(PlayerEntity player, String... stage) {
    
        return GameStageHelper.hasAnyOf(player, stage);
    }
    
    @ZenCodeType.Method
    public static boolean hasAllGameStages(PlayerEntity player, String... stage) {
    
        return GameStageHelper.hasAllOf(player, stage);
    }
}