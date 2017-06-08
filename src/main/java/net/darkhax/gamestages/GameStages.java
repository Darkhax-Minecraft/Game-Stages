package net.darkhax.gamestages;

import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.DefaultStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.Storage;
import net.darkhax.gamestages.commands.CommandStageTree;
import net.darkhax.gamestages.packet.PacketRequestClientSync;
import net.darkhax.gamestages.packet.PacketStage;
import net.darkhax.gamestages.packet.PacketStageAll;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "gamestages", name = "Game Stages", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.0.0.381,);")
public class GameStages {

    public static final NetworkHandler NETWORK = new NetworkHandler("gamestages");

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        NETWORK.register(PacketStage.class, Side.CLIENT);
        NETWORK.register(PacketStageAll.class, Side.CLIENT);
        NETWORK.register(PacketRequestClientSync.class, Side.SERVER);
        CapabilityManager.INSTANCE.register(IStageData.class, new Storage(), DefaultStageData.class);
        MinecraftForge.EVENT_BUS.register(new PlayerDataHandler());
    }

    @EventHandler
    public void serverStarting (FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandStageTree());
    }
}