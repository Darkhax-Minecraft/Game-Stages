package net.darkhax.gamestages;

import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.command.CommandTree;
import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.gamestages.commands.CommandStageTree;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.packet.PacketSyncClient;
import net.darkhax.gamestages.proxy.GameStagesServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "gamestages", name = "Game Stages", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.2.458,);", certificateFingerprint = "@FINGERPRINT@")
public class GameStages {
    
    public static final LoggingHelper LOG = new LoggingHelper("gamestages");
    public static final NetworkHandler NETWORK = new NetworkHandler("gamestages");
    public static final CommandTree COMMAND = new CommandStageTree();
    
    public static final String CLIENT_PROXY_CLASS = "net.darkhax.gamestages.proxy.GameStagesClient";
    public static final String SERVER_PROXY_CLASS = "net.darkhax.gamestages.proxy.GameStagesServer";
    @SidedProxy(clientSide = GameStages.CLIENT_PROXY_CLASS, serverSide = GameStages.SERVER_PROXY_CLASS)
    public static GameStagesServer proxy;
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        // Packets
        NETWORK.register(PacketSyncClient.class, Side.CLIENT);
        
        BookshelfRegistry.addCommand(COMMAND);
        GameStageSaveHandler.reloadFakePlayers();
    }
}