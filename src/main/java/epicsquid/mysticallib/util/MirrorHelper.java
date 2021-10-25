package epicsquid.mysticallib.util;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MirrorHelper {
  public static boolean setFinalValue(Class<?> clazz, String srgName, Object instance, Object newValue) {
    Field f = ObfuscationReflectionHelper.findField(clazz, srgName);
    f.setAccessible(true);
    Field modifiers = ObfuscationReflectionHelper.findField(Field.class, "modifiers");
    modifiers.setAccessible(true);
    try {
      modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
      f.set(instance, newValue);
      return true;
    } catch (IllegalAccessException e) {
      return false;
    }
  }

  public static boolean setStaticFinalValue(Class<?> clazz, String srgName, Object newValue) {
    return setFinalValue(clazz, srgName, clazz, newValue);
  }
}
