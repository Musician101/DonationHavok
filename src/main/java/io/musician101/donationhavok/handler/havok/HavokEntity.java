package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class HavokEntity extends HavokDoubleOffset {

    private final CompoundNBT nbtTagCompound;

    public HavokEntity() {
        super(0, 0D, 0D, 0D);
        this.nbtTagCompound = new CompoundNBT();
        nbtTagCompound.putString("id", "minecraft:chicken");
        nbtTagCompound.putBoolean("CustomNameVisible", true);
        nbtTagCompound.putString("CustomName", "Duck");
    }

    public HavokEntity(int delay, double xOffset, double yOffset, double zOffset, CompoundNBT nbtTagCompound) {
        super(delay, xOffset, yOffset, zOffset);
        this.nbtTagCompound = nbtTagCompound;
        nbtTagCompound.remove("UUID");
        nbtTagCompound.remove("UUIDMost");
        nbtTagCompound.remove("UUIDLeast");
    }

    public CompoundNBT getCompoundNBT() {
        return nbtTagCompound;
    }

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        wreak("HavokEntity-Delay:" + getDelay(), () -> {
            World world = LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).getWorld(player.dimension);
            EntityType.loadEntityUnchecked(nbtTagCompound, world).ifPresent(entity -> {
                entity.setPosition(originalPos.getX() + getXOffset(), originalPos.getY() + getYOffset(), originalPos.getZ() + getZOffset());
                world.addEntity(entity);
            });
        });
    }

    public static class Serializer extends BaseSerializer<HavokEntity> {

        @Override
        public HavokEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            double xOffset = deserialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, 0D);
            double yOffset = deserialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, 0D);
            double zOffset = deserialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, 0D);
            CompoundNBT defaultNBT = new CompoundNBT();
            defaultNBT.putString("id", "minecraft:chicken");
            defaultNBT.putBoolean("CustomNameVisible", true);
            defaultNBT.putString("CustomName", "{\"Duck\"}");
            CompoundNBT nbt = deserialize(jsonObject, context, Keys.NBT, defaultNBT);
            return new HavokEntity(delay, xOffset, yOffset, zOffset, nbt);
        }

        @Override
        public JsonElement serialize(HavokEntity src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, src.getZOffset());
            serialize(jsonObject, context, Keys.NBT, src.getCompoundNBT());
            return jsonObject;
        }
    }
}
