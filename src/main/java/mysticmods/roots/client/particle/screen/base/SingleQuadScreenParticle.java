package mysticmods.roots.client.particle.screen.base;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class SingleQuadScreenParticle extends ScreenParticle {
  protected float quadSize = 1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;

  protected SingleQuadScreenParticle(ClientLevel level, double x, double y) {
    super(level, x, y);
  }

  protected SingleQuadScreenParticle(ClientLevel level, double x, double y, double xSpeed, double ySpeed) {
    super(level, x, y, xSpeed, ySpeed);
  }

  @Override
  public void render(VertexConsumer buffer, float partialTicks) {
    float size = this.getQuadSize(partialTicks);
    float u0 = this.getU0();
    float u1 = this.getU1();
    float v0 = this.getV0();
    float v1 = this.getV1();

    int light = getLightColor(partialTicks);

    float lerpRoll = Mth.lerp(partialTicks, this.oRoll, this.roll);
    Quaternionf quaternion = new Quaternionf(new AxisAngle4f(lerpRoll, new Vector3f(0.0f, 0.0f, 1.0f)));

    float quadZ = this.getQuadZ();

    Vector3f[] vectors = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0f), new Vector3f(-1.0f, 1.0f, 0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
    for (int i = 0; i < 4; i++) {
      vectors[i].rotate(quaternion);
      vectors[i].mul(size, size, 1.0f);
      vectors[i].add((float) this.x, (float) this.y, 0f);
    }

    buffer.addVertex(vectors[0].x, vectors[0].y, quadZ).setUv(u1, v1)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
    buffer.addVertex(vectors[1].x, vectors[1].y, quadZ).setUv(u1, v0)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
    buffer.addVertex(vectors[2].x, vectors[2].y, quadZ).setUv(u0, v0)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
    buffer.addVertex(vectors[3].x, vectors[3].y, quadZ).setUv(u0, v1)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
  }

  public float getQuadZ() {
    return 0;
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
}
