package io.musician101.donationhavok.havok;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker.Donation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.server.FMLServerHandler;

public class HavokRewards {

    private final boolean allowTargetViaNote;
    private final boolean targetAllPlayers;
    private final int delay;
    private final List<Double> triggerTiers;
    private final List<HavokBlock> blocks;
    private final List<HavokCommand> commands;
    private final List<HavokEntity> entities;
    private final List<HavokItemStack> itemStacks;
    private final List<HavokMessage> messages;
    private final List<HavokParticle> particles;
    private final List<HavokSound> sounds;
    private final List<String> targetPlayers;
    private final String name;

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

    public HavokRewards(boolean allowTargetViaNote, boolean targetAllPlayers, int delay, String name, List<Double> triggerTiers, List<HavokBlock> blocks, List<HavokCommand> commands, List<HavokEntity> entities, List<HavokItemStack> itemStacks, List<HavokMessage> messages, List<HavokParticle> particles, List<HavokSound> sounds, List<String> targetPlayers) {
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

    public int getDelay() {
        return delay;
    }

    public List<HavokMessage> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public List<String> getTargetPlayers() {
        return targetPlayers;
    }

    public boolean allowTargetViaNote() {
        return allowTargetViaNote;
    }

    public List<Double> getTriggerTiers() {
        return triggerTiers;
    }

    public boolean targetAllPlayers() {
        return targetAllPlayers;
    }

    public void wreak(EntityPlayer player, Donation donation) {
        DonationHavok.INSTANCE.getScheduler().scheduleTask("HavokRewards-Delay:" + delay, delay, () -> {
            ITextComponent firstLine = new TextComponentString(donation.getName()).setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new TextComponentString(" donated!").setStyle(new Style().setColor(TextFormatting.RESET)));
            ITextComponent secondLine = new TextComponentString("\nThey triggered ").setStyle(new Style().setColor(TextFormatting.RESET)).appendSibling(new TextComponentString(name).setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new TextComponentString("!").setStyle(new Style().setColor(TextFormatting.RESET)));
            ITextComponent finalMessage = new TextComponentString("").appendSibling(firstLine).appendSibling(secondLine);
            if (player.getEntityWorld().isRemote) {
                PlayerList playerList = FMLServerHandler.instance().getServer().getPlayerList();
                playerList.sendMessage(finalMessage, false);
            }
            else {
                player.sendMessage(finalMessage);
            }

            parseTargetPlayers(player, donation.getNote()).forEach(this::wreak);
        });
    }

    private List<EntityPlayer> parseTargetPlayers(EntityPlayer player, String note) {
        if (player.getEntityWorld().isRemote) {
            PlayerList playerList = FMLServerHandler.instance().getServer().getPlayerList();
            if (!targetPlayers.isEmpty()) {
                return targetPlayers.stream().map(playerList::getPlayerByUsername).filter(Objects::nonNull).collect(Collectors.toList());
            }

            if (targetAllPlayers) {
                return new ArrayList<>(playerList.getPlayers());
            }

            if (allowTargetViaNote && note.contains("@")) {
                List<String> usernames = Arrays.stream(note.split("@")).filter(string -> string.contains("@")).map(username -> username.replace("@", "")).collect(Collectors.toList());
                List<EntityPlayer> targetPlayers = new ArrayList<>();
                usernames.forEach(username -> {
                    EntityPlayer targetPlayer = playerList.getPlayerByUsername(username);
                    if (targetPlayer != null && targetPlayers.stream().map(EntityPlayer::getUniqueID).collect(Collectors.toList()).contains(targetPlayer.getUniqueID())) {
                        targetPlayers.add(targetPlayer);
                    }
                });

                return targetPlayers;
            }
        }

        return Collections.singletonList(player);
    }

    public void wreak(EntityPlayer player) {
        triggerTiers.forEach(tier -> DonationHavok.INSTANCE.getStreamLabsTracker().runDonation(player, tier));
        blocks.forEach(block -> block.wreak(player, player.getPosition()));
        entities.forEach(entity -> entity.wreak(player, player.getPosition()));
        itemStacks.forEach(itemStack -> itemStack.wreak(player, player.getPosition()));
        messages.forEach(message -> message.wreak(player, player.getPosition()));
        sounds.forEach(sound -> sound.wreak(player, player.getPosition()));
        particles.forEach(particle -> particle.wreak(player, player.getPosition()));
        commands.forEach(command -> command.wreak(player, player.getPosition()));
    }

    public List<HavokBlock> getBlocks() {
        return blocks;
    }

    public List<HavokCommand> getCommands() {
        return commands;
    }

    public List<HavokEntity> getEntities() {
        return entities;
    }

    public List<HavokItemStack> getItems() {
        return itemStacks;
    }

    public List<HavokParticle> getParticles() {
        return particles;
    }

    public List<HavokSound> getSounds() {
        return sounds;
    }
}
