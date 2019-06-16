package net.darkhax.gamestages.packet;

import java.util.function.Supplier;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.data.StageData;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class NetworkHandlerClient {

    public static MessageStages decodeStageMessage (PacketBuffer buffer) {

        final String[] stageNames = new String[buffer.readInt()];

        for (int i = 0; i < stageNames.length; i++) {

            stageNames[i] = buffer.readString(64);
        }

        return new MessageStages(stageNames);
    }

    public static void processSyncStagesMessage (MessageStages message, Supplier<Context> context) {

        // Reset the client data to a new object.
        final IStageData clientData = new StageData();

        // Re-add all stages
        for (final String stageName : message.getStages()) {

            clientData.addStage(stageName);
        }

        GameStageSaveHandler.setClientData(clientData);

        // Alert all the listeners.
        MinecraftForge.EVENT_BUS.post(new StagesSyncedEvent(clientData));
    }
}
