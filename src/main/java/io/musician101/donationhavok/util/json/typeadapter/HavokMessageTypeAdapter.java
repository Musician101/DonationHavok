package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokMessage;
import java.lang.reflect.Type;
import net.minecraft.util.text.ITextComponent;

import static io.musician101.donationhavok.util.json.JsonUtils.BROADCAST;
import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.MESSAGE;

public class HavokMessageTypeAdapter implements JsonDeserializer<HavokMessage>, JsonSerializer<HavokMessage> {

    @Override
    public HavokMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        boolean broadcast = jsonObject.get(BROADCAST).getAsBoolean();
        ITextComponent message = new ITextComponent.Serializer().deserialize(jsonObject.get(MESSAGE), typeOfT, context);
        return new HavokMessage(delay, broadcast, message);
    }

    @Override
    public JsonElement serialize(HavokMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(BROADCAST, src.broadcastEnabled());
        jsonObject.add(MESSAGE, new ITextComponent.Serializer().serialize(src.getMessage(), typeOfSrc, context));
        return jsonObject;
    }
}
