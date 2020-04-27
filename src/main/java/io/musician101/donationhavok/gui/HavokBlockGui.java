package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.handler.havok.HavokBlock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class HavokBlockGui extends Screen {

    private TextFieldWidget delay;
    private TextFieldWidget xOffset;
    private TextFieldWidget yOffset;
    private TextFieldWidget zOffset;
    private NBTTree nbtTree;
    private final HavokBlocksGui parent;
    private final HavokBlock block;
    private final int index;
    private int id = 0;

    public HavokBlockGui(HavokBlocksGui parent, HavokBlock block, int index) {
        super(new StringTextComponent("Block"));
        this.parent = parent;
        this.block = block;
        this.index = index;
    }

    @Override
    protected void init() {
        nbtTree = new NBTTree(block.getCompoundNBT(), this, 250, parent.height, parent.height / 16 + minecraft.fontRenderer.FONT_HEIGHT / 2 + 10, parent.height - 59, minecraft.fontRenderer.FONT_HEIGHT * 2 + 8);
        nbtTree.setLeftPos(width / 2 - 46);
        delay = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Delay:") + 5, height / 6 - 12, 200 - minecraft.fontRenderer.getStringWidth("Delay:") - 5, 20, block.getDelay() + "");
        delay.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^0-9])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        delay.setVisible(true);
        delay.setTextColor(TextFormatting.WHITE.getColor());
        xOffset = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("X Offset:") + 5, delay.y + delay.getHeight() + 5, 200 - minecraft.fontRenderer.getStringWidth("X Offset:") - 5, 20, block.getXOffset() + "");
        xOffset.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^0-9])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        xOffset.setVisible(true);
        xOffset.setTextColor(TextFormatting.WHITE.getColor());
        yOffset = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Y Offset:") + 5, xOffset.y + xOffset.getHeight() + 5, 200 - minecraft.fontRenderer.getStringWidth("Y Offset:") - 5, 20, block.getYOffset() + "");
        yOffset.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^0-9])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        yOffset.setVisible(true);
        yOffset.setTextColor(TextFormatting.WHITE.getColor());
        zOffset = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 200 + minecraft.fontRenderer.getStringWidth("Z Offset:") + 5, yOffset.y + yOffset.getHeight() + 5, 200 - minecraft.fontRenderer.getStringWidth("Z Offset:") - 5, 20, block.getZOffset() + "");
        zOffset.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^0-9])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        zOffset.setVisible(true);
        zOffset.setTextColor(TextFormatting.WHITE.getColor());
        addButton(new Button(100, 20, width / 2 - 100, height / 2 + 100, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    //TODO incomplete
                }

                minecraft.displayGuiScreen(parent);
            }, new StringTextComponent("Save Chantes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        delay.render(mouseX, mouseY, partialTicks);
        xOffset.render(mouseX, mouseY, partialTicks);
        yOffset.render(mouseX, mouseY, partialTicks);
        zOffset.render(mouseX, mouseY, partialTicks);
        drawCenteredString(minecraft.fontRenderer, "Block", width / 2, height / 16, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Delay:", width / 2 - 200, delay.y + 1 + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "X Offset:", width / 2 - 200, xOffset.y + 2 + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Y Offset:", width / 2 - 200, yOffset.y + 2 + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Z Offset:", width / 2 - 200, zOffset.y + 2 + minecraft.fontRenderer.FONT_HEIGHT / 2, TextFormatting.WHITE.getColor());
        nbtTree.render(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        delay.tick();
        xOffset.tick();
        yOffset.tick();
        zOffset.tick();
        //nbtTree.tick();
    }
}
