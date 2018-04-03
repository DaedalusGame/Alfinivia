package alfinivia.integration.crafttweaker;

import alfinivia.handlers.GenerationHandler;
import alfinivia.handlers.GenerationHandler.DecorationInfo;
import alfinivia.handlers.GenerationHandler.DecorationPhase;
import alfinivia.util.IBlockMatcher;
import alfinivia.util.IGeneratorConstructor;
import alfinivia.util.IPositionGetter;
import alfinivia.util.IPositionMatcher;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBiome;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.HashSet;

@ZenRegister
@ZenClass(DecorationBuilder.clazz)
public class DecorationBuilder {
    public static final String clazz = "mods.alfinivia.DecorationBuilder";
    static HashMap<String, IGeneratorConstructor> generatorTypes = new HashMap<>();

    public static void registerGenerator(String id, IGeneratorConstructor constructor)
    {
        generatorTypes.put(id,constructor);
    }

    String type;
    int times = 1;
    DecorationPhase phase;
    IPositionGetter position;
    IPositionMatcher chunkMatcher;
    IPositionMatcher positionMatcher;

    public DecorationBuilder(String type) {
        this.type = type;
    }

    @ZenMethod
    public static DecorationBuilder get(String type)
    {
        return new DecorationBuilder(type);
    }

    @ZenMethod
    public void times(int n) {
        times = n;
    }

    @ZenMethod
    public void prePopulateStage() {
        phase = DecorationPhase.PopulatePre;
    }

    @ZenMethod
    public void postPopulateStage() {
        phase = DecorationPhase.PopulatePost;
    }

    @ZenMethod
    public void preOregenStage() {
        phase = DecorationPhase.OrePre;
    }

    @ZenMethod
    public void postOregenStage() {
        phase = DecorationPhase.OrePost;
    }

    @ZenMethod
    public void preDecorateStage() {
        phase = DecorationPhase.DecoratePre;
    }

    @ZenMethod
    public void postDecorateStage() {
        phase = DecorationPhase.DecoratePost;
    }

    @ZenMethod
    public void atSurface(int offset)
    {
        position = IPositionGetter.getTopSurface(offset);
    }

    @ZenMethod
    public void atRandom(int min, int max)
    {
        position = IPositionGetter.getRandom(min,max);
    }

    @ZenMethod
    public void overBlock(int minHeight, int maxHeight, String[] blocks, int offset)
    {
        position = IPositionGetter.getTop(minHeight, maxHeight, IBlockMatcher.getBlocks(getBlocks(blocks)), offset);
    }

    @ZenMethod
    public void underBlock(int minHeight, int maxHeight, String[] blocks, int offset)
    {
        position = IPositionGetter.getBottom(minHeight, maxHeight, IBlockMatcher.getBlocks(getBlocks(blocks)), offset);
    }

    @ZenMethod
    public void ifInBiome(String biome) {
        Biome internalBiome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biome));
        setOrAddChunkMatcher(IPositionMatcher.inBiomeChunk(internalBiome));
        setOrAddPositionMatcher(IPositionMatcher.inBiome(internalBiome));
    }

    @ZenMethod
    public void ifInTemperature(float min, float max) {
        IPositionMatcher matcher = IPositionMatcher.inTemperature(min,max);
        setOrAddChunkMatcher(matcher);
        setOrAddPositionMatcher(matcher);
    }

    @ZenMethod
    public void ifInHumidity(float min, float max) {
        IPositionMatcher matcher = IPositionMatcher.inHumidity(min,max);
        setOrAddChunkMatcher(matcher);
        setOrAddPositionMatcher(matcher);
    }

    @ZenMethod
    public void ifAtElevation(int min, int max) {
        IPositionMatcher matcher = IPositionMatcher.atElevation(min,max);
        setOrAddPositionMatcher(matcher);
    }

    @ZenMethod
    public void ifOnBlock(String[] blocks) {
        setOrAddPositionMatcher(IPositionMatcher.overBlock(IBlockMatcher.getBlocks(getBlocks(blocks))));
    }

    @ZenMethod
    public void ifInBlock(String[] blocks) {
        setOrAddPositionMatcher(IPositionMatcher.inBlock(IBlockMatcher.getBlocks(getBlocks(blocks))));
    }

    @ZenMethod
    public void ifUnderBlock(String[] blocks) {
        setOrAddPositionMatcher(IPositionMatcher.underBlock(IBlockMatcher.getBlocks(getBlocks(blocks))));
    }

    @ZenMethod
    public void ifOnMaterial(IMaterial material) {
        setOrAddPositionMatcher(IPositionMatcher.overBlock(IBlockMatcher.getMaterial(CraftTweakerMC.getMaterial(material))));
    }

    @ZenMethod
    public void ifInMaterial(IMaterial material) {
        setOrAddPositionMatcher(IPositionMatcher.inBlock(IBlockMatcher.getMaterial(CraftTweakerMC.getMaterial(material))));
    }

    @ZenMethod
    public void ifUnderMaterial(IMaterial material) {
        setOrAddPositionMatcher(IPositionMatcher.underBlock(IBlockMatcher.getMaterial(CraftTweakerMC.getMaterial(material))));
    }

    private HashSet<Block> getBlocks(String[] blocks) {
        HashSet<Block> internalBlocks = new HashSet<>();
        for (String resname: blocks) {
            internalBlocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(resname)));
        }
        return internalBlocks;
    }

    private void setOrAddChunkMatcher(IPositionMatcher matcher) {
        chunkMatcher = chunkMatcher != null ? IPositionMatcher.and(chunkMatcher,matcher) : matcher;
    }

    private void setOrAddPositionMatcher(IPositionMatcher matcher) {
        positionMatcher = positionMatcher != null ? IPositionMatcher.and(positionMatcher,matcher) : matcher;
    }

    @ZenMethod
    public void build()
    {
        CraftTweakerAPI.apply(new Build(this));
    }

    public void buildInternal()
    {
        if(!generatorTypes.containsKey(type) || phase == null || times == 0 || chunkMatcher == null || position == null)
            return;
        DecorationInfo info = new DecorationInfo();
        info.constructor = generatorTypes.get(type);
        info.times = times;
        info.phase = phase;
        info.canGenerateBiome = chunkMatcher;
        info.position = position;
        info.canGenerate = positionMatcher;

        GenerationHandler.REGISTRY.add(info);
    }

    public static class Build implements IAction
    {
        DecorationBuilder builder;

        public Build(DecorationBuilder builder)
        {
            this.builder = builder;
        }

        @Override
        public void apply() {
            builder.buildInternal();
        }

        @Override
        public String describe() {
            return "adding decoration handler for "+builder.type;
        }
    }
}
