package net.darkhax.gamestages.addons.crt.util;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.util.DimensionCondition")
@FunctionalInterface
public interface DimensionCondition {
    
    @ZenCodeType.Method
    boolean test (PlayerEntity player, ResourceLocation to, ResourceLocation from);
}