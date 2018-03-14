package alfinivia.integration.crafttweaker.crossmod;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("immersiveengineering")
@ZenClass("mods.alfinivia.IBulletEntityImpact")
@ZenRegister
public interface IBulletEntityImpact {
    void apply(IWorld world, IEntity target, IEntityLivingBase shooter, IEntity bullet, boolean headshot);

    @ZenMethod
    static IBulletEntityImpact damage(String damageType, float damage, int fire, boolean pierce, boolean resetHurt, float headshotMultiplier) {
        DamageSource source = new DamageSource(damageType);
        if(pierce)
            source = source.setDamageBypassesArmor();
        final DamageSource finalSource = source;
        return (world, target, shooter, bullet, headshot) -> {
            Entity entity = CraftTweakerMC.getEntity(target);
            if(!world.isRemote() && target != null && entity.attackEntityFrom(finalSource,(headshot?headshotMultiplier:0) * damage))
            {
                if(resetHurt)
                    entity.hurtResistantTime = 0;
                if(fire > 0)
                    entity.setFire(fire);
            }
        };
    }
}
