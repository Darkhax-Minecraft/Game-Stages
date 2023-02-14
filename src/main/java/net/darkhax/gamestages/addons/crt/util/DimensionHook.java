package net.darkhax.gamestages.addons.crt.util;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.util.DimensionHook")
@FunctionalInterface
public interface DimensionHook {
    
    @ZenCodeType.Method
    void accept (Player player, ResourceLocation to, ResourceLocation from);
}