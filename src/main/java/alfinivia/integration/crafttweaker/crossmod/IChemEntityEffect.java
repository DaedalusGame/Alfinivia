package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.player.IPlayer;

public interface IChemEntityEffect {
    void apply(IEntityLivingBase target, IPlayer shooter, IItemStack thrower, ILiquidStack fluid);
}
