package epicsquid.roots.util;

import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class OreCondition {
    public static Map<String, Boolean> ORES_EXISTING = new HashMap<>();

    public static boolean oreExists(String oreName) {
        if (!ORES_EXISTING.containsKey(oreName)) {
            ORES_EXISTING.put(oreName, OreDictionary.getOres(oreName, false).size() != 0);
        }

        return ORES_EXISTING.get(oreName);
    }
}
