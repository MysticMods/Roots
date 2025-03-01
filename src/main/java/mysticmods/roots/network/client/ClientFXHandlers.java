package mysticmods.roots.network.client;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.ClientFX;
import mysticmods.roots.client.ParticleUtil;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClientFXHandlers {
  public static void geas(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    if (entity != null) {
      ParticleUtil.addTrackingEmitter(entity, 15, ClientFX::geasEffect);
    }
  }

  public static void castChannel(Spell spell, int casterId, Vec3 start, Vec3 stop, int ticks) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity caster = minecraft.level.getEntity(casterId);
    Player player = minecraft.player;
    if (player != null && caster != null) {
      int col1 = spell.getColor1();
      int col2 = spell.getColor2();

      minecraft.level.addParticle(
          new ColorGravityParticleOptions(
              ModParticles.CHANNEL,
              col1,
              col2,
              0f
          ),
          start.x,
          start.y,
          start.z,
          stop.x,
          stop.y,
          stop.z
      );
      minecraft.level.addParticle(
          new ColorGravityParticleOptions(
              ModParticles.CHANNEL,
              col2,
              col1,
              0f
          ),
          start.x,
          start.y,
          start.z,
          stop.x,
          stop.y,
          stop.z
      );
    }
  }

  public static void growth(Vec3 location) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null) {
      for (int i = 0; i < 2; i++) {
        double progress = minecraft.level.random.nextDouble();
        double angle = progress * Math.PI * 4;
        double radius = progress * 0.5;

        double xOffset = radius * Math.cos(angle);
        double zOffset = radius * Math.sin(angle);
        double yOffset = 0.3 + (minecraft.level.random.nextFloat() - 0.5) * 0.1;

        minecraft.level.addParticle(
            new ColorGravityParticleOptions(
                ModParticles.GROWTH,
                0x248542,
                0f
            ),
            location.x + xOffset,
            location.y + yOffset,
            location.z + zOffset,
            0,
            0.05,
            0
        );
      }
    }
  }
}
