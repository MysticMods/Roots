package epicsquid.roots.util;

import net.minecraft.entity.EntityLivingBase;

public class DamageUtil {
  public static void unhurt(EntityLivingBase entity) {
    entity.hurtResistantTime = 0;
    entity.hurtTime = 0;
  }
}
