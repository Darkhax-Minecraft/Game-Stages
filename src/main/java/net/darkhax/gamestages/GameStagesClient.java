package net.darkhax.gamestages;

import net.darkhax.gamestages.packet.GameStagesClientPacketHandler;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@ClientOnly
public class GameStagesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        GameStagesClientPacketHandler.registerS2CPackets();
    }
}
