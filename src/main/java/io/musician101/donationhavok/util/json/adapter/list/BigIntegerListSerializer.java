package io.musician101.donationhavok.util.json.adapter.list;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BigIntegerListSerializer implements ListSerializer<BigInteger> {

    @Override
    public List<BigInteger> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(JsonElement::getAsBigInteger).collect(Collectors.toList());
    }

    @Override
    public JsonElement serialize(List<BigInteger> src, Type typeOfSrc, JsonSerializationContext context) {
        return src.stream().map(JsonPrimitive::new).collect(Keys.jsonArrayCollector());
    }
}
