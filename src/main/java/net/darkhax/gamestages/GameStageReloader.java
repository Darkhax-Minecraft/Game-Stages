package net.darkhax.gamestages;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;

public class GameStageReloader implements IFutureReloadListener {

    @Override
    public final CompletableFuture<Void> reload (IFutureReloadListener.IStage stage, IResourceManager manager, IProfiler preparationProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        
        return CompletableFuture.completedFuture(null).thenCompose(stage::wait).thenAcceptAsync(n -> {
            
            GameStageSaveHandler.reloadFakePlayers();            
            GameStageSaveHandler.reloadKnownStages();
        }, gameExecutor);
    }
}