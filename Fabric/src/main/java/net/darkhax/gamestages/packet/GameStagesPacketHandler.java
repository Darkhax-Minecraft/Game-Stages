package net.darkhax.gamestages.packet;

import net.darkhax.gamestages.GameStages;
import net.minecraft.resources.ResourceLocation;

public abstract class GameStagesPacketHandler {
    protected static final ResourceLocation CHANNEL = new ResourceLocation(GameStages.MOD_ID, "main");
}
