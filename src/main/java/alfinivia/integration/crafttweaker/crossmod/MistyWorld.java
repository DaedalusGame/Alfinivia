package alfinivia.integration.crafttweaker.crossmod;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockDefinition;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ru.liahim.mist.api.registry.IMistHarvest;
import ru.liahim.mist.api.registry.MistRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("mist")
@ZenClass(MistyWorld.clazz)
@ZenRegister
public class MistyWorld {
    public static final String clazz = "mods.alfinivia.MistyWorld";

    @ZenMethod
    public static void addCompostable(IItemStack stack)
    {
        CraftTweakerAPI.apply(new AddCompostable(InputHelper.toStack(stack)));
    }

    @ZenMethod
    public static void addHarvestType(IBlockDefinition block, int min, int max)
    {
        addHarvestType((Block)block.getInternal(),min,max);
    }

    @ZenMethod
    public static void addHarvestType(String block, int min, int max)
    {
        addHarvestType(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block)),min,max);
    }

    public static void addHarvestType(Block block, int min, int max)
    {
        if(max > min) {
            min = min % max;
            max = min % max;
            min = min % max;
        }
        IMistHarvest.HarvestType type = getHarvestType(min,max);
        if(type != null)
            CraftTweakerAPI.apply(new AddHarvestType(block,type));
        else
            CraftTweakerAPI.logError("Tried to use a harvest type out of bounds (1-3)");
    }

    private static IMistHarvest.HarvestType getHarvestType(int min, int max)
    {
        String s = min + "" + max;

        switch (s)
        {
            case("11"):
                return IMistHarvest.HarvestType.WP1_1;
            case("12"):
                return IMistHarvest.HarvestType.WP1_2;
            case("13"):
                return IMistHarvest.HarvestType.WP1_3;
            case("22"):
                return IMistHarvest.HarvestType.WP2_2;
            case("23"):
                return IMistHarvest.HarvestType.WP2_3;
            case("33"):
                return IMistHarvest.HarvestType.WP3_3;
            default:
                return null;
        }
    }

    public static class AddCompostable implements IAction
    {
        ItemStack stack;

        public AddCompostable(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            MistRegistry.registerCompostIngredient(stack);
        }

        @Override
        public String describe() {
            return "adding misty world compostable item "+stack.getDisplayName();
        }
    }

    public static class AddHarvestType implements IAction
    {
        Block block;
        IMistHarvest.HarvestType type;

        public AddHarvestType(Block block, IMistHarvest.HarvestType type) {
            this.block = block;
            this.type = type;
        }

        @Override
        public void apply() {
            MistRegistry.registerHarvestType(block,type);
        }

        @Override
        public String describe() {
            return "adding harvest type for block "+block.getRegistryName().toString()+" (min:"+type.getMinWaterPerm()+" max:"+type.getMaxWaterPerm()+")";
        }
    }
}
