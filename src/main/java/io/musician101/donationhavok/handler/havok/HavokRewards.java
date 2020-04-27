package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.SaleHandler;
import io.musician101.donationhavok.handler.StreamlabsHandler.Donation;
import io.musician101.donationhavok.handler.twitch.event.Cheer;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import io.musician101.donationhavok.handler.twitch.event.Subscription;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
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
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class HavokRewards {

    private final boolean allowTargetViaNote;
    @Nonnull
    private final List<HavokBlock> blocks;
    @Nonnull
    private final List<HavokCommand> commands;
    private final int delay;
    private final double discount;
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
    private final int saleLength;
    @Nonnull
    private final List<HavokSchematic> schematics;
    @Nonnull
    private final List<HavokSound> sounds;
    @Nonnull
    private final List<HavokStructure> structures;
    private final boolean targetAllPlayers;
    @Nonnull
    private final List<String> targetPlayers;
    @Nonnull
    private final List<Double> triggerTiers;
    private final boolean triggersSale;

    public HavokRewards() {
        this.allowTargetViaNote = true;
        this.targetAllPlayers = false;
        this.triggerTiers = new ArrayList<>();
        this.triggersSale = false;
        this.discount = 0.5;
        this.delay = 0;
        this.saleLength = 1200;
        this.blocks = Collections.singletonList(new HavokBlock());
        this.commands = Collections.singletonList(new HavokCommand());
        this.entities = Collections.singletonList(new HavokEntity());
        this.itemStacks = Collections.singletonList(new HavokItemStack());
        this.messages = Collections.singletonList(new HavokMessage());
        this.particles = Collections.singletonList(new HavokParticle());
        this.schematics = Collections.singletonList(new HavokSchematic());
        this.sounds = Collections.singletonList(new HavokSound());
        this.structures = Collections.singletonList(new HavokStructure());
        this.name = "A Default Reward";
        this.targetPlayers = new ArrayList<>();
    }

    public HavokRewards(boolean allowTargetViaNote, boolean targetAllPlayers, boolean triggersSale, double discount, int delay, int saleLength, @Nonnull String name, @Nonnull List<Double> triggerTiers, @Nonnull List<HavokBlock> blocks, @Nonnull List<HavokCommand> commands, @Nonnull List<HavokEntity> entities, @Nonnull List<HavokItemStack> itemStacks, @Nonnull List<HavokMessage> messages, @Nonnull List<HavokParticle> particles, @Nonnull List<HavokSchematic> schematics, @Nonnull List<HavokSound> sounds, @Nonnull List<HavokStructure> structures, @Nonnull List<String> targetPlayers) {
        this.allowTargetViaNote = allowTargetViaNote;
        this.targetAllPlayers = targetAllPlayers;
        this.triggersSale = triggersSale;
        this.discount = Math.min(discount, 1D);
        this.delay = delay;
        this.saleLength = saleLength;
        this.name = name;
        this.triggerTiers = triggerTiers;
        this.blocks = blocks;
        this.commands = commands;
        this.entities = entities;
        this.itemStacks = itemStacks;
        this.messages = messages;
        this.particles = particles;
        this.schematics = schematics;
        this.sounds = sounds;
        this.structures = structures;
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

    public double getDiscount() {
        return discount;
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

    public int getSaleLength() {
        return saleLength;
    }

    @Nonnull
    public List<HavokSchematic> getSchematics() {
        return schematics;
    }

    @Nonnull
    public List<HavokSound> getSounds() {
        return sounds;
    }

    @Nonnull
    public List<HavokStructure> getStructures() {
        return structures;
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
    private List<PlayerEntity> parseTargetPlayers(@Nonnull String note) {
        MinecraftServer server = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        if (server.isDedicatedServer()) {
            PlayerList playerList = server.getPlayerList();
            if (!targetPlayers.isEmpty()) {
                return targetPlayers.stream().map(playerList::getPlayerByUsername).filter(Objects::nonNull).collect(Collectors.toList());
            }

            if (targetAllPlayers) {
                return new ArrayList<>(playerList.getPlayers());
            }

            if (allowTargetViaNote && note.contains("@")) {
                List<String> userNames = Arrays.stream(note.split("@")).filter(string -> string.contains("@")).map(username -> username.replace("@", "")).collect(Collectors.toList());
                List<PlayerEntity> targetPlayers = new ArrayList<>();
                userNames.forEach(username -> {
                    PlayerEntity targetPlayer = playerList.getPlayerByUsername(username);
                    if (targetPlayer != null && targetPlayers.stream().map(PlayerEntity::getUniqueID).collect(Collectors.toList()).contains(targetPlayer.getUniqueID())) {
                        targetPlayers.add(targetPlayer);
                    }
                });

                return targetPlayers;
            }

            Optional<PlayerEntity> player = DonationHavok.getInstance().getConfig().getGeneralConfig().getPlayer();
            return player.map(Collections::singletonList).orElse(Collections.emptyList());
        }

        return Collections.singletonList(Minecraft.getInstance().player);
    }

    private void sendMessage(@Nonnull ITextComponent firstLine) {
        ITextComponent finalMessage = firstLine.appendSibling(new StringTextComponent("\nThey triggered ").setStyle(new Style().setColor(TextFormatting.RESET)).appendSibling(new StringTextComponent(name).setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new StringTextComponent("!").setStyle(new Style().setColor(TextFormatting.RESET))));
        MinecraftServer server = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        if (server.isDedicatedServer()) {
            PlayerList playerList = server.getPlayerList();
            playerList.sendMessage(finalMessage, false);
        }
        else {
            Minecraft.getInstance().player.sendMessage(finalMessage);
        }
    }

    public boolean targetAllPlayers() {
        return targetAllPlayers;
    }

    public boolean triggersSale() {
        return triggersSale;
    }

    public void wreak(@Nonnull Donation donation) {
        DonationHavok.getInstance().getScheduler().scheduleTask("HavokRewards-Donation-Delay:" + delay, delay, () -> {
            generateBook(donation.getName(), donation.getNote());
            sendMessage(new StringTextComponent(donation.getName()).setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new StringTextComponent(" donated!").setStyle(new Style().setColor(TextFormatting.RESET))));
            parseTargetPlayers(donation.getNote()).forEach(this::wreak);
        });
    }

    private void generateBook(@Nonnull String donor, @Nonnull String message) {
        MinecraftServer server = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        DonationHavok.getInstance().getConfig().getGeneralConfig().getPlayer().filter(p -> DonationHavok.getInstance().getConfig().getGeneralConfig().generateBook()).ifPresent(player -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("author", donor);
            tag.putString("title", name);
            ListNBT pages = new ListNBT();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("text", message);
            pages.add(new StringNBT(jsonObject.toString()));
            tag.put("pages", pages);
            ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
            book.setTag(tag);
            BlockPos pos = player.getPosition();
            World world = server.getWorld(player.dimension);
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), book));
        });
    }

    public void wreak(@Nonnull PlayerEntity player) {
        triggerTiers.forEach(tier -> DonationHavok.getInstance().getStreamLabsHandler().runDonation(player, tier));
        blocks.forEach(block -> block.wreak(player, player.getPosition()));
        commands.forEach(command -> command.wreak(player, player.getPosition()));
        entities.forEach(entity -> entity.wreak(player, player.getPosition()));
        itemStacks.forEach(itemStack -> itemStack.wreak(player, player.getPosition()));
        messages.forEach(message -> message.wreak(player, player.getPosition()));
        particles.forEach(particle -> particle.wreak(player, player.getPosition()));
        schematics.forEach(schematic -> schematic.wreak(player, player.getPosition()));
        sounds.forEach(sound -> sound.wreak(player, player.getPosition()));
        structures.forEach(structure -> structure.wreak(player, player.getPosition()));
        if (triggersSale) {
            SaleHandler.startSale(discount, saleLength);
        }
    }

    public void wreak(@Nonnull Subscription subscription) {
        DonationHavok.getInstance().getScheduler().scheduleTask("HavokRewards-Subscription-Delay:" + delay, delay, () -> {
            boolean isGift = subscription.isGift();
            int streak = subscription.getStreak();
            SubPlan subPlan = subscription.getSubPlan();
            ITextComponent firstLine = new StringTextComponent(subscription.getSubBuyer()).setStyle(new Style().setColor(TextFormatting.AQUA));
            if (isGift) {
                firstLine.appendSibling(new StringTextComponent(" just gifted "));
            }
            else {
                firstLine.appendSibling(new StringTextComponent(" just subscribed").setStyle(new Style().setColor(TextFormatting.RESET)));
            }

            if (subPlan == SubPlan.PRIME) {
                firstLine.appendSibling(new StringTextComponent(" with ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new StringTextComponent("Twitch Prime").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
            }
            else if (subPlan != SubPlan.UNKNOWN) {
                String donationTier = "";
                switch (subPlan) {
                    case TIER_1:
                        donationTier = isGift ? "Tier 1" : "$4.99 sub";
                        break;
                    case TIER_2:
                        donationTier = isGift ? "Tier 2" : "$9.99 sub";
                        break;
                    case TIER_3:
                        donationTier = isGift ? "Tier 3" : "$24.99 sub";
                        break;
                }

                firstLine.appendSibling(new StringTextComponent((isGift ? "" : " with") + " a ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new StringTextComponent(donationTier).setStyle(new Style().setColor(TextFormatting.GOLD)));
            }

            firstLine.appendSibling(new StringTextComponent("!").setStyle(new Style().setColor(TextFormatting.RESET)));
            if (streak > 1) {
                firstLine.appendSibling(new StringTextComponent("\nThey have been subscribed for ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new StringTextComponent(streak + " months").setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new StringTextComponent(" in a row!").setStyle(new Style().setColor(TextFormatting.RESET)));
            }

            generateBook(subscription.getSubBuyer(), subscription.getMessage());
            sendMessage(firstLine);
            parseTargetPlayers(subscription.getMessage()).forEach(this::wreak);
        });
    }

    public void wreak(@Nonnull Cheer cheer) {
        DonationHavok.getInstance().getScheduler().scheduleTask("HavokRewards-Cheer-Delay:" + delay, delay, () -> {
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

            generateBook(cheer.getUser(), cheer.getMessage());
            sendMessage(new StringTextComponent(cheer.getUser()).setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new StringTextComponent(" just cheered for ").setStyle(new Style().setColor(TextFormatting.RESET))).appendSibling(new StringTextComponent(bits + " bits").setStyle(new Style().setColor(color))).appendSibling(new StringTextComponent("!").setStyle(new Style().setColor(TextFormatting.RESET))));
            parseTargetPlayers(cheer.getMessage()).forEach(this::wreak);
        });
    }

    public static class Serializer extends BaseSerializer<HavokRewards> {

        @Override
        public HavokRewards deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean allowTargetViaNote = deserialize(jsonObject, context, Keys.ALLOW_TARGET_VIA_NOTE, true);
            boolean targetAllPlayers = deserialize(jsonObject, context, Keys.TARGET_ALL_PLAYERS, false);
            boolean triggerSale = deserialize(jsonObject, context, Keys.TRIGGERS_SALE, false);
            double discount = deserialize(jsonObject, context, Keys.DISCOUNT, 0.5);
            String name = deserialize(jsonObject, context, Keys.NAME, "A Default Reward");
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            int saleLength = deserialize(jsonObject, context, Keys.SALE_LENGTH, 0);
            List<HavokBlock> blocks = deserialize(jsonObject, context, Keys.BLOCKS, Collections.emptyList());
            List<HavokCommand> commands = deserialize(jsonObject, context, Keys.COMMANDS, Collections.emptyList());
            List<HavokEntity> entities = deserialize(jsonObject, context, Keys.ENTITIES, Collections.emptyList());
            List<HavokItemStack> itemStacks = deserialize(jsonObject, context, Keys.ITEMS, Collections.emptyList());
            List<HavokMessage> messages = deserialize(jsonObject, context, Keys.MESSAGES, Collections.emptyList());
            List<HavokParticle> particles = deserialize(jsonObject, context, Keys.PARTICLES, Collections.emptyList());
            List<HavokSchematic> schematics = deserialize(jsonObject, context, Keys.SCHEMATICS, Collections.emptyList());
            List<HavokSound> sounds = deserialize(jsonObject, context, Keys.SOUNDS, Collections.emptyList());
            List<HavokStructure> structures = deserialize(jsonObject, context, Keys.STRUCTURES, Collections.emptyList());
            List<String> targetPlayers = deserialize(jsonObject, context, Keys.TARGET_PLAYERS, Collections.emptyList());
            List<Double> triggerTiers = deserialize(jsonObject, context, Keys.TRIGGER_TIERS, Collections.emptyList());
            return new HavokRewards(allowTargetViaNote, targetAllPlayers, triggerSale, discount, delay, saleLength, name, triggerTiers, blocks, commands, entities, itemStacks, messages, particles, schematics, sounds, structures, targetPlayers);
        }

        @Override
        public JsonElement serialize(HavokRewards src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ALLOW_TARGET_VIA_NOTE, src.allowTargetViaNote());
            serialize(jsonObject, context, Keys.TARGET_ALL_PLAYERS, src.targetAllPlayers());
            serialize(jsonObject, context, Keys.TRIGGERS_SALE, src.triggersSale());
            serialize(jsonObject, context, Keys.DISCOUNT, src.getDiscount());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.SALE_LENGTH, src.getSaleLength());
            serialize(jsonObject, context, Keys.NAME, src.getName());
            serialize(jsonObject, context, Keys.BLOCKS, src.getBlocks());
            serialize(jsonObject, context, Keys.COMMANDS, src.getCommands());
            serialize(jsonObject, context, Keys.ENTITIES, src.getEntities());
            serialize(jsonObject, context, Keys.ITEMS, src.getItems());
            serialize(jsonObject, context, Keys.MESSAGES, src.getMessages());
            serialize(jsonObject, context, Keys.PARTICLES, src.getParticles());
            serialize(jsonObject, context, Keys.SCHEMATICS, src.getSchematics());
            serialize(jsonObject, context, Keys.SOUNDS, src.getSounds());
            serialize(jsonObject, context, Keys.STRUCTURES, src.getStructures());
            serialize(jsonObject, context, Keys.TARGET_PLAYERS, src.getTargetPlayers());
            serialize(jsonObject, context, Keys.TRIGGER_TIERS, src.getTriggerTiers());
            return jsonObject;
        }
    }
}
