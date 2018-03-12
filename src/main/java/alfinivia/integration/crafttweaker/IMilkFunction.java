package alfinivia.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.alfinivia.IMilkFunction")
@ZenRegister
public interface IMilkFunction {
    IItemStack process(IEntity target, IItemStack item, IPlayer user);
}
