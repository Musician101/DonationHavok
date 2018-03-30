package io.musician101.donationhavok.handler.discovery;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

public class DiscoveryHandler {

    @Nonnull
    private final List<Discovery> currentDiscoveries;
    private final boolean hideUntilDiscovered;
    @Nonnull
    private final List<Discovery> legendaryDiscoveries;

    public DiscoveryHandler() {
        this(false, Collections.singletonList(new Discovery(1D, "Musician101", "A Default Reward")), Collections.singletonList(new Discovery(1D, "Musician101", "A Default Reward")));
    }

    private DiscoveryHandler(boolean hideUntilDiscovered, @Nonnull List<Discovery> currentDiscoveries, @Nonnull List<Discovery> legendaryDiscoveries) {
        this.hideUntilDiscovered = hideUntilDiscovered;
        this.currentDiscoveries = currentDiscoveries;
        this.legendaryDiscoveries = legendaryDiscoveries;
    }

    @Nonnull
    public List<Discovery> getCurrentDiscoveries() {
        return currentDiscoveries;
    }

    @Nonnull
    public List<Discovery> getLegendaryDiscoveries() {
        return legendaryDiscoveries;
    }

    public boolean hideCurrentUntilDiscovered() {
        return hideUntilDiscovered;
    }

    public void rewardsDiscovered(double tier, String donorName, HavokRewards rewards) {
        Discovery discovery = new Discovery(tier, donorName, rewards.getName());
        if (hideUntilDiscovered && !currentDiscoveries.contains(discovery)) {
            currentDiscoveries.add(discovery);
        }
    }

    public static class Serializer extends BaseSerializer<DiscoveryHandler> {

        @Override
        public DiscoveryHandler deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean hideCurrentUntilDiscovered = deserialize(jsonObject, context, Keys.HIDE_CURRENT_UNTIL_DISCOVERED, false);
            List<Discovery> currentDiscoveries = deserialize(jsonObject, context, Keys.CURRENT, Collections.emptyList());
            List<Discovery> legendaryDiscoveries = deserialize(jsonObject, context, Keys.LEGENDARY, Collections.emptyList());
            return new DiscoveryHandler(hideCurrentUntilDiscovered, currentDiscoveries, legendaryDiscoveries);
        }

        @Override
        public JsonElement serialize(DiscoveryHandler src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.HIDE_CURRENT_UNTIL_DISCOVERED, src.hideCurrentUntilDiscovered());
            serialize(jsonObject, context, Keys.CURRENT, src.getCurrentDiscoveries());
            serialize(jsonObject, context, Keys.LEGENDARY, src.getLegendaryDiscoveries());
            return jsonObject;
        }
    }
}
