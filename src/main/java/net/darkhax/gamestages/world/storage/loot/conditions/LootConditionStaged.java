package net.darkhax.gamestages.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class LootConditionStaged implements ILootCondition {
    
    private final String stage;
    
    public LootConditionStaged(String chanceIn) {
        
        this.stage = chanceIn;
    }
    
    @Override
    public boolean test (LootContext context) {
        
        final Entity entityPlayer = context.get(LootParameters.LAST_DAMAGE_PLAYER);
        return entityPlayer instanceof PlayerEntity ? GameStageHelper.hasStage((PlayerEntity) entityPlayer, this.stage) : false;
    }
    
    public static class Serializer extends ILootCondition.AbstractSerializer<LootConditionStaged> {
        
        public Serializer() {
            
            super(new ResourceLocation("required_stage"), LootConditionStaged.class);
        }
        
        @Override
        public void serialize (JsonObject json, LootConditionStaged value, JsonSerializationContext context) {
            
            json.addProperty("stage_name", Float.valueOf(value.stage));
        }
        
        @Override
        public LootConditionStaged deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new LootConditionStaged(JSONUtils.getString(json, "stage_name"));
        }
    }
}