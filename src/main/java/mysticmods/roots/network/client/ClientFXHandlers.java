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
}
