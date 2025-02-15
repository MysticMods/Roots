package mysticmods.roots.client;

import mysticmods.roots.init.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class ClientFX {
  public static void geasEffect(ClientLevel level, Entity e) {
    RandomSource random = e.getRandom();
    double yaw = Math.toRadians(180);
    double x = e.getX() + (0.25f) * Math.sin(yaw);
    double y = e.getY() + e.getEyeHeight() + 0.25f;
    double z = e.getZ() + (0.25f) * Math.cos(yaw);
    level.addParticle(ModParticles.GEAS.value(), x, y, z, 0, 0, 0);
  }
}
