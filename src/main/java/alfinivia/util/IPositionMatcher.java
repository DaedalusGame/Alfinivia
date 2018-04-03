package alfinivia.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public interface IPositionMatcher {
    boolean matches(World world, Random random, BlockPos pos);

    static IPositionMatcher and(IPositionMatcher a, IPositionMatcher b) {
        return (world, random, pos) -> a.matches(world,random,pos) && b.matches(world,random,pos);
    }

    static IPositionMatcher inBiome(Biome biome) {
        return (world, random, chunkpos) -> world.getBiome(chunkpos) == biome;
    }

    static IPositionMatcher inBiomeChunk(Biome biome) {
        return (world, random, chunkpos) -> world.getBiome(chunkpos) == biome
                        || world.getBiome(chunkpos.add(0,0,15)) == biome
                        || world.getBiome(chunkpos.add(15,0,0)) == biome
                        || world.getBiome(chunkpos.add(15,0,15)) == biome;
    }

    static IPositionMatcher inTemperature(float min, float max) {
        return (world, random, pos) -> {
            float temperature = world.getBiome(pos).getTemperature(pos);
            return temperature > min && temperature < max;
        };
    }

    static IPositionMatcher inHumidity(float min, float max) {
        return (world, random, pos) -> {
            float temperature = world.getBiome(pos).getRainfall();
            return temperature > min && temperature < max;
        };
    }

    static IPositionMatcher atElevation(int min, int max) {
        return (world, random, pos) -> pos.getY() > min && pos.getY() < max;
    }

    static IPositionMatcher inBlock(IBlockMatcher blockMatcher) {
        return (world, random, pos) -> blockMatcher.applies(world,pos,world.getBlockState(pos));
    }

    static IPositionMatcher overBlock(IBlockMatcher blockMatcher) {
        return (world, random, pos) -> blockMatcher.applies(world,pos,world.getBlockState(pos.down()));
    }

    static IPositionMatcher underBlock(IBlockMatcher blockMatcher) {
        return (world, random, pos) -> blockMatcher.applies(world,pos,world.getBlockState(pos.up()));
    }
}
