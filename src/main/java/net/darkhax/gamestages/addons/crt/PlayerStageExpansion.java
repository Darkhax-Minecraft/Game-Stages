package net.darkhax.gamestages.addons.crt;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class PlayerStageExpansion {

    // Checks if the player has the passed stage.
    @ZenMethod
    public static boolean hasGameStage (IPlayer player, String stage) {

        final EntityPlayer actualPlayer = CraftTweakerMC.getPlayer(player);
        return GameStageHelper.hasStage(actualPlayer, stage);
    }

    // Checks if the player has any of the passed stages. They only need one.
    @ZenMethod
    public static boolean hasAnyGameStages (IPlayer player, String... stages) {

        final EntityPlayer actualPlayer = CraftTweakerMC.getPlayer(player);
        return GameStageHelper.hasAnyOf(actualPlayer, stages);
    }

    // Checks if the player has all of the passed stages.
    @ZenMethod
    public static boolean hasAllGameStages (IPlayer player, String... stages) {

        final EntityPlayer actualPlayer = CraftTweakerMC.getPlayer(player);
        return GameStageHelper.hasAllOf(actualPlayer, stages);
    }

    // Unlocks a stage for a player.
    @ZenMethod
    public static void addGameStage (IPlayer player, String stage) {

        final EntityPlayer actualPlayer = CraftTweakerMC.getPlayer(player);
        GameStageHelper.addStage(actualPlayer, stage);
    }

    // Locks a stage for a player.
    @ZenMethod
    public static void removeGameStage (IPlayer player, String stage) {

        final EntityPlayer actualPlayer = CraftTweakerMC.getPlayer(player);
        GameStageHelper.removeStage(actualPlayer, stage);
    }
}
