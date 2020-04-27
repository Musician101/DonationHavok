package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.config.TwitchCommandConfig;
import io.musician101.donationhavok.config.TwitchCommandsConfig;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class TwitchCommandsConfigGui extends Screen {

    @Nonnull
    private final TwitchConfigGui parent;
    @Nonnull
    private TwitchCommandsConfig config;
    private GuiCheckBox enabled;
    private TwitchCommandConfig discoveryCommand;
    private TwitchCommandConfig playersCommand;
    private TwitchCommandConfig rewardsCommand;
    private int id = 0;

    TwitchCommandsConfigGui(@Nonnull TwitchConfigGui parent, @Nonnull TwitchCommandsConfig config) {
        super(new StringTextComponent("Twitch Command Settings"));
        this.parent = parent;
        this.config = config;
        this.discoveryCommand = config.getDiscoveryCommandConfig();
        this.playersCommand = config.getPlayersCommandConfig();
        this.rewardsCommand = config.getRewardsCommandConfig();
    }

    @Override
    protected void init() {
        enabled = addButton(new GuiCheckBox( width / 2 - minecraft.fontRenderer.getStringWidth("Enabled?") / 2 - 7, height / 6 + 8, "Enabled?", discoveryCommand.isEnabled()));
        Button discoveryCommandButton = addButton(new Button(100, 10, width / 2 - 100, height / 6 + enabled.getHeight() + 15, "Discovery", b -> minecraft.displayGuiScreen(new TwitchCommandConfigGui(TwitchCommandsConfigGui.this, discoveryCommand, "Discovery", (isEnabled, permissions) -> discoveryCommand = new TwitchCommandConfig(isEnabled, permissions)))));
        Button playersCommandButton = addButton(new Button(100, 10, width / 2 - 100, discoveryCommandButton.y + discoveryCommandButton.getHeight() + 5, "Players", b -> minecraft.displayGuiScreen(new TwitchCommandConfigGui(TwitchCommandsConfigGui.this, playersCommand, "Players", (isEnabled, permissions) -> playersCommand = new TwitchCommandConfig(isEnabled, permissions)))));
        Button rewardsCommandButton = addButton(new Button(100, 10, width / 2 - 100, playersCommandButton.y + playersCommandButton.getHeight() + 5, "Rewards", b -> minecraft.displayGuiScreen(new TwitchCommandConfigGui(TwitchCommandsConfigGui.this, rewardsCommand, "Rewards", (isEnabled, permissions) -> rewardsCommand = new TwitchCommandConfig(isEnabled, permissions)))));
        addButton(new Button(100, 10, width / 2 - 100, rewardsCommandButton.y + rewardsCommandButton.getHeight() + 5, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    parent.twitchCommands = new TwitchCommandsConfig(enabled.isChecked(), discoveryCommand, playersCommand, rewardsCommand);
                }

                minecraft.displayGuiScreen(parent);
            }, new StringTextComponent("Do you want to save your changes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 6 - 8, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
    }
}
