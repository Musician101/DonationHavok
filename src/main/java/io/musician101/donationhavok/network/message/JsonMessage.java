package io.musician101.donationhavok.network.message;

import com.google.gson.JsonObject;
import javax.annotation.Nonnull;

//TODO split into config and discovery messages
@Deprecated
public class JsonMessage {

    @Nonnull
    private JsonObject jsonObject;

    public JsonMessage() {
        this.jsonObject = new JsonObject();
    }

    public JsonMessage(@Nonnull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Nonnull
    public JsonObject getMessage() {
        return jsonObject;
    }
}
