package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IItemFertilizerMultiplier")
@ZenRegister
public interface IItemFertilizerMultiplier {
    float getMultiplier(IItemStack fertilizer, IItemStack seed, IItemStack soil);

    @ZenMethod
    static IItemFertilizerMultiplier byNumber(float multiplier)
    {
        return (fertilizer, seed, soil) -> multiplier;
    }
}
