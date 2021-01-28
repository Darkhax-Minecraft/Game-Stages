package net.darkhax.gamestages.addons.crt;

import com.blamejared.crafttweaker.impl.commands.script_examples.ExampleCollectionEvent;

import net.darkhax.gamestages.GameStages;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CraftTweakerEventSubscription {
    
    @SubscribeEvent
    public static void addExampleScriptFiles (ExampleCollectionEvent event) {
        
        event.addResource(new ResourceLocation(GameStages.MOD_ID, "gamestages/stage_on_join"));
    }
}