package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.havok.HavokBlock;
import java.util.List;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class HavokBlocksGui extends Screen {

    private RewardTierConfigGui parent;
    List<HavokBlock> blocks;
    HavokBlockList blockList;
    private int id = 0;

    public HavokBlocksGui(RewardTierConfigGui parent, List<HavokBlock> blocks) {
        super(new StringTextComponent("HavokBlocks"));
        this.parent = parent;
        this.blocks = blocks;
    }

    //TODO add clone buttons
    @Override
    protected void init() {
        blockList = new HavokBlockList(this, blocks);
        blockList.setLeftPos(width / 2 - 46);
        children.add(blockList);
        Button newButton = addButton(new Button(width / 2 - 205, blockList.getTop(), 155, 20, "New", button -> {
            minecraft.displayGuiScreen(new HavokBlockGui(this, new HavokBlock(), -1));
        }));
        Button editButton = addButton(new Button(width / 2 - 205, newButton.y + newButton.getHeight() + 5, 155, 20, "Edit", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        Button removeButton = addButton(new Button(width / 2 - 205, editButton.y + editButton.getHeight() + 5, 155, 20, "Remove", button -> {
            DonationHavok.getInstance().getLogger().error("quack");
        }));
        addButton(new Button(width / 2 - 205, removeButton.y + removeButton.getHeight() + 5, 155, 20, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    parent.blocks.clear();
                    parent.blocks.addAll(blocks);
                }

                minecraft.displayGuiScreen(parent);
            }, new StringTextComponent("Save Changes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return blockList.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        blockList.render(mouseX, mouseY, partialTicks);
        drawCenteredString(minecraft.fontRenderer, "Blocks", width / 2, height / 16, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        blockList.refreshList();
    }
}
