package alfinivia.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityAnimal;
import crafttweaker.api.entity.IEntityCreature;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashSet;

@ZenClass("mods.alfinivia.IEntityFunction")
@ZenRegister
public interface IEntityFunction {
    boolean applies(IEntity entity);

    @ZenMethod
    static IEntityFunction getEntity(IEntityDefinition definition)
    {
        return entity -> definition.getId().equals(getRegistryName(entity));
    }

    @ZenMethod
    static IEntityFunction getEntities(IEntityDefinition[] definitions)
    {
        HashSet<String> ids = new HashSet<>();
        for(IEntityDefinition definition : definitions)
            ids.add(definition.getId());
        return entity -> ids.contains(getRegistryName(entity));
    }

    @ZenMethod
    static IEntityFunction getAnimals() {
        return entity -> entity instanceof IEntityAnimal;
    }

    @ZenMethod
    static IEntityFunction getCreatures() {
        return entity -> entity instanceof IEntityCreature;
    }

    @ZenMethod
    static IEntityFunction getPlayers() {
        return entity -> entity instanceof IPlayer;
    }

    @ZenMethod
    static IEntityFunction getBosses() {
        return IEntity::isBoss;
    }

    static String getRegistryName(IEntity entity) {
        ResourceLocation location = EntityList.getKey(CraftTweakerMC.getEntity(entity));
        return location == null ? null : location.toString();
    }
}
