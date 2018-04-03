package alfinivia.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

public class CraftTweaker {
    @ZenExpansion("crafttweaker.item.IIngredient")
    @ZenRegister
    public static class IngredientExtensions {
        @ZenMethod
        public static IIngredient onlyNoNBT(IIngredient ingredient)
        {
            return ingredient.only(iItemStack -> !iItemStack.hasTag());
        }

        @ZenMethod
        public static IIngredient onlyEnergyAtLeast(IIngredient ingredient, int energy)
        {
            return ingredient.only(iItemStack -> getEnergy(CraftTweakerMC.getItemStack(iItemStack)) >= energy);
        }

        @ZenMethod
        public static IIngredient onlyEnergyAtMost(IIngredient ingredient, int energy)
        {
            return ingredient.only(iItemStack -> getEnergy(CraftTweakerMC.getItemStack(iItemStack)) <= energy);
        }

        @ZenMethod
        public static IIngredient onlyEnergyBetween(IIngredient ingredient, int min, int max)
        {
            return ingredient.only(iItemStack -> isBetween(getEnergy(CraftTweakerMC.getItemStack(iItemStack)),min,max));
        }

        @ZenMethod
        public static IIngredient onlyFluid(IIngredient ingredient, ILiquidStack fluid)
        {
            return ingredient.only(iItemStack -> fluid.matches(iItemStack.getLiquid()));
        }

        @ZenMethod
        public static IIngredient onlyTool(IIngredient ingredient, String toolclass)
        {
            return ingredient.only(iItemStack -> hasToolClass(CraftTweakerMC.getItemStack(iItemStack),toolclass));
        }

        @ZenMethod
        public static IIngredient transformConsumeFluid(IIngredient ingredient, int consumed)
        {
            return ingredient.transform((iItemStack, iPlayer) -> {
                ItemStack stack = (ItemStack) iItemStack.getInternal();
                IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
                if(handler != null) {
                    handler.drain(consumed,true);
                    return CraftTweakerMC.getIItemStack(handler.getContainer());
                }
                return iItemStack;
            });
        }

        @ZenMethod
        public static IIngredient transformFillFluid(IIngredient ingredient, ILiquidStack fill)
        {
            return ingredient.transform((iItemStack, iPlayer) -> {
                ItemStack stack = (ItemStack) iItemStack.getInternal();
                IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
                if(handler != null) {
                    handler.fill(CraftTweakerMC.getLiquidStack(fill),true);
                    return CraftTweakerMC.getIItemStack(handler.getContainer());
                }
                return iItemStack;
            });
        }

        @ZenMethod
        public static IIngredient transformEnergy(IIngredient ingredient, int given)
        {
            return ingredient.transform((iItemStack, iPlayer) -> {
                ItemStack stack = (ItemStack) iItemStack.getInternal();
                IEnergyStorage energyStorage = getEnergyStorage(stack);
                if(energyStorage != null) {
                    if(given > 0)
                        energyStorage.receiveEnergy(given,false);
                    else
                        energyStorage.extractEnergy(-given,false);
                }
                return iItemStack;
            });
        }

        private static boolean hasToolClass(ItemStack stack, String toolclass)
        {
            return stack.getItem().getToolClasses(stack).contains(toolclass);
        }

        private static IEnergyStorage getEnergyStorage(ItemStack stack)
        {
            Item item = stack.getItem();
            if(item instanceof IEnergyStorage)
                return (IEnergyStorage) item;
            if(stack.hasCapability(CapabilityEnergy.ENERGY,null))
                return stack.getCapability(CapabilityEnergy.ENERGY,null);
            return null;
        }

        private static int getEnergy(ItemStack stack)
        {
            IEnergyStorage energyStorage = getEnergyStorage(stack);
            if(energyStorage != null) {
                return energyStorage.getEnergyStored();
            }
            return 0;
        }

        private static boolean isBetween(int n, int min, int max)
        {
            return n >= min && n <= max;
        }

        private static boolean isBetween(float n, float min, float max)
        {
            return n >= min && n <= max;
        }

        private static boolean isBetween(double n, double min, double max)
        {
            return n >= min && n <= max;
        }
    }
}
