package io.musician101.donationhavok.gui;

import io.musician101.donationhavok.gui.PermissionsList.PermissionEntry;
import io.musician101.donationhavok.handler.twitch.commands.CommandPermission;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.TextFormatting;

public class PermissionsList extends ExtendedList<PermissionEntry> {

    CommandPermission permission;
    private int index;
    private final List<CommandPermission> permissions;

    public PermissionsList(TwitchCommandConfigGui parent, List<CommandPermission> permissions) {
        super(parent.getMinecraft(), 250, parent.height, parent.height / 16 + parent.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 10, parent.height - 59, parent.getMinecraft().fontRenderer.FONT_HEIGHT * 2 + 8);
        this.permissions = permissions;
        this.centerListVertically = false;
        setIndex(0);
    }

    public void setIndex(int index) {
        if (this.index == index) {
            this.index = -1;
        }
        else {
            this.index = index;
        }

        this.permission = (this.index >= 0 && this.index < permissions.size()) ? permissions.get(this.index) : null;
    }

    @Override
    public int getRowWidth() {
        return width;
    }

    @Override
    protected int getScrollbarPosition() {
        return getRight() - 6;
    }

    void refreshList() {
        clearEntries();
        sort();
        permissions.forEach(permission -> addEntry(new PermissionEntry(permission)));
    }

    private void sort() {
        permissions.sort(Enum::compareTo);
    }

    public void swap(List<CommandPermission> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
        sort();
        setIndex(0);
    }

    static class PermissionEntry extends ExtendedList.AbstractListEntry<PermissionEntry> {

        private final CommandPermission permission;

        PermissionEntry(CommandPermission permission) {
            this.permission = permission;
        }

        @Override
        public void render(int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean p_render_8_, float partialTicks) {
            FontRenderer fr = Minecraft.getInstance().fontRenderer;
            fr.drawString(permission.toString(), left + 3, top + 2 + fr.FONT_HEIGHT / 2F, TextFormatting.WHITE.getColor());
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            //parent.setIndex(getIndex());
            return false;
        }
    }
}
