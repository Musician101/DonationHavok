package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.StreamLabsHandler.Donation;
import io.musician101.donationhavok.handler.twitch.event.Cheer;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import io.musician101.donationhavok.handler.twitch.event.Subscription;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.server.FMLServerHandler;

@TypeOf(HavokRewards.Serializer.class)
public class HavokRewards {

    private final boolean allowTargetViaNote;
    @Nonnull
    private final List<HavokBlock> blocks;
    @Nonnull
    private final List<HavokCommand> commands;
    private final int delay;
    @Nonnull
    private final List<HavokEntity> entities;
    @Nonnull
    private final List<HavokItemStack> itemStacks;
    @Nonnull
    private final List<HavokMessage> messages;
    @Nonnull
    private final String name;
    @Nonnull
    private final List<HavokParticle> particles;
    @Nonnull
    private final List<HavokSound> sounds;
    private final boolean targetAllPlayers;
    @Nonnull
    private final List<String> targetPlayers;
    @Nonnull
    private final List<Double> triggerTiers;

    public HavokRewards() {
        this.allowTargetViaNote = true;
        this.targetAllPlayers = false;
        this.delay = 0;
        this.triggerTiers = new ArrayList<>();
        this.blocks = Collections.singletonList(new HavokBlock());
        this.commands = Collections.singletonList(new HavokCommand());
        this.entities = Collections.singletonList(new HavokEntity());
        this.itemStacks = Collections.singletonList(new HavokItemStack());
        this.messages = Collections.singletonList(new HavokMessage());
        this.particles = Collections.singletonList(new HavokParticle());
        this.sounds = Collections.singletonList(new HavokSound());
        this.name = "A Default Reward";
        this.targetPlayers = new ArrayList<>();
    }

    public HavokRewards(boolean allowTargetViaNote, boolean targetAllPlayers, int delay, @Nonnull String name, @Nonnull List<Double> triggerTiers, @Nonnull List<HavokBlock> blocks, @Nonnull List<HavokCommand> commands, @Nonnull List<HavokEntity> entities, @Nonnull List<HavokItemStack> itemStacks, @Nonnull List<HavokMessage> messages, @Nonnull List<HavokParticle> particles, @Nonnull List<HavokSound> sounds, @Nonnull List<String> targetPlayers) {
        this.allowTargetViaNote = allowTargetViaNote;
        this.targetAllPlayers = targetAllPlayers;
        this.delay = delay;
        this.name = name;
        this.triggerTiers = triggerTiers;
        this.blocks = blocks;
        this.commands = commands;
        this.entities = entities;
        this.itemStacks = itemStacks;
        this.messages = messages;
        this.particles = particles;
        this.sounds = sounds;
        this.targetPlayers = targetPlayers;
    }

    public boolean allowTargetViaNote() {
        return allowTargetViaNote;
    }

    @Nonnull
    public List<HavokBlock> getBlocks() {
        return blocks;
    }

    @Nonnull
    public List<HavokCommand> getCommands() {
        return commands;
    }

    public int getDelay() {
        return delay;
    }

    @Nonnull
    public List<HavokEntity> getEntities() {
        return entities;
    }

    @Nonnull
    public List<HavokItemStack> getItems() {
        return itemStacks;
    }

    @Nonnull
    public List<HavokMessage> getMessages() {
        return messages;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public List<HavokParticle> getParticles() {
        return particles;
    }

    @Nonnull
    public List<HavokSound> getSounds() {
        return sounds;
    }

    @Nonnull
    public List<String> getTargetPlayers() {
        return targetPlayers;
    }

    @Nonnull
    public List<Double> getTriggerTiers() {
        return triggerTiers;
    }

    @Nonnull
    private List<EntityPlayer> parseTargetPlayers(@Nonnull String note) {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            PlayerList playerList = FMLServerHandler.instance().getServer().getPlayerList();
            if (!targetPlayers.isEmpty()) {
                return targetPlayers.stream().map(playerList::getPlayerByUsername).filter(Objects::nonNull).collect(Collectors.toList());
            }

            if (targetAllPlayers) {
                return new ArrayList<>(playerList.getPlayers());
            }

            if (allowTargetViaNote && note.contains("@")) {
                List<String> userNames = Arrays.stream(note.split("@")).filter(string -> string.contains("@")).map(username -> username.replace("@", "")).collect(Collectors.toList());
                List<EntityPlayer> targetPlayers = new ArrayList<>();
                userNames.forEach(username -> {
                    EntityPlayer targetPlayer = playerList.getPlayerByUsername(username);
                    if (targetPlayer != null && targetPlayers.stream().map(EntityPlayer::getUniqueID).collect(Collectors.toList()).contains(targetPlayer.getUniqueID())) {
                        targetPlayers.add(targetPlayer);
                    }
                });

                return targetPlayers;
            }

            Optional<EntityPlayer> player = DonationHavok.INSTANCE.getRewardsHandler().getPlayer();
            return player.isPresent() ? Collections.singletonList(player.get()) : Collections.emptyList();
        }

        return Collections.singletonList(Minecraft.getMinecraft().player);
    }

    private void sendMessage(@Nonnull ITextComponent firstLine) {
        ITextComponent finalMessage = firstLine.appendSibling(new TextComponentString("\nThey triggered ").setStyle(new Style().setColor(TextFormatting.RESET)).appendSibling(new TextComponentString(name).setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new TextComponentString("!").setStyle(new Style().setColor(TextFormatting.RESET))));
        if (FMLCommonHandler.instance().getSide().isServer()) {
            PlayerList playerList = FMLServerHandler.instance().getServer().getPlayerList();
            playerList.sendMessage(finalMessage, false);
        }
        else {
            Minecraft.getMinecraft().player.sendMessage(finalMessage);
        }
    }

    public boolean targetAllPlayers() {
        return targetAllPlayers;
    }

    public void wreak(@Nonnull Donation donation) {
        DonationHavok.INSTANCE.getScheduler().scheduleTask("HavokRewards-Donation-Delay:" + delay, delay, () -> {
            sendMessage(new TextComponentString(donation.getName()).setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new TextComponentString(" donated!").setStyle(new Style().setColor(TextFormatting.RESET))));
            parseTargetPlayers(donation.getNote()).forEach(this::wreak);
        });
    }

    public void wreak(@Nonnull EntityPlayer player) {
        triggerTiers.forEach(tier -> DonationHavok.INSTANCE.getRewardsHandler().runDonation(player, tier));
        blocks.forEach(block -> block.wreak(player, player.getPosition()));
        entities.forEach(entity -> entity.wreak(player, player.getPosition()));
        itemStacks.forEach(itemStack -> itemStack.wreak(player, player.getPosition()));
        messages.forEach(message -> message.wreak(player, player.getPosition()));
        sounds.forEach(sound -> sound.wreak(player, player.getPosition()));
        particles.forEach(particle -> particle.wreak(player, player.getPosition()));
        commands.forEach(command -> command.wreak(player, player.getPosition()));
    }

    public void wreak(@Nonnull Subscription subscription) {
        DonationHavok.INSTANCE.getScheduler().scheduleTask("HavokRewards-Subscription-Delay:" + delay, delay, () -> {
            int streak = subscription.getStreak();
            SubPlan subPlan = subscription.getSubPlan();
            ITextComponent firstLine = new TextComponentString(subscription.getUser()).setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new TextComponentString(" just subscribed").setStyle(new Style().setColor(TextFormatting.RESET)));
            if (subPlan == SubPlan.PRIME) {
                firstLine.appendSibling(new TextComponentString(" with ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new TextComponentString("Twitch Prime").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
            }
            else if (subPlan != SubPlan.UNKNOWN) {
                String donationTier = "";
                switch (subPlan) {
                    case TIER_1:
                        donationTier = "with a $4.99 sub";
                        break;
                    case TIER_2:
                        donationTier = "with a $9.99 sub";
                        break;
                    case TIER_3:
                        donationTier = "with a $24.99 sub";
                        break;
                }

                firstLine.appendSibling(new TextComponentString(" with a ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new TextComponentString(donationTier).setStyle(new Style().setColor(TextFormatting.GOLD)));
            }

            firstLine.appendSibling(new TextComponentString("!").setStyle(new Style().setColor(TextFormatting.RESET)));
            if (streak > 1) {
                firstLine.appendSibling(new TextComponentString("\nThey have been subscribed for ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new TextComponentString(streak + " months").setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new TextComponentString(" in a row!").setStyle(new Style().setColor(TextFormatting.RESET)));
            }

            sendMessage(firstLine);
            parseTargetPlayers(subscription.getMessage()).forEach(this::wreak);
        });
    }

    public void wreak(@Nonnull Cheer cheer) {
        DonationHavok.INSTANCE.getScheduler().scheduleTask("HavokRewards-Cheer-Delay:" + delay, delay, () -> {
            int bits = cheer.getBits();
            TextFormatting color = TextFormatting.RED;
            if (bits >= 1 && bits <= 99) {
                color = TextFormatting.GRAY;
            }
            else if (bits >= 100 && bits <= 999) {
                color = TextFormatting.DARK_PURPLE;
            }
            else if (bits >= 999 && bits <= 4999) {
                color = TextFormatting.AQUA;
            }
            else if (bits >= 5000 && bits <= 9999) {
                color = TextFormatting.BLUE;
            }

            sendMessage(new TextComponentString(cheer.getUser()).setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new TextComponentString(" just cheered for ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new TextComponentString(Integer.toString(bits) + " bits").setStyle(new Style().setColor(color))).appendSibling(new TextComponentString("!").setStyle(new Style().setColor(TextFormatting.RESET))));
            parseTargetPlayers(cheer.getMessage()).forEach(this::wreak);
        });
    }

    public static class Serializer extends BaseSerializer<HavokRewards> {

        @Override
        public HavokRewards deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean allowTargetViaNote = deserialize(jsonObject, context, Keys.ALLOW_TARGET_VIA_NOTE, true);
            boolean targetAllPlayers = deserialize(jsonObject, context, Keys.TARGET_ALL_PLAYERS, false);
            String name = deserialize(jsonObject, context, Keys.NAME, "A Default Reward");
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            List<HavokBlock> blocks = deserialize(jsonObject, context, Keys.BLOCKS, Collections.emptyList());
            List<HavokCommand> commands = deserialize(jsonObject, context, Keys.COMMANDS, Collections.emptyList());
            List<HavokEntity> entities = deserialize(jsonObject, context, Keys.ENTITIES, Collections.emptyList());
            List<HavokItemStack> itemStacks = deserialize(jsonObject, context, Keys.ITEMS, Collections.emptyList());
            List<HavokMessage> messages = deserialize(jsonObject, context, Keys.MESSAGES, Collections.emptyList());
            List<HavokParticle> particles = deserialize(jsonObject, context, Keys.PARTICLES, Collections.emptyList());
            List<HavokSound> sounds = deserialize(jsonObject, context, Keys.SOUNDS, Collections.emptyList());
            List<String> targetPlayers = deserialize(jsonObject, context, Keys.TARGET_PLAYERS, Collections.emptyList());
            List<Double> triggerTiers = deserialize(jsonObject, context, Keys.TRIGGER_TIERS, Collections.emptyList());
            return new HavokRewards(allowTargetViaNote, targetAllPlayers, delay, name, triggerTiers, blocks, commands, entities, itemStacks, messages, particles, sounds, targetPlayers);
        }

        @Override
        public JsonElement serialize(HavokRewards src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ALLOW_TARGET_VIA_NOTE, src.allowTargetViaNote());
            serialize(jsonObject, context, Keys.TARGET_ALL_PLAYERS, src.targetAllPlayers());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.NAME, src.getName());
            serialize(jsonObject, context, Keys.BLOCKS, src.getBlocks());
            serialize(jsonObject, context, Keys.COMMANDS, src.getCommands());
            serialize(jsonObject, context, Keys.ENTITIES, src.getEntities());
            serialize(jsonObject, context, Keys.ITEMS, src.getItems());
            serialize(jsonObject, context, Keys.MESSAGES, src.getMessages());
            serialize(jsonObject, context, Keys.PARTICLES, src.getParticles());
            serialize(jsonObject, context, Keys.SOUNDS, src.getSounds());
            serialize(jsonObject, context, Keys.TARGET_PLAYERS, src.getTargetPlayers());
            serialize(jsonObject, context, Keys.TRIGGER_TIERS, src.getTriggerTiers());
            return jsonObject;
        }
    }
}
