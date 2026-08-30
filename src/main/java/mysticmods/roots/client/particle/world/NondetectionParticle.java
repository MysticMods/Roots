package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.particle.IParticleHolder;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

// TODO: This sucks
public class NondetectionParticle extends RootsParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;

  private static final float[][] colors = {
      convertColor(0xffe383),
      convertColor(0xffbd83),
      convertColor(0xffb4eb),
      convertColor(0x9da2ff),
      convertColor(0x9dfff9),
      convertColor(0xadff9d),
      convertColor(0xe7ff9d),
      convertColor(0x9db9ff),
      convertColor(0xffb69d),
      convertColor(0xff9dc4),
      convertColor(0x9dffa6),
      convertColor(0xc1ddff)
  };

  private final LivingEntity entity;
  private final int count = 48;
  private final TextureAtlasSprite[] sprites;
  private final int[] assignedColors;

  private int curIndex = 0;

  private static float[] convertColor(int color) {
    return new float[]{
        ((color >> 16) & 0xFF) / 255.0f,
        ((color >> 8) & 0xFF) / 255.0f,
        (color & 0xFF) / 255.0f
    };
  }

  protected NondetectionParticle(SpriteSet sprites, ClientLevel level, double x, double y, double z, LivingEntity entity, int c1, int c2) {
    super(level, x, y, z);
    this.sprites = new TextureAtlasSprite[count];
    this.assignedColors = new int[count];
    for (int i = 0; i < count; i++) {
      this.sprites[i] = sprites.get(random);
      this.assignedColors[i] = random.nextInt(colors.length);
    }
    this.entity = entity;
    this.lifetime = 100;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.028f;
    this.defaultAlpha = false;
    this.defaultColor = false;
    this.defaultLight = false;
    this.defaultMovement = false;
    this.defaultRoll = false;
    this.curIndex = 0;
    tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

  protected int getLightColor(double x, double y, double z, float partialTick) {
    BlockPos blockpos = BlockPos.containing(x, y, z);
    return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(this.level, blockpos) : 0;
  }

  @Override
  public void tick() {
    if (entity == null || entity.isRemoved()) {
      this.remove();
      return;
    }

    MobEffectInstance effect = entity.getEffect(ModEffects.NONDETECTION);
    if (effect == null) {
      this.remove();
      return;
    }

    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    this.x = entity.getX();
    this.y = entity.getY();
    this.z = entity.getZ();

    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }
    }
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    // Otherwise it won't render in first person
    return AABB.INFINITE;
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

  @Override
  protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
    if (entity == null || entity.isRemoved() || removed) {
      return;
    }

    if (!RenderTickHandler.isRenderingDelayedParticles()) {
      return;
    }

    Vec3 vec3 = camera.getPosition();

    double radius = 2.0f;
    double height = 1.0f;
    double anglePerShell = Math.PI * 2 / count;
    double angleOffset = Math.toRadians(entity.tickCount % 360);

    int newCount = count;

    for (int i = 0; i <= count; i++) {
      this.curIndex = i;
      double sin = Math.sin(angleOffset + i * anglePerShell);
      double cos = Math.cos(angleOffset + i * anglePerShell);

      double x = this.x + radius * sin;
      double y = this.y + height;
      double z = this.z + radius * cos;

      double xo = this.xo + radius * sin;
      double yo = this.yo + height;
      double zo = this.zo + radius * cos;

      float f = (float) (Mth.lerp(partialTicks, xo, x) - vec3.x());
      float f1 = (float) (Mth.lerp(partialTicks, yo, y) - vec3.y());
      float f2 = (float) (Mth.lerp(partialTicks, zo, z) - vec3.z());

      Quaternionf q = new Quaternionf(quaternion);
      /*      q.rotateZ(Mth.lerp(partialTicks, rolls[i][1], rolls[i][2]));*/

      float[] color = colors[assignedColors[i]];
      this.rCol = color[0];
      this.gCol = color[1];
      this.bCol = color[2];

      this.renderRotatedQuad(buffer, q, f, f1, f2, partialTicks);

      newCount--;
      if (newCount <= 0) {
        this.curIndex = 0;
        break;
      }
    }
  }

  @Override
  protected float getU0() {
    return getU0(curIndex);
  }

  @Override
  protected float getU1() {
    return getU1(curIndex);
  }

  @Override
  protected float getV0() {
    return getV0(curIndex);
  }

  @Override
  protected float getV1() {
    return getV1(curIndex);
  }

  protected float getU0(int i) {
    return this.sprites[i].getU0();
  }

  protected float getU1(int i) {
    return this.sprites[i].getU1();
  }

  protected float getV0(int i) {
    return this.sprites[i].getV0();
  }

  protected float getV1(int i) {
    return this.sprites[i].getV1();
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (!(entity instanceof LivingEntity living)) {
        return null;
      }

      IParticleHolder holder = IParticleHolder.getHolder(entity);
      if (holder == null) {
        return null;
      }

      Particle current = holder.getParticle(ModParticles.NONDETECTION.value());
      if (current != null) {
        current.setLifetime(100);
        return null;
      }

      var particle = new NondetectionParticle(sprite, level, x, y, z, living, type.color1(), type.color2());
      holder.setParticle(ModParticles.NONDETECTION.value(), particle);
      return particle;
    }
  }
}
