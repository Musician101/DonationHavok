package io.musician101.donationhavok.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.havok.HavokBlock;
import io.musician101.donationhavok.havok.HavokCommand;
import io.musician101.donationhavok.havok.HavokEntity;
import io.musician101.donationhavok.havok.HavokItemStack;
import io.musician101.donationhavok.havok.HavokMessage;
import io.musician101.donationhavok.havok.HavokParticle;
import io.musician101.donationhavok.havok.HavokRewards;
import io.musician101.donationhavok.havok.HavokSound;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker;
import io.musician101.donationhavok.util.json.typeadapter.BlockStateTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokBlockTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokCommandTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokEntityTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokItemStackTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokMessageTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokParticleTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokRewardsTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.HavokSoundTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.ItemStackTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.NBTPrimitiveTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.NBTTagByteArrayTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.NBTTagCompoundTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.NBTTagIntArrayTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.NBTTagListTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.NonReplaceableBlocksTypeAdapter;
import io.musician101.donationhavok.util.json.typeadapter.StreamLabsTrackerTypeAdapter;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

public class JsonUtils {

    // @formatter:off
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(IBlockState.class, new BlockStateTypeAdapter())
            .registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter())
            .registerTypeAdapter(HavokBlock.class, new HavokBlockTypeAdapter())
            .registerTypeAdapter(HavokCommand.class, new HavokCommandTypeAdapter())
            .registerTypeAdapter(HavokItemStack.class, new HavokItemStackTypeAdapter())
            .registerTypeAdapter(HavokEntity.class, new HavokEntityTypeAdapter())
            .registerTypeAdapter(HavokMessage.class, new HavokMessageTypeAdapter())
            .registerTypeAdapter(HavokParticle.class, new HavokParticleTypeAdapter())
            .registerTypeAdapter(HavokSound.class, new HavokSoundTypeAdapter())
            .registerTypeAdapter(NBTPrimitive.class, new NBTPrimitiveTypeAdapter())
            .registerTypeAdapter(NBTTagByteArray.class, new NBTTagByteArrayTypeAdapter())
            .registerTypeAdapter(NBTTagIntArray.class, new NBTTagIntArrayTypeAdapter())
            .registerTypeAdapter(NBTTagList.class, new NBTTagListTypeAdapter())
            .registerTypeAdapter(NBTTagCompound.class, new NBTTagCompoundTypeAdapter())
            .registerTypeAdapter(HavokRewards.class, new HavokRewardsTypeAdapter())
            .registerTypeAdapter(new TypeToken<List<IBlockState>>(){}.getType(), new NonReplaceableBlocksTypeAdapter())
            .registerTypeAdapter(StreamLabsTracker.class, new StreamLabsTrackerTypeAdapter())
            .create();
    // @formatter:on
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ALLOW_TARGET_VIA_NOTE = "allow_target_via_note";
    public static final String BLOCK_STATE = "block_state";
    public static final String BLOCKS = "blocks";
    public static final String BROADCAST = "broadcast";
    public static final String BYTE = "byte";
    public static final String COMMAND = "command";
    public static final String COMMANDS = "commands";
    public static final String DELAY = "delay";
    public static final String ENTITIES = "entities";
    public static final String GENERATE_BOOK = "generate_book";
    public static final String ID = "id";
    public static final String INTEGER = "int";
    public static final String ITEMS = "items";
    public static final String ITEM_STACK = "item_stack";
    public static final String MC_NAME = "mc_name";
    public static final String MESSAGE = "message";
    public static final String MESSAGES = "messages";
    public static final String MIN_AMOUNT = "min_amount";
    public static final String NAME = "name";
    public static final String NBT = "nbt";
    public static final String NON_REPLACEABLE_BLOCKS = "non_replaceable_blocks";
    public static final String PARTICLES = "particles";
    public static final String PITCH = "pitch";
    public static final String REPLACE_UNBREAKABLE_BLOCKS = "replace_unbreakable_blocks";
    public static final String REWARDS = "rewards";
    public static final String SOUNDS = "sounds";
    public static final String STREAMER_NAME = "streamer_name";
    public static final String TARGET_ALL_PLAYERS = "target_all_players";
    public static final String TARGET_PLAYERS = "target_players";
    public static final String TILE_ENTITY = "tile_entity";
    public static final String TRIGGER_TIERS = "trigger_tiers";
    public static final String VOLUME = "volume";
    public static final String X_OFFSET = "x_offset";
    public static final String Y_OFFSET = "y_offset";
    public static final String Z_OFFSET = "z_offset";
    public static final String X_VELOCITY = "x_velocity";
    public static final String Y_VELOCITY = "y_velocity";
    public static final String Z_VELOCITY = "z_velocity";

    private JsonUtils() {

    }
}
