package net.darkhax.gamestages.addons.crt.util;

import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.util.FishingHook")
@FunctionalInterface
public interface FishingHook {

    @ZenCodeType.Method
    void accept (PlayerEntity player, FishingBobberEntity bobber, List<IItemStack> items);
}