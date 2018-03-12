package alfinivia.integration.crafttweaker;

import alfinivia.handlers.Attributes;
import alfinivia.handlers.DamageHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(DamageBuilder.clazz)
@ZenRegister
public class DamageBuilder {
    public static final String clazz = "mods.alfinivia.DamageBuilder";

    @ZenMethod
    public static DamageBuilder get(String className)
    {
        return new DamageBuilder(className);
    }

    private String className;
    private IEntityFunction entityMatch;
    private IEntityFunction sourceMatch;
    private String damageSource;
    private IDamageFunction function;
    private IAttribute attribute;

    public DamageBuilder(String className)
    {
        this.className = className;
    }

    @ZenMethod
    public void matchEntity(IEntityFunction entityMatch) {
        this.entityMatch = entityMatch;
    }

    @ZenMethod
    public void matchSource(IEntityFunction sourceMatch) {
        this.sourceMatch = sourceMatch;
    }

    @ZenMethod
    public void matchDamageSource(String damageSource) {
        this.damageSource = damageSource;
    }

    @ZenMethod
    public void setFunction(IDamageFunction function) {
        this.attribute = null;
        this.function = function;
    }

    @ZenMethod
    public void multiplyAttribute(float defaultValue, float minValue, float maxValue) {
        if(attribute != null) {
            CraftTweakerAPI.logError("Tried to register a second attribute for class " + className);
            return;
        }

        attribute = new RangedAttribute(null,"damage."+className,defaultValue,minValue,maxValue);
        function = (entity,amount) -> amount * (float)getAttributeValue(entity,attribute);
    }

    @ZenMethod
    public void addAttribute(float defaultValue, float minValue, float maxValue) {
        if(attribute != null) {
            CraftTweakerAPI.logError("Tried to register a second attribute for class " + className);
            return;
        }

        attribute = new RangedAttribute(null,"damage."+className,defaultValue,minValue,maxValue);
        function = (entity,amount) -> Math.max(0,amount + (float)getAttributeValue(entity,attribute));
    }

    private double getAttributeValue(IEntityLivingBase entity, IAttribute attribute)
    {
        return CraftTweakerMC.getEntityLivingBase(entity).getEntityAttribute(attribute).getAttributeValue();
    }

    private void resetAttribute()
    {
        if(attribute != null)
            CraftTweakerAPI.logError("Removed attribute request for " + className);
        attribute = null;
    }

    @ZenMethod
    public void build()
    {
        CraftTweakerAPI.apply(new Build(this));
    }

    public void buildInternal()
    {
        if(entityMatch == null || damageSource == null || function == null)
            return;
        DamageHandler.DamageInfo info = new DamageHandler.DamageInfo(entityMatch,function,damageSource);
        info.setSourceMatch(sourceMatch);
        if(attribute != null)
        {
            Attributes.request(attribute);
        }

        DamageHandler.REGISTRY.add(info);
    }

    public static class Build implements IAction
    {
        DamageBuilder builder;

        public Build(DamageBuilder builder)
        {
            this.builder = builder;
        }

        @Override
        public void apply() {
            builder.buildInternal();
        }

        @Override
        public String describe() {
            return "adding block breakspeed handler "+builder.className;
        }
    }
}
