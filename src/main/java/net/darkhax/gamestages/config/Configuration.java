package net.darkhax.gamestages.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
@Config(modid = "gamestages")
public class Configuration {
    public static Debug debug = new Debug();
    
    @SubscribeEvent
    public static void onConfigChangedEvent (ConfigChangedEvent.OnConfigChangedEvent event) {
        
        if (!event.getModID().equalsIgnoreCase("gamestages")) {
            return;
        }
        ConfigManager.sync("gamestages", Config.Type.INSTANCE);
    }
    
    public static class Debug {
        @Config.Comment("Debug logging for any kind of syncing or changes to a players stage.")
        @Config.Name("Log Debug Data")
        public boolean logDebug = true;
    }
}
