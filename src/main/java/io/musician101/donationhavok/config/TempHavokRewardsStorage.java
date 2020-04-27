package io.musician101.donationhavok.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

public class TempHavokRewardsStorage {

    final Map<Double, HavokRewards> rewards = new HashMap<>();

    TempHavokRewardsStorage() {
        rewards.put(1D, new HavokRewards());
    }

    public TempHavokRewardsStorage(Map<Double, HavokRewards> map) {
        rewards.putAll(map);
    }

    public static class Serializer extends BaseSerializer<TempHavokRewardsStorage> {

        @Override
        public TempHavokRewardsStorage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<Double, HavokRewards> map = new HashMap<>();
            StreamSupport.stream(json.getAsJsonArray().spliterator(), false).forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                double minAmount = deserialize(jsonObject, context, Keys.MIN_AMOUNT, 1D);
                HavokRewards rewards = context.deserialize(jsonObject, HavokRewards.class);
                map.put(minAmount, rewards);
            });
            return new TempHavokRewardsStorage(map);
        }

        @Override
        public JsonElement serialize(TempHavokRewardsStorage src, Type typeOfSrc, JsonSerializationContext context) {
            return src.rewards.entrySet().stream().map(entry -> {
                JsonObject jsonObject = context.serialize(entry.getValue()).getAsJsonObject();
                jsonObject.addProperty(Keys.MIN_AMOUNT.getKey(), entry.getKey());
                serialize(jsonObject, context, Keys.MIN_AMOUNT, entry.getKey());
                return jsonObject;
            }).collect(Keys.jsonArrayCollector());
        }
    }
}
