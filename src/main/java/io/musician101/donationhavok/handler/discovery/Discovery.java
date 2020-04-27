package io.musician101.donationhavok.handler.discovery;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;

public class Discovery {

    private final double amount;
    @Nonnull
    private final String donorName;
    @Nonnull
    private final String rewardName;

    public Discovery(double amount, @Nonnull String donorName, @Nonnull String rewardName) {
        this.amount = amount;
        this.donorName = donorName;
        this.rewardName = rewardName;
    }

    public double getAmount() {
        return amount;
    }

    @Nonnull
    public String getDonorName() {
        return donorName;
    }

    @Nonnull
    public String getRewardName() {
        return rewardName;
    }

    public static class Serializer extends BaseSerializer<Discovery> {

        @Override
        public Discovery deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            double amount = deserialize(jsonObject, context, Keys.AMOUNT, 0D);
            String rewardName = deserialize(jsonObject, context, Keys.REWARD_NAME, "Null");
            String donorName = deserialize(jsonObject, context, Keys.DONOR_NAME, "Null");
            return new Discovery(amount, rewardName, donorName);
        }

        @Override
        public JsonElement serialize(Discovery src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.AMOUNT, src.getAmount());
            serialize(jsonObject, context, Keys.REWARD_NAME, src.getRewardName());
            serialize(jsonObject, context, Keys.DONOR_NAME, src.getDonorName());
            return jsonObject;
        }
    }
}
