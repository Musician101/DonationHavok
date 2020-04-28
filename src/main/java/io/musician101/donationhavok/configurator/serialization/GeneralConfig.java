package io.musician101.donationhavok.configurator.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class GeneralConfig extends AbstractConfig<GeneralConfig> {

    private final boolean generateBook;
    private final boolean hideCurrentUntilDiscovered;
    private final boolean replaceUnbreakableBlocks;
    private final int delay;
    @Nonnull
    private final List<String> nonReplaceableBlocks;
    @Nonnull
    private final String mcName;

    public GeneralConfig() {
        this(true, false, false, 0, Collections.singletonList("minecraft:chest"), "Musician101");
    }

    public GeneralConfig(boolean generateBook, boolean hideCurrentUntilDiscovered, boolean replaceUnbreakableBlocks, @Nonnegative int delay, @Nonnull List<String> nonReplaceableBlocks, @Nonnull String mcName) {
        this.generateBook = generateBook;
        this.hideCurrentUntilDiscovered = hideCurrentUntilDiscovered;
        this.replaceUnbreakableBlocks = replaceUnbreakableBlocks;
        this.delay = delay;
        this.nonReplaceableBlocks = nonReplaceableBlocks;
        this.mcName = mcName;
    }

    public int getDelay() {
        return delay;
    }

    @Nonnull
    public String getMCName() {
        return mcName;
    }

    @Nonnull
    public List<String> getNonReplaceableBlocks() {
        return nonReplaceableBlocks;
    }

    public boolean generateBook() {
        return generateBook;
    }

    public boolean hideCurrentUntilDiscovered() {
        return hideCurrentUntilDiscovered;
    }

    public boolean replaceUnbreakableBlocks() {
        return replaceUnbreakableBlocks;
    }

    @Nonnull
    @Override
    protected File getFile() {
        return new File("general.json");
    }

    public static class Serializer extends BaseSerializer<GeneralConfig> {

        @Override
        public GeneralConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean generateBook = deserialize(jsonObject, context, "generate_book", true);
            boolean hideDiscoveriesUntilDiscovered = deserialize(jsonObject, context, "hide_current_until_discovered", false);
            boolean replaceUnbreakableBlocks = deserialize(jsonObject, context, "replace_unbreakable_blocks", false);
            int delay = deserialize(jsonObject, context, "delay", 10);
            List<String> nonReplaceableBlocks = deserialize(jsonObject, context, "non_replaceable_blocks", Collections.singletonList("minecraft:chest"));
            String mcName = deserialize(jsonObject, context, "mc_name", "Your Minecraft name here!");
            return new GeneralConfig(generateBook, hideDiscoveriesUntilDiscovered, replaceUnbreakableBlocks, delay, nonReplaceableBlocks, mcName);
        }

        @Override
        public JsonElement serialize(GeneralConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, "generate_book", src.generateBook());
            serialize(jsonObject, context, "hide_current_until_discovered", src.hideCurrentUntilDiscovered());
            serialize(jsonObject, context, "replace_unbreakable_blocks", src.replaceUnbreakableBlocks());
            serialize(jsonObject, context, "delay", src.getDelay());
            serialize(jsonObject, context, "mc_name", src.getMCName());
            serialize(jsonObject, context, "non_replaceable_blocks", src.getNonReplaceableBlocks());
            return jsonObject;
        }
    }
}
