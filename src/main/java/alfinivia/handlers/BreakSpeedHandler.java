package alfinivia.handlers;

import alfinivia.integration.crafttweaker.IBlockSpeedFunction;
import alfinivia.util.IBlockMatcher;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class BreakSpeedHandler {
    public static ArrayList<BreakSpeedInfo> REGISTRY = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreak(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = player.getHeldItemMainhand();

        for (BreakSpeedInfo info : REGISTRY) {
            IBlockMatcher matcher = info.blockMatchers.get(state.getBlock());
            if(matcher == null || !matcher.applies(world,pos,state))
                continue;
            if(info.toolFilter != null && !info.toolFilter.apply(stack))
                continue;
            event.setNewSpeed(info.getMultiplier(player,world,pos) * event.getNewSpeed());
        }
    }

    public static class BreakSpeedInfo
    {
        private HashMap<Block, IBlockMatcher> blockMatchers;

        public void setBlockMatchers(HashMap<Block, IBlockMatcher> blockMatchers) {
            this.blockMatchers = blockMatchers;
        }

        public void setToolFilter(Ingredient toolFilter) {
            this.toolFilter = toolFilter;
        }

        public void setAttribute(IAttribute attribute) {
            this.attribute = attribute;
        }

        public void setMultiplier(float multiplier) {
            this.multiplier = multiplier;
        }

        public void setFunction(IBlockSpeedFunction function) {
            this.function = function;
        }

        private Ingredient toolFilter;
        private IAttribute attribute;
        private float multiplier;
        private IBlockSpeedFunction function;

        public float getMultiplier(EntityPlayer user, World world, BlockPos pos)
        {
            float result = multiplier;
            if(attribute != null)
                result *= user.getEntityAttribute(attribute).getAttributeValue();
            if(function != null)
                result *= function.getMultiplier(CraftTweakerMC.getIPlayer(user),CraftTweakerMC.getIWorld(world),CraftTweakerMC.getIBlockPos(pos));
            return result;
        }
    }
}
