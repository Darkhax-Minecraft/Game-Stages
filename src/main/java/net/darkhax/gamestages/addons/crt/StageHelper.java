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

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.entity.CTEntityIngredient;
import com.blamejared.crafttweaker.api.event.CTEventManager;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import net.darkhax.gamestages.addons.crt.util.DimensionCondition;
import net.darkhax.gamestages.addons.crt.util.FishingHook;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.item.IItemStack;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.addons.crt.util.FishingCondition;
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
    public static void grantStageOnKill(CTEntityIngredient condition, String... stages) {

        grantStageOnKill(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(CTEntityIngredient condition, @Nullable BiConsumer<Player, LivingEntity> hook, String... stages) {

        grantStageOnKill((p, t) -> asPredicate(condition).test(t), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(@Nullable BiPredicate<Player, LivingEntity> condition, String... stages) {

        grantStageOnKill(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnKill(@Nullable BiPredicate<Player, LivingEntity> condition, @Nullable BiConsumer<Player, LivingEntity> hook, String... stages) {

        CTEventManager.register(LivingDeathEvent.class, event -> {

            final DamageSource source = event.getSource();

            if (source != null) {

                final LivingEntity target = event.getEntity();

                if (source.getEntity() instanceof ServerPlayer killingPlayer && target != null) {

                    if (condition == null || condition.test(killingPlayer, target)) {

                        if (grantStages(killingPlayer, stages) && hook != null) {

                            hook.accept(killingPlayer, target);
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

            if (event.getEntity() instanceof ServerPlayer player && (condition == null || condition.test(player, toDim, fromDim))) {

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
    public static void grantStageOnJoinWithCondition(@Nullable Predicate<Player> condition, String... stages) {

        StageHelper.grantStageOnJoin(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnJoinWithHook(@Nullable Consumer<Player> hook, String... stages) {

        StageHelper.grantStageOnJoin(null, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnJoin(@Nullable Predicate<Player> condition, @Nullable Consumer<Player> hook, String... stages) {

        CTEventManager.register(PlayerEvent.PlayerLoggedInEvent.class, event -> {

            if (event.getEntity() instanceof ServerPlayer player && (condition == null || condition.test(player))) {

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
    public static void grantStageOnExactLevel(int level, @Nullable ObjIntConsumer<Player> hook, String... stages) {

        StageHelper.grantStageOnLevel(l -> l == level, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(int level, String... stages) {

        StageHelper.grantStageOnLevel(level, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(int level, @Nullable ObjIntConsumer<Player> hook, String... stages) {

        StageHelper.grantStageOnLevel(l -> l >= level, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(IntPredicate levelCondition, String... stages) {

        StageHelper.grantStageOnLevel(levelCondition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnLevel(IntPredicate levelCondition, @Nullable ObjIntConsumer<Player> hook, String... stages) {

        CTEventManager.register(LevelChange.class, event -> {

            final int currentPlayerLevel = event.getEntity().experienceLevel + event.getLevels();

            if (event.getEntity() instanceof ServerPlayer player && levelCondition.test(currentPlayerLevel)) {

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
    public static void grantStageOnAdvancement(String targetId, @Nullable BiConsumer<Player, ResourceLocation> hook, String... stages) {

        grantStageOnAdvancement(adv -> targetId.equalsIgnoreCase(adv.toString()), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(String[] targetIds, String... stages) {

        grantStageOnAdvancement(targetIds, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(String[] targetIds, @Nullable BiConsumer<Player, ResourceLocation> hook, String... stages) {

        grantStageOnAdvancement(adv -> Arrays.stream(targetIds).anyMatch(target -> target.equalsIgnoreCase(adv.toString())), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(Predicate<ResourceLocation> predicate, String... stages) {

        grantStageOnAdvancement(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnAdvancement(Predicate<ResourceLocation> predicate, @Nullable BiConsumer<Player, ResourceLocation> hook, String... stages) {

        CTEventManager.register(AdvancementEvent.AdvancementEarnEvent.class, event -> {

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(event.getAdvancement().getId())) {

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
    public static void grantStageWhenCrafting(IIngredient stack, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        grantStageWhenCrafting(stack::matches, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenCrafting(Predicate<IItemStack> predicate, String... stages) {

        grantStageWhenCrafting(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenCrafting(Predicate<IItemStack> predicate, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        CTEventManager.register(PlayerEvent.ItemCraftedEvent.class, event -> {

            final MCItemStack output = new MCItemStack(event.getCrafting());

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(output)) {

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
    public static void grantStageWhenSmelting(IIngredient stack, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        grantStageWhenSmelting((player, smelted) -> stack.matches(smelted), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenSmelting(BiPredicate<Player, IItemStack> predicate, String... stages) {

        grantStageWhenSmelting(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenSmelting(BiPredicate<Player, IItemStack> predicate, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        CTEventManager.register(PlayerEvent.ItemSmeltedEvent.class, event -> {

            final MCItemStack output = new MCItemStack(event.getSmelting());

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(player, output)) {

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, output);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(MobEffect effect, String... stages) {

        grantStageWhenBrewing(effect, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(MobEffect effect, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        grantStageWhenBrewing((player, brewed) -> hasEffect(effect, PotionUtils.getMobEffects(brewed.getInternal())), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(Potion potion, String... stages) {

        grantStageWhenBrewing(potion, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(Potion potion, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        grantStageWhenBrewing((player, brewed) -> PotionUtils.getPotion(brewed.getInternal()) == potion, hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(IIngredient stack, String... stages) {

        grantStageWhenBrewing((player, brewed) -> stack.matches(brewed), stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(IIngredient stack, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        grantStageWhenBrewing((player, brewed) -> stack.matches(brewed), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(BiPredicate<Player, IItemStack> predicate, String... stages) {

        grantStageWhenBrewing(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenBrewing(BiPredicate<Player, IItemStack> predicate, @Nullable BiConsumer<Player, IItemStack> hook, String... stages) {

        CTEventManager.register(PlayerBrewedPotionEvent.class, event -> {

            final MCItemStack output = new MCItemStack(event.getStack());

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(player, output)) {

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

            final List<ItemStack> output = event.getDrops();

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(player, event.getHookEntity(), output)) {

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, event.getHookEntity(), output);
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageWhenPickedUp(IIngredient ingredient, String... stages) {

        grantStageWhenPickedUp(ingredient, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenPickedUp(IIngredient ingredient, @Nullable BiConsumer<Player, ItemEntity> hook, String... stages) {

        grantStageWhenPickedUp((player, stack) -> ingredient.matches(stack), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenPickedUp(BiPredicate<Player, IItemStack> predicate, String... stages) {

        grantStageWhenPickedUp(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageWhenPickedUp(BiPredicate<Player, IItemStack> predicate, @Nullable BiConsumer<Player, ItemEntity> hook, String... stages) {

        CTEventManager.register(PlayerEvent.ItemPickupEvent.class, event -> {

            final IItemStack output = new MCItemStack(event.getStack());

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(player, output)) {

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, event.getOriginalEntity());
                }
            }
        });
    }

    @ZenCodeType.Method
    public static void grantStageOnCrit(CTEntityIngredient condition, String... stages) {

        grantStageOnCrit(condition, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnCrit(CTEntityIngredient condition, @Nullable BiConsumer<Player, Entity> hook, String... stages) {

        grantStageOnCrit((player, target) -> asPredicate(condition).test(target), hook, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnCrit(BiPredicate<Player, Entity> predicate, String... stages) {

        grantStageOnCrit(predicate, null, stages);
    }

    @ZenCodeType.Method
    public static void grantStageOnCrit(BiPredicate<Player, Entity> predicate, @Nullable BiConsumer<Player, Entity> hook, String... stages) {

        CTEventManager.register(CriticalHitEvent.class, event -> {

            if (event.getEntity() instanceof ServerPlayer player && predicate.test(player, event.getTarget())) {

                if (grantStages(player, stages) && hook != null) {

                    hook.accept(player, event.getTarget());
                }
            }
        });
    }

    private static boolean grantStages(ServerPlayer player, String... stages) {

        boolean result = false;
        final IStageData data = GameStageHelper.getPlayerData(player);

        if (data != null) {

            for (final String stageName : stages) {

                if (!data.hasStage(stageName)) {

                    GameStageHelper.addStage(player, stageName);
                    result = true;
                }
            }
        }

        return result;
    }

    private static Predicate<Entity> asPredicate(CTEntityIngredient ingredient) {

        return ingredient.mapTo(
                internal -> toTest -> toTest.getType() == internal,
                (internal, amount) -> toTest -> toTest.getType().is(internal) && amount <= 1,
                (stream) -> toTest -> stream.anyMatch(sub -> sub.test(toTest))
        );
    }

    private static boolean anyMatch(IIngredient ingredient, List<ItemStack> stacks) {

        for (ItemStack stack : stacks) {

            if (ingredient.matches(new MCItemStack(stack))) {

                return true;
            }
        }

        return false;
    }

    private static boolean hasEffect(MobEffect effect, List<MobEffectInstance> toTest) {

        for (MobEffectInstance effectToTest : toTest) {

            if (effectToTest.getEffect() == effect) {

                return true;
            }
        }

        return false;
    }
}