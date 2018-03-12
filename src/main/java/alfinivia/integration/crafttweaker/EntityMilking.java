package alfinivia.integration.crafttweaker;

import alfinivia.handlers.MilkingHandler;
import alfinivia.util.IngredientCraftTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(EntityMilking.clazz)
public class EntityMilking {
    public static final String clazz = "mods.alfinivia.Milking";

    @ZenMethod
    public static void add(@NotNull IIngredient input, @NotNull IEntityDefinition entity, IItemStack output, boolean needsSneaking) {
        CraftTweakerAPI.apply(new Add(new MilkingHandler.MilkingInfo(new IngredientCraftTweaker(input),IEntityFunction.getEntity(entity), InputHelper.toStack(output),needsSneaking),entity.getId()));
    }

    @ZenMethod
    public static void add(@NotNull IIngredient input, @NotNull IEntityFunction entity, IItemStack output, boolean needsSneaking) {
        CraftTweakerAPI.apply(new Add(new MilkingHandler.MilkingInfo(new IngredientCraftTweaker(input), entity, InputHelper.toStack(output),needsSneaking),"custom entity match"));
    }

    @ZenMethod
    public static void add(@NotNull IIngredient input, @NotNull IEntityDefinition entity, IMilkFunction output, boolean needsSneaking) {
        CraftTweakerAPI.apply(new Add(new MilkingHandler.MilkingInfo(new IngredientCraftTweaker(input), IEntityFunction.getEntity(entity), output,needsSneaking),entity.getId()));
    }

    @ZenMethod
    public static void add(@NotNull IIngredient input, @NotNull IEntityFunction entity, IMilkFunction output, boolean needsSneaking) {
        CraftTweakerAPI.apply(new Add(new MilkingHandler.MilkingInfo(new IngredientCraftTweaker(input), entity, output,needsSneaking),"custom entity match"));
    }

    public static class Add extends BaseListAddition<MilkingHandler.MilkingInfo> {
        String entity;

        protected Add(MilkingHandler.MilkingInfo recipe, String entity) {
            super("Milking",MilkingHandler.REGISTRY, Lists.newArrayList(recipe));
            this.entity = entity;
        }

        @Override
        protected String getRecipeInfo(MilkingHandler.MilkingInfo recipe) {
            return recipe.inputItem.toString()+" on "+entity+" => "+(recipe.function != null ? "custom output" : recipe.outputItem.toString())+(recipe.requiresSneaking?" (sneaking)":"");
        }
    }
}
