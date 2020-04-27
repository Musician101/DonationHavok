package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

public class HavokSound extends HavokDoubleOffset {

    private final float pitch;
    private final SoundEvent soundEvent;
    private final float volume;

    public HavokSound() {
        super(0, 0D, 0D, 0D);
        this.pitch = 1F;
        this.volume = 1F;
        this.soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("minecraft:entity.generic.explode"));
    }

    public HavokSound(int delay, double xOffset, double yOffset, double zOffset, float pitch, float volume, SoundEvent soundEvent) {
        super(delay, xOffset, yOffset, zOffset);
        this.soundEvent = soundEvent;
        this.pitch = pitch;
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public float getVolume() {
        return volume;
    }

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        wreak("HavokParticle-Delay:" + getDelay(), () -> player.getEntityWorld().playSound(player, originalPos.add(getXOffset(), getYOffset(), getZOffset()), soundEvent, SoundCategory.MASTER, volume, pitch));
    }

    public static class Serializer extends BaseSerializer<HavokSound> {

        @Override
        public HavokSound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            double xOffset = deserialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, 0D);
            double yOffset = deserialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, 0D);
            double zOffset = deserialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, 0D);
            float pitch = deserialize(jsonObject, context, Keys.PITCH, 0F);
            float volume = deserialize(jsonObject, context, Keys.VOLUME, 0F);
            String id = deserialize(jsonObject, context, Keys.ID, "entity.generic.explode");
            SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id));
            if (sound == null) {
                throw new JsonParseException("Sound with id " + id + " does not exist.");
            }

            return new HavokSound(delay, xOffset, yOffset, zOffset, pitch, volume, sound);
        }

        @Override
        public JsonElement serialize(HavokSound src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, src.getZOffset());
            serialize(jsonObject, context, Keys.PITCH, src.getPitch());
            serialize(jsonObject, context, Keys.VOLUME, src.getVolume());
            ResourceLocation id = src.getSoundEvent().getRegistryName();
            serialize(jsonObject, context, Keys.ID, id == null ? "null" : id.toString());
            return jsonObject;
        }
    }
}
