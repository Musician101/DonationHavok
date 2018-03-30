package io.musician101.donationhavok.util.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.util.json.JsonKeyImpl;
import javax.annotation.Nonnull;

public abstract class BaseSerializer<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    protected BaseSerializer() {

    }

    @Nonnull
    protected final <J extends JsonElement, V> V deserialize(@Nonnull JsonObject parent, @Nonnull JsonDeserializationContext context, @Nonnull JsonKeyImpl<J, V> key, @Nonnull V defaultValue) {
        return key.deserializeFromParent(parent, context).orElse(defaultValue);
    }

    protected final <J extends JsonElement, V> void serialize(@Nonnull JsonObject parent, @Nonnull JsonSerializationContext context, @Nonnull JsonKeyImpl<J, V> key, @Nonnull V defaultValue) {
        key.serialize(defaultValue, parent, context);
    }
}
