package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokSound;
import java.lang.reflect.Type;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.ID;
import static io.musician101.donationhavok.util.json.JsonUtils.PITCH;
import static io.musician101.donationhavok.util.json.JsonUtils.VOLUME;
import static io.musician101.donationhavok.util.json.JsonUtils.X_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Y_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Z_OFFSET;

public class HavokSoundTypeAdapter implements JsonDeserializer<HavokSound>, JsonSerializer<HavokSound> {

    @Override
    public HavokSound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        double xOffset = jsonObject.get(X_OFFSET).getAsDouble();
        double yOffset = jsonObject.get(Y_OFFSET).getAsDouble();
        double zOffset = jsonObject.get(Z_OFFSET).getAsDouble();
        float pitch = jsonObject.get(PITCH).getAsFloat();
        float volume = jsonObject.get(VOLUME).getAsFloat();
        String id = jsonObject.get(ID).getAsString();
        SoundEvent sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(id));
        if (sound == null) {
            throw new JsonParseException("Sound with id " + id + " does not exist.");
        }

        return new HavokSound(delay, xOffset, yOffset, zOffset, pitch, volume, sound);
    }

    @Override
    public JsonElement serialize(HavokSound src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(X_OFFSET, src.getXOffset());
        jsonObject.addProperty(Y_OFFSET, src.getYOffset());
        jsonObject.addProperty(Z_OFFSET, src.getZOffset());
        jsonObject.addProperty(PITCH, src.getPitch());
        jsonObject.addProperty(VOLUME, src.getVolume());
        ResourceLocation id = src.getSoundEvent().getRegistryName();
        jsonObject.addProperty(ID, id == null ? "null" : id.toString());
        return jsonObject;
    }
}
