package alfinivia.integration.crafttweaker;

import alfinivia.handlers.LiquidInteractionHandler;
import alfinivia.util.IFluidMatcher;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LiquidInteraction.clazz)
public class LiquidInteraction {
    public static final String clazz = "mods.alfinivia.LiquidInteraction";

    @ZenMethod
    public static void add(@NotNull IIngredient fluidA, @NotNull IIngredient fluidB, IItemStack output) {
        ItemStack stack = InputHelper.toStack(output);
        IFluidMatcher inputA = IFluidMatcher.getIIngredient(fluidA);
        IFluidMatcher inputB = IFluidMatcher.getIIngredient(fluidB);
        if(InputHelper.isABlock(stack)) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            CraftTweakerAPI.apply(new Add(new LiquidInteractionHandler.LiquidInteraction(inputA,inputB,block.getStateFromMeta(stack.getMetadata())),fluidA,fluidB));
        }
    }

    public static class Add extends BaseListAddition<LiquidInteractionHandler.LiquidInteraction>
    {
        IIngredient fluidA;
        IIngredient fluidB;

        public Add(LiquidInteractionHandler.LiquidInteraction recipe, IIngredient fluidA, IIngredient fluidB)
        {
            super("LiquidInteraction", LiquidInteractionHandler.REGISTRY, Lists.newArrayList(recipe));
            this.fluidA = fluidA;
            this.fluidB = fluidB;
        }

        @Override
        protected String getRecipeInfo(LiquidInteractionHandler.LiquidInteraction recipe) {
            return fluidA.toString()+" + "+fluidB.toString();
        }
    }
}
