package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class DandelionParticle extends RootsParticle {
  private int col1, col2;

  protected DandelionParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.lifetime = 50;
    this.alpha = 1f;
    this.xd = xSpeed;
    this.yd = ySpeed;
    this.zd = zSpeed;
    this.hasPhysics = false;
    this.quadSize = 0.12f;
    this.rollAmount = 0.1f + random.nextFloat() * 0.1f; // randomized roll amount
    this.col1 = 0xffec4f;
    this.col2 = 0xbd6a22;
  }

  @Override
  protected void updateColour(float f) {
  }

  @Override
  protected void updateAlpha(float f) {
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_CULL;
  }

  @Override
  public FacingCameraMode getFacingCameraMode() {
    return RootsParticle.FACING_UP;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;
      if (this.age % 8 == 0 && f < 0.8f) {
        RootsParticleOptions opts = random.nextBoolean() ? RootsParticleOptions.builder(ModParticles.PETAL).color(col1, col2).build() : RootsParticleOptions.builder(ModParticles.PETAL).color(col2, col1).build();
        level.addParticle(opts, this.x, this.y, this.z, (random.nextDouble() - 0.5) * 0.01, (0 - random.nextDouble()) * 0.01, (random.nextDouble() - 0.5) * 0.01);
      }
    }
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    if (shouldRender()) {
      Quaternionf quaternionf = new Quaternionf();
      this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
      if (this.roll != 0.0F) {
        quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
      }

      this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new DandelionParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }

}
