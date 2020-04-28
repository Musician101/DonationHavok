package io.musician101.donationhavok.configurator.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javax.annotation.Nonnull;

public abstract class BaseSerializer<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    BaseSerializer() {

    }

    @Nonnull
    protected final <V> V deserialize(@Nonnull JsonObject parent, @Nonnull JsonDeserializationContext context, @Nonnull String key, @Nonnull V defaultValue) {
        if (parent.has(key)) {
            return context.deserialize(parent.get(key), defaultValue.getClass());
        }

        return defaultValue;
    }

    protected final <V> void serialize(@Nonnull JsonObject parent, @Nonnull JsonSerializationContext context, @Nonnull String key, @Nonnull V value) {
        parent.add(key, context.serialize(value));
    }
}
