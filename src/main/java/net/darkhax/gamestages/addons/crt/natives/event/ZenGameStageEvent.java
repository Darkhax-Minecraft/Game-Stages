package net.darkhax.gamestages.addons.crt.natives.event;

import javax.annotation.Nonnull;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;

import net.darkhax.gamestages.event.GameStageEvent;

@ZenRegister
@NativeTypeRegistration(value = GameStageEvent.class, zenCodeName = "mods.gamestages.events.GameStageEvent")
public class ZenGameStageEvent {
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("stage")
    public static String getStageName (GameStageEvent event) {
        
        return event.getStageName();
    }
    
    @ZenRegister
    @NativeTypeRegistration(value = GameStageEvent.Added.class, zenCodeName = "mods.gamestages.events.GameStageAdded")
    public static class Added {
        
    }
    
    @ZenRegister
    @NativeTypeRegistration(value = GameStageEvent.Removed.class, zenCodeName = "mods.gamestages.events.GameStageRemoved")
    public static class Removed {
        
    }
    
    @ZenRegister
    @NativeTypeRegistration(value = GameStageEvent.Cleared.class, zenCodeName = "mods.gamestages.events.GameStageCleared")
    public static class Cleared {
        
    }
}