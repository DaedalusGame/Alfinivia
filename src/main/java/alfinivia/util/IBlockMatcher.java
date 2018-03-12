package alfinivia.util;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    static IBlockMatcher getMaterial(Material material)
    {
        return (world,pos,state) -> state.getMaterial() == material;
    }
}
