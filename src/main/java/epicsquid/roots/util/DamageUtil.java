package epicsquid.roots.util;

import net.minecraft.entity.LivingEntity;

public class DamageUtil {
  public static void unhurt(LivingEntity entity) {
    entity.hurtResistantTime = 0;
    entity.hurtTime = 0;
  }
}
