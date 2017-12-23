package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokItemStack;
import java.lang.reflect.Type;
import net.minecraft.item.ItemStack;

import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.ITEM_STACK;

public class HavokItemStackTypeAdapter implements JsonDeserializer<HavokItemStack>, JsonSerializer<HavokItemStack> {

    @Override
    public HavokItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        ItemStack itemStack = GSON.fromJson(jsonObject.getAsJsonObject(ITEM_STACK), ItemStack.class);
        return new HavokItemStack(delay, itemStack);
    }

    @Override
    public JsonElement serialize(HavokItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.add(ITEM_STACK, GSON.toJsonTree(src.getItemStack(), ItemStack.class));
        return jsonObject;
    }
}
