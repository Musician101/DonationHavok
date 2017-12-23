package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokParticle;
import java.lang.reflect.Type;
import net.minecraft.util.EnumParticleTypes;

import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.ID;
import static io.musician101.donationhavok.util.json.JsonUtils.X_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.X_VELOCITY;
import static io.musician101.donationhavok.util.json.JsonUtils.Y_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Y_VELOCITY;
import static io.musician101.donationhavok.util.json.JsonUtils.Z_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Z_VELOCITY;

public class HavokParticleTypeAdapter implements JsonDeserializer<HavokParticle>, JsonSerializer<HavokParticle> {

    @Override
    public HavokParticle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        double xOffset = jsonObject.get(X_OFFSET).getAsDouble();
        double yOffset = jsonObject.get(Y_OFFSET).getAsDouble();
        double zOffset = jsonObject.get(Z_OFFSET).getAsDouble();
        double xVelocity = jsonObject.get(X_VELOCITY).getAsDouble();
        double yVelocity = jsonObject.get(Y_VELOCITY).getAsDouble();
        double zVelocity = jsonObject.get(Z_VELOCITY).getAsDouble();
        String id = jsonObject.get(ID).getAsString();
        EnumParticleTypes particle = EnumParticleTypes.getByName(id);
        if (particle == null) {
            throw new JsonParseException("Particle with id " + id + " doesn't exist.");
        }

        return new HavokParticle(delay, xOffset, yOffset, zOffset, xVelocity, yVelocity, zVelocity, particle);
    }

    @Override
    public JsonElement serialize(HavokParticle src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ID, src.getParticle().getParticleName());
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(X_OFFSET, src.getXOffset());
        jsonObject.addProperty(Y_OFFSET, src.getYOffset());
        jsonObject.addProperty(Z_OFFSET, src.getZOffset());
        jsonObject.addProperty(X_VELOCITY, src.getXVelocity());
        jsonObject.addProperty(Y_VELOCITY, src.getYVelocity());
        jsonObject.addProperty(Z_VELOCITY, src.getZVelocity());
        return jsonObject;
    }
}
