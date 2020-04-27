package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.config.StreamlabsConfig;
import io.musician101.donationhavok.network.Network;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.network.NetworkDirection;

public class StreamlabsConfigGui extends Screen {

    private int id = 0;
    @Nonnull
    private StreamlabsConfig config;
    private GuiCheckBox enable;
    private TextFieldWidget accessToken;
    private TextFieldWidget delay;

    public StreamlabsConfigGui(@Nonnull StreamlabsConfig config) {
        super(new StringTextComponent("Streamlabs Settings"));
        this.config = config;
    }

    @Override
    protected void init() {
        enable = addButton(new GuiCheckBox(width / 2 - 100, height / 6 + 8, "Enable?", config.isEnabled()));
        accessToken = new TextFieldWidget(minecraft.fontRenderer, width / 2, height / 6 + 32, 80, minecraft.fontRenderer.FONT_HEIGHT + 5, config.getAccessToken());
        accessToken.setMaxStringLength(64);
        accessToken.setVisible(true);
        accessToken.setTextColor(TextFormatting.WHITE.getColor());
        children.add(accessToken);
        delay = new TextFieldWidget(minecraft.fontRenderer, width / 2, height / 6 + 56, 80, minecraft.fontRenderer.FONT_HEIGHT + 5, config.getDelay() + "");
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
        addButton(new Button(100, 10, width / 2 - 100, height / 6 + 80, "Done", (button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    StreamlabsConfig slc = new StreamlabsConfig(enable.isChecked(), Integer.parseInt(delay.getText()), accessToken.getText());
                    Network.INSTANCE.sendTo(slc, minecraft.player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
                }

                minecraft.displayGuiScreen(null);
            }, new StringTextComponent("Save changes?"), new StringTextComponent("")));
        })));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 6 - 8, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Access Token:", width / 2 - 100, height / 6 + 35, TextFormatting.WHITE.getColor());
        drawString(minecraft.fontRenderer, "Delay:", width / 2 - 100, height / 6 + 59, TextFormatting.WHITE.getColor());
        delay.render(mouseX, mouseY, partialTicks);
        accessToken.render(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        accessToken.tick();
        delay.tick();
    }
}
