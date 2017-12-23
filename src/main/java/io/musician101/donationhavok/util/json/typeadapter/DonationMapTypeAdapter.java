package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokRewards;
import java.lang.reflect.Type;
import java.util.TreeMap;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.MIN_AMOUNT;
import static io.musician101.donationhavok.util.json.JsonUtils.REWARDS;

public class DonationMapTypeAdapter implements JsonDeserializer<TreeMap<Double, HavokRewards>>, JsonSerializer<TreeMap<Double, HavokRewards>> {

    @Override
    public TreeMap<Double, HavokRewards> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TreeMap<Double, HavokRewards> map = new TreeMap<>();
        json.getAsJsonArray().forEach(jsonElement -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            double minAmount = jsonObject.get(MIN_AMOUNT).getAsDouble();
            HavokRewards rewards = GSON.fromJson(jsonObject.getAsJsonObject(REWARDS), HavokRewards.class);
            map.put(minAmount, rewards);
        });
        return map;
    }

    @Override
    public JsonElement serialize(TreeMap<Double, HavokRewards> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        src.forEach((minAmount, rewards) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(MIN_AMOUNT, minAmount);
            jsonObject.add(REWARDS, GSON.toJsonTree(rewards));
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }
}
