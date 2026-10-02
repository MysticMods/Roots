package mysticmods.roots.client.particle.screen;

import mysticmods.roots.client.particle.screen.base.RootsScreenParticle;
import mysticmods.roots.client.particle.screen.base.TextureSheetScreenParticle;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class RankScreenParticle extends RootsScreenParticle {
  private static final float RADIUS = 0.3f;
  private static final float ANGULAR_SPEED = 0.1f;
  private final float angleOffset;

  private final double startX, startY;
  private final int rank;

  protected RankScreenParticle(ClientLevel level, RootsParticleOptions options, double x, double y, double xSpeed, double ySpeed, int rank) {
    super(level, options, x, y, 0.0, 0.0);
    this.lifetime = 50000;
    this.startX = xSpeed;
    this.startY = ySpeed;
    this.rank = rank;
    this.angleOffset = (float) Math.toRadians(0) * Mth.TWO_PI;
    this.defaultMovement = false;
    this.perpetual = true;
    this.quadSize = 11f;

    this.updateSpiralPosition();
  }

  private void updateSpiralPosition() {
    float angle = this.angleOffset + this.age * ANGULAR_SPEED;
    this.setPos(
        this.startX + Mth.cos(angle) * RADIUS,
        this.startY + Mth.sin(angle) * RADIUS
    );
  }

  @Override
  protected void particleTick(float f) {
    super.particleTick(f);
    updateSpiralPosition();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class Provider implements ScreenParticleProvider<RootsParticleOptions> {
    @Override
    public @Nullable TextureSheetScreenParticle createParticle(SpriteSet sprites, RootsParticleOptions type, ClientLevel level, double x, double y, double xSpeed, double ySpeed) {
      RankScreenParticle particle = new RankScreenParticle(level, type, x, y, xSpeed, ySpeed, 2);
      particle.pickSprite(sprites);
      return particle;
    }
  }
}
