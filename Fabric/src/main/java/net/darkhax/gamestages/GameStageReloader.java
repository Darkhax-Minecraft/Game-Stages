package net.darkhax.gamestages;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class GameStageReloader implements IdentifiableResourceReloadListener {

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager manager, ProfilerFiller profiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.completedFuture(null).thenCompose(preparationBarrier::wait).thenAcceptAsync(n -> {
            GameStageSaveHandler.reloadKnownStages();
        }, gameExecutor);
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(GameStages.MOD_ID, "reloader");
    }
}