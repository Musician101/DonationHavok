package io.musician101.donationhavok.streamlabs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.havok.HavokRewards;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class StreamLabsTracker {

    private final boolean generateBook;
    private final boolean replaceUnbreakableBlocks;
    private long lastDonationTime = -1;
    private int ticksSinceLastRun = 0;
    private final int delay;
    private final List<IBlockState> nonReplaceableBlocks;
    private final TreeMap<Double, HavokRewards> rewards;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String accessToken;
    private final String mcName;
    private final String streamerName;

    /** Default Settings */
    public StreamLabsTracker() {
        this.generateBook = true;
        this.replaceUnbreakableBlocks = false;
        this.delay = 10;
        this.nonReplaceableBlocks = Collections.singletonList(Blocks.CHEST.getDefaultState());
        this.rewards = new TreeMap<>();
        rewards.put(1D, new HavokRewards());
        this.accessToken = "Your Access Token Here!";
        this.mcName = "Your Minecraft name here.";
        this.streamerName = "Your streamer name here.";
    }

    public StreamLabsTracker(boolean generateBook, boolean replaceUnbreakableBlocks, int delay, List<IBlockState> nonReplaceableBlocks, TreeMap<Double, HavokRewards> rewards, String accessToken, String mcName, String streamerName) throws ParseException {
        this.generateBook = generateBook;
        this.replaceUnbreakableBlocks = replaceUnbreakableBlocks;
        this.delay = delay;
        this.nonReplaceableBlocks = nonReplaceableBlocks;
        this.rewards = rewards;
        this.accessToken = accessToken;
        this.mcName = mcName;
        this.streamerName = streamerName;
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

    public int getDelay() {
        return delay;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public String getMCName() {
        return mcName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<IBlockState> getNonReplaceableBlocks() {
        return nonReplaceableBlocks;
    }

    public TreeMap<Double, HavokRewards> getRewards() {
        return rewards;
    }

    public boolean generateBook() {
        return generateBook;
    }

    public boolean replaceUnbreakableBlocks() {
        return replaceUnbreakableBlocks;
    }

    public boolean isReplaceable(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        return (blockState.getBlockHardness(world, blockPos) != -1 || replaceUnbreakableBlocks) || nonReplaceableBlocks.contains(blockState.getBlock().getDefaultState());
    }

    // Debug code left in to bulk but commented out to prevent accidental usage.
    /*public void test() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(mcName);
        if (player != null) {
            JsonArray donations;
            try {
                donations = getDonations();
            }
            catch (IOException e) {
                DonationHavok.INSTANCE.getLogger().warn("Did you forget or mistype your access token?");
                DonationHavok.INSTANCE.getLogger().warn(e.getMessage().replace(accessToken, "<STREAMLABS_ACCESS_TOKEN>"));
                return;
            }

            List<Donation> parsedDonations = new ArrayList<>();
            for (JsonElement donation : donations) {
                JsonObject donationJsonObject = donation.getAsJsonObject();
                double amount = donationJsonObject.get("amount").getAsDouble();
                String amountLabel = donationJsonObject.get("amount_label").getAsString();
                String name = donationJsonObject.getAsJsonObject("donator").get("name").getAsString();
                String note = donationJsonObject.has("message") ? donationJsonObject.get("message").getAsString() : "";
                parsedDonations.add(new Donation(name, amount, amountLabel, note));
            }

            int delay = getDelay();
            for (Donation donation : parsedDonations) {
                DonationHavok.INSTANCE.getScheduler().scheduleTask("Running Test Donations", delay, () -> runDonation(player, donation));
                delay += getDelay();
            }
        }
    }*/

    @SubscribeEvent
    public void serverTick(ServerTickEvent event) {
        if (ticksSinceLastRun == delay) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(mcName);
            if (player != null) {
                JsonArray donations;
                try {
                    donations = getDonations();
                }
                catch (IOException e) {
                    DonationHavok.INSTANCE.getLogger().warn("Did you forget or mistype your access token?");
                    DonationHavok.INSTANCE.getLogger().warn(e.getMessage().replace(accessToken, "<STREAMLABS_ACCESS_TOKEN>"));
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

                parsedDonations.forEach(donation -> runDonation(player, donation));
            }

            ticksSinceLastRun = 0;
        }
        else {
            ticksSinceLastRun++;
        }
    }

    private JsonArray getDonations() throws IOException {
        String url = "https://streamlabs.com/api/donations?access_token=" + accessToken + "&limit=1";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "DonationHavok/1");
        connection.connect();
        return GSON.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class).getAsJsonArray("donations");
    }

    public void runDonation(EntityPlayer player, double donation) {
        Entry<Double, HavokRewards> entry = rewards.floorEntry(donation);
        if (entry == null) {
            return;
        }

        entry.getValue().wreak(player);
    }

    public void runDonation(EntityPlayer player, Donation donation) {
        Entry<Double, HavokRewards> entry = this.rewards.floorEntry(donation.getAmount());
        if (entry == null) {
            return;
        }

        HavokRewards rewards = entry.getValue();
        rewards.wreak(player, donation);
        if (generateBook) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("author", donation.getName());
            tag.setString("title", rewards.getName());
            NBTTagList pages = new NBTTagList();
            player.sendMessage(new TextComponentString(donation.getNote()));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("text", donation.getNote());
            pages.appendTag(new NBTTagString(jsonObject.toString()));
            tag.setTag("pages", pages);
            ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
            book.setTagCompound(tag);
            BlockPos pos = player.getPosition();
            World world = player.getEntityWorld();
            EntityItem item = new EntityItem(world);
            item.setItem(book);
            item.setPositionAndRotation(pos.getX(), pos.getY() + 1, pos.getZ(), 0, 0);
            world.spawnEntity(item);
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
}
