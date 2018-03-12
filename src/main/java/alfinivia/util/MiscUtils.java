package alfinivia.util;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MiscUtils {
    public static IBlockState toBlock(IItemStack stack)
    {
        ItemStack item = InputHelper.toStack(stack);
        if(InputHelper.isABlock(stack)) {
            Block block = ((ItemBlock) item.getItem()).getBlock();
            return block.getStateFromMeta(stack.getMetadata());
        }
        return Blocks.AIR.getDefaultState();
    }
}
