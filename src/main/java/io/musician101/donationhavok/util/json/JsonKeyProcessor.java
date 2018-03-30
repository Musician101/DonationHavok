package io.musician101.donationhavok.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.util.json.adapter.AdapterOf;
import io.musician101.donationhavok.util.json.adapter.AdapterType;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.reflections.Reflections;

import static com.google.gson.internal.Primitives.isWrapperType;

@SuppressWarnings("unchecked")
public class JsonKeyProcessor {

    private static final List<JsonKeyImpl<? extends JsonElement, ?>> KEYS = new ArrayList<>();
    public static Gson GSON;

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <J extends JsonElement, V> Optional<JsonKeyImpl<J, V>> getJsonKey(String key) {
        return KEYS.stream().map(jsonKey -> {
            try {
                return (JsonKeyImpl<J, V>) jsonKey;
            }
            catch (ClassCastException e) {
                return null;
            }
        }).filter(Objects::nonNull).filter(jsonKey -> key.equals(jsonKey.getKey())).findFirst();
    }

    private static void handleJsonKey(JsonKeyImpl<? extends JsonElement, ?> jsonKey, GsonBuilder builder) {
        Optional typeAdapter = jsonKey.getTypeAdapter();
        if (typeAdapter.isPresent()) {
            if (!(typeAdapter.get() instanceof JsonSerializer<?> || typeAdapter.get() instanceof JsonDeserializer<?> || typeAdapter.get() instanceof InstanceCreator<?> || typeAdapter.get() instanceof TypeAdapter<?>)) {
                DonationHavok.INSTANCE.getLogger().warn("Warning: " + jsonKey.getKey() + " has an invalid type adapter.");
            }

            builder.registerTypeAdapter(jsonKey.getTokenType().getType(), typeAdapter.get());
            KEYS.add(jsonKey);
        }
        else {
            Type type = jsonKey.getTokenType().getType();
            if (!isWrapperType(jsonKey.getTokenType().getType()) && !type.equals(String.class)) {
                DonationHavok.INSTANCE.getLogger().warn(jsonKey.getKey() + " does not have a type adapter. Skipping.");
            }
        }
    }

    public static void init() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation();
        Reflections reflections = new Reflections("io.musician101.donationhavok");
        reflections.getTypesAnnotatedWith(JsonKeyCatalog.class).forEach(aClass -> {
            List<Field> fields = Arrays.asList(aClass.getFields());
            Stream.of(aClass.getFields()).filter(field -> Modifier.isStatic(field.getModifiers())).map(field -> {
                try {
                    return field.get(null);
                }
                catch (IllegalAccessException e) {
                    DonationHavok.INSTANCE.getLogger().error("Error! Failed to parse an instance of JsonKey in the catalog " + aClass.getCanonicalName() + ". Index: " + fields.indexOf(field));
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).filter(JsonKeyImpl.class::isInstance).map(JsonKeyImpl.class::cast).forEach(jsonKey -> handleJsonKey(jsonKey, builder));
        });

        reflections.getTypesAnnotatedWith(JsonKey.class).forEach(aClass -> {
            JsonKey jsonKey = aClass.getAnnotation(JsonKey.class);
            Class<?> typeAdapter = jsonKey.typeAdapter();
            try {
                handleJsonKey(new JsonKeyImpl<>(jsonKey.key(), TypeToken.get(aClass), jsonKey.typeAdapter().newInstance()), builder);
            }
            catch (InstantiationException | IllegalAccessException e) {
                DonationHavok.INSTANCE.getLogger().error("Error! " + typeAdapter.getCanonicalName() + " does not have a valid no args constructor.");
            }
        });

        reflections.getTypesAnnotatedWith(JsonKeys.class).forEach(aClass -> {
            JsonKeys jsonKeys = aClass.getAnnotation(JsonKeys.class);
            Class<?> typeAdapter = jsonKeys.typeAdapter();
            Stream.of(jsonKeys.keys()).forEach(key -> {
                try {
                    handleJsonKey(new JsonKeyImpl<>(key, TypeToken.get(aClass), typeAdapter.newInstance()), builder);
                }
                catch (InstantiationException | IllegalAccessException e) {
                    DonationHavok.INSTANCE.getLogger().error("Error! " + typeAdapter.getCanonicalName() + " does not have a valid no args constructor.");
                }
            });
        });

        reflections.getTypesAnnotatedWith(TypeOf.class).forEach((Class<?> aClass) -> {
            Class<?> typeAdapter = aClass.getAnnotation(TypeOf.class).value();
            try {
                builder.registerTypeAdapter(aClass, typeAdapter.newInstance());
            }
            catch (InstantiationException | IllegalAccessException e) {
                DonationHavok.INSTANCE.getLogger().error("Error! " + typeAdapter.getCanonicalName() + " does not have a valid no args constructor.", e);
            }
        });

        reflections.getTypesAnnotatedWith(AdapterOf.class).forEach(aClass -> {
            Optional<TypeToken<?>> adapterType = Stream.of(aClass.getFields()).filter(field -> Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(AdapterType.class)).map(field -> {
                try {
                    return (TypeToken<?>) field.get(null);
                }
                catch (IllegalAccessException e) {
                    DonationHavok.INSTANCE.getLogger().error("Error! Failed to get the value of " + field.getName() + " in " + aClass.getCanonicalName());
                    return (TypeToken<?>) null;
                }
            }).filter(Objects::nonNull).findFirst();
            if (!adapterType.isPresent()) {
                DonationHavok.INSTANCE.getLogger().error("Error! " + aClass.getCanonicalName() + " has annotation " + AdapterOf.class.getCanonicalName() + " but does not contain a static field with annotation " + AdapterType.class.getCanonicalName());
                return;
            }

            try {
                builder.registerTypeAdapter(adapterType.get().getType(), aClass.newInstance());
            }
            catch (InstantiationException | IllegalAccessException e) {
                DonationHavok.INSTANCE.getLogger().error("Error! " + aClass.getCanonicalName() + " does not have a valid no args constructor.");
            }
        });

        GSON = builder.create();
    }
}
