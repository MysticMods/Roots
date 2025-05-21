package mysticmods.roots.client.particle.screen.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class SingleQuadScreenParticle extends ScreenParticle {
  protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;

  protected SingleQuadScreenParticle(ClientLevel level, double x, double y) {
    super(level, x, y);
  }

  protected SingleQuadScreenParticle(
      ClientLevel level, double x, double y, double xSpeed, double ySpeed
  ) {
    super(level, x, y, xSpeed, ySpeed);
  }

  public SingleQuadScreenParticle.FacingCameraMode getFacingCameraMode() {
    return SingleQuadScreenParticle.FacingCameraMode.LOOKAT_XYZ;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    Quaternionf quaternionf = new Quaternionf();
    this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
    if (this.roll != 0.0F) {
      quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
    }

    this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
  }

  protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
    Vec3 vec3 = camera.getPosition();
    float f = (float) (Mth.lerp((double) partialTicks, this.xo, this.x) - vec3.x());
    float f1 = (float) (Mth.lerp((double) partialTicks, this.yo, this.y) - vec3.y());
    float f2 = (float) (Mth.lerp((double) partialTicks, 0, 0) - vec3.z());
    this.renderRotatedQuad(buffer, quaternion, f, f1, f2, partialTicks);
  }

  protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
    float f = this.getQuadSize(partialTicks);
    float f1 = this.getU0();
    float f2 = this.getU1();
    float f3 = this.getV0();
    float f4 = this.getV1();
    int i = this.getLightColor(partialTicks);
    this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
    this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
    this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
    this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
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
    Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
    buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
        .setUv(u, v)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
        .setLight(packedLight);
  }

  public float getQuadSize(float scaleFactor) {
    return this.quadSize;
  }

  public ScreenParticle scale(float scale) {
    this.quadSize *= scale;
    return this;
  }

  protected abstract float getU0();

  protected abstract float getU1();

  protected abstract float getV0();

  protected abstract float getV1();

  public interface FacingCameraMode {
    SingleQuadScreenParticle.FacingCameraMode LOOKAT_XYZ = (p_312316_, p_311843_, p_312119_) -> p_312316_.set(p_311843_.rotation());
    SingleQuadScreenParticle.FacingCameraMode LOOKAT_Y = (p_312695_, p_312346_, p_312064_) -> p_312695_.set(
        0.0F, p_312346_.rotation().y, 0.0F, p_312346_.rotation().w
    );

    void setRotation(Quaternionf quaternion, Camera camera, float partialTick);
  }
}
