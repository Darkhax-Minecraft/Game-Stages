package net.darkhax.gamestages.addons.crt.util;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.util.FishingHook")
@FunctionalInterface
public interface FishingHook {

    @ZenCodeType.Method
    void accept(Player player, net.minecraft.world.entity.projectile.FishingHook bobber, List<ItemStack> items);
}