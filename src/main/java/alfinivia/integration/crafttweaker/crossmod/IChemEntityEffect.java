package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IChemEntityEffect")
@ZenRegister
public interface IChemEntityEffect {
    void apply(IEntityLivingBase target, IPlayer shooter, IItemStack thrower, ILiquidStack fluid);
}
