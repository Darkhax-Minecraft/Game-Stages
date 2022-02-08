package net.darkhax.gamestages.addons.crt;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.blamejared.crafttweaker.api.event.CTEventManager;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.tag.MCTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.addons.crt.util.DimensionCondition;
import net.darkhax.gamestages.addons.crt.util.DimensionHook;
import net.darkhax.gamestages.data.IStageData;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent.LevelChange;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.StageHelper")
public class StageHelper {
    
    @ZenCodeType.Method
    public static void grantStageOnKill (MCEntityType type, String... stages) {

        grantStageOnKill(type, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnKill (MCEntityType type, @Nullable BiConsumer<Player, LivingEntity> hook, String... stages) {
        
        grantStageOnKill( (p, t) -> type.getInternal() == t.getType(), hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnKill (MCTag<MCEntityType> tag, String... stages) {
        
        grantStageOnKill(tag, null, stages);
    }
    
    @ZenCodeType.Method
    @SuppressWarnings("unchecked")
    public static void grantStageOnKill (MCTag<MCEntityType> tag, @Nullable BiConsumer<Player, LivingEntity> hook, String... stages) {
        grantStageOnKill( (p, t) -> tag.getInternalRaw().contains(t.getType()), hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnKill (@Nullable BiPredicate<Player, LivingEntity> condition, String... stages) {
        
        grantStageOnKill(condition, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnKill (@Nullable BiPredicate<Player, LivingEntity> condition, @Nullable BiConsumer<Player, LivingEntity> hook, String... stages) {
        
        CTEventManager.register(LivingDeathEvent.class, event -> {
            
            final DamageSource source = event.getSource();
            
            if (source != null) {
                
                final Entity killer = source.getEntity();
                final LivingEntity target = event.getEntityLiving();
                
                if (killer instanceof ServerPlayer && target != null) {
                    
                    final ServerPlayer player = (ServerPlayer) killer;
                    
                    if (condition == null || condition.test(player, target)) {
                        
                        if (grantStages(player, stages) && hook != null) {
                            
                            hook.accept(player, target);
                        }
                    }
                }
            }
        });
    }
    
    @ZenCodeType.Method
    public static void grantStageOnDimension (String[] targets, String... stages) {
        
        grantStageOnDimension(targets, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnDimension (String[] targets, @Nullable DimensionHook hook, String... stages) {
        
        grantStageOnDimension( (p, t, f) -> Arrays.stream(targets).anyMatch(target -> target.equalsIgnoreCase(t.toString())), hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnDimension (String targetTo, String... stages) {
        
        grantStageOnDimension(targetTo, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnDimension (String targetTo, @Nullable DimensionHook hook, String... stages) {
        
        grantStageOnDimension( (p, t, f) -> targetTo.equalsIgnoreCase(t.toString()), hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnDimension (@Nullable DimensionCondition condition, String... stages) {
        
        StageHelper.grantStageOnDimension(condition, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnDimension (@Nullable DimensionCondition condition, @Nullable DimensionHook hook, String... stages) {
        
        CTEventManager.register(PlayerEvent.PlayerChangedDimensionEvent.class, event -> {
            
            final ResourceLocation toDim = event.getTo().location();
            final ResourceLocation fromDim = event.getFrom().location();
            
            if (event.getPlayer() instanceof ServerPlayer && (condition == null || condition.test(event.getPlayer(), toDim, fromDim))) {
                
                final ServerPlayer player = (ServerPlayer) event.getPlayer();
                
                if (grantStages(player, stages) && hook != null) {
                    
                    hook.accept(player, toDim, fromDim);
                }
            }
        });
    }
    
    @ZenCodeType.Method
    public static void grantStageOnJoin (String... stages) {
        
        StageHelper.grantStageOnJoin(null, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnJoinWithCondition (@Nullable Predicate<Player> condition, String... stages) {
        
        StageHelper.grantStageOnJoin(condition, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnJoinWithHook (@Nullable Consumer<Player> hook, String... stages) {
        
        StageHelper.grantStageOnJoin(null, hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnJoin (@Nullable Predicate<Player> condition, @Nullable Consumer<Player> hook, String... stages) {
        
        CTEventManager.register(PlayerEvent.PlayerLoggedInEvent.class, event -> {
            
            if (event.getPlayer() instanceof ServerPlayer && (condition == null || condition.test(event.getPlayer()))) {
                
                final ServerPlayer player = (ServerPlayer) event.getPlayer();
                
                if (grantStages(player, stages) && hook != null) {
                    
                    hook.accept(player);
                }
            }
        });
    }
    
    @ZenCodeType.Method
    public static void grantStageOnExactLevel (int level, String... stages) {
        
        StageHelper.grantStageOnExactLevel(level, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnExactLevel (int level, @Nullable ObjIntConsumer<Player> hook, String... stages) {
        
        StageHelper.grantStageOnLevel(l -> l == level, hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnLevel (int level, String... stages) {
        
        StageHelper.grantStageOnLevel(level, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnLevel (int level, @Nullable ObjIntConsumer<Player> hook, String... stages) {
        
        StageHelper.grantStageOnLevel(l -> l >= level, hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnLevel (IntPredicate levelCondition, String... stages) {
        
        StageHelper.grantStageOnLevel(levelCondition, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnLevel (IntPredicate levelCondition, @Nullable ObjIntConsumer<Player> hook, String... stages) {
        
        CTEventManager.register(LevelChange.class, event -> {
            
            final int currentPlayerLevel = event.getPlayer().experienceLevel + event.getLevels();
            
            if (event.getPlayer() instanceof ServerPlayer && levelCondition.test(currentPlayerLevel)) {
                
                final ServerPlayer player = (ServerPlayer) event.getPlayer();
                
                if (grantStages(player, stages) && hook != null) {
                    
                    hook.accept(player, currentPlayerLevel);
                }
            }
        });
    }
    
    @ZenCodeType.Method
    public static void grantStageOnAdvancement (String targetId, String... stages) {
        
        grantStageOnAdvancement(targetId, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnAdvancement (String targetId, @Nullable BiConsumer<Player, ResourceLocation> hook, String... stages) {
        
        grantStageOnAdvancement(adv -> targetId.equalsIgnoreCase(adv.toString()), hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnAdvancement (String[] targetIds, String... stages) {
        
        grantStageOnAdvancement(targetIds, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnAdvancement (String[] targetIds, @Nullable BiConsumer<Player, ResourceLocation> hook, String... stages) {
        
        grantStageOnAdvancement(adv -> Arrays.stream(targetIds).anyMatch(target -> target.equalsIgnoreCase(adv.toString())), hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnAdvancement (Predicate<ResourceLocation> predicate, String... stages) {
        
        grantStageOnAdvancement(predicate, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageOnAdvancement (Predicate<ResourceLocation> predicate, @Nullable BiConsumer<Player, ResourceLocation> hook, String... stages) {
        
        CTEventManager.register(AdvancementEvent.class, event -> {
            
            if (event.getPlayer() instanceof ServerPlayer && predicate.test(event.getAdvancement().getId())) {
                
                final ServerPlayer player = (ServerPlayer) event.getPlayer();
                
                if (grantStages(player, stages) && hook != null) {
                    
                    hook.accept(player, event.getAdvancement().getId());
                }
            }
        });
    }
    
    @ZenCodeType.Method
    public static void grantStageWhenCrafting (IIngredient stack, String... stages) {
        
        grantStageWhenCrafting(stack, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageWhenCrafting (IIngredient stack, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {
        
        grantStageWhenCrafting(stack::matches, hook, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageWhenCrafting (Predicate<IItemStack> predicate, String... stages) {
        
        grantStageWhenCrafting(predicate, null, stages);
    }
    
    @ZenCodeType.Method
    public static void grantStageWhenCrafting (Predicate<IItemStack> predicate, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {
        
        CTEventManager.register(PlayerEvent.ItemCraftedEvent.class, event -> {
            
            final MCItemStack output = new MCItemStack(event.getCrafting());
            
            if (event.getPlayer() instanceof ServerPlayer && predicate.test(output)) {
                
                final ServerPlayer player = (ServerPlayer) event.getPlayer();
                
                if (grantStages(player, stages) && hook != null) {
                    
                    hook.accept(player, output);
                }
            }
        });
    }
    
    private static boolean grantStages (ServerPlayer player, String... stages) {
        
        boolean result = false;
        final IStageData data = GameStageHelper.getPlayerData(player);
        
        for (final String stageName : stages) {
            
            if (!data.hasStage(stageName)) {
                
                GameStageHelper.addStage(player, stageName);
                result = true;
            }
        }
        
        return result;
    }
}