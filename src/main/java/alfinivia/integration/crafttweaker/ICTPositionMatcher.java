package alfinivia.integration.crafttweaker;

import alfinivia.util.IPositionMatcher;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.Random;

@ZenClass("mods.alfinivia.IPositionMatcher")
@ZenRegister
public interface ICTPositionMatcher extends IPositionMatcher {
    boolean matches(IWorld world, IBlockPos pos);

    @Override
    default boolean matches(World world, Random random, BlockPos pos) {
        return matches(CraftTweakerMC.getIWorld(world),CraftTweakerMC.getIBlockPos(pos));
    }
}
