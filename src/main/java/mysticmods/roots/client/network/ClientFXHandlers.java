package mysticmods.roots.client.network;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.layer.WarningLayer;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClientFXHandlers {
  public static void castChannel(Spell spell, int casterId, Vec3 start, Vec3 stop, int ticks) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity caster = minecraft.level.getEntity(casterId);
    Player player = minecraft.player;
    if (player != null && caster != null) {
      int col1 = spell.getColor1();
      int col2 = spell.getColor2();

      minecraft.level.addParticle(
          new ColorGravityParticleOptions(
              ModParticles.CHANNEL_TARGET,
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
              ModParticles.CHANNEL_TARGET,
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

  public static void rampantGrowth(BlockPos location) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null) {
      for (int i = 0; i < 2; i++) {
        double progress = minecraft.level.random.nextDouble();
        double angle = progress * Math.PI * 4;
        double radius = progress * 0.5;

        double xOffset = radius * Math.cos(angle);
        double zOffset = radius * Math.sin(angle);
        double yOffset = (minecraft.level.random.nextFloat() - 0.5) * 0.1;

        minecraft.level.addParticle(
            new ColorGravityParticleOptions(
                ModParticles.GROWTH,
                0x2ce713,
                0x4d7e20,
                0f
            ),
            location.getX() + 0.5 + xOffset,
            location.getY() + yOffset,
            location.getZ() + 0.5 + zOffset,
            0,
            0.05,
            0
        );
      }
    }
  }

  public static void growth(BlockPos location) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null) {
      for (int i = 0; i < 1; i++) {
        double progress = minecraft.level.random.nextDouble();
        double angle = progress * Math.PI * 4;
        double radius = progress * 0.5;

        double xOffset = radius * Math.cos(angle);
        double zOffset = radius * Math.sin(angle);
        double yOffset = (minecraft.level.random.nextFloat() - 0.5) * 0.1;

        minecraft.level.addParticle(
            new ColorGravityParticleOptions(
                ModParticles.GROWTH,
                0xc2d02a,
                0x7fc73c,
                0f
            ),
            location.getX() + 0.5 + xOffset,
            location.getY() + yOffset,
            location.getZ() + 0.5 + zOffset,
            0,
            0.05,
            0
        );
      }
    }
  }

  public static void alert(int entityId) {
    if (ConfigManager.ALERTNESS_VISUAL.getAsBoolean()) {
      WarningLayer.warningTicks = ConfigManager.ALERTNESS_DURATION.getAsInt();
    }

    if (ConfigManager.ALERTNESS_SOUND.getAsBoolean()) {
      Minecraft minecraft = Minecraft.getInstance();
      Entity entity = minecraft.level.getEntity(entityId);
      if (entity != null) {
        minecraft.level.playLocalSound(entity, ModSounds.ALERTNESS.get(), SoundSource.NEUTRAL, 1f, 1f);
      }
    }
  }
}
