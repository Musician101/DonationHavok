package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.config.GeneralConfig;
import io.musician101.donationhavok.network.Network;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.network.NetworkDirection;

public class GeneralConfigGui extends Screen {

    @Nonnull
    private final GeneralConfig config;
    private int id = 0;
    private GuiCheckBox generateBook;
    private GuiCheckBox hideCurrentUntilDiscovered;
    private GuiCheckBox replaceUnbreakableBlocks;
    private TextFieldWidget delay;
    private TextFieldWidget mcName;
    @Nonnull
    final List<Block> nonReplaceableBlocks;

    public GeneralConfigGui(@Nonnull GeneralConfig config) {
        super(new StringTextComponent("General Settings"));
        this.config = config;
        nonReplaceableBlocks = config.getNonReplaceableBlocks();
    }

    @Override
    protected void init() {
        generateBook = addButton(new GuiCheckBox(width / 2 - 100, height / 6 + 8, "Generate Book", config.generateBook()));
        hideCurrentUntilDiscovered = addButton(new GuiCheckBox(width / 2 - 100, height / 6 + 32, "Hide Current Rewards Until Discovered", config.hideCurrentUntilDiscovered()));
        replaceUnbreakableBlocks = addButton(new GuiCheckBox(width / 2 - 100, height / 6 + 56, "Replace Unbreakable Blocks", config.replaceUnbreakableBlocks()));
        delay = new TextFieldWidget(minecraft.fontRenderer, width / 2, height / 6 + 80, 80, minecraft.fontRenderer.FONT_HEIGHT + 5, config.getDelay() + "");
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
        mcName = new TextFieldWidget(minecraft.fontRenderer, width / 2, height / 6 + 104, 80, minecraft.fontRenderer.FONT_HEIGHT + 5, config.getMCName());
        mcName.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^a-zA-Z0-9_])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        mcName.setVisible(true);
        mcName.setTextColor(TextFormatting.WHITE.getColor());
        children.add(mcName);
        addButton(new Button(100, 10, width / 2 - 100, height / 6 + 128, "Non-Replaceable Blocks", button -> {
            minecraft.displayGuiScreen(new NonReplaceableBlocksGui(GeneralConfigGui.this, nonReplaceableBlocks));
        }));
        addButton(new Button(100, 10, width / 2 - 100, height / 6 + 152, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    GeneralConfig gc = new GeneralConfig(generateBook.isChecked(), hideCurrentUntilDiscovered.isChecked(), replaceUnbreakableBlocks.isChecked(), Integer.parseInt(delay.getText()), nonReplaceableBlocks, "Musician101");
                    Network.INSTANCE.sendTo(gc, minecraft.player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
                }

                minecraft.displayGuiScreen(null);
            }, new StringTextComponent("Do you want to save your changes?"), new StringTextComponent("")));
        }) );
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 6 - 8, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Delay:", width / 2 - 100, height / 6 + 83, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Minecraft Name:", width / 2 - 100, height / 6 + 107, TextFormatting.WHITE.getColor());
        delay.render(mouseX, mouseY, partialTicks);
        mcName.render(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        delay.tick();
        mcName.tick();
    }
}
