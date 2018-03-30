package io.musician101.donationhavok.util.json.adapter.list;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObjectListSerializer<V> implements ListSerializer<V> {

    private final Class<V> valueClass;

    @SuppressWarnings("unchecked")
    public ObjectListSerializer(Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    public List<V> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(jsonObject -> context.<V>deserialize(jsonObject, valueClass)).collect(Collectors.toList());
    }

    @Override
    public JsonElement serialize(List<V> src, Type typeOfSrc, JsonSerializationContext context) {
        return src.stream().map(context::serialize).collect(Keys.jsonArrayCollector());
    }
}
