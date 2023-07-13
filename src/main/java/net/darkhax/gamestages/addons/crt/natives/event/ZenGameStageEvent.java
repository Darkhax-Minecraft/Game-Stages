package net.darkhax.gamestages.addons.crt.natives.event;

import javax.annotation.Nonnull;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.event.ZenEvent;
import com.blamejared.crafttweaker.api.event.bus.ForgeEventBusWire;
import com.blamejared.crafttweaker.api.event.bus.IEventBus;
import com.blamejared.crafttweaker.natives.event.item.ExpandItemTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.openzen.zencode.java.ZenCodeType;

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
    @ZenEvent
    @NativeTypeRegistration(value = GameStageEvent.Added.class, zenCodeName = "mods.gamestages.events.GameStageAdded")
    public static class Added {

        @ZenEvent.Bus
        public static final IEventBus<GameStageEvent.Added> BUS = IEventBus.direct(GameStageEvent.Added.class, ForgeEventBusWire.of());
    }
    
    @ZenRegister
    @ZenEvent
    @NativeTypeRegistration(value = GameStageEvent.Removed.class, zenCodeName = "mods.gamestages.events.GameStageRemoved")
    public static class Removed {

        @ZenEvent.Bus
        public static final IEventBus<GameStageEvent.Removed> BUS = IEventBus.direct(GameStageEvent.Removed.class, ForgeEventBusWire.of());
    }
    
    @ZenRegister
    @ZenEvent
    @NativeTypeRegistration(value = GameStageEvent.Cleared.class, zenCodeName = "mods.gamestages.events.GameStageCleared")
    public static class Cleared {

        @ZenEvent.Bus
        public static final IEventBus<GameStageEvent.Cleared> BUS = IEventBus.direct(GameStageEvent.Cleared.class, ForgeEventBusWire.of());
    }
}