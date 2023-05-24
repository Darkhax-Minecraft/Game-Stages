package net.darkhax.gamestages.event;

import net.darkhax.gamestages.data.IStageData;
import org.quiltmc.qsl.base.api.event.Event;
import org.quiltmc.qsl.base.api.event.EventAwareListener;

@FunctionalInterface
public interface StagesSyncedEvent extends EventAwareListener {
    Event<StagesSyncedEvent> STAGES_SYNCED_EVENT = Event.create(StagesSyncedEvent.class, callbacks -> (data) -> {
        for (var callback: callbacks) {
            callback.onStagesSynced(data);
        }
    });

    void onStagesSynced(IStageData data);
}
