package io.musician101.donationhavok.gui.render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public final class IBlockStateCellRenderer implements ListCellRenderer<IBlockState> {

    @Override
    public Component getListCellRendererComponent(JList<? extends IBlockState> list, IBlockState value, int index, boolean isSelected, boolean cellHasFocus) {
        Block block = value.getBlock();
        ResourceLocation resourceLocation = Block.REGISTRY.getNameForObject(block);
        JLabel jLabel = new JLabel(resourceLocation.toString());
        jLabel.setOpaque(true);
        jLabel.setIconTextGap(12);
        if (isSelected) {
            jLabel.setBackground(new Color(184, 207, 229));
        }
        else {
            jLabel.setBackground(Color.WHITE);
        }

        return jLabel;
    }
}
