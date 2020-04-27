package io.musician101.donationhavok.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.musician101.donationhavok.gui.BlockList.BlockEntry;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockList extends ExtendedList<BlockEntry> {

    private final NonReplaceableBlocksGui parent;
    Block block;
    private int index;
    private final List<Block> blocks;

    BlockList(NonReplaceableBlocksGui parent, List<Block> blocks) {
        super(parent.getMinecraft(), 250, parent.height, parent.height / 16 + parent.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 10, parent.height - 59, parent.getMinecraft().fontRenderer.FONT_HEIGHT * 2 + 8);
        this.parent = parent;
        this.blocks = blocks;
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

    void refreshList(String string) {
        clearEntries();
        sort();
        blocks.stream().filter(block -> {
            ResourceLocation rs = ForgeRegistries.BLOCKS.getKey(block);
            if (rs == null) {
                return false;
            }

            if (StringUtils.isNullOrEmpty(string)) {
                return true;
            }

            return blocks.contains(block);
        }).forEach(block -> addEntry(new BlockEntry(block, this)));
    }

    private void sort() {
        blocks.sort((b1, b2) -> {
            IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;
            return registry.getKey(b1).compareTo(registry.getKey(b2));
        });
    }

    public void swap(List<Block> blocks) {
        this.blocks.clear();
        this.blocks.addAll(blocks);
        sort();
        setIndex(0);
    }

    static class BlockEntry extends ExtendedList.AbstractListEntry<BlockList.BlockEntry> {

        private final Block block;
        private final BlockList parent;

        BlockEntry(Block block, BlockList parent) {
            this.block = block;
            this.parent = parent;
        }

        @Override
        public void render(int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean p_render_8_, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(AbstractGui.STATS_ICON_LOCATION);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(left + 2, top + 19, 100).tex(0, 18 * 0.0078125F).endVertex();
            bufferbuilder.pos(left + 20, top + 19, 100).tex(18 * 0.0078125F, 18 * 0.0078125F).endVertex();
            bufferbuilder.pos(left + 20, top + 1, 100).tex(18 * 0.0078125F, 0).endVertex();
            bufferbuilder.pos(left + 2, top + 1, 100).tex(0, 0).endVertex();
            tessellator.draw();
            String id = ForgeRegistries.BLOCKS.getKey(block).toString();
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
            itemRender.zLevel = 100;
            itemRender.renderItemIntoGUI(block.asItem().getDefaultInstance(), left + 3, top + 2);
            itemRender.zLevel = 0;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            FontRenderer fr = Minecraft.getInstance().fontRenderer;
            int itemIDLeft = left + 23;
            int itemIDTop = top + 2 + fr.FONT_HEIGHT / 2;
            if (fr.getStringWidth(id) > 212) {
                fr.drawString(fr.trimStringToWidth(id, 209) + "...", itemIDLeft, itemIDTop, TextFormatting.WHITE.getColor());
            }
            else {
                fr.drawString(id, itemIDLeft, itemIDTop, TextFormatting.WHITE.getColor());
            }
        }
    }
}
