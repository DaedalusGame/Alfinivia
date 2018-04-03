package alfinivia.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public interface IBlockMatcher {
    boolean applies(World world, BlockPos pos, IBlockState state);

    static IBlockMatcher getAnyMeta()
    {
        return (world,pos,state) -> true;
    }

    static IBlockMatcher getMeta(int meta)
    {
        return (world,pos,state) -> state.getBlock().getMetaFromState(state) == meta;
    }

    static IBlockMatcher getBlockMeta(Block block, int meta) {
        return (world, pos, state) -> state.getBlock() == block && state.getBlock().getMetaFromState(state) == meta;
    }

    static IBlockMatcher getBlock(Block block)
    {
        return (world, pos, state) -> state.getBlock() == block;
    }

    static IBlockMatcher getBlocks(HashSet<Block> blocks) {
        return (world, pos, state) -> blocks.contains(state.getBlock());
    }

    static IBlockMatcher getMaterial(Material material)
    {
        return (world,pos,state) -> state.getMaterial() == material;
    }
}
