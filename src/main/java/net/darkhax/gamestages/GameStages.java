package net.darkhax.gamestages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.network.NetworkHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.packet.PacketSyncClient;
import net.darkhax.gamestages.world.storage.loot.conditions.LootConditionStaged;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.fml.common.Mod;

@Mod("gamestages")
public class GameStages {

    public static final Logger LOG = LogManager.getLogger("Game Stages");
    public static final NetworkHelper NETWORK = new NetworkHelper("gamestages:main", "3.0.x");

    public GameStages () {

        // Packets
        NETWORK.registerEnqueuedMessage(PacketSyncClient.class, PacketSyncClient::encodeMessage, PacketSyncClient::decodeMessage, PacketSyncClient::proccessMessage);

        // BookshelfRegistry.addCommand(COMMAND);
        GameStageSaveHandler.reloadFakePlayers();

        LootConditionManager.registerCondition(new LootConditionStaged.Serializer());
    }
}