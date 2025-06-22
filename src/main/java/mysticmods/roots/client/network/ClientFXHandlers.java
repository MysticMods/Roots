package mysticmods.roots.client.network;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.gui.overlay.WarningOverlay;
import mysticmods.roots.client.particle.Beam;
import mysticmods.roots.client.particle.bolt.LightningPreset;
import mysticmods.roots.client.particle.bolt.PositionProvider;
import mysticmods.roots.client.particle.screen.ScreenParticleEngine;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.recipe.TaggedPedestalCrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;

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
          RootsParticleOptions.builder(ModParticles.CHANNEL_JAUNT).color(col1, col2).entityId(casterId).build(),
          caster.getX(),
          caster.getY(),
          caster.getZ(),
          1, 0, handOffset
      );
      minecraft.level.addParticle(
          RootsParticleOptions.builder(ModParticles.CHANNEL_JAUNT).color(col2, col1).entityId(casterId).build(),
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

    minecraft.level.addParticle(RootsParticleOptions.builder(ModParticles.PETAL_SHELL).color(color1, color2)
        .entityId(entityId).build(), true, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
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

    RootsParticleOptions opt1 = RootsParticleOptions.builder(ModParticles.MAGNETISM).color(color2, color1)
        .entityId(entityId).build();
    RootsParticleOptions opt2 = RootsParticleOptions.builder(ModParticles.MAGNETISM).color(color1, color2)
        .entityId(entityId).build();

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

    RootsParticleOptions first = RootsParticleOptions.builder(ModParticles.SHATTER_BEAM).color(color1, color2).build();
    RootsParticleOptions second = RootsParticleOptions.builder(ModParticles.SHATTER_BEAM).color(color2, color1).build();

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
            ? RootsParticleOptions.builder(ModParticles.EXTENSION).color(color2, color1).build()
            : RootsParticleOptions.builder(ModParticles.EXTENSION).color(color1, color2).build();

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

      minecraft.level.addParticle(RootsParticleOptions.builder(ModParticles.SKY_SOARER_EMITTER).color(color1, color2)
          .entityId(entityId).build(), entity.getX(), entity.getY(), entity.getZ(), duration + 25, 5, 0.9);
    }
  }

  public static void dandelionWinds(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.random;


    if (entity != null) {
      int color1 = ModSpells.DANDELION_WINDS.get().getColor1();
      int color2 = ModSpells.DANDELION_WINDS.get().getColor2();

      Vec3 look = entity.getViewVector(1.0F);
      double yaw = Math.atan2(look.z, look.x) - Math.PI / 2;

      double dirX = -Math.sin(yaw);
      double dirZ = Math.cos(yaw);

      double sideX = dirZ;
      double sideZ = -dirX;

      for (int i = 0; i < 28; i++) {
        double lateralOffset = (random.nextDouble() - 0.5) * 6.0;
        double forwardOffset = 0.5;

        double spawnX = entity.getX() - dirX * forwardOffset + sideX * lateralOffset;
        double spawnY = entity.getY() + entity.getBbHeight() * 0.5 + (random.nextDouble() - 0.5) * 1.5;
        double spawnZ = entity.getZ() - dirZ * forwardOffset + sideZ * lateralOffset;

        double speed = 0.4 + random.nextDouble() * 0.4;
        double vx = dirX * speed;
        double vy = 0;
        double vz = dirZ * speed;

        minecraft.level.addParticle(
            RootsParticleOptions.builder(ModParticles.WIND).build(),
            spawnX, spawnY, spawnZ,
            vx, vy, vz
        );
      }
      for (int i = 0; i < 8; i++) {
        double offset = (random.nextDouble() - 0.5) * 1.8; // lateral variation in front cone
        double forwardOffset = 0.2;

        double x = entity.getX() + dirX * forwardOffset + sideX * offset;
        double y = entity.getY() + entity.getBbHeight() * 0.4 + (random.nextDouble()) * 0.4;
        double z = entity.getZ() + dirZ * forwardOffset + sideZ * offset;

        double speed = 0.02 + random.nextDouble() * 0.02;
        double vx = dirX * speed + (random.nextDouble() - 0.5) * 0.01;
        double vy = 0.01 + (random.nextDouble() - 0.5) * 0.01;
        double vz = dirZ * speed + (random.nextDouble() - 0.5) * 0.01;

        minecraft.level.addParticle(
            RootsParticleOptions.builder(ModParticles.DANDELION).build(),
            x, y, z,
            vx, vy, vz
        );
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
        RootsParticleOptions opts = random.nextBoolean() ? RootsParticleOptions.builder(ModParticles.SMOKE)
            .color(color1, color2).build() : RootsParticleOptions.builder(ModParticles.SMOKE).color(color2, color1)
            .build();
        double rad = Math.toRadians(i);
        double x = entity.getX() + (1.5 * random.nextDouble()) * Math.sin(rad);
        double y = entity.getY() + 0.5;
        double z = entity.getZ() + (1.5 * random.nextDouble()) * Math.cos(rad);
        double vx = 0.0825 * Math.sin(rad);
        double vz = 0.0825 * Math.cos(rad);
        minecraft.level.addParticle(opts, x, y, z, vx, 0.0525 * (random.nextDouble() - 0.5), vz);
        if (random.nextInt(3) == 0) {
          minecraft.level.addParticle(opts.builder().type(ModParticles.FOG)
              .build(), x, y - 0.4, z, vx, 0.0525 * (random.nextDouble() - 0.5), vz);
        }
      }
    }
  }

  public static void disarm(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    int color1 = ModSpells.DISARM.get().getColor1();
    int color2 = ModSpells.DISARM.get().getColor2();
    Entity entity = minecraft.level.getEntity(entityId);
    if (entity != null) {
      minecraft.level.addParticle(RootsParticleOptions.builder(ModParticles.DISARM_EMITTER).color(color1, color2)
          .entityId(entityId).build(), entity.getX(), entity.getY(), entity.getZ(), 18, 2, 0.4);
    }
  }

  public static void spiral(BlockPos position, double radius, double angle, int color1, int color2) {
    Minecraft minecraft = Minecraft.getInstance();
    minecraft.level.addParticle(RootsParticleOptions.builder(ModParticles.SPIRAL).color(color1, color2).build(),
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
            RootsParticleOptions.builder(
                ModParticles.ANIMAL_HARVEST).color(
                col1,
                col2).build(),
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
          RootsParticleOptions.builder(ModParticles.CHANNEL_FAIL).color(col1, col2).entityId(casterId).build(),
          caster.getX(),
          caster.getY(),
          caster.getZ(),
          radius, angle, handOffset
      );

      radius = 0.08 + minecraft.level.random.nextDouble() * 0.08;
      angle = minecraft.level.random.nextDouble() * (2 * Math.PI);
      minecraft.level.addParticle(
          RootsParticleOptions.builder(ModParticles.CHANNEL_FAIL).color(col2, col1).entityId(casterId).build(),
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
          RootsParticleOptions.builder(
              ModParticles.CHANNEL).color(
              col1,
              col2).entityId(
              casterId).build(),
          start.x,
          start.y,
          start.z,
          radius,
          0,
          0
      );
      minecraft.level.addParticle(
          RootsParticleOptions.builder(
              ModParticles.CHANNEL).color(
              col2,
              col1).entityId(
              casterId).build(),
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
          RootsParticleOptions.builder(
              ModParticles.CHANNEL_TARGET).color(
              col1,
              col2).build(),
          start.x,
          start.y,
          start.z,
          stop.x,
          stop.y,
          stop.z
      );
      minecraft.level.addParticle(
          RootsParticleOptions.builder(
              ModParticles.CHANNEL_TARGET).color(
              col2,
              col1).build(),
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
            RootsParticleOptions.builder(
                ModParticles.GROWTH).color(
                0x2ce713,
                0x4d7e20).build(),
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
        double yOffset = (minecraft.level.random.nextDouble() - 0.5) * 0.1;

        minecraft.level.addParticle(
            RootsParticleOptions.builder(
                ModParticles.GROWTH).color(
                0xc2d02a,
                0x7fc73c).build(),
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
      WarningOverlay.warningTicks = ConfigManager.ALERTNESS_DURATION.getAsInt();
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
    RenderTickHandler.renderBeam(new Beam.EntityBeam(new Beam.BeamAlpha(255), entity1, entity2, 500));
  }

  public static void createBolt(int renderer, LightningPreset preset, int start, int end, int segments) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity1 = minecraft.level.getEntity(start);
    Entity entity2 = minecraft.level.getEntity(end);
    if (entity1 == null || entity2 == null) {
      return;
    }

    if (preset.getShouldAdd().getAsBoolean()) {
      RenderTickHandler.renderBolt(renderer, preset.getBoltCreator()
          .create(PositionProvider.ofEyes(entity1, entity2, 0.5f), segments));
    }
  }

  public static void createBolt(int renderer, LightningPreset preset, Vec3 start, Vec3 end, int segments) {
    if (preset.getShouldAdd().getAsBoolean()) {
      RenderTickHandler.renderBolt(renderer, preset.getBoltCreator().create(PositionProvider.of(start, end), segments));
    }
  }

  public static void createBolt(int renderer, LightningPreset preset, int start, Vec3 end, int segments) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity1 = minecraft.level.getEntity(start);
    if (entity1 == null) {
      return;
    }

    if (preset.getShouldAdd().getAsBoolean()) {
      RenderTickHandler.renderBolt(renderer, preset.getBoltCreator()
          .create(PositionProvider.ofEyes(entity1, end, 0.5f), segments));
    }
  }

  public static void castLifeDrain(int entityId, double distance, int angle) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    RandomSource random = minecraft.level.getRandom();

    int color1 = 0x1e0e13;//ModSpells.LIFE_DRAIN.get().getColor1();
    int color2 = 0x6b5766; //ModSpells.LIFE_DRAIN.get().getColor2();

    RootsParticleOptions opt1 = RootsParticleOptions.builder(ModParticles.LIFE_DRAIN).color(color2, color1)
        .entityId(entityId).build();
    RootsParticleOptions opt2 = RootsParticleOptions.builder(ModParticles.LIFE_DRAIN).color(color1, color2)
        .entityId(entityId).build();

    if (entity != null) {
      double x = entity.getX();
      double y = entity.getY();
      double z = entity.getZ();

      for (int i = 0; i < 25; i++) {
        double rand = random.nextDouble() - 0.5;
        double r = random.nextDouble() * distance;

        minecraft.level.addParticle(random.nextBoolean() ? opt1 : opt2, x, y, z, r, rand, angle);
      }
    }
  }

  public static void drainLife(int entityId, int casterId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    Entity caster = minecraft.level.getEntity(casterId);
    if (entity == null || caster == null) {
      return;
    }

    minecraft.level.addParticle(
        RootsParticleOptions.builder(ModParticles.LIFE_DRAIN_EMITTER).color(ModSpells.LIFE_DRAIN).entityId(entityId)
            .casterId(casterId).build(),
        entity.getX(),
        entity.getY(),
        entity.getZ(),
        60,
        0,
        0
    );
  }

  public static void harvestPositions(List<BlockPos> positions) {
    Minecraft minecraft = Minecraft.getInstance();

    for (BlockPos pos : positions) {
      Vec3 vec = Vec3.atCenterOf(pos);
      minecraft.level.addParticle(RootsParticleOptions.builder(ModParticles.HARVEST)
          .build(), vec.x, vec.y, vec.z, 0, 0, 0);
    }
  }

  public static void desaturate(float heartsStart, float heartsNow, int oldFood, int newFood) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null || oldFood == 0 || heartsNow <= heartsStart) {
      return;
    }

    Player player = minecraft.player;
    int guiWidth = minecraft.getWindow().getGuiScaledWidth();
    int guiHeight = minecraft.getWindow().getGuiScaledHeight();

    int visibleHearts = Math.min(10, (int) Math.ceil(player.getMaxHealth() / 2f));
    Vector2d heart = getHeartIcon(player, guiWidth, guiHeight, visibleHearts - 2);

    for (int i = 0; i < oldFood; i++) {
      Vector2d pos = getFoodIcon(player, guiWidth, guiHeight, newFood + i);
      ScreenParticleEngine.addHudParticle(
          RootsParticleOptions.builder(ModParticles.DESATURATE).build(),
          pos.x, pos.y,
          heart.x, heart.y
      );
    }
  }

  public static Vector2d getFoodIcon(Player player, int guiWidth, int guiHeight, int foodLevel) {
    int foodIcons = Math.min(10, (foodLevel + 1) / 2);

    boolean hasMountHealth = player.getVehicle() instanceof LivingEntity mount && mount.isAlive() && mount.getMaxHealth() > 20;

    int baseY = guiHeight - 39;
    if (hasMountHealth) {
      baseY -= 10;
    }

    int baseX = guiWidth / 2 + 91;

    return new Vector2d(baseX - (foodIcons - 1) * 8 - 9 + 4.5, baseY + 4.5);
  }

  public static Vector2d getHeartIcon(Player player, int guiWidth, int guiHeight, float heartIndex) {
    boolean hasMountHealth = player.getVehicle() instanceof LivingEntity mount && mount.isAlive() && mount.getMaxHealth() > 20;

    int baseY = guiHeight - 39;
    if (hasMountHealth) {
      baseY -= 10;
    }

    int baseX = guiWidth / 2 - 91;

    int row = (int) (heartIndex / 10);
    int col = (int) (heartIndex % 10);

    return new Vector2d(baseX + col * 8 + 4.5, baseY + row * 10 + 4.5);
  }


  public static void heal(int entityId, float amount) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    if (entity == null) {
      return;
    }

    RandomSource random = minecraft.level.getRandom();

    RootsParticleOptions options = RootsParticleOptions.builder(ModParticles.HEAL).build();

    if (entity.getId() == minecraft.player.getId()) {
      Player player = minecraft.player;
      int guiWidth = minecraft.getWindow().getGuiScaledWidth();
      int guiHeight = minecraft.getWindow().getGuiScaledHeight();

      int visibleHearts = Math.min(10, (int) Math.ceil(player.getMaxHealth() / 2f));
      Vector2d heart = getHeartIcon(player, guiWidth, guiHeight, visibleHearts - 2);
      for (int a = 0; a < amount; a += 2) {
        ScreenParticleEngine.addHudParticle(options, heart.x, heart.y - 4 - random.nextDouble() * 3, (random.nextDouble() - 0.5) * 0.8, -(1.4 + random.nextDouble()));
      }
    }

    for (float a = 0; a < amount; a += 0.5f) {
      minecraft.level.addParticle(options, entity.getX(), entity.getY() + 0.5, entity.getZ(), (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() * 0.2), (random.nextDouble() - 0.5) * 0.2);
    }
  }

  public static void nondetection(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);

    int color1 = ModSpells.NONDETECTION.get().getColor1();
    int color2 = ModSpells.NONDETECTION.get().getColor2();

    if (entity == null) {
      return;
    }

    minecraft.level.addParticle(RootsParticleOptions.builder(ModParticles.NONDETECTION).color(color1, color2)
        .entityId(entityId).build(), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
  }

  public static void sanctuary(int entityId, int radiusInt) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);

    double radius = Math.sqrt(radiusInt);

    RandomSource random = minecraft.level.random;

    if (entity == null) {
      return;
    }

    double x = entity.getX();
    double y = entity.getY() + (entity.getEyeHeight() * 0.3);
    double z = entity.getZ();

    for (float angle = 0; angle < 360; angle += 1 + random.nextFloat() * 2) {
      double radians = Math.toRadians(angle);

      double offsetX = Math.cos(radians) * radius;
      double offsetZ = Math.sin(radians) * radius;
      double offsetY = (random.nextDouble() - 0.5) * 0.2;

      Vec3 spawnPos = new Vec3(x + offsetX, y + offsetY, z + offsetZ);

      RootsParticleOptions opts = random.nextBoolean() ? RootsParticleOptions.builder(ModParticles.SANCTUARY)
          .color(ModSpells.SANCTUARY).build() : RootsParticleOptions.builder(ModParticles.SANCTUARY)
          .color(ModSpells.SANCTUARY).swapColors().build();

      minecraft.level.addParticle(opts, spawnPos.x, spawnPos.y, spawnPos.z, 0, 0, 0);
    }
  }

  public static void saturate(int entityId, int oldFood, int newFood) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null || newFood == oldFood) {
      return;
    }

    Entity entity = minecraft.level.getEntity(entityId);
    if (entity == null) {
      return;
    }

    int delta = newFood - oldFood;

    RandomSource random = minecraft.level.getRandom();
    RootsParticleOptions options = RootsParticleOptions.builder(ModParticles.SATURATE).build();

    Player player = minecraft.player;
    if (player != null && player.getId() == entityId) {
      int guiWidth = minecraft.getWindow().getGuiScaledWidth();
      int guiHeight = minecraft.getWindow().getGuiScaledHeight();

      for (int i = 0; i < delta; i++) {
        Vector2d pos = getFoodIcon(player, guiWidth, guiHeight, newFood - i);
        ScreenParticleEngine.addHudParticle(options,
            pos.x, pos.y - 4 - random.nextDouble() * 3, (random.nextDouble() - 0.5) * 0.8, -(1.4 + random.nextDouble()));
      }
    }

    for (int i = 0; i < delta; i++) {
      minecraft.level.addParticle(options, entity.getX(), entity.getY() + 0.5, entity.getZ(), (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() * 0.2), (random.nextDouble() - 0.5) * 0.2);
    }

    // TODO: Particles to demonstration saturation to others
  }

  public static void startGroveCrafting(BlockPos groveCrafter, List<TaggedPedestalCrafting.ItemPosition> positions) {
    Minecraft minecraft = Minecraft.getInstance();
    Vec3 dest = Vec3.atBottomCenterOf(groveCrafter).add(0, 1.05, 0);

    RandomSource random = minecraft.level.getRandom();

    for (int i = 0; i < positions.size(); i++) {
      BlockPos pos = positions.get(i).position();
      ItemStack item = positions.get(i).item();

      Vec3 spawnPos = Vec3.atBottomCenterOf(pos).add(0, 1.6, 0);

      // TODO: Delay actually spawning some of these
      int total = (7 + random.nextInt(8));
      int baseDelay = 10; // base starting delay
      int maxSpread = 80; // total maximum spread across the whole sequence

      for (int j = 0; j < total; j++) {
        double linearProgress = (double) j / total; // 0.0 -> 1.0
        double curvedProgress = Math.pow(linearProgress, 0.4);
        int spreadDelay = (int) (curvedProgress * maxSpread);
        int jitter = random.nextInt(5);
        int delay = baseDelay + spreadDelay + jitter;
        // TODO: Generate a stream of additive particles that spiral around the average of the bezier curves
        Minecraft.getInstance().level.addParticle(
            RootsParticleOptions.builder(ModParticles.GROVE_ITEM).item(item)
                .delay(delay).build(),
            spawnPos.x, spawnPos.y, spawnPos.z,
            dest.x, dest.y, dest.z
        );
      }
    }
  }

  public static void growthAmplifierGrew(BlockPos amplifier, Vec3 target) {
    Level level = Minecraft.getInstance().level;
    RandomSource random = level.getRandom();

    Vec3 start = Vec3.atCenterOf(amplifier);

    Vec3 diff = target.subtract(start).normalize();
    Vec3 motion = diff.scale(0.1 + random.nextDouble() * 0.05);

    for (int i = 0; i < 13; i++) {
      Vec3 offset = start.add(
          (random.nextDouble() - 0.5) * 0.2,
          (random.nextDouble() - 0.5) * 0.2,
          (random.nextDouble() - 0.5) * 0.2
      );
      level.addParticle(
          RootsParticleOptions.builder(ModParticles.GROVE_CRAFTER).build(),
          offset.x, offset.y, offset.z,
          motion.x,
          motion.y,
          motion.z
      );
    }
  }
}
