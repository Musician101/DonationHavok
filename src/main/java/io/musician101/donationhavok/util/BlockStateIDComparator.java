package io.musician101.donationhavok.util;

import java.util.Comparator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockStateIDComparator implements Comparator<BlockState> {

    @Override
    public int compare(BlockState o1, BlockState o2) {
        IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;
        return registry.getKey(o1.getBlock()).compareTo(registry.getKey(o2.getBlock()));
    }
}
