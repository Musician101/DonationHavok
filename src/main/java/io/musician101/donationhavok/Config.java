package io.musician101.donationhavok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.StreamLabsHandler;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.handler.twitch.TwitchHandler;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;

@TypeOf(Config.Serializer.class)
public class Config {

    @Nonnull
    private final DiscoveryHandler discoveryHandler;
    @Nonnull
    private final HavokRewardsHandler havokRewardsHandler;
    @Nonnull
    private final StreamLabsHandler streamLabsHandler;
    @Nonnull
    private final TwitchHandler twitchHandler;

    Config() {
        this.discoveryHandler = new DiscoveryHandler();
        this.havokRewardsHandler = new HavokRewardsHandler();
        this.streamLabsHandler = new StreamLabsHandler();
        this.twitchHandler = new TwitchHandler();
    }

    public Config(@Nonnull DiscoveryHandler discoveryHandler, @Nonnull HavokRewardsHandler havokRewardsHandler, @Nonnull StreamLabsHandler streamLabsHandler, @Nonnull TwitchHandler twitchHandler) {
        this.discoveryHandler = discoveryHandler;
        this.havokRewardsHandler = havokRewardsHandler;
        this.streamLabsHandler = streamLabsHandler;
        this.twitchHandler = twitchHandler;
    }

    @Nonnull
    public DiscoveryHandler getDiscoveryHandler() {
        return discoveryHandler;
    }

    @Nonnull
    public HavokRewardsHandler getHavokRewardsHandler() {
        return havokRewardsHandler;
    }

    @Nonnull
    public StreamLabsHandler getStreamLabsHandler() {
        return streamLabsHandler;
    }

    @Nonnull
    public TwitchHandler getTwitchHandler() {
        return twitchHandler;
    }

    public static class Serializer extends BaseSerializer<Config> {

        @Override
        public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            DiscoveryHandler discoveryHandler = Keys.DISCOVERY.deserializeFromParent(jsonObject).orElse(new DiscoveryHandler());
            HavokRewardsHandler havokRewardsHandler = Keys.GENERAL.deserializeFromParent(jsonObject).orElse(new HavokRewardsHandler());
            StreamLabsHandler streamLabsHandler = Keys.STREAM_LABS.deserializeFromParent(jsonObject).orElse(new StreamLabsHandler());
            TwitchHandler twitchHandler = Keys.TWITCH.deserializeFromParent(jsonObject).orElse(new TwitchHandler());
            return new Config(discoveryHandler, havokRewardsHandler, streamLabsHandler, twitchHandler);
        }

        @Override
        public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            Keys.DISCOVERY.serialize(src.getDiscoveryHandler(), jsonObject);
            Keys.GENERAL.serialize(src.getHavokRewardsHandler(), jsonObject);
            Keys.STREAM_LABS.serialize(src.getStreamLabsHandler(), jsonObject);
            Keys.TWITCH.serialize(src.getTwitchHandler(), jsonObject);
            return jsonObject;
        }
    }
}
