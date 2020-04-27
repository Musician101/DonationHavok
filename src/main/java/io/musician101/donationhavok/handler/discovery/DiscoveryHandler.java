package io.musician101.donationhavok.handler.discovery;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.Reference;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Logger;

import static io.musician101.donationhavok.util.json.Keys.GSON;

public class DiscoveryHandler {

    @Nonnull
    private final List<Discovery> currentDiscoveries;
    @Nonnull
    private final List<Discovery> legendaryDiscoveries;

    public DiscoveryHandler() {
        this(Collections.singletonList(new Discovery(1D, "Musician101", "A Default Reward")), Collections.singletonList(new Discovery(1D, "Musician101", "A Default Reward")));
    }

    @Nonnull
    public static DiscoveryHandler load() {
        DonationHavok mod = DonationHavok.getInstance();
        Logger logger = mod.getLogger();
        File file = getFile();
        if (!file.exists()) {
            try {
                DiscoveryHandler dh = new DiscoveryHandler();
                dh.save();
                return dh;
            }
            catch (IOException e) {
                logger.error("An error occurred while creating the default discovery file.", e);
                return null;
            }
        }

        try (FileReader fr = new FileReader(file)) {
            return GSON.fromJson(fr, DiscoveryHandler.class);
        }
        catch (IOException e) {
            logger.error("An error occurred while loading the discovery.", e);
            return null;
        }
    }

    public void save() throws IOException {
        File file = getFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        try(FileWriter fw = new FileWriter(file)) {
            GSON.toJson(this, fw);
        }
    }

    private static File getFile() {
        return new File("config/" + Reference.MOD_ID +  "/discoveries.json");
    }

    private DiscoveryHandler(@Nonnull List<Discovery> currentDiscoveries, @Nonnull List<Discovery> legendaryDiscoveries) {
        this.currentDiscoveries = currentDiscoveries;
        this.legendaryDiscoveries = legendaryDiscoveries;
    }

    @Nonnull
    public List<Discovery> getCurrentDiscoveries() {
        return currentDiscoveries;
    }

    @Nonnull
    public Optional<Discovery> getCurrentDiscovery(double tier) {
        return currentDiscoveries.stream().filter(discovery -> discovery.getAmount() == tier).findFirst();
    }

    @Nonnull
    public List<Discovery> getLegendaryDiscoveries() {
        return legendaryDiscoveries;
    }

    @Nonnull
    public List<Discovery> getLegendaryDiscovery(double tier) {
        return legendaryDiscoveries.stream().filter(discovery -> discovery.getAmount() == tier).collect(Collectors.toList());
    }

    public void rewardsDiscovered(double tier, String donorName, HavokRewards rewards) {
        Discovery discovery = new Discovery(tier, donorName, rewards.getName());
        if (DonationHavok.getInstance().getConfig().getGeneralConfig().hideCurrentUntilDiscovered() && !currentDiscoveries.contains(discovery)) {
            currentDiscoveries.add(discovery);
        }
    }

    public static class Serializer extends BaseSerializer<DiscoveryHandler> {

        @Override
        public DiscoveryHandler deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            List<Discovery> currentDiscoveries = deserialize(jsonObject, context, Keys.CURRENT, Collections.emptyList());
            List<Discovery> legendaryDiscoveries = deserialize(jsonObject, context, Keys.LEGENDARY, Collections.emptyList());
            return new DiscoveryHandler(currentDiscoveries, legendaryDiscoveries);
        }

        @Override
        public JsonElement serialize(DiscoveryHandler src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.CURRENT, src.getCurrentDiscoveries());
            serialize(jsonObject, context, Keys.LEGENDARY, src.getLegendaryDiscoveries());
            return jsonObject;
        }
    }
}
