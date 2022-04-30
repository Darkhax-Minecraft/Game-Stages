package net.darkhax.gamestages.addons.crt;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import net.darkhax.gamestages.addons.crt.util.DimensionCondition;
import net.darkhax.gamestages.addons.crt.util.FishingHook;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.blamejared.crafttweaker.impl.events.CTEventManager;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import com.blamejared.crafttweaker.impl.tag.MCTag;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.addons.crt.util.FishingCondition;
import net.darkhax.gamestages.addons.crt.util.DimensionHook;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent.LevelChange;

@ZenRegister
@ZenCodeType.Name("mods.gamestages.StageHelper")
public class StageHelper {

    @ZenCodeType.Method
    public static void grantStageOnKill(MCEntityType type, String... stages) {

        grantStageOnKill(type, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(MCEntityType type, @Nullable BiConsumer<PlayerEntity, LivingEntity> hook, String... stages) {

        grantStageOnKill((p, t) -> type.getInternal() == t.getType(), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(MCTag<MCEntityType> tag, String... stages) {

        grantStageOnKill(tag, null, stages);
    }

    @ZenCodeType.Method
    @SuppressWarnings("unchecked")
    public static void grantStageOnKill(MCTag<MCEntityType> tag, @Nullable BiConsumer<PlayerEntity, LivingEntity> hook, String... stages) {

        grantStageOnKill((p, t) -> tag.getInternalRaw().contains(t.getType()), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(@Nullable BiPredicate<PlayerEntity, LivingEntity> condition, String... stages) {

        grantStageOnKill(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(@Nullable BiPredicate<PlayerEntity, LivingEntity> condition, @Nullable BiConsumer<PlayerEntity, LivingEntity> hook, String... stages) {

        CTEventManager.register(LivingDeathEvent.class, event -> {

            final DamageSource source = event.getSource();

            if (source != null) {

                final Entity killer = source.getEntity();
                final LivingEntity target = event.getEntityLiving();

                if (killer instanceof ServerPlayerEntity && target != null) {

                    final ServerPlayerEntity player = (ServerPlayerEntity) killer;

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
    public static void grantStageOnDimension(String[] targets, String... stages) {

        grantStageOnDimension(targets, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnDimension(String[] targets, @Nullable DimensionHook hook, String... stages) {

        grantStageOnDimension((p, t, f) -> Arrays.stream(targets).anyMatch(target -> target.equalsIgnoreCase(t.toString())), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnDimension(String targetTo, String... stages) {

        grantStageOnDimension(targetTo, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnDimension(String targetTo, @Nullable DimensionHook hook, String... stages) {

        grantStageOnDimension((p, t, f) -> targetTo.equalsIgnoreCase(t.toString()), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnDimension(@Nullable DimensionCondition condition, String... stages) {

        StageHelper.grantStageOnDimension(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnDimension(@Nullable DimensionCondition condition, @Nullable DimensionHook hook, String... stages) {

        CTEventManager.register(PlayerEvent.PlayerChangedDimensionEvent.class, event -> {

            final ResourceLocation toDim = event.getTo().location();
            final ResourceLocation fromDim = event.getFrom().location();

            if (event.getPlayer() instanceof ServerPlayerEntity && (condition == null || condition.test(event.getPlayer(), toDim, fromDim))) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, toDim, fromDim);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageOnJoin(String... stages) {

        StageHelper.grantStageOnJoin(null, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnJoinWithCondition(@Nullable Predicate<PlayerEntity> condition, String... stages) {

        StageHelper.grantStageOnJoin(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnJoinWithHook(@Nullable Consumer<PlayerEntity> hook, String... stages) {

        StageHelper.grantStageOnJoin(null, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnJoin(@Nullable Predicate<PlayerEntity> condition, @Nullable Consumer<PlayerEntity> hook, String... stages) {

        CTEventManager.register(PlayerEvent.PlayerLoggedInEvent.class, event -> {

            if (event.getPlayer() instanceof ServerPlayerEntity && (condition == null || condition.test(event.getPlayer()))) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageOnExactLevel(int level, String... stages) {

        StageHelper.grantStageOnExactLevel(level, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnExactLevel(int level, @Nullable ObjIntConsumer<PlayerEntity> hook, String... stages) {

        StageHelper.grantStageOnLevel(l -> l == level, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(int level, String... stages) {

        StageHelper.grantStageOnLevel(level, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(int level, @Nullable ObjIntConsumer<PlayerEntity> hook, String... stages) {

        StageHelper.grantStageOnLevel(l -> l >= level, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(IntPredicate levelCondition, String... stages) {

        StageHelper.grantStageOnLevel(levelCondition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(IntPredicate levelCondition, @Nullable ObjIntConsumer<PlayerEntity> hook, String... stages) {

        CTEventManager.register(LevelChange.class, event -> {

            final int currentPlayerLevel = event.getPlayer().experienceLevel + event.getLevels();

            if (event.getPlayer() instanceof ServerPlayerEntity && levelCondition.test(currentPlayerLevel)) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, currentPlayerLevel);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(String targetId, String... stages) {

        grantStageOnAdvancement(targetId, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(String targetId, @Nullable BiConsumer<PlayerEntity, ResourceLocation> hook, String... stages) {

        grantStageOnAdvancement(adv -> targetId.equalsIgnoreCase(adv.toString()), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(String[] targetIds, String... stages) {

        grantStageOnAdvancement(targetIds, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(String[] targetIds, @Nullable BiConsumer<PlayerEntity, ResourceLocation> hook, String... stages) {

        grantStageOnAdvancement(adv -> Arrays.stream(targetIds).anyMatch(target -> target.equalsIgnoreCase(adv.toString())), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(Predicate<ResourceLocation> predicate, String... stages) {

        grantStageOnAdvancement(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(Predicate<ResourceLocation> predicate, @Nullable BiConsumer<PlayerEntity, ResourceLocation> hook, String... stages) {

        CTEventManager.register(AdvancementEvent.class, event -> {

            if (event.getPlayer() instanceof ServerPlayerEntity && predicate.test(event.getAdvancement().getId())) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, event.getAdvancement().getId());
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageWhenCrafting(IIngredient stack, String... stages) {

        grantStageWhenCrafting(stack, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenCrafting(IIngredient stack, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        grantStageWhenCrafting(stack::matches, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenCrafting(Predicate<IItemStack> predicate, String... stages) {

        grantStageWhenCrafting(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenCrafting(Predicate<IItemStack> predicate, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        CTEventManager.register(PlayerEvent.ItemCraftedEvent.class, event -> {

            final MCItemStack output = new MCItemStack(event.getCrafting());

            if (event.getPlayer() instanceof ServerPlayerEntity && predicate.test(output)) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, output);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageWhenSmelting(IIngredient stack, String... stages) {

        grantStageWhenSmelting((player, smelted) -> stack.matches(smelted), null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenSmelting(IIngredient stack, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        grantStageWhenSmelting((player, smelted) -> stack.matches(smelted), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenSmelting(BiPredicate<PlayerEntity, IItemStack> predicate, String... stages) {

        grantStageWhenSmelting(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenSmelting(BiPredicate<PlayerEntity, IItemStack> predicate, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        CTEventManager.register(PlayerEvent.ItemSmeltedEvent.class, event -> {

            final MCItemStack output = new MCItemStack(event.getSmelting());

            if (event.getPlayer() instanceof ServerPlayerEntity && predicate.test(event.getPlayer(), output)) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, output);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(Effect effect, String... stages) {

        grantStageWhenBrewing(effect, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(Effect effect, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        grantStageWhenBrewing((player, brewed) -> hasEffect(effect, PotionUtils.getMobEffects(brewed.getInternal())), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(Potion potion, String... stages) {

        grantStageWhenBrewing(potion, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(Potion potion, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        grantStageWhenBrewing((player, brewed) -> PotionUtils.getPotion(brewed.getInternal()) == potion, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(IIngredient stack, String... stages) {

        grantStageWhenBrewing((player, brewed) -> stack.matches(brewed), stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(IIngredient stack, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        grantStageWhenBrewing((player, brewed) -> stack.matches(brewed), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(BiPredicate<PlayerEntity, IItemStack> predicate, String... stages) {

        grantStageWhenBrewing(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(BiPredicate<PlayerEntity, IItemStack> predicate, @Nullable BiConsumer<PlayerEntity, IItemStack> hook, String... stages) {

        CTEventManager.register(PlayerBrewedPotionEvent.class, event -> {

            final MCItemStack output = new MCItemStack(event.getStack());

            if (event.getPlayer() instanceof ServerPlayerEntity && predicate.test(event.getPlayer(), output)) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, output);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageWhenFishing(IIngredient ingredient, String... stages) {

        grantStageWhenFishing((player, bobber, drops) -> anyMatch(ingredient, drops), null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenFishing(IIngredient ingredient, @Nullable FishingHook hook, String... stages) {

        grantStageWhenFishing((player, bobber, drops) -> anyMatch(ingredient, drops), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenFishing(FishingCondition predicate, @Nullable FishingHook hook, String... stages) {

        CTEventManager.register(ItemFishedEvent.class, event -> {

            final List<IItemStack> output = CraftTweakerHelper.getIItemStacks(event.getDrops());

            if (event.getPlayer() instanceof ServerPlayerEntity && predicate.test(event.getPlayer(), event.getHookEntity(), output)) {

                final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, event.getHookEntity(), output);
                }
            }
        });
    }

    private static boolean grantStages(ServerPlayerEntity player, String... stages) {

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

    private static boolean anyMatch(IIngredient ingredient, List<IItemStack> stacks) {

        for (IItemStack stack : stacks) {

            if (ingredient.matches(stack)) {

                return true;
            }
        }

        return false;
    }

    private static boolean hasEffect(Effect effect, List<EffectInstance> toTest) {

        for (EffectInstance effectToTest : toTest) {

            if (effectToTest.getEffect() == effect) {

                return true;
            }
        }

        return false;
    }
}