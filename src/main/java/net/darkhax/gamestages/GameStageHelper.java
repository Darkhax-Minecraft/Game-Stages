package net.darkhax.gamestages;

import java.util.Arrays;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.gamestages.packet.PacketSyncClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;

public class GameStageHelper {

    public static boolean hasStage (EntityPlayer player, String stage) {

        return hasStage(player, getPlayerData(player), stage);
    }

    public static boolean hasStage (EntityPlayer player, IStageData data, String stage) {

        final GameStageEvent.Check event = new GameStageEvent.Check(player, stage, data.hasStage(stage));
        MinecraftForge.EVENT_BUS.post(event);
        return event.hasStage();
    }

    public static void addStage (EntityPlayer player, String stage) {

        if (!MinecraftForge.EVENT_BUS.post(new GameStageEvent.Add(player, stage))) {

            getPlayerData(player).addStage(stage);
            MinecraftForge.EVENT_BUS.post(new GameStageEvent.Added(player, stage));
        }
    }

    public static void removeStage (EntityPlayer player, String stage) {

        if (!MinecraftForge.EVENT_BUS.post(new GameStageEvent.Remove(player, stage))) {

            getPlayerData(player).removeStage(stage);
            MinecraftForge.EVENT_BUS.post(new GameStageEvent.Removed(player, stage));
        }
    }

    public static boolean hasAnyOf (EntityPlayer player, String... stages) {

        final IStageData playerData = getPlayerData(player);
        return Arrays.stream(stages).anyMatch(stage -> hasStage(player, playerData, stage));
    }

    public static boolean hasAllOf (EntityPlayer player, String... stages) {

        final IStageData playerData = getPlayerData(player);
        return Arrays.stream(stages).allMatch(stage -> hasStage(player, playerData, stage));
    }

    public static IStageData getPlayerData (EntityPlayer player) {

        if (player instanceof FakePlayer) {

            // TODO
        }

        return GameStageSaveHandler.getPlayerData(player.getPersistentID().toString());
    }

    public static void syncPlayer (EntityPlayerMP player) {

        final IStageData info = GameStageHelper.getPlayerData(player);
        GameStages.LOG.info("Syncing data for {}.", player.getName());
        GameStages.NETWORK.sendTo(new PacketSyncClient(info.getStages()), player);
    }
}