package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class VelocityQuadParticle extends Particle {
  protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
  protected double xdo, ydo, zdo;

  protected VelocityQuadParticle(ClientLevel level, double x, double y, double z) {
    super(level, x, y, z);
  }

  protected VelocityQuadParticle(
      ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
  ) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }

  @Override
  public void tick() {
    this.xdo = this.xd;
    this.ydo = this.yd;
    this.zdo = this.zd;
    super.tick();
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    Vec3 cameraPosition = renderInfo.getPosition();

    float px = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
    float py = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
    float pz = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

    float vx = (float) Mth.lerp(partialTicks, this.xdo, this.xd);
    float vy = (float) Mth.lerp(partialTicks, this.ydo, this.yd);
    float vz = (float) Mth.lerp(partialTicks, this.zdo, this.zd);

    Quaternionf quaternionf = new Quaternionf();
    Vec3 velocity = new Vec3(vx, vy, vz);
    if (velocity.lengthSqr() > 1e-6f) {
      Vector3f right = new Vector3f((float) velocity.x, (float) velocity.y, (float) velocity.z).normalize(); // X axis
      Vector3f up = new Vector3f(0, 1, 0);

      // handle degenerate case where up and right are parallel
      if (Math.abs(right.dot(up)) > 0.99f) {
        up.set(0, 0, 1);
      }

      Vector3f forward = new Vector3f();
      up.cross(right, forward).normalize(); // Z axis (out of quad)

      up = new Vector3f();
      forward.cross(right, up).normalize(); // recompute corrected Y axis

      Matrix4f matrix = new Matrix4f()
          .identity()
          .m00(right.x).m01(up.x).m02(forward.x)
          .m10(right.y).m11(up.y).m12(forward.y)
          .m20(right.z).m21(up.z).m22(forward.z);

      quaternionf.setFromNormalized(matrix);
    } else {
      quaternionf.identity();
    }
    if (this.roll != 0.0F) {
      quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
    }

    this.renderRotatedQuad(buffer, quaternionf, px, py, pz, partialTicks);
  }

  protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
    float f = this.getQuadSize(partialTicks);
    float u0 = this.getU0();
    float u1 = this.getU1();
    float v0 = this.getV0();
    float v1 = this.getV1();
    int i = this.getLightColor(partialTicks);

    // Front face
    this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, u1, v1, i);
    this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, u1, v0, i);
    this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, u0, v0, i);
    this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, u0, v1, i);

    // Back face
this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, u0, v1, i);
this.renderVertex(buffer, quaternion, x, y, z, -1.0F,  1.0F, f, u0, v0, i);
this.renderVertex(buffer, quaternion, x, y, z,  1.0F,  1.0F, f, u1, v0, i);
this.renderVertex(buffer, quaternion, x, y, z,  1.0F, -1.0F, f, u1, v1, i);
  }

  private void renderVertex(
      VertexConsumer buffer,
      Quaternionf quaternion,
      float x,
      float y,
      float z,
      float xOffset,
      float yOffset,
      float quadSize,
      float u,
      float v,
      int packedLight
  ) {
    Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0f).rotate(quaternion).mul(quadSize).add(x, y, z);
    buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
        .setUv(u, v)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
        .setLight(packedLight);
  }

  @Override
  public net.minecraft.world.phys.AABB getRenderBoundingBox(float partialTicks) {
    float size = getQuadSize(partialTicks);
    return new net.minecraft.world.phys.AABB(this.x - size, this.y - size, this.z - size, this.x + size, this.y + size, this.z + size);
  }

  public float getQuadSize(float scaleFactor) {
    return this.quadSize;
  }

  @Override
  public Particle scale(float scale) {
    this.quadSize *= scale;
    return super.scale(scale);
  }

  protected abstract float getU0();

  protected abstract float getU1();

  protected abstract float getV0();

  protected abstract float getV1();
}
