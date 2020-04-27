package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.config.RewardsConfig;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.network.Network;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkDirection;

public class RewardTiersConfigGui extends Screen {

    @Nonnull
    private final TreeMap<Double, HavokRewards> rewards;
    private RewardsList rewardsList;
    private TextFieldWidget searchAmount;
    private TextFieldWidget searchName;
    private Button newButton;
    private Button editButton;
    private Button removeButton;
    private int id = 0;

    public RewardTiersConfigGui(@Nonnull RewardsConfig config) {
        super(new StringTextComponent("Rewards Config"));
        this.rewards = config.getRewards();
    }

    @Override
    protected void init() {
        rewardsList = new RewardsList(this, rewards);
        rewardsList.setLeftPos(width / 2 - 46);
        children.add(rewardsList);
        searchAmount = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 205, rewardsList.getTop() + minecraft.fontRenderer.FONT_HEIGHT + 5, 155, 20, "");
        searchAmount.setValidator(s -> {
            if (StringUtils.isNullOrEmpty(s)) {
                return true;
            }

            Pattern pattern = Pattern.compile("([^0-9.])");
            Matcher matcher = pattern.matcher(s);
            return !matcher.find();
        });
        searchAmount.setVisible(true);
        searchAmount.setTextColor(TextFormatting.WHITE.getColor());
        searchAmount.changeFocus(true);
        searchAmount.setCanLoseFocus(false);
        children.add(searchAmount);
        searchName = new TextFieldWidget(minecraft.fontRenderer, width / 2 - 205, searchAmount.y + searchAmount.getHeight() + minecraft.fontRenderer.FONT_HEIGHT + 5, 155, 20, "");
        searchName.setVisible(true);
        searchName.setTextColor(TextFormatting.WHITE.getColor());
        searchName.changeFocus(true);
        searchName.setCanLoseFocus(false);
        children.add(searchName);
        newButton = addButton(new Button(width / 2 - 205, searchName.y + searchName.getHeight() + 5, 155, 20, "New", button -> minecraft.displayGuiScreen(new RewardTierConfigGui(RewardTiersConfigGui.this))));
        editButton = addButton(new Button(width / 2 - 205, newButton.y + newButton.getHeight() + 5, 155, 20, "Edit", button -> {

        }));
        removeButton = addButton(new Button(width / 2 - 205, editButton.y + editButton.getHeight() + 5, 155, 20, "Remove", button -> {

        }));
        addButton(new Button(width / 2 - 205, removeButton.y + removeButton.getHeight() + 5, 155, 20, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                if (b) {
                    RewardsConfig rc = new RewardsConfig(rewards);
                    Network.INSTANCE.sendTo(rc, minecraft.player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
                }

                minecraft.displayGuiScreen(null);
            }, new StringTextComponent("Save Changes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        rewardsList.render(mouseX, mouseY, partialTicks);
        searchAmount.render(mouseX, mouseY, partialTicks);
        searchName.render(mouseX, mouseY, partialTicks);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 16, TextFormatting.WHITE.getColor());
        drawCenteredString(minecraft.fontRenderer, "Search by Amount", searchAmount.x + searchAmount.getWidth() / 2, searchAmount.y - 10, TextFormatting.WHITE.getColor());
        drawCenteredString(minecraft.fontRenderer, "Search by Name", searchName.x + searchName.getWidth() / 2, searchName.y - 10, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        rewardsList.refreshList(searchAmount.getText(), searchName.getText());
        searchAmount.tick();
        searchName.tick();
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return rewardsList.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }
}
