package net.darkhax.gamestages;

import net.darkhax.gamestages.command.GameStageCommands;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStages implements ModInitializer {
    
    public static final String MOD_ID = "gamestages";
    public static final Logger LOG = LogManager.getLogger("Game Stages");

    // @ClientOnly
    // private void onF3Text (CustomizeGuiOverlayEvent.DebugText event) {
    //
    //     final MinecraftClient mc = MinecraftClient.getInstance();
    //
    //     if (mc.options.debugEnabled) {
    //
    //         if (mc.player.isSneaking()) {
    //
    //             final IStageData data = GameStageHelper.getPlayerData(mc.player);
    //
    //             if (data != null) {
    //
    //                 event.getRight().add(Formatting.GOLD + Formatting.UNDERLINE.toString() + "GameStages");
    //                 event.getRight().add("Count: " + data.getStages().size());
    //                 event.getRight().add("Type: " + data.getClass().getName());
    //                 event.getRight().add("Stages: " + data.getStages().toString());
    //             }
    //         }
    //
    //         else {
    //
    //             event.getRight().add(Formatting.GOLD + Formatting.UNDERLINE.toString() + "GameStages [Shift]");
    //         }
    //     }
    // }

    @Override
    public void onInitialize() {
        GameStageSaveHandler.reloadFakePlayers();
        GameStageSaveHandler.reloadKnownStages();
        GameStageCommands.initializeCommands();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new GameStageReloader());

        ServerPlayConnectionEvents.JOIN.register(GameStageSaveHandler::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(GameStageSaveHandler::onPlayerDisconnect);

    //     if (isClient()) {
    //         MinecraftForge.EVENT_BUS.addListener(this::onF3Text);
    //     }
    }

}