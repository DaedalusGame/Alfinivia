package alfinivia.handlers;

import alfinivia.util.IGeneratorConstructor;
import alfinivia.util.IPositionGetter;
import alfinivia.util.IPositionMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GenerationHandler {
    public static ArrayList<DecorationInfo> REGISTRY = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPrePopulateBiome(PopulateChunkEvent.Pre event){
        decorateChunk(event.getWorld(),event.getRand(),new BlockPos(event.getChunkX(),0,event.getChunkZ()),DecorationPhase.PopulatePre);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPostPopulateBiome(PopulateChunkEvent.Post event){
        decorateChunk(event.getWorld(),event.getRand(),new BlockPos(event.getChunkX(),0,event.getChunkZ()),DecorationPhase.PopulatePost);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreGenerateOres(OreGenEvent.Pre event){
        decorateChunk(event.getWorld(),event.getRand(),event.getPos(),DecorationPhase.OrePre);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreGenerateOres(OreGenEvent.Post event){
        decorateChunk(event.getWorld(),event.getRand(),event.getPos(),DecorationPhase.OrePost);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreDecorateBiome(DecorateBiomeEvent.Pre event){
        decorateChunk(event.getWorld(),event.getRand(),event.getPos(),DecorationPhase.DecoratePre);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPostDecorateBiome(DecorateBiomeEvent.Post event){
        decorateChunk(event.getWorld(),event.getRand(),event.getPos(),DecorationPhase.DecoratePost);
    }

    public void decorateChunk(World world, Random random, BlockPos chunkpos, DecorationPhase phase)
    {
        REGISTRY.stream().filter(info -> info.phase == phase && info.isValid(world, random, chunkpos)).forEach(info -> info.decorate(world, random, chunkpos));
    }

    public static class DecorationInfo
    {
        public DecorationPhase phase;
        public IGeneratorConstructor constructor;
        public int times;
        public IPositionGetter position;
        public IPositionMatcher canGenerateBiome;
        public IPositionMatcher canGenerate;
        public HashMap<String, Object> parameters;

        public boolean isValid(World world, Random random, BlockPos chunkpos)
        {
            return canGenerateBiome.matches(world,random,chunkpos);
        }

        public void decorate(World world, Random random, BlockPos chunkpos)
        {
            WorldGenerator generator = constructor.get(parameters);
            for(int i = 0; i < times; i++) {
                BlockPos pos = position.get(world, random, chunkpos);
                if(pos != null && canGenerate != null && canGenerate.matches(world,random,pos))
                    generator.generate(world, random, pos);
            }
        }
    }

    public enum DecorationPhase
    {
        PopulatePre,
        PopulatePost,
        DecoratePre,
        DecoratePost,
        OrePre,
        OrePost,
    }
}
