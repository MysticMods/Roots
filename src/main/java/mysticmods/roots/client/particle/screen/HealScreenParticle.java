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

public class HealScreenParticle extends RootsScreenParticle {
  protected HealScreenParticle(ClientLevel level, RootsParticleOptions options, double x, double y, double xSpeed, double ySpeed) {
    super(level, options, x, y, xSpeed, ySpeed);
    this.xd = xSpeed;
    this.yd = ySpeed;
    this.lifetime = 65;
    this.quadSize = 4f;
    this.oRoll = this.roll = (float) Math.toRadians(180);
    this.defaultAlpha = false;
  }

  @Override
  protected void updateQuadSize(float f) {
    f = f * f * f;
    this.quadSize = 4f * (1f - f);
  }

  @Override
  protected void particleTick(float f) {
    if (f > 0.1f && this.yd < 0) {
      this.yd *= 0.8f;
    }
    if (f > 0.2f && this.yd < 0) {
      this.gravity = 0.4f;
      this.yd = 0.2f;
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

  public static class Provider implements ScreenParticleProvider<RootsParticleOptions> {
    @Override
    public @Nullable TextureSheetScreenParticle createParticle(SpriteSet sprites, RootsParticleOptions type, ClientLevel level, double x, double y, double xSpeed, double ySpeed) {
      HealScreenParticle particle = new HealScreenParticle(level, type, x, y, xSpeed, ySpeed);
      particle.pickSprite(sprites);
      return particle;
    }
  }
}
