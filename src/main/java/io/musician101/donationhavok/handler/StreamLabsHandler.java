package io.musician101.donationhavok.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.config.RewardsConfig;
import io.musician101.donationhavok.config.StreamlabsConfig;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static io.musician101.donationhavok.util.json.Keys.GSON;

public class StreamlabsHandler {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private long lastDonationTime = -1;
    private int ticksSinceLastRun = 0;

    private JsonArray getDonations() throws IOException {
        String url = "https://streamlabs.com/api/donations?access_token=" + DonationHavok.getInstance().getConfig().getStreamlabsConfig().getAccessToken() + "&limit=1";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "DonationHavok/1");
        connection.connect();
        return GSON.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class).getAsJsonArray("donations");
    }

    @SubscribeEvent
    public void serverTick(ServerTickEvent event) {
        StreamlabsConfig slc = DonationHavok.getInstance().getConfig().getStreamlabsConfig();
        if (ticksSinceLastRun == slc.getDelay() && slc.isEnabled()) {
            if (!DonationHavok.getInstance().getConfig().getStreamlabsConfig().isEnabled()) {
                return;
            }

            DonationHavok instance = DonationHavok.getInstance();
            instance.getConfig().getGeneralConfig().getPlayer().ifPresent(player -> {
                JsonArray donations;
                try {
                    donations = getDonations();
                }
                catch (IOException e) {
                    instance.getLogger().warn("Did you forget or mistype your access token?");
                    instance.getLogger().warn(e.getMessage().replace(slc.getAccessToken(), "<STREAM_LABS_ACCESS_TOKEN>"));
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

                parsedDonations.forEach(this::runDonation);
            });
            ticksSinceLastRun = 0;
        }
        else {
            ticksSinceLastRun++;
        }
    }

    public void runDonation(Donation donation) {
        RewardsConfig rewardsConfig = DonationHavok.getInstance().getConfig().getRewardsConfig();
        rewardsConfig.getRewardContents(SaleHandler.getDiscountedAmount(donation.getAmount())).ifPresent(rewards -> {
            Double rewardAmount = rewardsConfig.getRewards().floorKey(donation.getAmount());
            if (rewardAmount != null) {
                DonationHavok.getInstance().getDiscoveryHandler().rewardsDiscovered(rewardAmount, donation.getName(), rewards);
            }

            rewards.wreak(donation);
        });
    }

    public void runDonation(@Nonnull PlayerEntity player, double tier) {
        DonationHavok.getInstance().getConfig().getRewardsConfig().getRewardContents(SaleHandler.getDiscountedAmount(tier)).ifPresent(rewards -> rewards.wreak(player));
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
}
