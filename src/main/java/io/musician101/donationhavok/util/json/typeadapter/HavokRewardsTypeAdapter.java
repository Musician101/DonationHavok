package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokBlock;
import io.musician101.donationhavok.havok.HavokCommand;
import io.musician101.donationhavok.havok.HavokEntity;
import io.musician101.donationhavok.havok.HavokItemStack;
import io.musician101.donationhavok.havok.HavokMessage;
import io.musician101.donationhavok.havok.HavokParticle;
import io.musician101.donationhavok.havok.HavokRewards;
import io.musician101.donationhavok.havok.HavokSound;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.musician101.donationhavok.util.json.JsonUtils.ALLOW_TARGET_VIA_NOTE;
import static io.musician101.donationhavok.util.json.JsonUtils.BLOCKS;
import static io.musician101.donationhavok.util.json.JsonUtils.COMMANDS;
import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.ENTITIES;
import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.ITEMS;
import static io.musician101.donationhavok.util.json.JsonUtils.MESSAGES;
import static io.musician101.donationhavok.util.json.JsonUtils.NAME;
import static io.musician101.donationhavok.util.json.JsonUtils.PARTICLES;
import static io.musician101.donationhavok.util.json.JsonUtils.SOUNDS;
import static io.musician101.donationhavok.util.json.JsonUtils.TARGET_ALL_PLAYERS;
import static io.musician101.donationhavok.util.json.JsonUtils.TARGET_PLAYERS;
import static io.musician101.donationhavok.util.json.JsonUtils.TRIGGER_TIERS;

public class HavokRewardsTypeAdapter implements JsonDeserializer<HavokRewards>, JsonSerializer<HavokRewards> {

    @Override
    public HavokRewards deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        boolean allowTargetViaNote = jsonObject.get(ALLOW_TARGET_VIA_NOTE).getAsBoolean();
        boolean targetAllPlayers = jsonObject.get(TARGET_ALL_PLAYERS).getAsBoolean();
        String name = jsonObject.get(NAME).getAsString();
        int delay = jsonObject.get(DELAY).getAsInt();
        List<HavokBlock> blocks = convertToList(jsonObject.getAsJsonArray(BLOCKS), block -> GSON.fromJson(block, HavokBlock.class));
        List<HavokCommand> commands = convertToList(jsonObject.getAsJsonArray(COMMANDS), command -> GSON.fromJson(command, HavokCommand.class));
        List<HavokEntity> entities = convertToList(jsonObject.getAsJsonArray(ENTITIES), entity -> GSON.fromJson(entity, HavokEntity.class));
        List<HavokItemStack> itemStacks = convertToList(jsonObject.getAsJsonArray(ITEMS), itemStack -> GSON.fromJson(itemStack, HavokItemStack.class));
        List<HavokMessage> messages = convertToList(jsonObject.getAsJsonArray(MESSAGES), message -> GSON.fromJson(message, HavokMessage.class));
        List<HavokParticle> particles = convertToList(jsonObject.getAsJsonArray(PARTICLES), particle -> GSON.fromJson(particle, HavokParticle.class));
        List<HavokSound> sounds = convertToList(jsonObject.getAsJsonArray(SOUNDS), sound -> GSON.fromJson(sound, HavokSound.class));
        List<String> targetPlayers = convertToList(jsonObject.getAsJsonArray(TARGET_PLAYERS), JsonElement::getAsString);
        List<Double> triggerTiers = convertToList(jsonObject.getAsJsonArray(TRIGGER_TIERS), JsonElement::getAsDouble);
        return new HavokRewards(allowTargetViaNote, targetAllPlayers, delay, name, triggerTiers, blocks, commands, entities, itemStacks, messages, particles, sounds, targetPlayers);
    }

    private <V> List<V> convertToList(JsonArray jsonArray, Function<JsonElement, V> mapper) {
        return StreamSupport.stream(jsonArray.spliterator(), false).map(mapper).collect(Collectors.toList());
    }

    @Override
    public JsonElement serialize(HavokRewards src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ALLOW_TARGET_VIA_NOTE, src.allowTargetViaNote());
        jsonObject.addProperty(TARGET_ALL_PLAYERS, src.targetAllPlayers());
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(NAME, src.getName());
        jsonObject.add(BLOCKS, convertToJsonArray(src.getBlocks(), block -> GSON.toJsonTree(block, HavokBlock.class)));
        jsonObject.add(COMMANDS, convertToJsonArray(src.getCommands(), command -> GSON.toJsonTree(command, HavokCommand.class)));
        jsonObject.add(ENTITIES, convertToJsonArray(src.getEntities(), entity -> GSON.toJsonTree(entity, HavokEntity.class)));
        jsonObject.add(ITEMS, convertToJsonArray(src.getItems(), itemStack -> GSON.toJsonTree(itemStack, HavokItemStack.class)));
        jsonObject.add(MESSAGES, convertToJsonArray(src.getMessages(), message -> GSON.toJsonTree(message, HavokMessage.class)));
        jsonObject.add(PARTICLES, convertToJsonArray(src.getParticles(), particle -> GSON.toJsonTree(particle, HavokParticle.class)));
        jsonObject.add(SOUNDS, convertToJsonArray(src.getSounds(), sound -> GSON.toJsonTree(sound, HavokSound.class)));
        jsonObject.add(TARGET_PLAYERS, convertToJsonArray(src.getTargetPlayers(), JsonPrimitive::new));
        jsonObject.add(TRIGGER_TIERS, convertToJsonArray(src.getTriggerTiers(), JsonPrimitive::new));
        return jsonObject;
    }

    private <V> JsonArray convertToJsonArray(List<V> list, Function<V, JsonElement> mapper) {
        JsonArray jsonArray = new JsonArray();
        list.stream().map(mapper).forEach(jsonArray::add);
        return jsonArray;
    }
}
