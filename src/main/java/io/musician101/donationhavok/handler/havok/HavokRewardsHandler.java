package io.musician101.donationhavok.handler.havok;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class HavokRewardsHandler {

    private static final SaleHandler SALE_HANDLER = new SaleHandler();
    private final int delay;
    private final boolean generateBook;
    @Nonnull
    private final String mcName;
    @Nonnull
    private final List<IBlockState> nonReplaceableBlocks;
    private final boolean replaceUnbreakableBlocks;
    @Nonnull
    private final TreeMap<Double, HavokRewards> rewards = new TreeMap<>(Double::compare);

    public HavokRewardsHandler() {
        this(0, true, "Your Minecraft name here!", Collections.singletonList(Blocks.CHEST.getDefaultState()), false, ImmutableMap.of(1D, new HavokRewards()));
    }

    private HavokRewardsHandler(int delay, boolean generateBook, @Nonnull String mcName, @Nonnull List<IBlockState> nonReplaceableBlocks, boolean replaceUnbreakableBlocks, Map<Double, HavokRewards> rewards) {
        this.delay = delay;
        this.generateBook = generateBook;
        this.mcName = mcName;
        this.nonReplaceableBlocks = nonReplaceableBlocks;
        this.replaceUnbreakableBlocks = replaceUnbreakableBlocks;
        this.rewards.putAll(rewards);
    }

    public static void startSale(double discount, int saleLength) {
        SALE_HANDLER.startSale(discount, saleLength);
    }

    public static void stopSale() {
        SALE_HANDLER.stopSale();
    }

    public boolean generateBook() {
        return generateBook;
    }

    public void generateBook(@Nonnull EntityPlayer player, @Nonnull String donor, @Nonnull String message, @Nonnull HavokRewards rewards) {
        if (DonationHavok.INSTANCE.getRewardsHandler().generateBook()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("author", donor);
            tag.setString("title", rewards.getName());
            NBTTagList pages = new NBTTagList();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("text", message);
            pages.appendTag(new NBTTagString(jsonObject.toString()));
            tag.setTag("pages", pages);
            ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
            book.setTagCompound(tag);
            BlockPos pos = player.getPosition();
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
            EntityItem item = new EntityItem(world);
            item.setItem(book);
            item.setPositionAndRotation(pos.getX(), pos.getY() + 1, pos.getZ(), 0, 0);
            world.spawnEntity(item);
        }
    }

    public int getDelay() {
        return delay;
    }

    @Nonnull
    public String getMCName() {
        return mcName;
    }

    @Nonnull
    public List<IBlockState> getNonReplaceableBlocks() {
        return nonReplaceableBlocks;
    }

    @Nonnull
    public Optional<EntityPlayer> getPlayer() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            return Optional.ofNullable(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(mcName));
        }
        else {
            return Optional.ofNullable(Minecraft.getMinecraft().player);
        }
    }

    @Nonnull
    public Optional<HavokRewards> getRewards(double tier) {
        return Optional.ofNullable(rewards.floorEntry(SALE_HANDLER.isRunning() ? tier : tier / SALE_HANDLER.getDiscount())).map(Entry::getValue);
    }

    @Nonnull
    public TreeMap<Double, HavokRewards> getRewards() {
        return rewards;
    }

    public boolean isReplaceable(@Nonnull World world, @Nonnull BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        return blockState.getBlockHardness(world, blockPos) != -1 || replaceUnbreakableBlocks || nonReplaceableBlocks.contains(blockState.getBlock().getDefaultState());

    }

    public boolean replaceUnbreakableBlocks() {
        return replaceUnbreakableBlocks;
    }

    public void runDonation(@Nonnull EntityPlayer player, double tier) {
        getRewards(tier).ifPresent(rewards -> rewards.wreak(player));
    }

    // Debug code left in to bulk test but commented out to prevent accidental usage.
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
                String name = donationJsonObject.getAsJsonObject("donor").get("name").getAsString();
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

    public static class Serializer extends BaseSerializer<HavokRewardsHandler> {

        @Override
        public HavokRewardsHandler deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean generateBook = deserialize(jsonObject, context, Keys.GENERATE_BOOK, true);
            boolean replaceUnbreakableBlocks = deserialize(jsonObject, context, Keys.REPLACE_UNBREAKABLE_BLOCKS, false);
            int delay = deserialize(jsonObject, context, Keys.DELAY, 10);
            List<IBlockState> nonReplaceableBlocks = deserialize(jsonObject, context, Keys.NON_REPLACEABLE_BLOCKS, Collections.singletonList(Block.REGISTRY.getNameForObject(Blocks.CHEST).toString()))
                    .stream().map(ResourceLocation::new).map(Block.REGISTRY::getObject).map(Block::getDefaultState).collect(Collectors.toList());
            TreeMap<Double, HavokRewards> rewardsMap = new TreeMap<>(Double::compare);
            rewardsMap.putAll(deserialize(jsonObject, context, Keys.REWARDS, new TempHavokRewardsStorage()).rewards);
            String mcName = deserialize(jsonObject, context, Keys.MC_NAME, "Your Minecraft name here!");
            return new HavokRewardsHandler(delay, generateBook, mcName, nonReplaceableBlocks, replaceUnbreakableBlocks, rewardsMap);
        }

        @Override
        public JsonElement serialize(HavokRewardsHandler src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.GENERATE_BOOK, src.generateBook());
            serialize(jsonObject, context, Keys.MC_NAME, src.getMCName());
            serialize(jsonObject, context, Keys.NON_REPLACEABLE_BLOCKS, src.getNonReplaceableBlocks().stream().map(IBlockState::getBlock).map(Block.REGISTRY::getNameForObject).map(ResourceLocation::toString).collect(Collectors.toList()));
            serialize(jsonObject, context, Keys.REPLACE_UNBREAKABLE_BLOCKS, src.replaceUnbreakableBlocks());
            serialize(jsonObject, context, Keys.REWARDS, new TempHavokRewardsStorage(src.getRewards()));
            return jsonObject;
        }
    }
}
