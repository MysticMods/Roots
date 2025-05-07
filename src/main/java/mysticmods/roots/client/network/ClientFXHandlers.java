package mysticmods.roots.client.network;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.layer.WarningLayer;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClientFXHandlers {
  public static void spiral(BlockPos position, double radius, double angle, int color1, int color2) {
    Minecraft minecraft = Minecraft.getInstance();
    minecraft.level.addParticle(new RootsParticleOptions(ModParticles.SPIRAL, color1, color2),
        position.getX() + 0.5f,
        position.getY() + 1.15f,
        position.getZ() + 0.5f, radius, angle, 0);
  }

  public static void animalHarvest(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.random;
    if (entity != null) {
      int col2 = 0xb764c2;
      int col1 = 0xf9a0ee;

      double size = (entity.getBbHeight() + entity.getBbWidth()) / 4;

      for (int i = 0; i < 40; i++) {

        double angle = random.nextDouble() * 2 * Math.PI; // 0 to 360 degrees
        double radius = random.nextDouble() * 0.2 * size * 0.5; // scale outward spread by entity size

        double xSpeed = Math.cos(angle) * radius;
        double zSpeed = Math.sin(angle) * radius;

        double ySpeed = 0.08 + random.nextDouble() * 0.04 + (size * 0.02); // upward bias scaled by size

        // Emit near top of entity, scaled by height
        double yOffset = entity.getY() + entity.getBbHeight() * (0.8 + random.nextDouble() * 0.2);
        minecraft.level.addParticle(
            new RootsParticleOptions(
                ModParticles.ANIMAL_HARVEST,
                col1,
                col2
            ),
            entity.getX(),
            yOffset,
            entity.getZ(),
            xSpeed,
            ySpeed,
            zSpeed
        );
      }
    }
  }

  public static void aquaBubble(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.random;
    if (entity != null) {
      for (int i = 0; i < 4; i++) {
        minecraft.level.addParticle(
            ModParticles.AIR_BUBBLE.value(),
            entity.getX() + (random.nextDouble() - 0.5),
            entity.getY() + (entity.getEyeHeight()) + (random.nextDouble() - 0.5),
            entity.getZ() + (random.nextDouble() - 0.5),
            (random.nextDouble() - 0.5) * 0.25,
            (random.nextDouble() - 0.5) * 0.34,
            (random.nextDouble() - 0.5) * 0.28
        );
      }
    }
  }

  public static void castAquaBubble(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.random;
    if (entity != null) {
      for (int i = 0; i < 31; i++) {
        minecraft.level.addParticle(
            ModParticles.AIR_BUBBLE.value(),
            entity.getX() + (random.nextDouble() - 0.5),
            entity.getY() + (entity.getEyeHeight()) + (random.nextDouble() - 0.5),
            entity.getZ() + (random.nextDouble() - 0.5),
            (random.nextDouble() - 0.5) * 0.25,
            (random.nextDouble() - 0.5) * 0.34,
            (random.nextDouble() - 0.5) * 0.28
        );
      }
    }
  }

  public static void castChannelFail(Spell spell, int casterId, int ticks) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity caster = minecraft.level.getEntity(casterId);
    if (caster != null) {
      int col1 = spell.getColor1();
      int col2 = spell.getColor2();

      InteractionHand hand = InteractionHand.MAIN_HAND;
      if (caster instanceof Player player) {
        hand = player.getUsedItemHand();
      }

      double radius = 0.05 + minecraft.level.random.nextDouble() * 0.05;
      double angle = minecraft.level.random.nextDouble() * (2 * Math.PI);

      Vec3 lookDir = caster.getViewVector(1.0f).normalize();
      Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();
      Vec3 upVec = rightVec.cross(lookDir).normalize();

      double localX = Math.cos(angle) * radius;
      double localY = Math.sin(angle) * radius;
      Vec3 circleOffset = rightVec.scale(localX).add(upVec.scale(localY));

      double handOffset = hand == InteractionHand.MAIN_HAND ? 0.3 : -0.3;

      Vec3 eyePos = caster.getEyePosition(1.0f);
      Vec3 start = eyePos.add(lookDir.scale(0.6)).add(circleOffset).add(rightVec.scale(handOffset));


      minecraft.level.addParticle(
          new RootsParticleOptions(ModParticles.CHANNEL_FAIL, col1, col2, casterId),
          start.x,
          start.y,
          start.z,
          0, 0, 0
      );

      radius = 0.05 + minecraft.level.random.nextDouble() * 0.05;
      angle = minecraft.level.random.nextDouble() * (2 * Math.PI);
      localX = Math.cos(angle) * radius;
      localY = Math.sin(angle) * radius;
      circleOffset = rightVec.scale(localX).add(upVec.scale(localY));
      start = eyePos.add(lookDir.scale(0.6)).add(circleOffset).add(rightVec.scale(handOffset));

      minecraft.level.addParticle(
          new RootsParticleOptions(ModParticles.CHANNEL_FAIL, col2, col1, casterId),
          start.x,
          start.y,
          start.z,
          0, 0, 0
      );
    }
  }

  public static void castChannel(Spell spell, int casterId, Vec3 start, int ticks) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity caster = minecraft.level.getEntity(casterId);
    Player player = minecraft.player;
    if (player != null && caster != null) {
      start = player.getPosition(0f);
      int col1 = spell.getColor1();
      int col2 = spell.getColor2();

      double radius = 0.1 + minecraft.level.random.nextDouble() * 0.15;

      minecraft.level.addParticle(
          new RootsParticleOptions(
              ModParticles.CHANNEL,
              col1,
              col2,
              casterId
          ),
          start.x,
          start.y,
          start.z,
          radius,
          0,
          0
      );
      minecraft.level.addParticle(
          new RootsParticleOptions(
              ModParticles.CHANNEL,
              col2,
              col1,
              casterId
          ),
          start.x,
          start.y,
          start.z,
          radius,
          0,
          0
      );
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
          new RootsParticleOptions(
              ModParticles.CHANNEL_TARGET,
              col1,
              col2
          ),
          start.x,
          start.y,
          start.z,
          stop.x,
          stop.y,
          stop.z
      );
      minecraft.level.addParticle(
          new RootsParticleOptions(
              ModParticles.CHANNEL_TARGET,
              col2,
              col1
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
        double xOffset = minecraft.level.random.nextDouble() - 0.5;
        double zOffset = minecraft.level.random.nextDouble() - 0.5;
        double yOffset = (minecraft.level.random.nextFloat() - 0.5) * 0.1;

        minecraft.level.addParticle(
            new RootsParticleOptions(
                ModParticles.GROWTH,
                0x2ce713,
                0x4d7e20
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
        double xOffset = minecraft.level.random.nextDouble() - 0.5;
        double zOffset = minecraft.level.random.nextDouble() - 0.5;
        double yOffset = (minecraft.level.random.nextFloat() - 0.5) * 0.1;

        minecraft.level.addParticle(
            new RootsParticleOptions(
                ModParticles.GROWTH,
                0xc2d02a,
                0x7fc73c
            ),
            location.getX() + xOffset,
            location.getY() + yOffset,
            location.getZ() + zOffset,
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
