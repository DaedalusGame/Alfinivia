package alfinivia.util;

import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.HashMap;

public interface IGeneratorConstructor {
    WorldGenerator get(HashMap<String,Object> parameters);
}
