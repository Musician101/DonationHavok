package io.musician101.donationhavok.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.musician101.donationhavok.gui.HavokBlockList.BlockEntry;
import io.musician101.donationhavok.handler.havok.HavokBlock;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.state.IProperty;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

public class HavokBlockList extends ExtendedList<BlockEntry> {

    private final HavokBlocksGui parent;
    HavokBlock block;
    private int index;
    private final List<HavokBlock> blocks;

    HavokBlockList(HavokBlocksGui parent, List<HavokBlock> blocks) {
        super(parent.getMinecraft(), 250, parent.height, parent.height / 16 + parent.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 10, parent.height - 59, parent.getMinecraft().fontRenderer.FONT_HEIGHT * 4 + 8);
        this.parent = parent;
        this.blocks = blocks;
        centerListVertically = false;
        setIndex(0);
    }

    public void setIndex(int index) {
        if (this.index == index) {
            this.index = -1;
        }
        else {
            this.index = index;
        }

        this.block = (this.index >= 0 && this.index < blocks.size()) ? blocks.get(this.index) : null;
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
        blocks.forEach(block -> addEntry(new HavokBlockList.BlockEntry(block, this)));
    }

    static class BlockEntry extends ExtendedList.AbstractListEntry<HavokBlockList.BlockEntry> {

        private final HavokBlock block;
        private final HavokBlockList parent;

        BlockEntry(HavokBlock block, HavokBlockList parent) {
            this.block = block;
            this.parent = parent;
        }

        @Override
        public void render(int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean p_render_8_, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(Screen.STATS_ICON_LOCATION);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos((double) (left + 2), (double) (top + 19), 100 ).tex(0, (double) (18 * 0.0078125F)).endVertex();
            bufferbuilder.pos((double) (left + 20), (double) (top + 19), 100).tex((double) (18 * 0.0078125F), (double) (18 * 0.0078125F)).endVertex();
            bufferbuilder.pos((double) (left + 20), (double) (top + 1), 100).tex((double) (18 * 0.0078125F), 0).endVertex();
            bufferbuilder.pos((double) (left + 2), (double) (top + 1), 100).tex(0, 0).endVertex();
            tessellator.draw();
            BlockState blockState = block.getBlockState();
            Block block = blockState.getBlock();
            String id = ForgeRegistries.BLOCKS.getKey(block).toString();
            List<String> properties = new ArrayList<>();
            blockState.getValues().forEach((key, value) -> properties.add(key.getName() + "=" + ((IProperty) key).getName(value)));
            String state = "BlockState: " + StringUtils.join(properties, ", ");
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            ItemRenderer itemRender = parent.parent.getMinecraft().getItemRenderer();
            itemRender.zLevel = 100;
            itemRender.renderItemIntoGUI(block.asItem().getDefaultInstance(), left + 3, top + 2);
            itemRender.zLevel = 0;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            FontRenderer fr = Minecraft.getInstance().fontRenderer;
            int itemIDLeft = left + 23;
            int itemIDTop = top + 1;
            if (fr.getStringWidth(id) > 212) {
                fr.drawString(fr.trimStringToWidth(id, 209) + "...", itemIDLeft, itemIDTop, TextFormatting.WHITE.getColor());
            }
            else {
                fr.drawString(id, itemIDLeft, itemIDTop, TextFormatting.WHITE.getColor());
            }

            fr.drawString("Delay: " + this.block.getDelay() + " | Offset: " + this.block.getXOffset() + ", " + this.block.getYOffset() + ", " + this.block.getZOffset(), itemIDLeft, itemIDTop + fr.FONT_HEIGHT + 1, TextFormatting.GRAY.getColor());
            if (fr.getStringWidth(state) > 232) {
                fr.drawString(fr.trimStringToWidth(state, 229) + "...", left + 3, itemIDTop + fr.FONT_HEIGHT * 2 + 2, TextFormatting.GRAY.getColor());
            }
            else {
                fr.drawString(state, left + 3, itemIDTop + fr.FONT_HEIGHT * 2 + 2, TextFormatting.GRAY.getColor());
            }

            String nbtKeys = "NBT: " + StringUtils.join(this.block.getCompoundNBT().keySet(), ", ");
            if (fr.getStringWidth(nbtKeys) > 232) {
                fr.drawString(nbtKeys, left + 3, itemIDTop + fr.FONT_HEIGHT * 3 + 3, TextFormatting.GRAY.getColor());
            }
            else {
                fr.drawString(nbtKeys, left + 3, itemIDTop + fr.FONT_HEIGHT * 3 + 3, TextFormatting.GRAY.getColor());
            }
        }
    }
}
