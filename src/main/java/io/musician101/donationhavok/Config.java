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
            DiscoveryHandler discoveryHandler = deserialize(jsonObject, context, Keys.DISCOVERY, new DiscoveryHandler());
            HavokRewardsHandler havokRewardsHandler = deserialize(jsonObject, context, Keys.GENERAL, new HavokRewardsHandler());
            StreamLabsHandler streamLabsHandler = deserialize(jsonObject, context, Keys.STREAM_LABS, new StreamLabsHandler());
            TwitchHandler twitchHandler = deserialize(jsonObject, context, Keys.TWITCH, new TwitchHandler());
            return new Config(discoveryHandler, havokRewardsHandler, streamLabsHandler, twitchHandler);
        }

        @Override
        public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DISCOVERY, src.getDiscoveryHandler());
            serialize(jsonObject, context, Keys.GENERAL, src.getHavokRewardsHandler());
            serialize(jsonObject, context, Keys.STREAM_LABS, src.getStreamLabsHandler());
            serialize(jsonObject, context, Keys.TWITCH, src.getTwitchHandler());
            return jsonObject;
        }
    }
}
