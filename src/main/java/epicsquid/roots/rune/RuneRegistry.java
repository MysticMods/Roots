package epicsquid.roots.rune;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class RuneRegistry {
  public static Map<String, Class<? extends RuneBase>> runeRegistry = new HashMap<>();

  public static void init() {
    runeRegistry.put("fleetness_rune", FleetnessRune.class);
  }

  public static RuneBase getRune(NBTTagCompound compound) {
    String runeString = compound.getString("rune");
    RuneBase rune = getRune(runeString);

    if (rune != null) {
      rune.readFromEntity(compound);
    }

    return rune;
  }

  public static RuneBase getRune(String runeString) {
    RuneBase rune = null;
    if (runeRegistry.get(runeString) != null) {
      try {
        rune = runeRegistry.get(runeString).getDeclaredConstructor().newInstance();
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
    }

    return rune;
  }

}
