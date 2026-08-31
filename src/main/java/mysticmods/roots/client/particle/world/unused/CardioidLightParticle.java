package mysticmods.roots.client.particle.world.unused;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.client.particle.world.RootsParticle;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CardioidLightParticle extends RootsParticle {
  private final float quadSizeStart;

  protected CardioidLightParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, c1, c2);
    this.lifetime = 50;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = ySpeed;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = this.quadSizeStart = 0.6f;
    this.rollAmount = 0f; //(random.nextFloat() - 0.5f) * 0.1f;
  }

  @Override
  protected void updateAlpha(float f) {
    //super.updateAlpha(f * f);
  }

  @Override
  protected void updateQuadSize(float f) {
    this.quadSize = quadSizeStart - 0.05f * f * f * f;
  }

  @Override

  protected void particleTick(float f) {
    if (this.age > 5) {
      this.yd *= 1.08f;
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.CARDIOID_GLOW;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
    Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
    buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
        .setUv(u, v)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
        .setLight(packedLight)
        .setUv1(age, lifetime);
  }

  public record SmallProvider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new CardioidLightParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }

  public record LargeProvider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new CardioidLightParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
