package io.musician101.donationhavok.util.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import java.lang.reflect.Type;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class PlacementSettingsSerializer extends BaseSerializer<PlacementSettings> {

    @Override
    public PlacementSettings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        PlacementSettings placementSettings = new PlacementSettings();
        placementSettings.setMirror(deserialize(jsonObject, context, Keys.MIRROR, Mirror.NONE));
        placementSettings.setRotation(deserialize(jsonObject, context, Keys.ROTATION, Rotation.NONE));
        placementSettings.setIntegrity(deserialize(jsonObject, context, Keys.INTEGRITY, 1F));
        return placementSettings;
    }

    @Override
    public JsonElement serialize(PlacementSettings src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        serialize(jsonObject, context, Keys.MIRROR, src.getMirror());
        serialize(jsonObject, context, Keys.ROTATION, src.getRotation());
        serialize(jsonObject, context, Keys.INTEGRITY, src.getIntegrity());
        return jsonObject;
    }
}
