package net.darkhax.gamestages.packet;

import java.util.Collection;
import java.util.function.Supplier;

import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.data.StageData;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSyncClient {

    private final String[] stages;

    public PacketSyncClient (String... stages) {

        this.stages = stages;
    }

    public PacketSyncClient (Collection<String> stages) {

        this(stages.toArray(new String[0]));
    }

    public static void encodeMessage (PacketSyncClient packet, PacketBuffer buffer) {

        buffer.writeInt(packet.stages.length);

        for (final String stageName : packet.stages) {

            buffer.writeString(stageName, 64);
        }
    }

    public static PacketSyncClient decodeMessage (PacketBuffer buffer) {

        final String[] stageNames = new String[buffer.readInt()];

        for (int i = 0; i < stageNames.length; i++) {

            stageNames[i] = buffer.readString(64);
        }

        return new PacketSyncClient(stageNames);
    }

    public static void proccessMessage (PacketSyncClient packet, Supplier<Context> context) {

        // Reset the client data to a new object.
        final IStageData clientData = new StageData();

        // Re-add all stages
        for (final String stageName : packet.stages) {

            clientData.addStage(stageName);
        }

        GameStageSaveHandler.setClientData(clientData);

        // Allert all the listeners.
        MinecraftForge.EVENT_BUS.post(new StagesSyncedEvent(clientData, PlayerUtils.getClientPlayer()));
    }
}
