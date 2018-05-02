package io.musician101.donationhavok.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.handler.StreamLabsHandler;
import io.musician101.donationhavok.handler.discovery.Discovery;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.havok.HavokBlock;
import io.musician101.donationhavok.handler.havok.HavokCommand;
import io.musician101.donationhavok.handler.havok.HavokEntity;
import io.musician101.donationhavok.handler.havok.HavokItemStack;
import io.musician101.donationhavok.handler.havok.HavokMessage;
import io.musician101.donationhavok.handler.havok.HavokParticle;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.handler.havok.HavokSchematic;
import io.musician101.donationhavok.handler.havok.HavokSound;
import io.musician101.donationhavok.handler.havok.HavokStructure;
import io.musician101.donationhavok.handler.havok.TempHavokRewardsStorage;
import io.musician101.donationhavok.handler.twitch.TwitchHandler;
import io.musician101.donationhavok.handler.twitch.commands.CommandPermission;
import io.musician101.donationhavok.handler.twitch.commands.DiscoveryCommand;
import io.musician101.donationhavok.handler.twitch.commands.PlayersCommand;
import io.musician101.donationhavok.handler.twitch.commands.RewardsCommand;
import io.musician101.donationhavok.handler.twitch.commands.TwitchCommands;
import io.musician101.donationhavok.util.json.adapter.BlockStateSerializer;
import io.musician101.donationhavok.util.json.adapter.ItemStackSerializer;
import io.musician101.donationhavok.util.json.adapter.MirrorSerializer;
import io.musician101.donationhavok.util.json.adapter.NonReplaceableBlocksSerializer;
import io.musician101.donationhavok.util.json.adapter.PlacementSettingsSerializer;
import io.musician101.donationhavok.util.json.adapter.RotationSerializer;
import io.musician101.donationhavok.util.json.adapter.list.DoubleListSerializer;
import io.musician101.donationhavok.util.json.adapter.list.ObjectListSerializer;
import io.musician101.donationhavok.util.json.adapter.list.StringListSerializer;
import io.musician101.donationhavok.util.json.adapter.nbt.NBTTagCompoundSerializer;
import java.util.List;
import java.util.stream.Collector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.structure.template.PlacementSettings;

import static io.musician101.donationhavok.util.json.JsonKeyImpl.booleanKey;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.doubleKey;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.floatKey;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.integerKey;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.key;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.listKey;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.longKey;
import static io.musician101.donationhavok.util.json.JsonKeyImpl.stringKey;

@JsonKeyCatalog
public class Keys {

    public static final JsonKeyImpl<JsonPrimitive, String> ACCESS_TOKEN = stringKey("access_token");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> ALLOW_TARGET_VIA_NOTE = booleanKey("allow_target_via_note");
    public static final JsonKeyImpl<JsonPrimitive, Double> AMOUNT = doubleKey("amount");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> BITS_TRIGGER = booleanKey("bits_trigger");
    public static final JsonKeyImpl<JsonArray, List<HavokBlock>> BLOCKS = listKey("blocks", HavokBlock.class, new ObjectListSerializer<>(HavokBlock.class));
    public static final JsonKeyImpl<JsonObject, IBlockState> BLOCK_STATE = key("block_state", TypeToken.get(IBlockState.class), new BlockStateSerializer());
    public static final JsonKeyImpl<JsonPrimitive, Boolean> BROADCAST = booleanKey("broadcast");
    public static final String BYTE = "byte";
    public static final JsonKeyImpl<JsonPrimitive, String> COMMAND = stringKey("command");
    public static final JsonKeyImpl<JsonArray, List<HavokCommand>> COMMANDS = listKey("commands", HavokCommand.class, new ObjectListSerializer<>(HavokCommand.class));
    public static final JsonKeyImpl<JsonArray, List<CommandPermission>> COMMAND_PERMISSIONS = listKey("permissions", CommandPermission.class, new ObjectListSerializer<>(CommandPermission.class));
    public static final JsonKeyImpl<JsonArray, List<Discovery>> CURRENT = listKey("current", Discovery.class, new ObjectListSerializer<>(Discovery.class));
    public static final JsonKeyImpl<JsonPrimitive, Integer> DELAY = integerKey("delay");
    public static final JsonKeyImpl<JsonPrimitive, Double> DISCOUNT = doubleKey("discount");
    public static final JsonKeyImpl<JsonObject, DiscoveryHandler> DISCOVERY = key("discovery", TypeToken.get(DiscoveryHandler.class), new DiscoveryHandler.Serializer());
    public static final JsonKeyImpl<JsonObject, DiscoveryCommand> DISCOVERY_COMMAND = key("discovery", TypeToken.get(DiscoveryCommand.class), new DiscoveryCommand.Serializer());
    public static final JsonKeyImpl<JsonPrimitive, String> DONOR_NAME = stringKey("donor_name");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> ENABLE = booleanKey("enable");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> ENABLE_COMMANDS = booleanKey("enable");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> ENABLE_PLAYER_LIST_COMMAND = booleanKey("player_list");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> ENABLE_REWARDS_LIST_COMMAND = booleanKey("rewards_list");
    public static final JsonKeyImpl<JsonArray, List<HavokEntity>> ENTITIES = listKey("entities", HavokEntity.class, new ObjectListSerializer<>(HavokEntity.class));
    public static final JsonKeyImpl<JsonPrimitive, Boolean> FACTOR_SUB_STREAK = booleanKey("factor_sub_streak");
    public static final JsonKeyImpl<JsonObject, HavokRewardsHandler> GENERAL = key("general", TypeToken.get(HavokRewardsHandler.class), new HavokRewardsHandler.Serializer());
    public static final JsonKeyImpl<JsonPrimitive, Boolean> GENERATE_BOOK = booleanKey("generate_book");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> HIDE_CURRENT_UNTIL_DISCOVERED = booleanKey("hide_current_until_discovered");
    public static final JsonKeyImpl<JsonPrimitive, String> ID = stringKey("id");
    public static final String INTEGER = "int";
    public static final JsonKeyImpl<JsonPrimitive, Float> INTEGRITY = floatKey("integrity");
    public static final JsonKeyImpl<JsonArray, List<HavokItemStack>> ITEMS = listKey("items", HavokItemStack.class, new ObjectListSerializer<>(HavokItemStack.class));
    public static final JsonKeyImpl<JsonObject, ItemStack> ITEM_STACK = key("item_stack", TypeToken.get(ItemStack.class), new ItemStackSerializer());
    public static final JsonKeyImpl<JsonArray, List<Discovery>> LEGENDARY = listKey("legendary", Discovery.class, new ObjectListSerializer<>(Discovery.class));
    public static final JsonKeyImpl<JsonPrimitive, String> MC_NAME = stringKey("mc_name");
    public static final String MESSAGE = "message";
    public static final JsonKeyImpl<JsonArray, List<HavokMessage>> MESSAGES = listKey("messages", HavokMessage.class, new ObjectListSerializer<>(HavokMessage.class));
    public static final JsonKeyImpl<JsonPrimitive, Double> MIN_AMOUNT = doubleKey("min_amount");
    public static final JsonKeyImpl<JsonPrimitive, Mirror> MIRROR = key("mirror", TypeToken.get(Mirror.class), new MirrorSerializer());
    public static final JsonKeyImpl<JsonPrimitive, String> NAME = stringKey("name");
    public static final JsonKeyImpl<JsonObject, NBTTagCompound> NBT = key("nbt", TypeToken.get(NBTTagCompound.class), new NBTTagCompoundSerializer());
    public static final JsonKeyImpl<JsonArray, List<IBlockState>> NON_REPLACEABLE_BLOCKS = key("non_replaceable_blocks", new TypeToken<List<IBlockState>>() {

    }, new NonReplaceableBlocksSerializer());
    public static final JsonKeyImpl<JsonArray, List<HavokParticle>> PARTICLES = listKey("particles", HavokParticle.class, new ObjectListSerializer<>(HavokParticle.class));
    public static final JsonKeyImpl<JsonPrimitive, Float> PITCH = floatKey("pitch");
    public static final JsonKeyImpl<JsonObject, PlacementSettings> PLACEMENT_SETTINGS = key("placement_settings", TypeToken.get(PlacementSettings.class), new PlacementSettingsSerializer());
    public static final JsonKeyImpl<JsonObject, PlayersCommand> PLAYERS_COMMAND = key("players", TypeToken.get(PlayersCommand.class), new PlayersCommand.Serializer());
    public static final JsonKeyImpl<JsonPrimitive, String> RELATIVE_PATH = stringKey("relative_path");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> REPLACE_UNBREAKABLE_BLOCKS = booleanKey("replace_unbreakable_blocks");
    public static final JsonKeyImpl<JsonArray, TempHavokRewardsStorage> REWARDS = key("rewards", new TypeToken<TempHavokRewardsStorage>() {

    }, new TempHavokRewardsStorage.Serializer());
    public static final JsonKeyImpl<JsonObject, RewardsCommand> REWARDS_COMMAND = key("rewards", TypeToken.get(RewardsCommand.class), new RewardsCommand.Serializer());
    public static final JsonKeyImpl<JsonPrimitive, String> REWARD_NAME = stringKey("reward_name");
    public static final JsonKeyImpl<JsonPrimitive, Rotation> ROTATION = key("rotation", TypeToken.get(Rotation.class), new RotationSerializer());
    public static final JsonKeyImpl<JsonPrimitive, Boolean> ROUND_SUBS = booleanKey("round_subs");
    public static final JsonKeyImpl<JsonPrimitive, Integer> SALE_LENGTH = integerKey("sale_length");
    public static final JsonKeyImpl<JsonArray, List<HavokSchematic>> SCHEMATICS = listKey("schematics", HavokSchematic.class, new ObjectListSerializer<>(HavokSchematic.class));
    public static final JsonKeyImpl<JsonPrimitive, Long> SEED = longKey("seed");
    public static final JsonKeyImpl<JsonArray, List<HavokSound>> SOUNDS = listKey("sounds", HavokSound.class, new ObjectListSerializer<>(HavokSound.class));
    public static final JsonKeyImpl<JsonObject, StreamLabsHandler> STREAM_LABS = key("stream_labs", TypeToken.get(StreamLabsHandler.class), new StreamLabsHandler.Serializer());
    public static final JsonKeyImpl<JsonArray, List<HavokStructure>> STRUCTURES = listKey("structures", HavokStructure.class, new ObjectListSerializer<>(HavokStructure.class));
    public static final JsonKeyImpl<JsonPrimitive, String> STRUCTURE_NAME = stringKey("structure_name");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> SUBS_TRIGGER = booleanKey("subs_trigger");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> TARGET_ALL_PLAYERS = booleanKey("target_all_players");
    public static final JsonKeyImpl<JsonArray, List<String>> TARGET_PLAYERS = listKey("target_players", String.class, new StringListSerializer());
    public static final JsonKeyImpl<JsonObject, NBTTagCompound> TILE_ENTITY = key("tile_entity", TypeToken.get(NBTTagCompound.class), new NBTTagCompoundSerializer());
    public static final JsonKeyImpl<JsonPrimitive, String> TMI_TOKEN = stringKey("tmi_token");
    public static final JsonKeyImpl<JsonPrimitive, Boolean> TRIGGERS_SALE = booleanKey("trigger_sale");
    public static final JsonKeyImpl<JsonArray, List<Double>> TRIGGER_TIERS = listKey("trigger_tiers", Double.class, new DoubleListSerializer());
    public static final JsonKeyImpl<JsonObject, TwitchHandler> TWITCH = key("twitch", TypeToken.get(TwitchHandler.class), new TwitchHandler.Serializer());
    public static final JsonKeyImpl<JsonObject, TwitchCommands> TWITCH_COMMANDS = key("commands", TypeToken.get(TwitchCommands.class), new TwitchCommands.Serializer());
    public static final JsonKeyImpl<JsonPrimitive, String> TWITCH_NAME = stringKey("streamer_name");
    public static final JsonKeyImpl<JsonPrimitive, Float> VOLUME = floatKey("volume");
    public static final JsonKeyImpl<JsonPrimitive, Double> X_OFFSET_DOUBLE = doubleKey("x_offset");
    public static final JsonKeyImpl<JsonPrimitive, Integer> X_OFFSET_INT = integerKey("x_offset");
    public static final JsonKeyImpl<JsonPrimitive, Double> X_VELOCITY = doubleKey("x_velocity");
    public static final JsonKeyImpl<JsonPrimitive, Double> Y_OFFSET_DOUBLE = doubleKey("y_offset");
    public static final JsonKeyImpl<JsonPrimitive, Integer> Y_OFFSET_INT = integerKey("y_offset");
    public static final JsonKeyImpl<JsonPrimitive, Double> Y_VELOCITY = doubleKey("y_velocity");
    public static final JsonKeyImpl<JsonPrimitive, Double> Z_OFFSET_DOUBLE = doubleKey("z_offset");
    public static final JsonKeyImpl<JsonPrimitive, Integer> Z_OFFSET_INT = integerKey("z_offset");
    public static final JsonKeyImpl<JsonPrimitive, Double> Z_VELOCITY = doubleKey("z_velocity");

    private Keys() {

    }

    public static Collector<JsonElement, JsonArray, JsonArray> jsonArrayCollector() {
        return Collector.of(JsonArray::new, JsonArray::add, (left, right) -> {
            left.addAll(right);
            return left;
        });
    }
}
