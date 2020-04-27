package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.handler.havok.HavokRewards;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;

public class RewardsList extends ExtendedList<RewardsList.RewardsEntry> {

    private Entry<Double, HavokRewards> rewardTier;
    private int index;
    private final List<Entry<Double, HavokRewards>> rewardTiers;

    RewardsList(RewardTiersConfigGui parent, TreeMap<Double, HavokRewards> rewardTiers) {
        super(parent.getMinecraft(), 250, parent.height, parent.height / 16 + parent.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 10, parent.height - 59, parent.getMinecraft().fontRenderer.FONT_HEIGHT * 4 + 6);
        this.rewardTiers = new ArrayList<>(rewardTiers.entrySet());
        this.centerListVertically = false;
        setIndex(0);
    }

    public void setIndex(int index) {
        if (this.index == index) {
            this.index = -1;
        }
        else {
            this.index = index;
        }

        this.rewardTier = (this.index >= 0 && this.index < rewardTiers.size()) ? rewardTiers.get(this.index) : null;
    }

    @Override
    public int getRowWidth() {
        return width;
    }

    @Override
    protected int getScrollbarPosition() {
        return getRight() - 6;
    }

    void refreshList(String amount, String rewardName) {
        clearEntries();
        sort();
        rewardTiers.stream().filter(rewardTier -> {
            if (!StringUtils.isNullOrEmpty(amount)) {
                return (rewardTier.getKey() + "").contains(amount);
            }

            return true;
        }).filter(rewardTier -> {
            if (!StringUtils.isNullOrEmpty(rewardName)) {
                return rewardTier.getValue().getName().contains(rewardName);
            }

            return true;
        }).forEach(rewardTier -> addEntry(new RewardsEntry(rewardTier)));
    }

    private void sort() {
        rewardTiers.sort(Comparator.comparing(Entry::getKey));
    }

    static class RewardsEntry extends ExtendedList.AbstractListEntry<RewardsEntry> {

        private final Entry<Double, HavokRewards> rewardTier;

        RewardsEntry(Entry<Double, HavokRewards> rewardTier) {
            this.rewardTier = rewardTier;
        }

        @Override
        public void render(int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean p_render_8_, float partialTicks) {
            double amount = rewardTier.getKey();
            HavokRewards rewards = rewardTier.getValue();
            FontRenderer fr = Minecraft.getInstance().fontRenderer;
            fr.drawString(rewards.getName() + " for " + amount, left + 3, top + 2, TextFormatting.WHITE.getColor());
            fr.drawString("Delay: " + rewards.getDelay() + " | Starts Sale? " + rewards.triggersSale(), left + 3, top + 11, TextFormatting.GRAY.getColor());
            fr.drawString("Discount: " + rewards.getDiscount() + " | Sale Length: " + rewards.getSaleLength(), left + 3, top + 20, TextFormatting.GRAY.getColor());
            String parts = "Parts: ";
            if (!rewards.getBlocks().isEmpty()) {
                parts += "Blocks, ";
            }

            if (!rewards.getCommands().isEmpty()) {
                parts += "Commands, ";
            }

            if (!rewards.getEntities().isEmpty()) {
                parts += "Entities, ";
            }

            if (!rewards.getItems().isEmpty()) {
                parts += "Items, ";
            }

            if (!rewards.getMessages().isEmpty()) {
                parts += "Messages, ";
            }

            if (!rewards.getParticles().isEmpty()) {
                parts += "Particles, ";
            }

            if (!rewards.getTargetPlayers().isEmpty()) {
                parts += "Target Players, ";
            }

            if (!rewards.getTriggerTiers().isEmpty()) {
                parts += "Trigger Tiers, ";
            }

            if (!rewards.getSchematics().isEmpty()) {
                parts += "Schematics, ";
            }

            if (!rewards.getSounds().isEmpty()) {
                parts += "Sounds, ";
            }

            if (!rewards.getStructures().isEmpty()) {
                parts += "Structures";
            }

            if (parts.equals("Parts: ")) {
                parts += "None";
            }

            if (fr.getStringWidth(parts) > 212) {
                fr.drawString(fr.trimStringToWidth(parts, 209) + "...", left + 3, top + 29, TextFormatting.GRAY.getColor());
            }
            else {
                fr.drawString(parts, left + 3, top + 26 + fr.FONT_HEIGHT, TextFormatting.GRAY.getColor());
            }
        }
    }
}
