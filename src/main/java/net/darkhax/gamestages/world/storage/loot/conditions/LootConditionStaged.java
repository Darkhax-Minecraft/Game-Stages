package net.darkhax.gamestages.world.storage.loot.conditions;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootConditionStaged implements LootCondition {

    private final String stage;

    public LootConditionStaged (String chanceIn) {

        this.stage = chanceIn;
    }

    @Override
    public boolean testCondition (Random rand, LootContext context) {

        final Entity entityPlayer = context.getKillerPlayer();
        return entityPlayer instanceof EntityPlayer ? GameStageHelper.hasStage((EntityPlayer) entityPlayer, this.stage) : false;
    }

    public static class Serializer extends LootCondition.Serializer<LootConditionStaged> {

        public Serializer () {

            super(new ResourceLocation("required_stage"), LootConditionStaged.class);
        }

        @Override
        public void serialize (JsonObject json, LootConditionStaged value, JsonSerializationContext context) {

            json.addProperty("stage_name", Float.valueOf(value.stage));
        }

        @Override
        public LootConditionStaged deserialize (JsonObject json, JsonDeserializationContext context) {

            return new LootConditionStaged(JsonUtils.getString(json, "stage_name"));
        }
    }
}