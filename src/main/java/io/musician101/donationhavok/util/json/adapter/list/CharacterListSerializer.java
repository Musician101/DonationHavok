package io.musician101.donationhavok.util.json.adapter.list;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CharacterListSerializer implements ListSerializer<Character> {

    @Override
    public List<Character> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(JsonElement::getAsCharacter).collect(Collectors.toList());
    }

    @Override
    public JsonElement serialize(List<Character> src, Type typeOfSrc, JsonSerializationContext context) {
        return src.stream().map(JsonPrimitive::new).collect(Keys.jsonArrayCollector());
    }
}
