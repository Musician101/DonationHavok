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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

@TypeOf(HavokEntity.Serializer.class)
public class HavokEntity extends HavokDoubleOffset {

    private final NBTTagCompound nbtTagCompound;

    public HavokEntity() {
        super(0, 0D, 0D, 0D);
        this.nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("id", "minecraft:chicken");
        nbtTagCompound.setBoolean("CustomNameVisible", true);
        nbtTagCompound.setString("CustomName", "Duck");
    }

    public HavokEntity(int delay, double xOffset, double yOffset, double zOffset, NBTTagCompound nbtTagCompound) {
        super(delay, xOffset, yOffset, zOffset);
        this.nbtTagCompound = nbtTagCompound;
        nbtTagCompound.removeTag("UUID");
        nbtTagCompound.removeTag("UUIDMost");
        nbtTagCompound.removeTag("UUIDLeast");
    }

    public NBTTagCompound getNBTTagCompound() {
        return nbtTagCompound;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        wreak("HavokEntity-Delay:" + getDelay(), () -> {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
            Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(nbtTagCompound.getString("id")), world);
            if (entity != null) {
                entity.readFromNBT(nbtTagCompound);
                entity.setPosition(originalPos.getX() + getXOffset(), originalPos.getY() + getYOffset(), originalPos.getZ() + getZOffset());
                world.spawnEntity(entity);
            }
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
            NBTTagCompound defaultNBT = new NBTTagCompound();
            defaultNBT.setString("id", "minecraft:chicken");
            defaultNBT.setBoolean("CustomNameVisible", true);
            defaultNBT.setString("CustomName", "Duck");
            NBTTagCompound nbt = deserialize(jsonObject, context, Keys.NBT, defaultNBT);
            return new HavokEntity(delay, xOffset, yOffset, zOffset, nbt);
        }

        @Override
        public JsonElement serialize(HavokEntity src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, src.getZOffset());
            serialize(jsonObject, context, Keys.NBT, src.getNBTTagCompound());
            return jsonObject;
        }
    }
}
