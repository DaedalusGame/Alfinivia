package alfinivia.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class Attributes {
    public static ArrayList<IAttribute> REQUESTED_ATTRIBUTES = new ArrayList<>();

    @SubscribeEvent
    public void onEntityConstructEvent(EntityEvent.EntityConstructing event)
    {
        Entity entity = event.getEntity();
        if(entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            for(IAttribute attribute : REQUESTED_ATTRIBUTES)
            {
                living.getAttributeMap().registerAttribute(attribute);
            }
        }
    }
}
