package alfinivia.handlers;

import alfinivia.integration.crafttweaker.IEntityFunction;
import alfinivia.integration.crafttweaker.IMilkFunction;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class MilkingHandler {
    public static ArrayList<MilkingInfo> REGISTRY = new ArrayList<>();

    @SubscribeEvent
    public void onMilk(PlayerInteractEvent.EntityInteractSpecific event)
    {
        Entity target = event.getTarget();
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        if(event.getWorld().isRemote)
            return;

        for (MilkingInfo info : REGISTRY) {
            if(info.matches(player,target,stack))
            {
                stack.shrink(1);
                ItemStack result = info.getResult(player,target,stack);
                result = result.copy();
                if (stack.isEmpty())
                    player.setHeldItem(event.getHand(), result);
                else if (!player.inventory.addItemStackToInventory(result))
                    player.dropItem(result, false);
                event.setCancellationResult(EnumActionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }
        }
    }

    public static class MilkingInfo
    {
        public MilkingInfo(Ingredient input, IEntityFunction entityMatch, ItemStack output, boolean sneak)
        {
            inputItem = input;
            entity = entityMatch;
            outputItem = output;
            requiresSneaking = sneak;
        }

        public MilkingInfo(Ingredient input, IEntityFunction entityMatch, IMilkFunction output, boolean sneak)
        {
            this(input,entityMatch,ItemStack.EMPTY,sneak);
            function = output;
        }

        public Ingredient inputItem;
        private IEntityFunction entity;
        public ItemStack outputItem = ItemStack.EMPTY;
        public IMilkFunction function;
        public boolean requiresSneaking;

        public boolean matches(EntityPlayer player, Entity target, ItemStack item)
        {
            boolean flag = !requiresSneaking || player.isSneaking();
            return flag && inputItem.apply(item) && entity.applies(CraftTweakerMC.getIEntity(target));
        }

        public ItemStack getResult(EntityPlayer player, Entity target, ItemStack item)
        {
            ItemStack result = outputItem;
            if(function != null)
                result = CraftTweakerMC.getItemStack(function.process(CraftTweakerMC.getIEntity(target),CraftTweakerMC.getIItemStack(item),CraftTweakerMC.getIPlayer(player)));
            return result;
        }
    }
}
