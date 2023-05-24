package net.darkhax.gamestages;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class GameStageReloader implements IdentifiableResourceReloader {

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager manager, ProfilerFiller profiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {

        return CompletableFuture.completedFuture(null).thenCompose(preparationBarrier::wait).thenAcceptAsync(n -> {
            GameStageSaveHandler.reloadKnownStages();
        }, gameExecutor);
    }

    @Override
    public @NotNull ResourceLocation getQuiltId() {
        return new ResourceLocation("gamestages", "stage_resources");
    }
}