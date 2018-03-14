package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IBulletFired")
@ZenRegister
public interface IBulletFired {
    void apply(IPlayer shooter, IItemStack cartridge, IEntity projectile, boolean charged);
}
