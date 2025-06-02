package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.IParticleHolder;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class NondetectionParticle extends RootsParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected int count, maxCount;
  protected float[][] rolls;

  private final LivingEntity entity;

  protected NondetectionParticle(ClientLevel level, double x, double y, double z, LivingEntity entity, int c1, int c2) {
    super(level, x, y, z);
    this.entity = entity;
    this.lifetime = 100;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 0.8f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.08f;
    this.defaultAlpha = false;
    this.defaultColor = false;
    this.defaultLight = false;
    this.defaultMovement = false;
    this.defaultRoll = false;
    tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_ADDITIVE;
  }

  @Override
  protected int getLightColor(float partialTick) {
    BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
    return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(this.level, blockpos) : 0;
  }

  @Override
  public void tick() {
    if (entity == null || entity.isRemoved()) {
      this.remove();
      return;
    }

    MobEffectInstance effect = entity.getEffect(ModEffects.NONDETECTION);
    if (effect == null) {
      this.remove();
      return;
    }

    this.count = 30;

/*    SnapshotStorage storage = entity.getData(ModAttachments.SNAPSHOT_STORAGE);
    if (storage == null) {
      this.remove();
      return;
    }

    PetalShellSnapshot snapshot = storage.getSnapshot(entity, ModSerializers.PETAL_SHELL.get());
    if (snapshot == null) {
      this.remove();
      return;
    }*/

    this.maxCount = 30;

    if (rolls == null) {
      rolls = new float[maxCount][3];
      for (int i = 0; i < maxCount; i++) {
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
      rolls[i][2] = rolls[i][1];
      rolls[i][1] += rolls[i][0];
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
  protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
    if (entity == null || entity.isRemoved() || removed) {
      return;
    }

    Vec3 vec3 = camera.getPosition();

    double radius = 2.0f;
    double height = 1.0f;
    double anglePerShell = Math.PI * 2 / count;
    double angleOffset = Math.toRadians(entity.tickCount % 360);

    int newCount = count;

    for (int i = 0; i <= maxCount; i++) {
      double sin = Math.sin(angleOffset + i * anglePerShell);
      double cos = Math.cos(angleOffset + i * anglePerShell);

      double x = this.x + radius * sin;
      double y = this.y + height;
      double z = this.z + radius * cos;

      double xo = this.xo + radius * sin;
      double yo = this.yo + height;
      double zo = this.zo + radius * cos;

      float f = (float) (Mth.lerp(partialTicks, xo, x) - vec3.x());
      float f1 = (float) (Mth.lerp(partialTicks, yo, y) - vec3.y());
      float f2 = (float) (Mth.lerp(partialTicks, zo, z) - vec3.z());

      Quaternionf q = new Quaternionf(quaternion);
      q.rotateZ(Mth.lerp(partialTicks, rolls[i][1], rolls[i][2]));

      this.renderRotatedQuad(buffer, q, f, f1, f2, partialTicks);

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

      Particle current = ((IParticleHolder) entity).roots_1_21$getParticle(ModParticles.NONDETECTION.value());
      if (current != null) {
        current.setLifetime(100);
        return null;
      }

      var particle = new NondetectionParticle(level, x, y, z, living, type.color1(), type.color2());
      particle.pickSprite(sprite);
      ((IParticleHolder) entity).roots_1_21$setParticle(ModParticles.NONDETECTION.value(), particle);
      return particle;
    }
  }
}
