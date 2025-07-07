package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.client.particle.IParticleHolder;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.snapshot.PetalShellSnapshot;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class PetalShellParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected int count, maxCount;
  protected float[][] rolls;

  private final LivingEntity entity;

  protected PetalShellParticle(ClientLevel level, double x, double y, double z, LivingEntity entity, int c1, int c2) {
    super(level, x, y, z);
    this.entity = entity;
    this.lifetime = 100;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.2f;
    tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    if (entity == null || entity.isRemoved()) {
      this.remove();
      return;
    }

    MobEffectInstance effect = entity.getEffect(ModEffects.PETAL_SHELL);
    if (effect == null) {
      this.remove();
      return;
    }

    this.count = effect.getAmplifier() + 1;

    SnapshotStorage storage = entity.getData(ModAttachments.SNAPSHOT_STORAGE);
    if (storage == null) {
      this.remove();
      return;
    }

    PetalShellSnapshot snapshot = storage.getSnapshot(entity, ModSerializers.PETAL_SHELL.get());
    if (snapshot == null) {
      this.remove();
      return;
    }

    this.maxCount = snapshot.getCount();

if (rolls == null || rolls.length < count) {
  rolls = new float[Math.max(count, maxCount)][3];
  for (int i = 0; i < rolls.length; i++) {
    rolls[i][0] = 0.05f + (random.nextFloat() - 0.5f) * 0.05f;
    rolls[i][1] = 0;
    rolls[i][2] = 0;
  }
}

    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    this.x = entity.getX();
    this.y = entity.getY();
    this.z = entity.getZ();

    for (int i = 0; i < maxCount; i++) {
      rolls[i][1] = rolls[i][2];
      rolls[i][2] += rolls[i][0];
    }

    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }
    }
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    // Otherwise it won't render in first person
    return AABB.INFINITE;
  }

  @Override
  public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
    if (entity == null || entity.isRemoved() || removed) {
      return;
    }

    Vec3 camPos = camera.getPosition();
    Vec3 entityPos = entity.getPosition(partialTicks);

    double radius = 0.8f;
    double height = 1.0f;
    double anglePerShell = Math.PI * 2 / count;
    double angleOffset = Math.toRadians((entity.tickCount + partialTicks)% 360);

    int newCount = count;

    for (int i = 0; i < count; i++) {
      double sin = Math.sin(angleOffset + i * anglePerShell);
      double cos = Math.cos(angleOffset + i * anglePerShell);

      float px = (float) (entityPos.x + radius * sin - camPos.x);
      float py = (float) (entityPos.y + height - camPos.y);
      float pz = (float) (entityPos.z + radius * cos - camPos.z);

      Quaternionf q = new Quaternionf();
      this.getFacingCameraMode().setRotation(q, camera, partialTicks);
      q.rotateZ(Mth.lerp(partialTicks, rolls[i][1], rolls[i][2]));

      this.renderRotatedQuad(buffer, q, px, py, pz, partialTicks);

      newCount--;
      if (newCount <= 0) {
        break;
      }
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (!(entity instanceof LivingEntity living)) {
        return null;
      }

      IParticleHolder holder = IParticleHolder.getHolder(entity);
      if (holder == null) {
        return null;
      }

      Particle current = holder.getParticle(ModParticles.PETAL_SHELL.value());
      if (current != null) {
        return null;
      }

      var particle = new PetalShellParticle(level, x, y, z, living, type.color1(), type.color2());
      particle.pickSprite(sprite);
      holder.setParticle(ModParticles.PETAL_SHELL.value(), particle);
      return particle;
    }
  }
}
