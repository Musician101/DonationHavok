package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.config.TwitchCommandConfig;
import io.musician101.donationhavok.handler.twitch.commands.CommandPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

//TODO need to go through GUIs again to fix button positions and sizes
public class TwitchCommandConfigGui extends Screen {

    @Nonnull
    private final BiConsumer<Boolean, List<CommandPermission>> action;
    @Nonnull
    private final TwitchCommandsConfigGui parent;
    @Nonnull
    private final TwitchCommandConfig config;
    @Nonnull
    private final String commandName;
    private Button addRemoveButton;
    private Button availablePermissionsButton;
    private Button selectedPermissionsButton;
    private GuiCheckBox enabled;
    private final List<CommandPermission> selectedPermissions = new ArrayList<>();
    private int id = 0;
    private PermissionsList permissionsList;

    TwitchCommandConfigGui(@Nonnull TwitchCommandsConfigGui parent, @Nonnull TwitchCommandConfig config, @Nonnull String commandName, @Nonnull BiConsumer<Boolean, List<CommandPermission>> action) {
        super(new StringTextComponent(commandName + " Command Settings"));
        this.parent = parent;
        this.config = config;
        this.commandName = commandName;
        selectedPermissions.addAll(config.getPermissions());
        this.action = action;
    }

    @Override
    protected void init() {
        List<CommandPermission> availablePermissions = Stream.of(CommandPermission.values()).filter(permission -> !selectedPermissions.contains(permission)).collect(Collectors.toList());
        permissionsList = new PermissionsList(this, availablePermissions);
        permissionsList.setLeftPos(width / 2 - 46);
        setFocused(permissionsList);
        children.add(permissionsList);
        availablePermissionsButton = addButton(new Button(permissionsList.getLeft(), permissionsList.getBottom() + 5, permissionsList.getWidth() / 2 - 1, 20, "Available Permissions", b -> {
            availablePermissionsButton.active = false;
            selectedPermissionsButton.active = true;
            addRemoveButton.setMessage("Add");
            permissionsList.swap(Stream.of(CommandPermission.values()).filter(permission -> !selectedPermissions.contains(permission)).collect(Collectors.toList()));
        }));
        availablePermissionsButton.active = false;
        selectedPermissionsButton = addButton(new Button(permissionsList.getLeft() + permissionsList.getWidth() / 2 + 1, permissionsList.getBottom() + 5, availablePermissionsButton.getWidth(), 20, "Selected Permissions", button -> {
            availablePermissionsButton.active = true;
            selectedPermissionsButton.active = false;
            addRemoveButton.setMessage("Remove");
            permissionsList.swap(selectedPermissions);
        }));
        enabled = addButton(new GuiCheckBox( width / 2 - 205, permissionsList.getTop(), "Enabled?", config.isEnabled()));
        addRemoveButton = addButton(new Button(width / 2 - 205, enabled.y + enabled.getHeight() + 5, 155, 20, "Add", button -> {
            if (availablePermissionsButton.active) {
                selectedPermissions.remove(permissionsList.permission);
                permissionsList.swap(selectedPermissions);
            }
            else {
                selectedPermissions.add(permissionsList.permission);
                permissionsList.swap(Stream.of(CommandPermission.values()).filter(permissions -> !selectedPermissions.contains(permissions)).collect(Collectors.toList()));
            }
        }));
        addButton(new Button(width / 2 - 205, addRemoveButton.y + addRemoveButton.getHeight() + 5, 155, 20, "Done", button -> {
            minecraft.displayGuiScreen(new ConfirmScreen(b -> {
                    if (b) {
                        action.accept(enabled.isChecked(), selectedPermissions);
                    }

                    minecraft.displayGuiScreen(parent);
            }, new StringTextComponent("Save changes?"), new StringTextComponent("")));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        permissionsList.render(mouseX, mouseY, partialTicks);
        drawCenteredString(minecraft.fontRenderer, title.getFormattedText(), width / 2, height / 16, TextFormatting.WHITE.getColor());
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        permissionsList.refreshList();
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return permissionsList.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }
}
