package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;

public interface IChemBlockEffect {
    void apply(IWorld world, IBlockPos pos, IFacing facing, IPlayer entityPlayer, IItemStack itemStack, ILiquidStack fluid);
}
