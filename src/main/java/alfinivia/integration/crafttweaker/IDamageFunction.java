package alfinivia.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import net.minecraft.util.math.MathHelper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.alfinivia.IDamageFunction")
@ZenRegister
public interface IDamageFunction {
    float getDamage(IEntityLivingBase entity, float amount);

    @ZenMethod
    static IDamageFunction multiply(float multiplier) {
        return (entity,amount) -> amount * multiplier;
    }

    @ZenMethod
    static IDamageFunction add(float add) {
        return (entity,amount) -> Math.max(0,amount + add);
    }

    @ZenMethod
    static IDamageFunction clamp(float lowerbound, float upperbound) {
        return (entity,amount) -> MathHelper.clamp(amount,lowerbound,upperbound);
    }

    @ZenMethod
    static IDamageFunction threshold(float lowerbound)
    {
        return (entity,amount) -> amount >= lowerbound ? amount : 0;
    }
}
