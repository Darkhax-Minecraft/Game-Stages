package net.darkhax.gamestages;

import net.darkhax.gamestages.packet.GameStagesPacketHandler;
import net.minecraft.ChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.gamestages.command.GameStageCommands;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(GameStages.MOD_ID)
public class GameStages {
    
    public static final String MOD_ID = "gamestages";
    public static final Logger LOG = LogManager.getLogger("Game Stages");
    public static final GameStagesPacketHandler NETWORK = new GameStagesPacketHandler();
    
    public GameStages() {

        GameStageSaveHandler.reloadFakePlayers();
        GameStageSaveHandler.reloadKnownStages();
        GameStageCommands.initializeCommands();
        
        if (FMLEnvironment.dist.isClient()) {
            
            MinecraftForge.EVENT_BUS.addListener(this::onF3Text);
        }
        
        MinecraftForge.EVENT_BUS.addListener(this::registerReloadListeners);
    }
    
    private void registerReloadListeners(AddReloadListenerEvent event) {
        
        event.addListener(new GameStageReloader());
    }
    
    @OnlyIn(Dist.CLIENT)
    private void onF3Text (RenderGameOverlayEvent.Text event) {
        
        final Minecraft mc = Minecraft.getInstance();
        
        if (mc.options.renderDebug) {
            
            if (mc.player.isShiftKeyDown()) {
                
                final IStageData data = GameStageHelper.getPlayerData(mc.player);
                
                if (data != null) {
                    
                    event.getRight().add(ChatFormatting.GOLD + ChatFormatting.UNDERLINE.toString() + "GameStages");
                    event.getRight().add("Count: " + data.getStages().size());
                    event.getRight().add("Type: " + data.getClass().getName());
                    event.getRight().add("Stages: " + data.getStages().toString());
                }
            }
            
            else {
                
                event.getRight().add(ChatFormatting.GOLD + ChatFormatting.UNDERLINE.toString() + "GameStages [Shift]");
            }
        }
    }
}