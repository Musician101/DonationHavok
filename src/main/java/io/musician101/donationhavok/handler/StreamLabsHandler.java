package io.musician101.donationhavok.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.StreamLabsHandler.Serializer;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import static io.musician101.donationhavok.util.json.JsonKeyProcessor.GSON;

@TypeOf(Serializer.class)
public class StreamLabsHandler {

    @Nonnull
    private final String accessToken;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final int delay;
    private final boolean enabled;
    private long lastDonationTime = -1;
    private int ticksSinceLastRun = 0;

    public StreamLabsHandler() {
        this.accessToken = "Your StreamLabs access token here!";
        this.delay = 10;
        enabled = false;
    }

    private StreamLabsHandler(@Nonnull String accessToken, int delay, boolean enabled) throws ParseException {
        this.accessToken = accessToken;
        this.delay = delay;
        this.enabled = enabled;
        if (enabled) {
            try {
                for (JsonElement donation : getDonations()) {
                    long donationTime = dateFormat.parse(donation.getAsJsonObject().get("created_at").getAsString()).getTime();
                    lastDonationTime = Math.max(donationTime, lastDonationTime);
                }
            }
            catch (IOException e) {
                DonationHavok.INSTANCE.getLogger().warn(e.getMessage().replace(accessToken, "<STREAMLABS_ACCESS_TOKEN>"));
            }
        }
    }

    @Nonnull
    public String getAccessToken() {
        return accessToken;
    }

    public int getDelay() {
        return delay;
    }

    private JsonArray getDonations() throws IOException {
        String url = "https://streamlabs.com/api/donations?access_token=" + accessToken + "&limit=1";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "DonationHavok/1");
        connection.connect();
        return GSON.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class).getAsJsonArray("donations");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void runDonation(@Nonnull Donation donation) {
        HavokRewardsHandler handler = DonationHavok.INSTANCE.getRewardsHandler();
        handler.getPlayer().ifPresent(player -> handler.getRewards(donation.getAmount()).ifPresent(rewards -> {
            Double rewardAmount = handler.getRewards().floorKey(donation.getAmount());
            if (rewardAmount != null) {
                DonationHavok.INSTANCE.getDiscoveryHandler().rewardsDiscovered(rewardAmount, donation.getName(), rewards);
            }

            handler.generateBook(player, donation.getName(), donation.getNote(), rewards);
            rewards.wreak(donation);
        }));
    }

    @SubscribeEvent
    public void serverTick(ServerTickEvent event) {
        if (ticksSinceLastRun == delay) {
            DonationHavok instance = DonationHavok.INSTANCE;
            JsonArray donations;
            try {
                donations = getDonations();
            }
            catch (IOException e) {
                instance.getLogger().warn("Did you forget or mistype your access token?");
                instance.getLogger().warn(e.getMessage().replace(accessToken, "<STREAM_LABS_ACCESS_TOKEN>"));
                return;
            }

            List<Donation> parsedDonations = new ArrayList<>();
            for (JsonElement donation : donations) {
                try {
                    JsonObject donationJsonObject = donation.getAsJsonObject();
                    long donationTime = dateFormat.parse(donationJsonObject.get("created_at").getAsString()).getTime();
                    if (donationTime > lastDonationTime) {
                        double amount = donationJsonObject.get("amount").getAsDouble();
                        String amountLabel = donationJsonObject.get("amount_label").getAsString();
                        String name = donationJsonObject.getAsJsonObject("donator").get("name").getAsString();
                        String note = donationJsonObject.has("message") ? donationJsonObject.get("message").getAsString() : "";
                        parsedDonations.add(new Donation(name, amount, amountLabel, note));
                        lastDonationTime = donationTime;
                    }
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            instance.getRewardsHandler().getPlayer().ifPresent(player -> parsedDonations.forEach(this::runDonation));
            ticksSinceLastRun = 0;
        }
        else {
            ticksSinceLastRun++;
        }
    }

    public static class Donation {

        private final double amount;
        private final String amountLabel;
        private final String name;
        private final String note;

        public Donation(String name, double amount, String amountLabel, String note) {
            this.name = name;
            this.amount = amount;
            this.amountLabel = amountLabel;
            this.note = note;
        }

        public double getAmount() {
            return amount;
        }

        public String getAmountLabel() {
            return amountLabel;
        }

        public String getName() {
            return name;
        }

        public String getNote() {
            return note;
        }
    }

    public static class Serializer extends BaseSerializer<StreamLabsHandler> {

        @Override
        public StreamLabsHandler deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 10);
            String accessToken = deserialize(jsonObject, context, Keys.ACCESS_TOKEN, "Your StreamLabs access token here!");
            boolean enabled = deserialize(jsonObject, context, Keys.ENABLE, false);
            try {
                return new StreamLabsHandler(accessToken, delay, enabled);
            }
            catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(StreamLabsHandler src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ACCESS_TOKEN, src.getAccessToken());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            return jsonObject;
        }
    }
}
