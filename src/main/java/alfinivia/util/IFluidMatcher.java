package alfinivia.util;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidMatcher {
    boolean applies(FluidStack fluid);

    static IFluidMatcher getIIngredient(IIngredient ingredient)
    {
        return fluid -> fluid != null && ingredient.matches(InputHelper.toILiquidStack(fluid));
    }
}
