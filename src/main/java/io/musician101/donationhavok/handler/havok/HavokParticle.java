package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class HavokParticle extends HavokDoubleOffset {

    private final ParticleType particle;
    private final double xVelocity;
    private final double yVelocity;
    private final double zVelocity;

    public HavokParticle() {
        super(0, 0D, 0D, 0D);
        this.particle = ParticleTypes.EXPLOSION;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.zVelocity = 0;
    }

    public HavokParticle(int delay, double xOffset, double yOffset, double zOffset, double xVelocity, double yVelocity, double zVelocity, ParticleType particle) {
        super(delay, xOffset, yOffset, zOffset);
        this.particle = particle;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
    }

    public ParticleType getParticle() {
        return particle;
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public double getZVelocity() {
        return zVelocity;
    }

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        wreak("HavokParticle-Delay:" + getDelay(), () -> {
            try {
                LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).getWorld(player.dimension).spawnParticle(particle.getDeserializer().deserialize(particle, new StringReader("")), originalPos.getX() + getXOffset(), originalPos.getY() + getYOffset(), originalPos.getZ() + getZOffset(), 1, xVelocity, yVelocity, zVelocity, 1);
            }
            catch (CommandSyntaxException e) {
                String message = "Particle parse error: " + particle.getRegistryName().toString();
                player.sendMessage(new StringTextComponent(message).setStyle(new Style().setColor(TextFormatting.RED)));
                DonationHavok.getInstance().getLogger().error(message, e);
            }
        });
    }

    public static class Serializer extends BaseSerializer<HavokParticle> {

        @Override
        public HavokParticle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            double xOffset = deserialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, 0D);
            double yOffset = deserialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, 0D);
            double zOffset = deserialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, 0D);
            double xVelocity = deserialize(jsonObject, context, Keys.X_VELOCITY, 0D);
            double yVelocity = deserialize(jsonObject, context, Keys.Y_VELOCITY, 0D);
            double zVelocity = deserialize(jsonObject, context, Keys.Z_VELOCITY, 0D);
            String id = deserialize(jsonObject, context, Keys.ID, "explosion");
            ParticleType particle = Registry.PARTICLE_TYPE.getValue(new ResourceLocation(id)).orElse(ParticleTypes.EXPLOSION);
            if (particle == null) {
                throw new JsonParseException("Particle with id " + id + " doesn't exist.");
            }

            return new HavokParticle(delay, xOffset, yOffset, zOffset, xVelocity, yVelocity, zVelocity, particle);
        }

        @Override
        public JsonElement serialize(HavokParticle src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ID, src.getParticle().getRegistryName().toString());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_DOUBLE, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_DOUBLE, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_DOUBLE, src.getZOffset());
            serialize(jsonObject, context, Keys.X_VELOCITY, src.getXVelocity());
            serialize(jsonObject, context, Keys.Y_VELOCITY, src.getYVelocity());
            serialize(jsonObject, context, Keys.Z_VELOCITY, src.getZVelocity());
            return jsonObject;
        }
    }
}
