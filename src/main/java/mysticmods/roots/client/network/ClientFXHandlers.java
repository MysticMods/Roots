package mysticmods.roots.client.network;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.gui.layer.WarningLayer;
import mysticmods.roots.client.particle.Beam;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ClientFXHandlers {
  public static void castChannelJaunt(Spell spell, int casterId, int ticks) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity caster = minecraft.level.getEntity(casterId);
    if (caster != null) {
      int col1 = spell.getColor1();
      int col2 = spell.getColor2();

      InteractionHand hand = InteractionHand.MAIN_HAND;
      if (caster instanceof Player player) {
        hand = player.getUsedItemHand();
      }

      double handOffset = hand == InteractionHand.MAIN_HAND ? 0.3 : -0.3;

      minecraft.level.addParticle(
          new RootsParticleOptions(ModParticles.CHANNEL_JAUNT, col1, col2, casterId),
          caster.getX(),
          caster.getY(),
          caster.getZ(),
          1, 0, handOffset
      );
      minecraft.level.addParticle(
          new RootsParticleOptions(ModParticles.CHANNEL_JAUNT, col2, col1, casterId),
          caster.getX(),
          caster.getY(),
          caster.getZ(),
          -1, 0, handOffset
      );
    }
  }

  public static void petalShell(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);

    int color1 = ModSpells.PETAL_SHELL.get().getColor1();
    int color2 = ModSpells.PETAL_SHELL.get().getColor2();

    if (entity == null) {
      return;
    }

    minecraft.level.addParticle(new RootsParticleOptions(ModParticles.PETAL_SHELL, color1, color2, entityId), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
/*    SnapshotHelper.applyLiving(entity, ModSerializers.PETAL_SHELL.get(), (e, snapshot) -> {
      if (!(e instanceof LivingEntity living)) {
        return;
      }

      MobEffectInstance effect = living.getEffect(ModEffects.PETAL_SHELL);
      if (effect == null) {
        return;
      }

      int count = effect.getAmplifier() + 1;
      int max = snapshot.getCount();

      double radius = 0.8f;
      double height = 1.0f;
      double anglePerShell = Math.PI * 2 / count;
      double angleOffset = Math.toRadians(e.tickCount % 360);

      for (int i = 0; i <= max; i++) {
        double x = e.getX() + radius * Math.sin(angleOffset + i * anglePerShell);
        double y = e.getY() + height;
        double z = e.getZ() + radius * Math.cos(angleOffset + i * anglePerShell);
        minecraft.level.addParticle(new RootsParticleOptions(ModParticles.PETAL_SHELL, color1, color2), x, y, z, 0, 0, 0);
        count--;
        if (count <= 0) {
          break;
        }
      }
    });*/
  }

  public static void castMagnetism(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.getRandom();

    int color1 = ModSpells.MAGNETISM.get().getColor1();
    int color2 = ModSpells.MAGNETISM.get().getColor2();

    RootsParticleOptions opt1 = new RootsParticleOptions(ModParticles.MAGNETISM, color2, color1, entityId);
    RootsParticleOptions opt2 = new RootsParticleOptions(ModParticles.MAGNETISM, color1, color2, entityId);

    if (entity != null) {
      double x = entity.getX();
      double y = entity.getY();
      double z = entity.getZ();

      for (double angle = 0; angle < 360; angle += 1 + random.nextDouble() * 2) {
        double radius = 3.2 + random.nextDouble() * 0.3;
        double yOffset = (random.nextDouble() - 0.5) * 0.2;

/*        double offsetX = Math.cos(angle) * radius;
        double offsetZ = Math.sin(angle) * radius;
        double offsetY = (random.nextDouble() - 0.5) * 0.2;

        Vec3 spawnPos = new Vec3(x + offsetX, y + offsetY, z + offsetZ);
        Vec3 motion = new Vec3(x - spawnPos.x, y - spawnPos.y, z - spawnPos.z).normalize()
            .scale(0.21 + random.nextDouble() * 0.09);*/


        minecraft.level.addParticle(random.nextBoolean() ? opt1 : opt2, x, y, z, radius, angle, yOffset);
      }
    }
  }

  public static void castShatter(int entityId, List<BlockPos> positions) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.getRandom();
    int color1 = ModSpells.SHATTER.get().getColor1();
    int color2 = ModSpells.SHATTER.get().getColor2();

    if (entity == null) {
      return;
    }

    Vec3 start = entity.getEyePosition();

    RootsParticleOptions first = new RootsParticleOptions(ModParticles.SHATTER_BEAM, color1, color2);
    RootsParticleOptions second = new RootsParticleOptions(ModParticles.SHATTER_BEAM, color2, color1);

    for (BlockPos pos : positions) {
      Vec3 stop = Vec3.atCenterOf(pos);
      Vec3 diff = stop.subtract(start).normalize();

      double dist = diff.length();
      Vec3 dir = diff.normalize();

      double step = 0.2;
      int steps = Math.max(1, Mth.floor(dist / step));

      for (int j = 0; j <= steps; j++) {
        double frac = j / (double) steps;
        Vec3 base = start.add(dir.scale(frac * dist));
        double jitter = 0.05;
        Vec3 spawnPos = base.add(
            (random.nextDouble() - 0.5) * jitter,
            (random.nextDouble() - 0.5) * jitter,
            (random.nextDouble() - 0.5) * jitter
        );

        minecraft.level.addParticle(random.nextBoolean() ? first : second, spawnPos.x, spawnPos.y, spawnPos.z, 0.05 * j, 0, 0);
      }
    }
  }

  public static void castExtension(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.getRandom();

    int color1 = ModSpells.EXTENSION.get().getColor1();
    int color2 = ModSpells.EXTENSION.get().getColor2();

    if (entity != null) {
      double x = entity.getX();
      double y = entity.getY() + (entity.getEyeHeight() * 0.8);
      double z = entity.getZ();

      for (float angle = 0; angle < 360; angle += 1 + random.nextFloat() * 2) {
        double radians = Math.toRadians(angle);
        double radius = 0.5 + random.nextDouble() * 0.3;

        double offsetX = Math.cos(radians) * radius;
        double offsetZ = Math.sin(radians) * radius;
        double offsetY = (random.nextDouble() - 0.5) * 0.2;

        Vec3 spawnPos = new Vec3(x + offsetX, y + offsetY, z + offsetZ);
        Vec3 motion = new Vec3(offsetX, offsetY, offsetZ).normalize().scale(0.1 + random.nextDouble() * 0.15);

        RootsParticleOptions opt = random.nextBoolean()
            ? new RootsParticleOptions(ModParticles.EXTENSION, color2, color1)
            : new RootsParticleOptions(ModParticles.EXTENSION, color1, color2);

        minecraft.level.addParticle(opt, spawnPos.x, spawnPos.y, spawnPos.z, motion.x, motion.y, motion.z);
      }
    }
  }

  // TODO: This probably doesn't need to be an emitter
  public static void castSkySorarer(int entityId, int duration) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);

    if (entity != null) {
      int color1 = 0xb0ecff; //ModSpells.SKY_SOARER.get().getColor1();
      int color2 = 0xccd3ff; //ModSpells.SKY_SOARER.get().getColor2();

      minecraft.level.addParticle(new RootsParticleOptions(ModParticles.SKY_SOARER_EMITTER, color1, color2, entityId), entity.getX(), entity.getY(), entity.getZ(), duration + 25, 5, 0.9);
    }
  }

  public static void dandelionWinds(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.random;

    if (entity != null) {
      int color1 = ModSpells.DANDELION_WINDS.get().getColor1();
      int color2 = ModSpells.DANDELION_WINDS.get().getColor2();

      Vec3 origin = entity.getEyePosition().subtract(0, 0.6, 0);
      Vec3 look = entity.getLookAngle().normalize();

      int streamCount = 6;

      for (int stream = 0; stream < streamCount; stream++) {
        // Slight variation per stream
        float angle = (float) ((random.nextDouble() - 0.5) * Math.toRadians(8)); // very narrow cone
        Vec3 streamDir = look.yRot(angle).normalize();

        // Base speed per stream (uniform)
        Vec3 baseMotion = streamDir.scale(0.07).add(0, 0.04, 0);

        // Each stream spawns multiple particles in a row
        for (int i = 0; i < 5; i++) {
          RootsParticleOptions opts = random.nextBoolean()
              ? new RootsParticleOptions(ModParticles.WIND, color1, color2)
              : new RootsParticleOptions(ModParticles.WIND, color2, color1);

          // Offset origin slightly to make trails
          Vec3 offset = streamDir.scale(i * 0.1);
          Vec3 spawnPos = origin.add(offset);

          minecraft.level.addParticle(opts, spawnPos.x, spawnPos.y, spawnPos.z, baseMotion.x, baseMotion.y, baseMotion.z);
        }
      }
    }
  }

  public static void acidCloud(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    int color1 = ModSpells.ACID_CLOUD.get().getColor1();
    int color2 = ModSpells.ACID_CLOUD.get().getColor2();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.random;
    if (entity != null) {
      for (float i = 0; i < 360; i += (random.nextFloat() * 5)) {
        RootsParticleOptions opts = random.nextBoolean() ? new RootsParticleOptions(ModParticles.SMOKE, color1, color2) : new RootsParticleOptions(ModParticles.SMOKE, color2, color1);
        double rad = Math.toRadians(i);
        double x = entity.getX() + (1.5 * random.nextDouble()) * Math.sin(rad);
        double y = entity.getY() + 0.5;
        double z = entity.getZ() + (1.5 * random.nextDouble()) * Math.cos(rad);
        double vx = 0.0825 * Math.sin(rad);
        double vz = 0.0825 * Math.cos(rad);
/*        if (random.nextBoolean()) {
          vx *= -1;
          vz *= -1;
        }*/
        minecraft.level.addParticle(opts, x, y, z, vx, 0.0525 * (random.nextDouble() - 0.5), vz);
      }
    }
  }

  public static void disarm(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    int color1 = ModSpells.DISARM.get().getColor1();
    int color2 = ModSpells.DISARM.get().getColor2();
    Entity entity = minecraft.level.getEntity(entityId);
    if (entity != null) {
      minecraft.level.addParticle(new RootsParticleOptions(ModParticles.DISARM_EMITTER, color1, color2, entityId), entity.getX(), entity.getY(), entity.getZ(), 18, 2, 0.4);
    }
  }

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

      double radius = 0.08 + minecraft.level.random.nextDouble() * 0.08;
      double angle = minecraft.level.random.nextDouble() * (2 * Math.PI);
      double handOffset = hand == InteractionHand.MAIN_HAND ? 0.3 : -0.3;

      minecraft.level.addParticle(
          new RootsParticleOptions(ModParticles.CHANNEL_FAIL, col1, col2, casterId),
          caster.getX(),
          caster.getY(),
          caster.getZ(),
          radius, angle, handOffset
      );

      radius = 0.08 + minecraft.level.random.nextDouble() * 0.08;
      angle = minecraft.level.random.nextDouble() * (2 * Math.PI);
      minecraft.level.addParticle(
          new RootsParticleOptions(ModParticles.CHANNEL_FAIL, col2, col1, casterId),
          caster.getX(),
          caster.getY(),
          caster.getZ(),
          radius, angle, handOffset
      );
    }
  }

  public static void castChannel(Spell spell, int casterId, int ticks) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity caster = minecraft.level.getEntity(casterId);
    Player player = minecraft.player;
    if (player != null && caster != null) {
      Vec3 start = player.getPosition(0f);
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

  public static void castChannelTarget(Spell spell, int casterId, Vec3 start, Vec3 stop, int ticks) {
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

  public static void addEntityBeam(int entityId1, int entityId2) {
    Minecraft mc = Minecraft.getInstance();
    if (mc == null) {
      return;
    }

    Entity entity1 = mc.level.getEntity(entityId1);
    Entity entity2 = mc.level.getEntity(entityId2);
    if (entity1 == null || entity2 == null) {
      return;
    }
    RenderTickHandler.renderBeam(new Beam.EntityBeam(new Beam.BeamAlpha(0.5f), entity1, entity2, 20));
  }
}
