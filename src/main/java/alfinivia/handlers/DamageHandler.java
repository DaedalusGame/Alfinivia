package alfinivia.handlers;

import alfinivia.integration.crafttweaker.IDamageFunction;
import alfinivia.integration.crafttweaker.IEntityFunction;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashSet;

public class DamageHandler {
    public static ArrayList<DamageInfo> REGISTRY = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHurt(LivingHurtEvent event) {
        float amount = getModifiedDamage(event.getEntityLiving(), event.getSource(), event.getAmount());
        event.setAmount(amount);
        if(amount == 0)
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onAttack(LivingAttackEvent event)
    {
        if(!event.isCanceled() && getModifiedDamage(event.getEntityLiving(), event.getSource(), event.getAmount()) == 0)
            event.setCanceled(true);
    }

    public float getModifiedDamage(EntityLivingBase entity, DamageSource source, float amount) {
        for (DamageInfo info : REGISTRY) {
            if(info.matches(entity, source))
                amount = info.function.getDamage(CraftTweakerMC.getIEntityLivingBase(entity), amount);
        }
        return amount;
    }

    public static class DamageInfo {
        IEntityFunction entityMatch;
        IEntityFunction sourceMatch;
        IDamageFunction function;
        HashSet<String> damageType;

        public DamageInfo(IEntityFunction entityMatch, IDamageFunction function, HashSet<String> damageType) {
            this.entityMatch = entityMatch;
            this.function = function;
            this.damageType = damageType;
        }

        public void setSourceMatch(IEntityFunction sourceMatch)
        {
            this.sourceMatch = sourceMatch;
        }

        public boolean matches(EntityLivingBase entity, DamageSource source)
        {
            if(!damageType.contains(source.getDamageType()))
                return false;
            if(!entityMatch.applies(CraftTweakerMC.getIEntity(entity)))
                return false;
            if(sourceMatch != null && !sourceMatch.applies(CraftTweakerMC.getIEntity(source.getImmediateSource()))&& !sourceMatch.applies(CraftTweakerMC.getIEntity(source.getTrueSource())))
                return false;
            return true;
        }
    }
}
