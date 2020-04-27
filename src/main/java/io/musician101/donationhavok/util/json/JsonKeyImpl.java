package io.musician101.donationhavok.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static io.musician101.donationhavok.util.json.Keys.GSON;

@SuppressWarnings("unchecked")
public class JsonKeyImpl<J extends JsonElement, V> {

    @Nonnull
    private final String key;
    @Nonnull
    private final TypeToken<V> tokenType;

    private JsonKeyImpl(@Nonnull String key, @Nonnull TypeToken<V> tokenType) {
        this.key = key;
        this.tokenType = tokenType;
    }

    public static JsonKeyImpl<JsonPrimitive, BigDecimal> bigDecimalKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(BigDecimal.class));
    }

    public static JsonKeyImpl<JsonPrimitive, BigInteger> bigIntegerKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(BigInteger.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Boolean> booleanKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Boolean.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Byte> byteKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Byte.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Character> characterKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Character.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Double> doubleKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Double.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Float> floatKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Float.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Integer> integerKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Integer.class));
    }

    public static <J extends JsonElement, V> JsonKeyImpl<J, V> key(@Nonnull String key, @Nonnull TypeToken<V> typeToken) {
        return new JsonKeyImpl<>(key, typeToken);
    }

    public static <V> JsonKeyImpl<JsonArray, List<V>> listKey(@Nonnull String key, Class<V> valueClass) {
        return new JsonKeyImpl<>(key, (TypeToken<List<V>>) TypeToken.getParameterized(List.class, valueClass));
    }

    public static <K, V> JsonKeyImpl<JsonArray, Map<K, V>> listKey(@Nonnull String key, Class<K> keyClass, Class<V> valueClass) {
        return new JsonKeyImpl<>(key, (TypeToken<Map<K, V>>) TypeToken.getParameterized(Map.class, keyClass, valueClass));
    }

    public static JsonKeyImpl<JsonPrimitive, Long> longKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Long.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Number> numberKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Number.class));
    }

    public static JsonKeyImpl<JsonPrimitive, Short> shortKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(Short.class));
    }

    public static JsonKeyImpl<JsonPrimitive, String> stringKey(@Nonnull String key) {
        return new JsonKeyImpl<>(key, TypeToken.get(String.class));
    }

    @Nonnull
    public Optional<V> deserialize(@Nonnull J json) {
        return deserialize(json, null);
    }

    @Nonnull
    public Optional<V> deserialize(@Nonnull J json, @Nullable JsonDeserializationContext context) {
        try {
            return Optional.of(context == null ? GSON.fromJson(json, tokenType.getType()) : context.deserialize(json, tokenType.getType()));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    @Nonnull
    public Optional<V> deserializeFromParent(@Nonnull JsonObject parent) {
        return deserializeFromParent(parent, null);
    }

    @Nonnull
    public Optional<V> deserializeFromParent(@Nonnull JsonObject parent, @Nullable JsonDeserializationContext context) {
        if (parent.has(key)) {
            return deserialize((J) parent.get(key), context);
        }

        return Optional.empty();
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public TypeToken<V> getTokenType() {
        return tokenType;
    }

    @Nonnull
    public J serialize(@Nonnull V value) {
        return serialize(value, (JsonSerializationContext) null);
    }

    @Nonnull
    public J serialize(@Nonnull V value, @Nullable JsonSerializationContext context) {
        return (J) (context == null ? GSON.toJsonTree(value) : context.serialize(value, tokenType.getType()));
    }

    public void serialize(@Nonnull V value, @Nonnull JsonObject jsonObject) {
        serialize(value, jsonObject, null);
    }

    public void serialize(@Nonnull V value, @Nonnull JsonObject jsonObject, @Nullable JsonSerializationContext context) {
        J json = serialize(value, context);
        jsonObject.add(key, json);
    }
}
