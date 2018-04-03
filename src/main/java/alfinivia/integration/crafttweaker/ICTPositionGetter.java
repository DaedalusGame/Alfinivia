package alfinivia.integration.crafttweaker;

import alfinivia.util.IPositionGetter;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.Random;

@ZenClass("mods.alfinivia.IPositionGetter")
@ZenRegister
public interface ICTPositionGetter extends IPositionGetter {
    IBlockPos get(IWorld world, IBlockPos pos);

    @Override
    default BlockPos get(World world, Random random, BlockPos pos)
    {
        return CraftTweakerMC.getBlockPos(get(CraftTweakerMC.getIWorld(world),CraftTweakerMC.getIBlockPos(pos)));
    }
}
