package io.musician101.donationhavok.util;

import java.util.Comparator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

public class IBlockStateIDComparator implements Comparator<IBlockState> {

    @Override
    public int compare(IBlockState o1, IBlockState o2) {
        RegistryNamespacedDefaultedByKey<ResourceLocation, Block> registry = Block.REGISTRY;
        return registry.getNameForObject(o1.getBlock()).toString().compareTo(registry.getNameForObject(o2.getBlock()).toString());
    }
}
