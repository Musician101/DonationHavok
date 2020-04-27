package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.havok.HavokBlock;
import io.musician101.donationhavok.handler.havok.HavokCommand;
import io.musician101.donationhavok.handler.havok.HavokEntity;
import io.musician101.donationhavok.handler.havok.HavokItemStack;
import io.musician101.donationhavok.handler.havok.HavokMessage;
import io.musician101.donationhavok.handler.havok.HavokParticle;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.handler.havok.HavokSchematic;
import io.musician101.donationhavok.handler.havok.HavokSound;
import io.musician101.donationhavok.handler.havok.HavokStructure;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class RewardTierConfigGui extends Screen {

    @Nonnull
    private final RewardTiersConfigGui parent;
    private final double minAmount;
    final List<HavokBlock> blocks;
    final List<HavokCommand> commands;
    final List<HavokEntity> entities;
    final List<HavokItemStack> itemStacks;
    final List<HavokMessage> messages;
    final List<HavokParticle> particles;
    final List<HavokSchematic> schematics;
    final List<HavokSound> sounds;
    final List<HavokStructure> structures;
    final List<String> targetPlayers;
    final List<Double> triggerTiers;
    private GuiCheckBox allowTargetViaNote;
    private GuiCheckBox targetAllPlayers;
    private GuiCheckBox triggerSale;
    private TextFieldWidget delay;
    private TextFieldWidget discount;
    private TextFieldWidget name;
    private TextFieldWidget saleLength;
    private final HavokRewards rewards;
    private int id = 0;

    RewardTierConfigGui(@Nonnull RewardTiersConfigGui parent) {
        this(new SimpleEntry<>(0D, new HavokRewards()), parent);
    }

    RewardTierConfigGui(@Nonnull Entry<Double, HavokRewards> rewardTier, @Nonnull RewardTiersConfigGui parent) {
        super(new StringTextComponent("Rewards"));
        this.parent = parent;
        this.minAmount = rewardTier.getKey();
        this.rewards = rewardTier.getValue();
        this.blocks = rewards.getBlocks();
        this.commands = rewards.getCommands();
        this.entities = rewards.getEntities();
        this.itemStacks = rewards.getItems();
        this.messages = rewards.getMessages();
        this.particles = rewards.getParticles();
        this.schematics = rewards.getSchematics();
        this.sounds = rewards.getSounds();
        this.structures = rewards.getStructures();
        this.targetPlayers = rewards.getTargetPlayers();
        this.triggerTiers = rewards.getTriggerTiers();
    }

    @Override
    protected void init() {
        allowTargetViaNote = addButton(new GuiCheckBox(width / 2 - 200, height / 6 - 12, "Target Players Via Donation Notes?", rewards.allowTargetViaNote()));
        targetAllPlayers = addButton(new GuiCheckBox(width / 2 - 200, allowTargetViaNote.y + 21, "Target All Players?", rewards.targetAllPlayers()));
        triggerSale = addButton(new GuiCheckBox(width / 2 - 200, targetAllPlayers.y + 21, "Triggers Sale?", rewards.triggersSale()));
        delay = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Delay:") + 5, triggerSale.y + 20, 200 - minecraft.fontRenderer.getStringWidth("Delay:") - 6, minecraft.fontRenderer.FONT_HEIGHT + 5, rewards.getDelay() + "");
        delay.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return false;
            }

            try {
                Integer.parseInt(s);
                return true;
            }
            catch (NumberFormatException ignored) {
                return false;
            }
        });
        delay.setVisible(true);
        delay.setTextColor(TextFormatting.WHITE.getColor());
        children.add(delay);
        discount = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Discount:") + 5, delay.y + 21, 200 - minecraft.fontRenderer.getStringWidth("Discount:") - 6, minecraft.fontRenderer.FONT_HEIGHT + 5, rewards.getDiscount() + "");
        discount.setVisible(true);
        discount.setTextColor(TextFormatting.WHITE.getColor());
        children.add(discount);
        name = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Name:") + 5, discount.y + 21, 200 - minecraft.fontRenderer.getStringWidth("Name:") - 6, minecraft.fontRenderer.FONT_HEIGHT + 5, rewards.getName() + "");
        name.setVisible(true);
        name.setTextColor(TextFormatting.WHITE.getColor());
        children.add(name);
        saleLength = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Sale Length:") + 5, name.y + 21, 200 - minecraft.fontRenderer.getStringWidth("Sale Length:") - 6, minecraft.fontRenderer.FONT_HEIGHT + 5, rewards.getSaleLength() + "");
        saleLength.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return false;
            }

            try {
                Integer.parseInt(s);
                return true;
            }
            catch (NumberFormatException ignored) {
                return false;
            }
        });
        saleLength.setVisible(true);
        saleLength.setTextColor(TextFormatting.WHITE.getColor());
        children.add(saleLength);
        Button blocks = addButton(new Button(100, 10,width / 2 - 200, saleLength.y + 18, "Blocks", button -> {
            Minecraft.getInstance().displayGuiScreen(new HavokBlocksGui(this, this.blocks));
        }));
        addButton(new Button(100, 10,width / 2 - 200, blocks.y + blocks.getHeight() + 1, "Commands", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button entities = addButton(new Button(100, 10,width / 2, height / 6 - 16, "Entities", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button itemStacks = addButton(new Button(100, 10,width / 2, entities.y + entities.getHeight() + 1, "ItemStacks", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button messages = addButton(new Button(100, 10,width / 2, itemStacks.y + itemStacks.getHeight() + 1, "Messages", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button particles = addButton(new Button(100, 10,width / 2, messages.y + messages.getHeight() + 1, "Particles", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button schematics = addButton(new Button(100, 10,width / 2, particles.y + particles.getHeight() + 1, "Schematics", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button sounds = addButton(new Button(100, 10,width / 2, schematics.y + schematics.getHeight() + 1, "Sounds", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button structures = addButton(new Button(100, 10,width / 2, sounds.y + sounds.getHeight() + 1, "Structures", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button targetPlayers = addButton(new Button(100, 10,width / 2, structures.y + structures.getHeight() + 1, "Target Players", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button triggerTiers = addButton(new Button(100, 10,width / 2, targetPlayers.y + targetPlayers.getHeight() + 1, "Trigger Tiers", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        addButton(new Button(100, 10,width / 2 - 100, triggerTiers.y + triggerTiers.getHeight() + 1, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    //TODO no code yet
                }

                minecraft.displayGuiScreen(parent);
            }, new StringTextComponent("Do you want to save your changes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        //blockList.drawScreen(mouseX, mouseY, partialTicks);
        delay.render(mouseX, mouseY, partialTicks);
        discount.render(mouseX, mouseY, partialTicks);
        name.render(mouseX, mouseY, partialTicks);
        saleLength.render(mouseX, mouseY, partialTicks);
        drawCenteredString(minecraft.fontRenderer, title.getString(), width / 2, height / 6 - 32, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Delay:", width / 2 - 200, delay.y + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Discount:", width / 2 - 200, discount.y + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Name:", width / 2 - 200, name.y + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Sale Length:", width / 2 - 200, saleLength.y + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        //blockList.refreshList();
        delay.tick();
        discount.tick();
        //discount.tick();
        //name.tick();
        //saleLength.tick();
    }
}
