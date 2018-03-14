package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IFluidFertilizerMultiplier")
@ZenRegister
public interface IFluidFertilizerMultiplier {
    float getMultiplier(ILiquidStack fertilizer, IItemStack seed, IItemStack soil);

    @ZenMethod
    static IFluidFertilizerMultiplier byNumber(float multiplier)
    {
        return (fertilizer, seed, soil) -> multiplier;
    }

    @ZenMethod
    static IFluidFertilizerMultiplier byPotionLevel(IPotion potion, float multiplier)
    {
        return (fertilizer, seed, soil) -> {
            float result = 1.0f;
            FluidStack stack = CraftTweakerMC.getLiquidStack(fertilizer);
            List<PotionEffect> list = PotionUtils.getEffectsFromTag(stack.tag);
            for (PotionEffect effect : list) {
                if(effect.getPotion() == CraftTweakerMC.getPotion(potion))
                    result *= (effect.getAmplifier()+1) * multiplier;
            }
            return result;
        };
    }
}
