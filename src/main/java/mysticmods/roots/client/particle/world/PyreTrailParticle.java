package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PyreTrailParticle extends TextureSheetParticle {
  protected final List<Snapshot> snapshots = new ArrayList<>();

  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rotSpeed, spinAcceleration;

  protected int snapshotFrequency = 20;
  protected int snapshotDecay = 60;

  protected PyreTrailParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 100;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd *= 0.03f;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.2f;
    this.rotSpeed = 0f;
    this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5 : 5);
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
    super.tick();
    if (!this.removed) {
      this.snapshots.removeIf(s -> s.isDecayed(this.age, this.snapshotDecay));

      if ((this.age % this.snapshotFrequency == 0) || this.age == 0) {


        this.snapshots.add(new Snapshot(this.age, this.rCol, this.gCol, this.bCol, this.x, this.y, this.z, this.xo, this.yo, this.zo, this.roll, this.oRoll));
      }

      float f = (float) this.age / (float) this.lifetime;
      f *= f;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      float spinFactor = 1.0f - f;
      spinFactor *= spinFactor;
      f *= f;
      f *= f;

      if (this.spinAcceleration != 0.0f) {
/*        this.rotSpeed += this.spinAcceleration / 20.0f * spinFactor;
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;*/
      }

      this.quadSize *= 1.0f - f;
    }
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    super.render(buffer, renderInfo, partialTicks);

    Vec3 camPos = renderInfo.getPosition();

    for (Snapshot s : snapshots) {
      float decayProgress = (float) (this.age - s.age) / this.snapshotDecay;
      if (decayProgress > 1.0f || decayProgress < 0.0f) continue;

      float alphaFade = this.alpha * (1.0f - decayProgress);
      float sizeFade = this.quadSize * (1.0f - decayProgress * 0.5f);

      float fX = (float) (Mth.lerp(partialTicks, s.xo, s.x) - camPos.x());
      float fY = (float) (Mth.lerp(partialTicks, s.yo, s.y) - camPos.y());
      float fZ = (float) (Mth.lerp(partialTicks, s.zo, s.z) - camPos.z());

      Quaternionf q = new Quaternionf();
      q.set(renderInfo.rotation());
      q.rotateZ(Mth.lerp(partialTicks, s.roll, s.oRoll)); // use stored roll

      renderSnapshotQuad(buffer, q, fX, fY, fZ, sizeFade, s.r, s.g, s.b, alphaFade, this.getU0(), this.getU1(), this.getV0(), this.getV1(), this.getLightColor(partialTicks));
    }
  }

  private void renderSnapshotQuad(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z, float size, double r, double g, double b, float alpha, float u0, float u1, float v0, float v1, int light) {
    renderVertex(buffer, rotation, x, y, z, 1.0f, -1.0f, size, u1, v1, r, g, b, alpha, light);
    renderVertex(buffer, rotation, x, y, z, 1.0f, 1.0f, size, u1, v0, r, g, b, alpha, light);
    renderVertex(buffer, rotation, x, y, z, -1.0f, 1.0f, size, u0, v0, r, g, b, alpha, light);
    renderVertex(buffer, rotation, x, y, z, -1.0f, -1.0f, size, u0, v1, r, g, b, alpha, light);
  }

  private void renderVertex(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z, float xOffset, float yOffset, float size, float u, float v, double r, double g, double b, float alpha, int light) {
    Vector3f vec = new Vector3f(xOffset, yOffset, 0.0F);
    vec.rotate(rotation).mul(size).add(x, y, z);
    buffer.addVertex(vec.x(), vec.y(), vec.z()).setUv(u, v).setColor((float) r, (float) g, (float) b, alpha)
        .setLight(light);
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new PyreTrailParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }

  public record Snapshot(int age, float r, float g, float b, double x, double y, double z, double xo, double yo,
                         double zo, float roll, float oRoll) {
    public boolean isDecayed(int age, int decay) {
      return this.age + decay < age;
    }
  }
}
