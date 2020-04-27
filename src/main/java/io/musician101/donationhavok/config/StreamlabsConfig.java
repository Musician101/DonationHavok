package io.musician101.donationhavok.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.io.File;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;

public class StreamlabsConfig extends AbstractConfig<StreamlabsConfig> {

    private final boolean enabled;
    private final int delay;
    @Nonnull
    private final String accessToken;

    StreamlabsConfig() {
        this(false, 10, "Your Streamlabs access token here!");
    }

    public StreamlabsConfig(boolean enabled, int delay, @Nonnull String accessToken) {
        this.enabled = enabled;
        this.delay = delay;
        this.accessToken = accessToken;
    }

    @Nonnull
    public String getAccessToken() {
        return accessToken;
    }

    public int getDelay() {
        return delay;
    }

    @Nonnull
    @Override
    protected File getFile() {
        return new File(getDir(), "streamlabs.json");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static class Serializer extends BaseSerializer<StreamlabsConfig> {

        @Override
        public StreamlabsConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 10);
            String accessToken = deserialize(jsonObject, context, Keys.ACCESS_TOKEN, "Your StreamLabs access token here!");
            boolean enabled = deserialize(jsonObject, context, Keys.ENABLE, false);
            return new StreamlabsConfig(enabled, delay, accessToken);
        }

        @Override
        public JsonElement serialize(StreamlabsConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ACCESS_TOKEN, src.getAccessToken());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            return jsonObject;
        }
    }
}
