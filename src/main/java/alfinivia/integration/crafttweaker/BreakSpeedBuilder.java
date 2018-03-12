package alfinivia.integration.crafttweaker;

import alfinivia.handlers.Attributes;
import alfinivia.handlers.BreakSpeedHandler;
import alfinivia.util.IBlockMatcher;
import alfinivia.util.IngredientCraftTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockDefinition;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;

@ZenClass(BreakSpeedBuilder.clazz)
@ZenRegister
public class BreakSpeedBuilder {
    public static final String clazz = "mods.alfinivia.BreakSpeedBuilder";

    @ZenMethod
    public static BreakSpeedBuilder get(String className)
    {
        return new BreakSpeedBuilder(className);
    }

    private String className;
    private HashMap<Block, IBlockMatcher> blockMatchers = new HashMap<>();
    private IIngredient tools;
    private IAttribute attribute;
    private float multiplier = 1.0f;
    private IBlockSpeedFunction function;

    public BreakSpeedBuilder(String className)
    {
        this.className = className;
    }

    @ZenMethod
    public void setTool(IIngredient tools)
    {
        if(this.tools != null)
            CraftTweakerAPI.logWarning("Replacing set of tools for class "+className+" with "+tools.toString());
        this.tools = tools;
    }

    @ZenMethod
    public void addTool(IIngredient tools)
    {
        if(this.tools == null)
            setTool(tools);
        else
            this.tools = this.tools.or(tools);
    }

    @ZenMethod
    public void setAttribute(float defaultValue, float minValue, float maxValue)
    {
        if(attribute != null) {
            CraftTweakerAPI.logError("Tried to register a second attribute for class " + className);
            return;
        }

        attribute = new RangedAttribute(null,"break."+className,defaultValue,minValue,maxValue).setShouldWatch(true);
    }

    @ZenMethod
    public void setMultiplier(float multiplier)
    {
        this.multiplier = multiplier;
    }

    @ZenMethod
    public void addBlock(IItemStack block)
    {
        ItemStack stack = InputHelper.toStack(block);
        int metadata = stack.getMetadata();
        if(InputHelper.isABlock(stack))
            addBlockInternal(((ItemBlock)stack.getItem()).getBlock(),metadata);
    }

    @ZenMethod
    public void addBlock(IBlockDefinition block, int meta)
    {
        addBlockInternal((Block)block.getInternal(),meta);
    }

    public void addBlockInternal(Block block, int meta)
    {
        if(meta == OreDictionary.WILDCARD_VALUE)
            blockMatchers.put(block,IBlockMatcher.getAnyMeta());
        else
            blockMatchers.put(block,IBlockMatcher.getMeta(meta));
    }

    @ZenMethod
    public void addBlocks(IItemStack[] blocks)
    {
        for (IItemStack block : blocks) {
            addBlock(block);
        }
    }

    @ZenMethod
    public void setFunction(IBlockSpeedFunction function)
    {
        if(function != null)
            CraftTweakerAPI.logWarning("Replacing blockspeed function for class "+className);
        this.function = function;
    }

    @ZenMethod
    public void build()
    {
        CraftTweakerAPI.apply(new Build(this));
    }

    public void buildInternal()
    {
        if(blockMatchers == null)
            return;
        if(attribute == null && function == null && multiplier == 1.0f)
            return;
        BreakSpeedHandler.BreakSpeedInfo info = new BreakSpeedHandler.BreakSpeedInfo();

        info.setBlockMatchers(blockMatchers);
        if(tools != null)
            info.setToolFilter(new IngredientCraftTweaker(tools));
        info.setMultiplier(multiplier);
        info.setFunction(function);
        if(attribute != null) {
            info.setAttribute(Attributes.request(attribute));
        }

        BreakSpeedHandler.REGISTRY.add(info);
    }

    public static class Build implements IAction
    {
        BreakSpeedBuilder builder;

        public Build(BreakSpeedBuilder builder)
        {
            this.builder = builder;
        }

        @Override
        public void apply() {
            builder.buildInternal();
        }

        @Override
        public String describe() {
            return "adding block breakspeed handler "+builder.className;
        }
    }
}
