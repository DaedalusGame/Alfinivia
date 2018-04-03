package alfinivia.integration.crafttweaker.crossmod;


import alfinivia.integration.crafttweaker.DecorationBuilder;
import twilightforest.biomes.TFGenHugeWaterLily;
import twilightforest.enums.FireJetVariant;
import twilightforest.world.*;

public class TwilightForest {
    public static void initialize()
    {
        DecorationBuilder.registerGenerator("bigMushgloom", (params) -> new TFGenBigMushgloom());
        DecorationBuilder.registerGenerator("canopyMushroom", (params) -> new TFGenCanopyMushroom());
        DecorationBuilder.registerGenerator("canopyOak", (params) -> new TFGenCanopyOak());
        DecorationBuilder.registerGenerator("canopyTree", (params) -> new TFGenCanopyTree());
        DecorationBuilder.registerGenerator("canopyDark", (params) -> new TFGenDarkCanopyTree());
        DecorationBuilder.registerGenerator("hollowLog", (params) -> new TFGenFallenHollowLog());
        DecorationBuilder.registerGenerator("smallLog", (params) -> new TFGenFallenSmallLog());
        DecorationBuilder.registerGenerator("fireJet", (params) -> new TFGenFireJet(FireJetVariant.JET_IDLE));
        DecorationBuilder.registerGenerator("smokeJet", (params) -> new TFGenFireJet(FireJetVariant.SMOKER));
        DecorationBuilder.registerGenerator("foundation", (params) -> new TFGenFoundation());
        DecorationBuilder.registerGenerator("groveRuins", (params) -> new TFGenGroveRuins());
        DecorationBuilder.registerGenerator("hangingLamp", (params) -> new TFGenHangingLamps());
        DecorationBuilder.registerGenerator("hollowStump", (params) -> new TFGenHollowStump());
        DecorationBuilder.registerGenerator("hollowTree", (params) -> new TFGenHollowTree());
        DecorationBuilder.registerGenerator("hugeLilyPad", (params) -> new TFGenHugeLilyPad());
        DecorationBuilder.registerGenerator("hugeWaterLily", (params) -> new TFGenHugeWaterLily());
        DecorationBuilder.registerGenerator("lamppost", (params) -> new TFGenLampposts());
        DecorationBuilder.registerGenerator("largeRainbowOak", (params) -> new TFGenLargeRainboak());
        DecorationBuilder.registerGenerator("largeWinter", (params) -> new TFGenLargeWinter());
        DecorationBuilder.registerGenerator("mangroveTree", (params) -> new TFGenMangroveTree());
        DecorationBuilder.registerGenerator("minersTree", (params) -> new TFGenMinersTree());
        DecorationBuilder.registerGenerator("monolith", (params) -> new TFGenMonolith());
        DecorationBuilder.registerGenerator("stalagmite", (params) -> new TFGenOutsideStalagmite());
        DecorationBuilder.registerGenerator("plantRoots", (params) -> new TFGenPlantRoots());
        DecorationBuilder.registerGenerator("rainbowOak", (params) -> new TFGenSmallRainboak());
        DecorationBuilder.registerGenerator("sortingTree", (params) -> new TFGenSortingTree());
        DecorationBuilder.registerGenerator("stoneCircle", (params) -> new TFGenStoneCircle());
        DecorationBuilder.registerGenerator("thorns", (params) -> new TFGenThorns());
        DecorationBuilder.registerGenerator("torchBerries", (params) -> new TFGenTorchBerries());
        DecorationBuilder.registerGenerator("timeTree", (params) -> new TFGenTreeOfTime(false));
        DecorationBuilder.registerGenerator("transformationTree", (params) -> new TFGenTreeOfTransformation());
        DecorationBuilder.registerGenerator("trollRoots", (params) -> new TFGenTrollRoots());
        DecorationBuilder.registerGenerator("woodRoots", (params) -> new TFGenWoodRoots());
    }
}
