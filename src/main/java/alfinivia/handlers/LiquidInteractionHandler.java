package alfinivia.handlers;

import alfinivia.util.IFluidMatcher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class LiquidInteractionHandler {
    public static ArrayList<LiquidInteraction> REGISTRY = new ArrayList<>();

    @SubscribeEvent
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        if (world.isRemote)
            return;

        FluidStack fluidA = getFluidAtLocation(world, pos);

        if(fluidA != null)
        for (LiquidInteraction interaction : REGISTRY) {
            if (!interaction.fluidA.applies(fluidA))
                break;
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos posNeighbor = pos.offset(facing);
                FluidStack fluidB = getFluidAtLocation(world, posNeighbor);
                if (fluidB != null && interaction.fluidB.applies(fluidB)) {
                    world.setBlockState(posNeighbor, interaction.block, 2);
                }
            }
        }
    }

    private FluidStack getFluidAtLocation(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IFluidBlock) {
            IFluidBlock fluidBlock = (IFluidBlock) block;
            FluidStack fluid = fluidBlock.drain(world, pos, false);
            if(fluid == null)
                fluid = new FluidStack(fluidBlock.getFluid(),1); //Match for fluidstack with size 2 for sourceblock only!
            return fluid;
        }
        if (state.getMaterial() == Material.WATER)
            return new FluidStack(FluidRegistry.WATER,1000);
        if (state.getMaterial() == Material.LAVA)
            return new FluidStack(FluidRegistry.LAVA,1000);
        return null;
    }

    public static class LiquidInteraction {
        public LiquidInteraction(IFluidMatcher fluidA, IFluidMatcher fluidB, IBlockState block) {
            this.fluidA = fluidA;
            this.fluidB = fluidB;
            this.block = block;
        }

        IFluidMatcher fluidA;
        IFluidMatcher fluidB;
        IBlockState block;
    }
}
