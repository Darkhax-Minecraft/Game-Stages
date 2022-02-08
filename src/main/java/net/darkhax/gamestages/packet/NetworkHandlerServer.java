package net.darkhax.gamestages.packet;

import net.minecraft.network.FriendlyByteBuf;

public class NetworkHandlerServer {
    
    public static void encodeStageMessage (MessageStages packet, FriendlyByteBuf buffer) {
        
        buffer.writeInt(packet.getStages().length);
        
        for (final String stageName : packet.getStages()) {
            
            buffer.writeUtf(stageName, 64);
        }
    }
}