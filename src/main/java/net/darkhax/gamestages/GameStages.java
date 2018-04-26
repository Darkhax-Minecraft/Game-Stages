package net.darkhax.gamestages;

import java.io.File;

import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.command.CommandTree;
import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.bookshelf.world.gamerule.GameRule;
import net.darkhax.gamestages.commands.CommandStageTree;
import net.darkhax.gamestages.data.FakePlayerData;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.packet.PacketSyncClient;
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
        NETWORK.register(PacketSyncClient.class, Side.CLIENT);

        BookshelfRegistry.addCommand(COMMAND);
        GameStageSaveHandler.reloadFromFile();
    }

    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {

        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}