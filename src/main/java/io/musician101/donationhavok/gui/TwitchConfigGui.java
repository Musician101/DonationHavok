package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.config.TwitchCommandsConfig;
import io.musician101.donationhavok.config.TwitchConfig;
import io.musician101.donationhavok.network.Network;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.network.NetworkDirection;

public class TwitchConfigGui extends Screen {

    @Nonnull
    private final TwitchConfig config;
    private GuiCheckBox enable;
    private GuiCheckBox bitsTrigger;
    private GuiCheckBox factorSubStreak;
    private GuiCheckBox roundSubs;
    private GuiCheckBox subsTrigger;
    private TextFieldWidget streamerName;
    @Nonnull
    TwitchCommandsConfig twitchCommands;

    public TwitchConfigGui(@Nonnull TwitchConfig config) {
        super(new StringTextComponent("Twitch Settings"));
        this.config = config;
        twitchCommands = config.getTwitchCommandsConfig();
    }

    @Override
    protected void init() {
        enable = addButton(new GuiCheckBox( width / 2 - 100, height / 6 + 8, "Enable?", config.isEnabled()));
        bitsTrigger = addButton(new GuiCheckBox( width / 2 - 100, height / 6 + 32, "Bits Trigger Rewards?", config.doBitsTrigger()));
        subsTrigger = addButton(new GuiCheckBox( width / 2 - 100, height / 6 + 56, "Subscriptions Trigger Rewards?", config.doSubsTrigger()));
        factorSubStreak = addButton(new GuiCheckBox( width / 2 - 100, height / 6 + 80, "Apply Sub Streak to Subscription Tier cost?", config.factorSubStreak()));
        roundSubs = addButton(new GuiCheckBox( width / 2 - 100, height / 6 + 104, "Round Subscriptions to the nearest whole dollar?", config.roundSubs()));
        streamerName = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 100 + minecraft.fontRenderer.getStringWidth("Twitch Name:") + 5, height / 6 + 128, 200 - minecraft.fontRenderer.getStringWidth("Twitch Name:") - 6, minecraft.fontRenderer.FONT_HEIGHT + 5, config.getTwitchName());
        streamerName.setVisible(true);
        streamerName.setTextColor(TextFormatting.WHITE.getColor());
        children.add(streamerName);
        addButton(new Button(100, 10, width / 2 - 100, height / 6 + 152, "Commands", button -> {
            minecraft.displayGuiScreen(new TwitchCommandsConfigGui(TwitchConfigGui.this, twitchCommands));
        }) );
        addButton(new Button(100, 10, width / 2 - 100, height / 6 + 174, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    TwitchConfig twitchConfig = new TwitchConfig(bitsTrigger.isChecked(), enable.isChecked(), roundSubs.isChecked(), factorSubStreak.isChecked(), subsTrigger.isChecked(), streamerName.getText(), twitchCommands);
                    Network.INSTANCE.sendTo(twitchConfig, minecraft.player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
                }

                minecraft.displayGuiScreen(null);
            }, new StringTextComponent("Do you want to save your changes?"), new StringTextComponent("")));
        }) );
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 6 - 8, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Twitch Name:", width / 2 - 100, height / 6 + 131, TextFormatting.WHITE.getColor());
        streamerName.render(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        streamerName.tick();
    }
}
