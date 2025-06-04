package mysticmods.roots.client.particle.screen;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.client.particle.screen.base.RootsScreenParticle;
import mysticmods.roots.client.particle.screen.base.TextureSheetScreenParticle;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.VecUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class Desaturate extends RootsScreenParticle {
  private final Vector2f startPos, stopPos, control1, control2;

  protected Desaturate(ClientLevel level, RootsParticleOptions options, double x, double y, double xSpeed, double ySpeed) {
    super(level, options, x, y, xSpeed, ySpeed);
    this.lifetime = 60;
    this.quadSize = 4f;
    this.oRoll = this.roll = (float) Math.toRadians(180);
    this.defaultMovement = false;
    this.startPos = new Vector2f((float) x, (float) y);
    this.stopPos = new Vector2f((float) xSpeed, (float) ySpeed);
    Vector2f delta = new Vector2f(this.stopPos).sub(this.startPos);

    this.rollAmount = (1f - Mth.clamp(delta.length() / 100f, 0f, 1f)) * random.nextFloat() * 0.2f;

    this.control1 = new Vector2f(startPos.x - delta.x * 0.1f, startPos.y - 30f);
    this.control2 = new Vector2f(stopPos.x + delta.x * 0.25f, stopPos.y - 1f);
  }

  @Override
  protected void particleTick(float f) {
    super.particleTick(f);

    Vector2f curvePoint = VecUtil.bezier(
        this.startPos, this.control1, this.control2, this.stopPos, f
    );
    this.x = curvePoint.x;
    this.y = curvePoint.y;
  }

  @Override
  protected void updateQuadSize(float f) {
    f = f * f * f;
    this.quadSize = 4f * (1f - f);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

  public static class Provider implements ScreenParticleProvider<RootsParticleOptions> {
    @Override
    public @Nullable TextureSheetScreenParticle createParticle(SpriteSet sprites, RootsParticleOptions type, ClientLevel level, double x, double y, double xSpeed, double ySpeed) {
      Desaturate particle = new Desaturate(level, type, x, y, xSpeed, ySpeed);
      particle.pickSprite(sprites);
      return particle;
    }
  }
}
