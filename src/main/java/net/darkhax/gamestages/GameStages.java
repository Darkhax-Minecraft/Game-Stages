package net.darkhax.gamestages;

import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.command.CommandTree;
import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.bookshelf.world.gamerule.GameRule;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.DefaultStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.Storage;
import net.darkhax.gamestages.commands.CommandStageTree;
import net.darkhax.gamestages.packet.PacketRequestClientSync;
import net.darkhax.gamestages.packet.PacketStage;
import net.darkhax.gamestages.packet.PacketSyncClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "gamestages", name = "Game Stages", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.2.458,);", certificateFingerprint = "@FINGERPRINT@")
public class GameStages {

    public static final LoggingHelper LOG = new LoggingHelper("gamestages");
    public static final NetworkHandler NETWORK = new NetworkHandler("gamestages");
    public static final CommandTree COMMAND = new CommandStageTree();

    // Unimplemented
    public static final GameRule GAME_RULE_SHARE_STAGES = new GameRule("shareGameStages", false);

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        // Packets
        NETWORK.register(PacketStage.class, Side.CLIENT);
        NETWORK.register(PacketSyncClient.class, Side.CLIENT);
        NETWORK.register(PacketRequestClientSync.class, Side.SERVER);

        CapabilityManager.INSTANCE.register(IStageData.class, new Storage(), DefaultStageData::new);
        MinecraftForge.EVENT_BUS.register(new PlayerDataHandler());
        BookshelfRegistry.addCommand(COMMAND);
    }

    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {

        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}