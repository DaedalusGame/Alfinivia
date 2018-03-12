package alfinivia.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.alfinivia.IBlockSpeedFunction")
@ZenRegister
public interface IBlockSpeedFunction {
    float getMultiplier(IPlayer player, IWorld world, IBlockPos pos);
}
