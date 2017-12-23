package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class ItemStackTypeAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ItemStack(GSON.fromJson(json, NBTTagCompound.class));
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return GSON.toJsonTree(src.writeToNBT(new NBTTagCompound()), NBTTagCompound.class);
    }
}
