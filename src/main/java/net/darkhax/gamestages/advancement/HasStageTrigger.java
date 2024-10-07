package net.darkhax.gamestages.advancement;

import com.google.gson.JsonObject;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.GameStages;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class HasStageTrigger extends SimpleCriterionTrigger<HasStageTrigger.Instance> {

    public static final ResourceLocation TRIGGER_ID = new ResourceLocation(GameStages.MOD_ID, "has_stage");
    public static final HasStageTrigger INSTANCE = new HasStageTrigger();

    public void trigger(ServerPlayer player, String stage) {
        this.trigger(player, predicate -> GameStageHelper.hasAllOf(player, stage));
    }

    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate playerContext, DeserializationContext context) {
        return new Instance(playerContext, json.get("stage").getAsString());
    }

    @Override
    public ResourceLocation getId() {
        return TRIGGER_ID;
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final String stage;

        public Instance(ContextAwarePredicate context, String stage) {
            super(TRIGGER_ID, context);
            this.stage = stage;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            final JsonObject json = super.serializeToJson(context);
            json.addProperty("stage", this.stage);
            return json;
        }
    }
}