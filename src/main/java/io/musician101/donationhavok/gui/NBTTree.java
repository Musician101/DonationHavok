package io.musician101.donationhavok.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants.NBT;

public class NBTTree extends AbstractList<NBTTree.NBTEntry> {

    private final CompoundNBT nbt;

    public NBTTree(CompoundNBT nbt, Screen parent, int width, int height, int top, int bottom, int slotHeight) {
        super(parent.getMinecraft(), width, height, top, bottom, slotHeight);
        this.nbt = nbt;
    }

    static class NBTEntry extends ExtendedList.AbstractListEntry<NBTEntry> {

        private final INBT nbt;
        private final Button button = new Button(16, 16, 0, 16, "-", b -> {
            if (b.getMessage().equals("-")) {
                b.setMessage("+");
            }
            else {
                b.setMessage("-");
            }
        });

        NBTEntry(INBT nbt) {
            this.nbt = nbt;
        }

        @Override
        public void render(int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean p_render_8_, float partialTicks) {
            switch (nbt.getId()) {
                case NBT.TAG_BYTE:
                    break;
                default:
            }
        }

        private void update() {
            //TODO left off here with custom config gui
        }
    }
}
