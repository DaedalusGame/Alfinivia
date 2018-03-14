package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IBulletBlockImpact")
@ZenRegister
public interface IBulletBlockImpact {
    void apply(IWorld world, IBlockPos pos, IFacing sidehit, IEntityLivingBase shooter, IEntity bullet, boolean headshot);
}
