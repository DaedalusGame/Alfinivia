package alfinivia.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IPositionGetter {
    BlockPos get(World world, Random random, BlockPos pos);

    static IPositionGetter getRandom(int min, int max) {
        return (world, random, pos) -> new BlockPos(pos.getX() + 8 + random.nextInt(16),min+random.nextInt(max-min),pos.getZ() + 8 + random.nextInt(16));
    }

    static IPositionGetter getTopSurface(int offset) {
        return (world, random, pos) -> getHeight(world,pos.getX() + 8 + random.nextInt(16),pos.getZ() + 8 + random.nextInt(16)).up(offset);
    }

    static IPositionGetter getTop(int min, int max, IBlockMatcher validBlock, int offset) {
        return (world, random, pos) -> getSurface(world,pos.getX() + 8 + random.nextInt(16),pos.getZ() + 8 + random.nextInt(16),validBlock,min,max,true);
    }

    static IPositionGetter getBottom(int min, int max, IBlockMatcher validBlock, int offset) {
        return (world, random, pos) -> getSurface(world,pos.getX() + 8 + random.nextInt(16),pos.getZ() + 8 + random.nextInt(16),validBlock,min,max,false);
    }

    static BlockPos getHeight(World world, int x, int z)
    {
        return new BlockPos(x,world.getHeight(x,z),z);
    }

    static BlockPos getSurface(World world, int x, int z, IBlockMatcher validBlock, int min, int max, boolean up)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x,up?min:max,z);
        BlockPos lastBlock = null;
        for(int i = 0; i < Math.abs(min-max); i++)
        {
            if(world.isAirBlock(pos) && lastBlock != null)
                break;
            if(validBlock.applies(world,pos,world.getBlockState(pos)))
                lastBlock = pos.toImmutable();
            pos.move(up ? EnumFacing.UP : EnumFacing.DOWN);
        }
        return lastBlock;
    }
}
