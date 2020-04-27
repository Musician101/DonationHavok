package io.musician101.donationhavok.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class GeneralConfig extends AbstractConfig<GeneralConfig> {

    private final boolean generateBook;
    private final boolean hideCurrentUntilDiscovered;
    private final boolean replaceUnbreakableBlocks;
    private final int delay;
    @Nonnull
    private final List<Block> nonReplaceableBlocks;
    @Nonnull
    private final String mcName;

    public GeneralConfig() {
        this(true, false, false, 0, Collections.singletonList(Blocks.CHEST), "Musician101");
    }

    public GeneralConfig(boolean generateBook, boolean hideCurrentUntilDiscovered, boolean replaceUnbreakableBlocks, int delay, @Nonnull List<Block> nonReplaceableBlocks, @Nonnull String mcName) {
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
    public List<Block> getNonReplaceableBlocks() {
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

    public boolean isReplaceable(@Nonnull World world, @Nonnull BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.getBlockHardness(world, blockPos) != -1 || replaceUnbreakableBlocks || nonReplaceableBlocks.contains(blockState.getBlock());
    }

    @Nonnull
    public Optional<PlayerEntity> getPlayer() {
        MinecraftServer server = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        if (server.isDedicatedServer()) {
            server.getPlayerList().getPlayerByUsername(mcName);
        }

        return Optional.ofNullable(Minecraft.getInstance().player);
    }

    @Nonnull
    @Override
    protected File getFile() {
        return new File(getDir(), "general.json");
    }

    public static class Serializer extends BaseSerializer<GeneralConfig> {

        @Override
        public GeneralConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean generateBook = deserialize(jsonObject, context, Keys.GENERATE_BOOK, true);
            boolean hideDiscoveriesUntilDiscovered = deserialize(jsonObject, context, Keys.HIDE_CURRENT_UNTIL_DISCOVERED, false);
            boolean replaceUnbreakableBlocks = deserialize(jsonObject, context, Keys.REPLACE_UNBREAKABLE_BLOCKS, false);
            int delay = deserialize(jsonObject, context, Keys.DELAY, 10);
            List<Block> nonReplaceableBlocks = deserialize(jsonObject, context, Keys.NON_REPLACEABLE_BLOCKS, Collections.singletonList(ForgeRegistries.BLOCKS.getKey(Blocks.CHEST).toString())).stream().map(ResourceLocation::new).map(ForgeRegistries.BLOCKS::getValue).filter(Objects::nonNull).collect(Collectors.toList());
            String mcName = deserialize(jsonObject, context, Keys.MC_NAME, "Your Minecraft name here!");
            return new GeneralConfig(generateBook, hideDiscoveriesUntilDiscovered, replaceUnbreakableBlocks, delay, nonReplaceableBlocks, mcName);
        }

        @Override
        public JsonElement serialize(GeneralConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.GENERATE_BOOK, src.generateBook());
            serialize(jsonObject, context, Keys.HIDE_CURRENT_UNTIL_DISCOVERED, src.hideCurrentUntilDiscovered());
            serialize(jsonObject, context, Keys.REPLACE_UNBREAKABLE_BLOCKS, src.replaceUnbreakableBlocks());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.MC_NAME, src.getMCName());
            serialize(jsonObject, context, Keys.NON_REPLACEABLE_BLOCKS, src.getNonReplaceableBlocks().stream().map(ForgeRegistries.BLOCKS::getKey).filter(Objects::nonNull).map(ResourceLocation::toString).collect(Collectors.toList()));
            return jsonObject;
        }
    }
}
