package net.darkhax.gamestages.event;

import net.darkhax.gamestages.data.IStageData;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface StagesSyncedEvent {
    Event<StagesSyncedEvent> STAGES_SYNCED_EVENT = EventFactory.createArrayBacked(StagesSyncedEvent.class, callbacks -> (data) -> {
        for (var callback: callbacks) {
            callback.onStagesSynced(data);
        }
    });

    void onStagesSynced(IStageData data);
}
