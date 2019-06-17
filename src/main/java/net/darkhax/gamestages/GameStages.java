package net.darkhax.gamestages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.network.NetworkHelper;
import net.darkhax.gamestages.command.GameStageCommands;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.packet.MessageStages;
import net.darkhax.gamestages.packet.NetworkHandlerClient;
import net.darkhax.gamestages.packet.NetworkHandlerServer;
import net.darkhax.gamestages.world.storage.loot.conditions.LootConditionStaged;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod("gamestages")
public class GameStages {
    
    public static final Logger LOG = LogManager.getLogger("Game Stages");
    public static final NetworkHelper NETWORK = new NetworkHelper("gamestages:main", "3.0.x");
    
    public GameStages() {
        
        NETWORK.registerMessage(MessageStages.class, NetworkHandlerServer::encodeStageMessage, t -> NetworkHandlerClient.decodeStageMessage(t), (t, u) -> NetworkHandlerClient.processSyncStagesMessage(t, u));
        GameStageSaveHandler.reloadFakePlayers();
        GameStageSaveHandler.reloadKnownStages();
        LootConditionManager.registerCondition(new LootConditionStaged.Serializer());
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }
    
    @SubscribeEvent
    public void onServerStarting (FMLServerStartingEvent event) {
        
        GameStageCommands.initializeCommands(event.getCommandDispatcher());
    }
}