package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TestParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rotSpeed, spinAcceleration;

  protected TestParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 25;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
/*    this.xd = xSpeed;
    this.yd = ySpeed;
    this.zd = zSpeed;*/
    this.xd = 0;
    this.yd *= 0.03f;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.18f;
    this.roll = this.oRoll = (float) Math.toDegrees(this.random.nextDouble());
    this.rotSpeed = 0f;
    this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5 : 5);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.GLOW;
    //return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {

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
        this.rotSpeed += this.spinAcceleration / 20.0f * spinFactor;
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;
      }

      this.quadSize *= 1.0f - f;
    }
/*    super.tick();
    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }
    }

    this.yd -= 0.008;

    this.xd *= 0.96;
    this.zd *= 0.96;

    if (this.age < this.lifetime / 3) {
      this.yd *= 0.9;
    }*/
  }

  public static class Copy extends TestParticle {
    private final TestParticle parent;

    protected Copy(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2, TestParticle parent) {
      super(level, x, y, z, xSpeed, ySpeed, zSpeed, c1, c2);
      this.parent = parent;
      this.sprite = parent.sprite;
      this.tick();
    }

    @Override
    public void tick() {
      this.x = parent.x;
      this.y = parent.y;
      this.z = parent.z;
      this.setBoundingBox(parent.getBoundingBox());
      this.setLocationFromBoundingbox();
      this.oR1 = parent.oR1;
      this.oG1 = parent.oG1;
      this.oB1 = parent.oB1;
      this.rCol2 = parent.rCol2;
      this.gCol2 = parent.gCol2;
      this.bcol2 = parent.bcol2;
      this.alpha = parent.alpha;
      this.xd = parent.xd;
      this.yd = parent.yd;
      this.zd = parent.zd;
      this.hasPhysics = parent.hasPhysics;
      this.quadSize = parent.quadSize;
      this.roll = parent.roll;
      this.age = parent.age;
      this.lifetime = parent.lifetime;
      this.removed = parent.removed;
      this.xo = parent.xo;
      this.yo = parent.yo;
      this.zo = parent.zo;
      this.roll = parent.roll;
      this.oRoll = parent.oRoll;
      this.friction = parent.friction;
      this.gravity = parent.gravity;
      this.sprite = parent.sprite;
      this.bbHeight = parent.bbHeight;
      this.bbWidth = parent.bbWidth;
      this.spinAcceleration = parent.spinAcceleration;
      this.rotSpeed = parent.rotSpeed;
      this.quadSize = parent.quadSize * 0.8f;
    }

    @Override
    public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
      //return RootsParticleRenderTypes.GLOW;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
      this.tick();
      return super.getQuadSize(scaleFactor);
    }

    @Override
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
      Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.1F).rotate(quaternion).mul(quadSize).add(x, y, z);
      buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
          .setUv(u, v)
          .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
          .setLight(packedLight);
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new TestParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      var particle2 = new Copy(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2(), particle);
      Minecraft.getInstance().particleEngine.add(particle2);
      return particle;
    }
  }
}
