package io.musician101.donationhavok.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

public class NonReplaceableBlocksGui extends Screen {

    @Nonnull
    private final GeneralConfigGui parent;
    private Button addRemoveButton;
    private Button availableBlocksButton;
    private Button selectedBlocksButton;
    private BlockList blockList;
    private TextFieldWidget search;
    private final List<Block> selectedBlocks = new ArrayList<>();

    NonReplaceableBlocksGui(@Nonnull GeneralConfigGui parent, @Nonnull List<Block> blocks) {
        super(new StringTextComponent("Non-Replaceable Blocks"));
        this.parent = parent;
        this.selectedBlocks.addAll(blocks);
    }

    @Override
    protected void init() {
        List<Block> availableBlocks = new ArrayList<>(ForgeRegistries.BLOCKS.getValues());
        availableBlocks.removeIf(block -> {
            if (block == Blocks.AIR) {
                return true;
            }

            if (block instanceof AirBlock) {
                return true;
            }

            return selectedBlocks.contains(block);
        });
        blockList = new BlockList(this, availableBlocks);
        blockList.setLeftPos(width / 2 - 46);
        children.add(blockList);
        availableBlocksButton = addButton(new Button(blockList.getLeft(), blockList.getBottom() + 5, blockList.getWidth() / 2 - 1, 20, "Available Blocks", button -> {
            availableBlocksButton.active = false;
            selectedBlocksButton.active = true;
            addRemoveButton.setMessage("Add");
            blockList.swap(ForgeRegistries.BLOCKS.getValues().stream().filter(block -> {
                if (block == Blocks.AIR) {
                    return false;
                }

                if (block instanceof AirBlock) {
                    return false;
                }

                return !selectedBlocks.contains(block);
            }).collect(Collectors.toList()));
        }));
        availableBlocksButton.active = false;
        selectedBlocksButton = addButton(new Button(blockList.getLeft() + blockList.getWidth() / 2 + 1, blockList.getBottom() + 5, availableBlocksButton.getWidth(), 20, "Selected Blocks", button -> {
            availableBlocksButton.active = true;
            selectedBlocksButton.active = false;
            addRemoveButton.setMessage("Remove");
            blockList.swap(selectedBlocks);
        }));
        search = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 205, blockList.getTop() + minecraft.fontRenderer.FONT_HEIGHT + 5, 155, 20, "");
        search.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^a-z:_])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        search.setVisible(true);
        search.setTextColor(TextFormatting.WHITE.getColor());
        search.changeFocus(true);
        search.setCanLoseFocus(false);
        children.add(search);
        addRemoveButton = addButton(new Button(width / 2 - 205, search.y + search.getHeight() + 5, 155, 20, "Add", button -> {
            if (availableBlocksButton.active) {
                selectedBlocks.remove(blockList.block);
                blockList.swap(selectedBlocks);
            }
            else {
                selectedBlocks.add(blockList.block);
                blockList.swap(ForgeRegistries.BLOCKS.getValues().stream().filter(block -> {
                    if (block == Blocks.AIR) {
                        return false;
                    }

                    if (block instanceof AirBlock) {
                        return false;
                    }

                    return !selectedBlocks.contains(block);
                }).collect(Collectors.toList()));
            }
        }));
        addButton(new Button(width / 2 - 205, addRemoveButton.y + addRemoveButton.getHeight() + 5, 155, 20, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    parent.nonReplaceableBlocks.clear();
                    parent.nonReplaceableBlocks.addAll(selectedBlocks);
                }

                minecraft.displayGuiScreen(parent);
            }, new StringTextComponent("Save changes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        blockList.render(mouseX, mouseY, partialTicks);
        search.render(mouseX, mouseY, partialTicks);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 16, TextFormatting.WHITE.getColor());
        drawCenteredString(minecraft.fontRenderer, "Search", search.x + search.getWidth() / 2, search.y - 10, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        blockList.refreshList(search.getText());
        search.tick();
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return blockList.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) || search.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return search.charTyped(p_charTyped_1_, p_charTyped_2_);
    }
}
