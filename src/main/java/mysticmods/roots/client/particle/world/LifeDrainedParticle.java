package mysticmods.roots.client.particle.world;

import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.VecUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LifeDrainedParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollAmount;
  private final Entity entity;
  private final Vec3 startPos, a, b;
  private Vec3 lastStop;

  protected LifeDrainedParticle(ClientLevel level, double x, double y, double z, int c1, int c2, Entity entity) {
    super(level, x, y, z);
    this.entity = entity;
    this.startPos = new Vec3(x, y, z);
    Vec3 stop = entity.getPosition(0f);
    Vec3 mid = VecUtil.midpoint(startPos, stop).subtract(startPos).yRot((float) Math.PI / 2);
    this.a = new Vec3(startPos.x - (stop.x - startPos.x) / 3.0, startPos.y - (stop.y - startPos.y) / 3.0, startPos.z - (stop.z - startPos.z) / 3.0).add(mid);
    this.b = new Vec3(stop.x + (stop.x - startPos.x) / 3.0 * 2.0, stop.y + (stop.y - startPos.y) / 3.0 * 2.0, stop.z + (stop.z - startPos.z) / 3.0 * 2.0).add(mid);

    this.lifetime = 60;
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
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.195f;
    this.gravity = 0.01f;
    this.tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime || entity == null || entity.isRemoved()) {
      this.remove();
    } else {
      float f = (float) this.age / (float) this.lifetime;

      Vec3 stop = entity.getPosition(0f);
      Vec3 bezier = VecUtil.bezier(startPos, a, b, stop, f);

      this.x = bezier.x;
      this.y = bezier.y;
      this.z = bezier.z;

      // Color lerp
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      // Roll and fade
      this.oRoll = this.roll;
      this.roll += this.rollAmount;
      this.alpha = 1f - f * f * f;
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      var particle = new LifeDrainedParticle(level, x, y, z, type.color1(), type.color2(), entity);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
