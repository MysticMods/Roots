package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class RootsParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bCol2;
  protected float rollAmount;
  protected boolean defaultLight = true;
  protected boolean defaultMovement = true;
  protected boolean delayedRender = true;
  protected boolean defaultAlpha = true;
  protected boolean defaultRoll = true;
  protected boolean defaultColor = true;

  protected boolean fastForwarding = false;

  protected RootsParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }

  protected RootsParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    unwrapColor(c1, c2);
  }

  protected RootsParticle(ClientLevel level, double x, double y, double z) {
    super(level, x, y, z);
  }

  protected RootsParticle(ClientLevel level, double x, double y, double z, int c1, int c2) {
    this(level, x, y, z);
    unwrapColor(c1, c2);
  }

  protected void unwrapColor(int c1, int c2) {
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bCol2 = ((c2) & 0xFF) / 255.0f;
  }

  protected void unwrapColor(int c1) {
    this.rCol = this.rCol2 = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.gCol2 = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.bCol2 = this.oB1 = ((c1) & 0xFF) / 255.0f;
  }

  protected void updateQuadSize(float f) {

  }

  protected void updateAlpha(float f) {
    if (defaultAlpha) {
      this.alpha = 1f - f;
    }
  }

  protected void updateRoll(float f) {
    if (defaultRoll) {
      this.oRoll = this.roll;
      this.roll = this.roll + this.rollAmount;
    }
  }

  protected void updateColour(float f) {
    if (defaultColor) {
      if (this.oB1 != this.bCol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bCol2 - this.oB1) * f;
      }
    }
  }

  protected void updateSprite(float f) {
  }

  protected void updateMovement(float f) {
    if (defaultMovement) {
      this.yd = this.yd - 0.04 * (double) this.gravity;
      this.move(this.xd, this.yd, this.zd);
      if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
        this.xd *= 1.1;
        this.zd *= 1.1;
      }

      this.xd = this.xd * (double) this.friction;
      this.yd = this.yd * (double) this.friction;
      this.zd = this.zd * (double) this.friction;
      if (this.onGround) {
        this.xd *= 0.7F;
        this.zd *= 0.7F;
      }
    }
  }

  protected float generateF() {
    return (float) this.age / (float) this.lifetime;
  }

  protected boolean isDelayedRender() {
    if (!ConfigManager.DELAYED_PARTICLES.get()) {
      return false;
    }
    ParticleRenderType type = this.getRenderType();
    if (type instanceof RootsParticleRenderTypes.RootsParticleRenderType rootsType) {
      return delayedRender && rootsType.isDelayed();
    } else {
      return false;
    }
  }

  protected boolean shouldRender() {
    return RenderTickHandler.isRenderingDelayedParticles() || !isDelayedRender();
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    if (shouldRender()) {
      super.render(buffer, renderInfo, partialTicks);
    }
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      float f = generateF();
      updateMovement(f);
      updateColour(f);
      updateAlpha(f);
      updateRoll(f);
      updateQuadSize(f);
      updateSprite(f);
      particleTick(f);
    }
  }

  public void fastForward(int ticks) {
    if (ticks > 0) {
      fastForwarding = true;
      for (int i = 0; i < ticks; i++) {
        this.tick();
      }
      fastForwarding = false;
    }
  }

  protected void particleTick(float f) {

  }

  @Override
  protected int getLightColor(float partialTick) {
    if (defaultLight) {
      return 0xf000f0 | getLightColorRaw(partialTick) & 0xff0000;
    } else {
      return getLightColorRaw(partialTick);
    }
  }

  protected int getLightColorRaw(float partialTick) {
    BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
    return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(this.level, blockpos) : 0;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.OPAQUE;
  }

  public static final FacingCameraMode FACING_UP = (quaternion, camera, partialTick) -> {
    quaternion.rotationX((float) Math.PI / 2);
  };

  public static final FacingCameraMode BILLBOARD_TILTED = (quaternion, camera, partialTick) -> {
    quaternion.set(camera.rotation());
    quaternion.rotateX((float) Math.toRadians(45));
  };

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    protected ParticleBuilder builder1 = null;
    protected ParticleBuilderSimple builder2 = null;
    protected SpriteSet sprites;

    public Provider(ParticleBuilder builder1, SpriteSet sprites) {
      this.builder1 = builder1;
      this.sprites = sprites;
    }

    public Provider(ParticleBuilderSimple builder2, SpriteSet sprites) {
      this.builder2 = builder2;
      this.sprites = sprites;
    }

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public @Nullable Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      if (builder1 == null && builder2 == null) {
        return null;
      }
      RootsParticle particle;
      if (builder1 != null) {
        particle = builder1.create(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      } else {
        particle = builder2.create(level, x, y, z, type.color1(), type.color2());
      }
      particle.pickSprite(sprites);
      if (type.fastForward() != -1) {
        particle.fastForward(type.fastForward());
      }
      return particle;
    }
  }

  @FunctionalInterface
  public interface ParticleBuilder {
    RootsParticle create(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2);
  }

  @FunctionalInterface
  public interface ParticleBuilderSimple {
    RootsParticle create(ClientLevel level, double x, double y, double z, int c1, int c2);
  }
}
