package net.darkhax.gamestages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.network.NetworkHelper;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.gamestages.command.GameStageCommands;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.packet.MessageStages;
import net.darkhax.gamestages.packet.NetworkHandlerClient;
import net.darkhax.gamestages.packet.NetworkHandlerServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("gamestages")
public class GameStages {
    
    public static final Logger LOG = LogManager.getLogger("Game Stages");
    public static final NetworkHelper NETWORK = new NetworkHelper("gamestages:main", "6.0.x");
    private static final RegistryHelper REGISTRY = new RegistryHelper("gamestages", LOG);
    
    public GameStages() {
        
        NETWORK.registerEnqueuedMessage(MessageStages.class, NetworkHandlerServer::encodeStageMessage, t -> NetworkHandlerClient.decodeStageMessage(t), (t, u) -> NetworkHandlerClient.processSyncStagesMessage(t, u));
        GameStageSaveHandler.reloadFakePlayers();
        GameStageSaveHandler.reloadKnownStages();
        GameStageCommands.initializeCommands(REGISTRY);
        REGISTRY.initialize(FMLJavaModLoadingContext.get().getModEventBus());
    }
}