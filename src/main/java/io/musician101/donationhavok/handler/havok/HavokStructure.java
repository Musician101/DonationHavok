package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.FMLCommonHandler;

@TypeOf(HavokStructure.Serializer.class)
public class HavokStructure extends HavokIntegerOffset {

    @Nonnull
    private final PlacementSettings placementSettings;
    private final long seed;
    @Nonnull
    private final String structureName;

    public HavokStructure() {
        this(0, 0, 0, 0, "endcity/base_floor", new PlacementSettings(), 0L);
    }

    public HavokStructure(int delay, Integer xOffset, Integer yOffset, Integer zOffset, @Nonnull String structureName, @Nonnull PlacementSettings placementSettings, long seed) {
        super(delay, xOffset, yOffset, zOffset);
        this.structureName = structureName;
        this.placementSettings = placementSettings;
        this.seed = seed;
    }

    @Nonnull
    public PlacementSettings getPlacementSettings() {
        return placementSettings;
    }

    public long getPlacementSettingsSeed() {
        return seed;
    }

    @Nonnull
    public String getStructureName() {
        return structureName;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer world = (WorldServer) server.getEntityWorld();
        wreak("HavokStructureDelay:" + getDelay(), () -> {
            Template template = world.getStructureTemplateManager().get(server, new ResourceLocation(structureName));
            if (template == null) {
                player.sendMessage(new TextComponentString("A structure was supposed to spawn, but the structure does not exist on the server."));
            }
            else {
                template.addBlocksToWorld(world, originalPos.add(getXOffset(), getYOffset(), getZOffset()), placementSettings);
            }
        });
    }

    public static class Serializer extends BaseSerializer<HavokStructure> {

        @Override
        public HavokStructure deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String structureName = deserialize(jsonObject, context, Keys.STRUCTURE_NAME, "");
            PlacementSettings placementSettings = deserialize(jsonObject, context, Keys.PLACEMENT_SETTINGS, new PlacementSettings());
            long seed = deserialize(jsonObject.getAsJsonObject(Keys.PLACEMENT_SETTINGS.getKey()), context, Keys.SEED, 0L);
            placementSettings.setSeed(seed);
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            int xOffset = deserialize(jsonObject, context, Keys.X_OFFSET_INT, 0);
            int yOffset = deserialize(jsonObject, context, Keys.Y_OFFSET_INT, 0);
            int zOffset = deserialize(jsonObject, context, Keys.Z_OFFSET_INT, 0);
            return new HavokStructure(delay, xOffset, yOffset, zOffset, structureName, placementSettings, seed);
        }

        @Override
        public JsonElement serialize(HavokStructure src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.STRUCTURE_NAME, src.getStructureName());
            serialize(jsonObject, context, Keys.PLACEMENT_SETTINGS, src.getPlacementSettings());
            serialize(jsonObject.getAsJsonObject(Keys.PLACEMENT_SETTINGS.getKey()), context, Keys.SEED, src.getPlacementSettingsSeed());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_INT, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_INT, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_INT, src.getZOffset());
            return jsonObject;
        }
    }
}
