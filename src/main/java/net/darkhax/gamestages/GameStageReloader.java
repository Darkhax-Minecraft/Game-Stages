package net.darkhax.gamestages;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public class GameStageReloader implements PreparableReloadListener {

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager manager, ProfilerFiller profiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {

        return CompletableFuture.completedFuture(null).thenCompose(stage::wait).thenAcceptAsync(n -> {

            GameStageSaveHandler.reloadFakePlayers();
            GameStageSaveHandler.reloadKnownStages();
        }, gameExecutor);
    }
}