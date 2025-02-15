package mysticmods.roots.client;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class ClientFX {
  public static void geasEffect(ClientLevel level, Entity e) {
    RandomSource random = e.getRandom();
    if (random.nextInt(5) == 0) {
      double x = e.getX() + random.nextFloat() - 0.5f;
      double y = e.getY() + e.getEyeHeight() + 0.25f + random.nextFloat() * 0.1f;
      double z = e.getZ() + random.nextFloat() - 0.5f;
      level.addParticle(new ColorGravityParticleOptions(
          ModParticles.GEAS,
          0xa74fff,
          0xff4fb0,
          0f
          ), x, y, z, 0, 0, 0);
    }
  }
}
