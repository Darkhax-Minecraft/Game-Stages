package net.darkhax.gamestages.packet;

import net.minecraft.network.PacketBuffer;

public class NetworkHandlerServer {
    
    public static void encodeStageMessage (MessageStages packet, PacketBuffer buffer) {
        
        buffer.writeInt(packet.getStages().length);
        
        for (final String stageName : packet.getStages()) {
            
            buffer.writeUtf(stageName, 64);
        }
    }
}