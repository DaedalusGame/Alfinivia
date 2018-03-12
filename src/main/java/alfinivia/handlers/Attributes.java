package alfinivia.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class Attributes {
    public static HashMap<String,IAttribute> REQUESTED_ATTRIBUTES = new HashMap<>();

    public static IAttribute request(IAttribute attribute)
    {
        String name = attribute.getName();
        if(REQUESTED_ATTRIBUTES.containsKey(name))
            return REQUESTED_ATTRIBUTES.get(name);
        REQUESTED_ATTRIBUTES.put(name,attribute);
        return attribute;
    }

    @SubscribeEvent
    public void onEntityConstructEvent(EntityEvent.EntityConstructing event)
    {
        Entity entity = event.getEntity();
        if(entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            for(IAttribute attribute : REQUESTED_ATTRIBUTES.values())
            {
                living.getAttributeMap().registerAttribute(attribute);
            }
        }
    }
}
