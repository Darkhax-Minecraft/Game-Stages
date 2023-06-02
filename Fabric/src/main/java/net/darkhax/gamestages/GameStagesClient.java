package net.darkhax.gamestages;

import net.darkhax.gamestages.packet.GameStagesClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GameStagesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameStagesClientPacketHandler.registerS2CPackets();
    }
}
