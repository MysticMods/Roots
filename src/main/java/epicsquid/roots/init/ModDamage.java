package epicsquid.roots.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.fml.common.Loader;

public class ModDamage {
  public static DamageSource RADIANT_DAMAGE = (new DamageSource("holy_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource radiantDamageFrom(EntityPlayer player) {
    DamageSource source = new EntityDamageSource("holy_damage", player).setDamageBypassesArmor().setMagicDamage();
    if (Loader.isModLoaded("consecration")) {
      source.setFireDamage();
    }
    return source;
  }

  public static void init() {
  }
}
