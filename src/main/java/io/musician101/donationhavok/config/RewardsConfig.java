package io.musician101.donationhavok.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;

public class RewardsConfig extends AbstractConfig<RewardsConfig> {

    @Nonnull
    private final TreeMap<Double, HavokRewards> rewards = new TreeMap<>(Double::compare);

    public RewardsConfig() {
        this(ImmutableMap.of(1D, new HavokRewards()));
    }

    public RewardsConfig(@Nonnull Map<Double, HavokRewards> rewards) {
        this.rewards.putAll(rewards);
    }

    @Nonnull
    @Override
    protected File getFile() {
        return new File(getDir(), "rewards.json");
    }

    @Nonnull
    public TreeMap<Double, HavokRewards> getRewards() {
        return rewards;
    }

    @Nonnull
    public Optional<HavokRewards> getRewardContents(double tier) {
        return Optional.ofNullable(rewards.floorEntry(tier)).map(Entry::getValue);
    }

    public static class Serializer extends BaseSerializer<RewardsConfig> {

        @Override
        public RewardsConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<Double, HavokRewards> map = new HashMap<>();
            StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(JsonElement::getAsJsonObject).forEach(jsonObject -> {
                double minAmount = deserialize(jsonObject, context, Keys.MIN_AMOUNT, 1D);
                HavokRewards rewards = context.deserialize(jsonObject, HavokRewards.class);
                map.put(minAmount, rewards);
            });
            return new RewardsConfig(map);
        }

        @Override
        public JsonElement serialize(RewardsConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray jsonArray = new JsonArray();
            src.getRewards().forEach((amount, rewards) -> {
                JsonObject jsonObject = context.serialize(rewards).getAsJsonObject();
                serialize(jsonObject, context, Keys.MIN_AMOUNT, amount);
                jsonArray.add(jsonObject);
            });
            return jsonArray;
        }
    }
}
