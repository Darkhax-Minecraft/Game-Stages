package net.darkhax.gamestages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.network.NetworkHelper;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.ModUtils;
import net.darkhax.gamestages.addons.crt.CraftTweakerEventSubscription;
import net.darkhax.gamestages.command.GameStageCommands;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.packet.MessageStages;
import net.darkhax.gamestages.packet.NetworkHandlerClient;
import net.darkhax.gamestages.packet.NetworkHandlerServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GameStages.MOD_ID)
public class GameStages {
    
    public static final String MOD_ID = "gamestages";
    public static final Logger LOG = LogManager.getLogger("Game Stages");
    public static final NetworkHelper NETWORK = new NetworkHelper("gamestages:main", "7.0.x");
    private static final RegistryHelper REGISTRY = new RegistryHelper("gamestages", LOG);
    
    public GameStages() {
        
        NETWORK.registerEnqueuedMessage(MessageStages.class, NetworkHandlerServer::encodeStageMessage, t -> NetworkHandlerClient.decodeStageMessage(t), (t, u) -> NetworkHandlerClient.processSyncStagesMessage(t, u));
        GameStageSaveHandler.reloadFakePlayers();
        GameStageSaveHandler.reloadKnownStages();
        GameStageCommands.initializeCommands(REGISTRY);
        REGISTRY.initialize(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }
    
    private void setup (FMLCommonSetupEvent event) {
        
        if (ModUtils.isInModList("crafttweaker")) {
            
            MinecraftForge.EVENT_BUS.register(CraftTweakerEventSubscription.class);
        }
    }
}