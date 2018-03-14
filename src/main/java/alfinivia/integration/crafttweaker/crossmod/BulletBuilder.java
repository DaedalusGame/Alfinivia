package alfinivia.integration.crafttweaker.crossmod;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.entities.EntityRevolvershot;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IWorld;
import crafttweaker.mc1120.world.MCFacing;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

@ModOnly("immersiveengineering")
@ZenClass(BulletBuilder.clazz)
@ZenRegister
public class BulletBuilder {
    public static final String clazz = "mods.alfinivia.BulletBuilder";

    @ZenMethod
    public static BulletBuilder get(String className)
    {
        return new BulletBuilder(className);
    }

    private String className;
    private ResourceLocation[] textures;
    private ItemStack casing = ItemStack.EMPTY;
    private IBulletEntityImpact entityImpact;
    private IBulletBlockImpact blockImpact;
    private IBulletFired fired;
    private boolean isProperCartridge = true;
    private boolean isValidForTurret = false;
    private int bulletAmount;
    private float gravity = 0;
    private float movementDecay = 0;
    private int tickLimit = 40;
    private IItemColor color = ((stack, tintIndex) -> 0xFFFFFFFF);

    public BulletBuilder(String className)
    {
        this.className = className;
    }

    @ZenMethod
    public void setTextures(String[] textures)
    {
        this.textures = new ResourceLocation[textures.length];
        for(int i = 0; i < textures.length; i++)
            this.textures[i] = new ResourceLocation(textures[i]);
    }

    @ZenMethod
    public void setCasing(IItemStack casing)
    {
        this.casing = InputHelper.toStack(casing);
    }

    @ZenMethod
    public void impactEntity(IBulletEntityImpact impact)
    {
        this.entityImpact = impact;
    }

    @ZenMethod
    public void impactBlock(IBulletBlockImpact impact)
    {
        this.blockImpact = impact;
    }

    @ZenMethod
    public void onFired(IBulletFired fired) {
        this.fired = fired;
    }

    @ZenMethod
    public void setTickLimit(int tickLimit) {
        this.tickLimit = tickLimit;
    }

    @ZenMethod
    public void setMovementDecay(float movementDecay) {
        this.movementDecay = movementDecay;
    }

    @ZenMethod
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    @ZenMethod
    public void setBulletAmount(int bulletAmount) {
        this.bulletAmount = bulletAmount;
    }

    @ZenMethod
    public void setValidForTurret(boolean validForTurret) {
        isValidForTurret = validForTurret;
    }

    @ZenMethod
    public void setProperCartridge(boolean properCartridge) {
        isProperCartridge = properCartridge;
    }

    @ZenMethod
    public void setColor(int layer, int color)
    {
        this.color = (stack, tintIndex) -> layer == tintIndex ? color : BulletBuilder.this.color.colorMultiplier(stack,tintIndex); //Not proud of this at all but syke
    }

    @ZenMethod
    public void setColorNBT(int layer, String tag)
    {
        this.color = (stack, tintIndex) -> layer == tintIndex ? getColorFromNBT(stack,tag) : BulletBuilder.this.color.colorMultiplier(stack,tintIndex); //Not proud of this at all but syke
    }

    private int getColorFromNBT(ItemStack stack, String tag) {
        NBTTagCompound compound = stack.getTagCompound();

        if(compound == null || !compound.hasKey(tag))
            return 0xFFFFFFFF;
        else
            return compound.getInteger(tag);
    }

    @ZenMethod
    public void build()
    {
        CraftTweakerAPI.apply(new Build(this));
    }

    public void buildInternal()
    {
        if(textures == null || className == null)
            return;

        CustomBullet bullet = new CustomBullet(textures, casing);
        bullet.entityImpact = entityImpact;
        bullet.blockImpact = blockImpact;
        bullet.fired = fired;
        bullet.color = color;
        bullet.isProperCartridge = isProperCartridge;
        bullet.isValidForTurret = isValidForTurret;
        bullet.projectileCount = bulletAmount;
        bullet.gravity = gravity;
        bullet.movementDecay = movementDecay;
        bullet.tickLimit = tickLimit;

        BulletHandler.registerBullet(className,bullet);
    }

    public static class Build implements IAction
    {
        BulletBuilder builder;

        public Build(BulletBuilder builder)
        {
            this.builder = builder;
        }

        @Override
        public void apply() {
            builder.buildInternal();
        }

        @Override
        public String describe() {
            return "adding IE bullet "+builder.className;
        }
    }

    public static class CustomBullet implements BulletHandler.IBullet
    {
        ResourceLocation[] textures;
        boolean isProperCartridge = true;
        boolean isValidForTurret = false;
        int projectileCount;
        ItemStack casing;
        IItemColor color;
        IBulletEntityImpact entityImpact;
        IBulletBlockImpact blockImpact;
        IBulletFired fired;
        float gravity = 0;
        float movementDecay = 0;
        int tickLimit = 40;

        public CustomBullet(ResourceLocation[] textures, ItemStack casing) {
            this.textures = textures;
            this.casing = casing;
        }

        @Override
        public boolean isProperCartridge() {
            return isProperCartridge;
        }

        @Override
        public int getProjectileCount(@Nullable EntityPlayer shooter) {
            return projectileCount;
        }

        @Override
        public Entity getProjectile(@Nullable EntityPlayer shooter, ItemStack cartridge, Entity projectile, boolean charged) {
            if(projectile instanceof EntityRevolvershot)
            {
                EntityRevolvershot shot = (EntityRevolvershot) projectile;
                shot.setGravity(gravity);
                shot.setMovementDecay(movementDecay);
                shot.setTickLimit(tickLimit);
            }

            if(fired != null)
                fired.apply(CraftTweakerMC.getIPlayer(shooter),CraftTweakerMC.getIItemStack(cartridge),CraftTweakerMC.getIEntity(projectile),charged);

            return projectile;
        }

        @Override
        public void onHitTarget(World world, RayTraceResult rayTraceResult, @Nullable EntityLivingBase shooter, Entity bullet, boolean headshot) {
            if(rayTraceResult.entityHit != null && entityImpact != null)
                entityImpact.apply(CraftTweakerMC.getIWorld(world),CraftTweakerMC.getIEntity(rayTraceResult.entityHit),CraftTweakerMC.getIEntityLivingBase(shooter),CraftTweakerMC.getIEntity(bullet),headshot);
            else if(rayTraceResult.entityHit == null && blockImpact != null)
                blockImpact.apply(CraftTweakerMC.getIWorld(world),CraftTweakerMC.getIBlockPos(rayTraceResult.getBlockPos()),new MCFacing(rayTraceResult.sideHit),CraftTweakerMC.getIEntityLivingBase(shooter),CraftTweakerMC.getIEntity(bullet),headshot);
        }

        @Override
        public ItemStack getCasing(ItemStack itemStack) {
            return casing;
        }

        @Override
        public ResourceLocation[] getTextures() {
            return textures;
        }

        @Override
        public int getColour(ItemStack itemStack, int i) {
            return color == null ? -1 : color.colorMultiplier(itemStack, i);
        }

        @Override
        public boolean isValidForTurret() {
            return isValidForTurret;
        }
    }
}