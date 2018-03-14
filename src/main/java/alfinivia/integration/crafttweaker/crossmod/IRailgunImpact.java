package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import stanhebben.zenscript.annotations.ZenClass;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IRailgunImpact")
@ZenRegister
public interface IRailgunImpact {
    boolean apply(IEntity target, IEntity shooter);
}
