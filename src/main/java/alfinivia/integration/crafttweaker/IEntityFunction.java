package alfinivia.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.alfinivia.IEntityFunction")
@ZenRegister
public interface IEntityFunction {
    boolean applies(IEntity entity);

    static IEntityFunction getIEntityDefinition(IEntityDefinition definition)
    {
        return entity -> definition.getId().equals(getRegistryName(entity));
    }

    static String getRegistryName(IEntity entity) {
        ResourceLocation location = EntityList.getKey(CraftTweakerMC.getEntity(entity));
        return location == null ? null : location.toString();
    }
}
