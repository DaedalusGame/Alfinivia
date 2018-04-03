package alfinivia.integration.crafttweaker.crossmod;

import alfinivia.integration.crafttweaker.DecorationBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class Vanilla {
    public static void initialize() {
        DecorationBuilder.registerGenerator("bigMushroom", (params) -> new WorldGenBigMushroom());
        DecorationBuilder.registerGenerator("bigRedMushroom", (params) -> new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK));
        DecorationBuilder.registerGenerator("bigBrownMushroom", (params) -> new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK));
        DecorationBuilder.registerGenerator("fossil", (params) -> new WorldGenFossils());
        DecorationBuilder.registerGenerator("iceSpike",(params) -> new  WorldGenIceSpike());
    }
}
