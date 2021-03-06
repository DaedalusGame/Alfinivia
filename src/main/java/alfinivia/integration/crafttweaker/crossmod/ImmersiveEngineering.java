package alfinivia.integration.crafttweaker.crossmod;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import blusunrize.immersiveengineering.api.tool.BelljarHandler.FluidFertilizerHandler;
import blusunrize.immersiveengineering.api.tool.BelljarHandler.ItemFertilizerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotionEffect;
import crafttweaker.mc1120.liquid.MCLiquidStack;
import crafttweaker.mc1120.world.MCFacing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.HashSet;

@ModOnly("immersiveengineering")
@ZenClass(ImmersiveEngineering.clazz)
@ZenRegister
public class ImmersiveEngineering {
    public static final String clazz = "mods.alfinivia.ImmersiveEngineering";

    @ZenMethod
    public static void addChemthrowerEffect(ILiquidStack liquid, boolean isGas, boolean isFlammable, String source, float damage)
    {
        DamageSource damageSource = new DamageSource(source);
        addChemthrowerEffect(liquid,isGas,isFlammable,new ChemthrowerHandler.ChemthrowerEffect_Damage(damageSource,damage));
    }

    @ZenMethod
    public static void addChemthrowerEffect(ILiquidStack liquid, boolean isGas, boolean isFlammable, String source, float damage, IPotionEffect[] effects)
    {
        DamageSource damageSource = source == null ? null : new DamageSource(source);
        PotionEffect[] actualEffects = new PotionEffect[effects.length];
        for(int i = 0; i < effects.length; i++)
            actualEffects[i] = CraftTweakerMC.getPotionEffect(effects[i]);
        addChemthrowerEffect(liquid,isGas,isFlammable,new ChemthrowerHandler.ChemthrowerEffect_Potion(damageSource,damage,actualEffects));
    }

    @ZenMethod
    public static void addChemthrowerEffect(ILiquidStack liquid, boolean isGas, boolean isFlammable, IChemEntityEffect entityEffect, IChemBlockEffect blockEffect)
    {
        addChemthrowerEffect(liquid,isGas,isFlammable,new CustomChemEffect(entityEffect,blockEffect));
    }

    public static void addChemthrowerEffect(ILiquidStack liquid, boolean isGas, boolean isFlammable, ChemthrowerEffect effect)
    {
        Fluid fluid = CraftTweakerMC.getLiquidStack(liquid).getFluid();
        CraftTweakerAPI.apply(new AddChemthrowerEffect(fluid,effect,isGas,isFlammable));
    }

    @ZenMethod
    public static void addRailgunBullet(IIngredient item, float damage, float gravity, int[][] colorMap)
    {
        addRailgunBullet(item,damage,gravity,null,colorMap);
    }

    @ZenMethod
    public static void addRailgunBullet(IIngredient item, float damage, float gravity, IRailgunImpact effect, int[][] colorMap)
    {
        RailgunHandler.RailgunProjectileProperties properties;
        if(effect != null)
            properties = new CustomRailgunProperties(damage,gravity,effect);
        else
            properties = new RailgunHandler.RailgunProjectileProperties(damage, gravity);
        properties.setColourMap(colorMap);
        CraftTweakerAPI.apply(new AddRailgunBullet(ApiUtils.createIngredientStack(CraftTweakerHelper.toObject(item)),properties));
    }

    @ZenMethod
    public static void addLiquidFertilizer(ILiquidStack liquid, float multiplier) {
        addLiquidFertilizer(liquid,IFluidFertilizerMultiplier.byNumber(multiplier));
    }

    @ZenMethod
    public static void addLiquidFertilizer(ILiquidStack liquid, IFluidFertilizerMultiplier multiplier) {
        FluidFertilizerHandler handler = new FluidFertilizerHandler() {
            @Override
            public boolean isValid(@Nullable FluidStack fluidStack) {
                return liquid.matches(new MCLiquidStack(fluidStack));
            }

            @Override
            public float getGrowthMultiplier(FluidStack fertilizer, ItemStack seed, ItemStack soil, TileEntity tileEntity) {
                return multiplier.getMultiplier(new MCLiquidStack(fertilizer),CraftTweakerMC.getIItemStack(seed),CraftTweakerMC.getIItemStack(soil));
            }
        };
        CraftTweakerAPI.apply(new AddFluidFertilizer(handler));
    }

    @ZenMethod
    public static void removeLiquidFertilizer(ILiquidStack liquid) {
        CraftTweakerAPI.apply(new RemoveFluidFertilizer(InputHelper.toFluid(liquid)));
    }

    @ZenMethod
    public static void addItemFertilizer(IIngredient item, float multiplier) {
        addItemFertilizer(item,IItemFertilizerMultiplier.byNumber(multiplier));
    }

    @ZenMethod
    public static void addItemFertilizer(IIngredient item, IItemFertilizerMultiplier multiplier) {
        ItemFertilizerHandler handler = new ItemFertilizerHandler() {
            @Override
            public boolean isValid(ItemStack itemStack) {
                return item.matches(CraftTweakerMC.getIItemStack(itemStack));
            }

            @Override
            public float getGrowthMultiplier(ItemStack fertilizer, ItemStack seed, ItemStack soil, TileEntity tileEntity) {
                return multiplier.getMultiplier(CraftTweakerMC.getIItemStack(fertilizer),CraftTweakerMC.getIItemStack(seed),CraftTweakerMC.getIItemStack(soil));
            }
        };
        CraftTweakerAPI.apply(new AddItemFertilizer(handler));
    }

    @ZenMethod
    public static void removeItemFertilizer(IItemStack item) {
        CraftTweakerAPI.apply(new RemoveItemFertilizer(InputHelper.toStack(item)));
    }

    public static class AddChemthrowerEffect implements IAction
    {
        Fluid fluid;
        ChemthrowerEffect effect;
        boolean isGas;
        boolean isFlammable;

        public AddChemthrowerEffect(Fluid fluid, ChemthrowerEffect effect, boolean isGas, boolean isFlammable) {
            this.fluid = fluid;
            this.effect = effect;
            this.isGas = isGas;
            this.isFlammable = isFlammable;
        }

        @Override
        public void apply() {
            ChemthrowerHandler.registerEffect(fluid,effect);
            if(isGas && !fluid.isGaseous())
                ChemthrowerHandler.registerGas(fluid);
            if(isFlammable)
                ChemthrowerHandler.registerFlammable(fluid);
        }

        @Override
        public String describe() {
            return "adding chemthrower effect for "+fluid.getName();
        }
    }

    public static class AddRailgunBullet implements IAction
    {
        IngredientStack stack;
        RailgunHandler.RailgunProjectileProperties properties;

        public AddRailgunBullet(IngredientStack stack, RailgunHandler.RailgunProjectileProperties properties) {
            this.stack = stack;
            this.properties = properties;
        }

        @Override
        public void apply() {
            RailgunHandler.projectilePropertyMap.add(Pair.of(stack, properties));
        }

        @Override
        public String describe() {
            return "adding railgun bullet";
        }
    }

    public static class AddFluidFertilizer implements IAction
    {
        FluidFertilizerHandler handler;

        public AddFluidFertilizer(FluidFertilizerHandler handler) {
            this.handler = handler;
        }

        @Override
        public void apply() {
            BelljarHandler.registerFluidFertilizer(handler);
        }

        @Override
        public String describe() {
            return "Adding fluid fertilizer";
        }
    }

    public static class RemoveFluidFertilizer implements IAction
    {
        FluidStack fluid;

        public RemoveFluidFertilizer(FluidStack fluid) {
            this.fluid = fluid;
        }

        @Override
        public void apply() {
            HashSet<FluidFertilizerHandler> set = ReflectionHelper.getPrivateValue(BelljarHandler.class,null,"fluidFertilizers");
            set.removeIf(handler -> handler.isValid(fluid));
        }

        @Override
        public String describe() {
            return "Removing all fluid fertilizers that match "+fluid.toString();
        }
    }

    public static class AddItemFertilizer implements IAction
    {
        ItemFertilizerHandler handler;

        public AddItemFertilizer(ItemFertilizerHandler handler) {
            this.handler = handler;
        }

        @Override
        public void apply() {
            BelljarHandler.registerItemFertilizer(handler);
        }

        @Override
        public String describe() {
            return "Adding item fertilizer";
        }
    }

    public static class RemoveItemFertilizer implements IAction
    {
        ItemStack stack;

        public RemoveItemFertilizer(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            HashSet<ItemFertilizerHandler> set = ReflectionHelper.getPrivateValue(BelljarHandler.class,null,"itemFertilizers");
            set.removeIf(handler -> handler.isValid(stack));
        }

        @Override
        public String describe() {
            return "Removing all item fertilizers that match "+stack.getDisplayName();
        }
    }

    public static class CustomChemEffect extends ChemthrowerEffect
    {
        IChemEntityEffect entityEffect;
        IChemBlockEffect blockEffect;

        public CustomChemEffect(IChemEntityEffect entityEffect, IChemBlockEffect blockEffect) {
            this.entityEffect = entityEffect;
            this.blockEffect = blockEffect;
        }

        @Override
        public void applyToEntity(EntityLivingBase entityLivingBase, @Nullable EntityPlayer entityPlayer, ItemStack itemStack, Fluid fluid) {
            applyToEntity(entityLivingBase, entityPlayer, itemStack, new FluidStack(fluid,1));
        }

        @Override
        public void applyToEntity(EntityLivingBase entityLivingBase, @Nullable EntityPlayer entityPlayer, ItemStack itemStack, FluidStack fluid) {
            entityEffect.apply(CraftTweakerMC.getIEntityLivingBase(entityLivingBase),CraftTweakerMC.getIPlayer(entityPlayer),CraftTweakerMC.getIItemStack(itemStack),new MCLiquidStack(fluid));
        }

        @Override
        public void applyToBlock(World world, RayTraceResult rayTraceResult, @Nullable EntityPlayer entityPlayer, ItemStack itemStack, Fluid fluid) {
            applyToBlock(world,rayTraceResult,entityPlayer,itemStack,new FluidStack(fluid,1));
        }

        @Override
        public void applyToBlock(World world, RayTraceResult rayTraceResult, @Nullable EntityPlayer entityPlayer, ItemStack itemStack, FluidStack fluid) {
            BlockPos pos = rayTraceResult.getBlockPos();
            EnumFacing facing = rayTraceResult.sideHit;

            blockEffect.apply(CraftTweakerMC.getIWorld(world),CraftTweakerMC.getIBlockPos(pos),new MCFacing(facing),CraftTweakerMC.getIPlayer(entityPlayer),CraftTweakerMC.getIItemStack(itemStack),new MCLiquidStack(fluid));
        }
    }

    public static class CustomRailgunProperties extends RailgunHandler.RailgunProjectileProperties {
        IRailgunImpact impact;

        public CustomRailgunProperties(double damage, double gravity, IRailgunImpact impact) {
            super(damage, gravity);
            this.impact = impact;
        }

        @Override
        public boolean overrideHitEntity(Entity entityHit, Entity shooter) {
            return impact.apply(CraftTweakerMC.getIEntity(entityHit),CraftTweakerMC.getIEntity(shooter));
        }
    }
}
