package mysticmods.roots.network.client;

import mysticmods.roots.client.ClientFX;
import mysticmods.roots.client.ParticleUtil;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class ClientFXHandlers {
  public static void geas(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    if (entity != null) {
      ParticleUtil.addTrackingEmitter(entity, ModParticles.GEAS.value(),  15, ClientFX::geasEffect);
    }
  }
}
