package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.havok.HavokRewards;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import static io.musician101.donationhavok.util.json.JsonUtils.ACCESS_TOKEN;
import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.GENERATE_BOOK;
import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.MC_NAME;
import static io.musician101.donationhavok.util.json.JsonUtils.MIN_AMOUNT;
import static io.musician101.donationhavok.util.json.JsonUtils.NON_REPLACEABLE_BLOCKS;
import static io.musician101.donationhavok.util.json.JsonUtils.REPLACE_UNBREAKABLE_BLOCKS;
import static io.musician101.donationhavok.util.json.JsonUtils.REWARDS;
import static io.musician101.donationhavok.util.json.JsonUtils.STREAMER_NAME;

public class StreamLabsTrackerTypeAdapter implements JsonDeserializer<StreamLabsTracker>, JsonSerializer<StreamLabsTracker> {

    @Override
    public StreamLabsTracker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        boolean generateBook = jsonObject.get(GENERATE_BOOK).getAsBoolean();
        boolean replaceUnbreakableBlocks = jsonObject.get(REPLACE_UNBREAKABLE_BLOCKS).getAsBoolean();
        int delay = jsonObject.get(DELAY).getAsInt();
        String accessToken = parseJsonString(jsonObject, ACCESS_TOKEN);
        String mcName = parseJsonString(jsonObject, MC_NAME);
        String streamerName = parseJsonString(jsonObject, STREAMER_NAME);
        List<IBlockState> nonReplaceableBlocks = GSON.fromJson(jsonObject.getAsJsonArray(NON_REPLACEABLE_BLOCKS), new TypeToken<List<IBlockState>>(){}.getType());
        TreeMap<Double, HavokRewards> rewardsMap = new TreeMap<>();
        jsonObject.getAsJsonArray(REWARDS).forEach(jsonElement -> {
            JsonObject reward = jsonElement.getAsJsonObject();
            double minAmount = reward.get(MIN_AMOUNT).getAsDouble();
            HavokRewards rewards;
            if (reward.has("rewards")) {
                rewards = GSON.fromJson(reward.getAsJsonObject(REWARDS), HavokRewards.class);
            }
            else {
                rewards = GSON.fromJson(reward, HavokRewards.class);
            }

            rewardsMap.put(minAmount, rewards);
        });
        try {
            return new StreamLabsTracker(generateBook, replaceUnbreakableBlocks, delay, nonReplaceableBlocks, rewardsMap, accessToken, mcName, streamerName);
        }
        catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    private String parseJsonString(JsonObject jsonObject, String key) {
        return jsonObject.get(key).getAsString();
    }

    @Override
    public JsonElement serialize(StreamLabsTracker src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACCESS_TOKEN, src.getAccessToken());
        jsonObject.addProperty(GENERATE_BOOK, src.generateBook());
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(MC_NAME, src.getMCName());
        jsonObject.addProperty(STREAMER_NAME, src.getStreamerName());
        jsonObject.addProperty(REPLACE_UNBREAKABLE_BLOCKS, src.replaceUnbreakableBlocks());
        JsonArray nonReplaceableBlocks = new JsonArray();
        src.getNonReplaceableBlocks().forEach(blockState -> nonReplaceableBlocks.add(Block.REGISTRY.getNameForObject(blockState.getBlock()).toString()));
        jsonObject.add(NON_REPLACEABLE_BLOCKS, nonReplaceableBlocks);
        JsonArray rewards = new JsonArray();
        src.getRewards().forEach((minAmount, havokRewards) -> {
            JsonObject reward = GSON.toJsonTree(havokRewards).getAsJsonObject();
            reward.addProperty(MIN_AMOUNT, minAmount);
            rewards.add(reward);
        });
        jsonObject.add(REWARDS, rewards);
        return jsonObject;
    }
}
