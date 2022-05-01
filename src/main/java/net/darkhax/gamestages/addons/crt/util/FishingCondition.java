package net.darkhax.gamestages.addons.crt.util;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.util.FishingCondition")
@FunctionalInterface
public interface FishingCondition {

    @ZenCodeType.Method
    boolean test(Player player, net.minecraft.world.entity.projectile.FishingHook bobber, List<ItemStack> items);
}