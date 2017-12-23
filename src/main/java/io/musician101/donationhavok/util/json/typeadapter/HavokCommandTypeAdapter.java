package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokCommand;
import java.lang.reflect.Type;

import static io.musician101.donationhavok.util.json.JsonUtils.COMMAND;
import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;

public class HavokCommandTypeAdapter implements JsonDeserializer<HavokCommand>, JsonSerializer<HavokCommand> {

    @Override
    public HavokCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        String command = jsonObject.get(COMMAND).getAsString();
        return new HavokCommand(delay, command);
    }

    @Override
    public JsonElement serialize(HavokCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(COMMAND, src.getCommand());
        return jsonObject;
    }
}
