package mysticmods.roots.client.particle.screen.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.RandomSource;
import org.joml.Vector2d;

public abstract class ScreenParticle {
  protected final ClientLevel level;
  protected double xo;
  protected double yo;
  protected double x;
  protected double y;
  protected double xd;
  protected double yd;
  protected boolean removed;
  protected final RandomSource random = RandomSource.create();
  protected int age;
  protected int lifetime;
  protected float gravity;
  protected float rCol = 1.0F;
  protected float gCol = 1.0F;
  protected float bCol = 1.0F;
  protected float alpha = 1.0F;
  protected float roll;
  protected float oRoll;
  protected float friction = 0.98F;

  protected ScreenParticle(ClientLevel level, double x, double y) {
    this.level = level;
    this.setPos(x, y);
    this.xo = x;
    this.yo = y;
    this.lifetime = (int) (4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
  }

  public ScreenParticle(ClientLevel level, double x, double y, double xSpeed, double ySpeed) {
    this(level, x, y);
    this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
    this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.4F;
    double d0 = (Math.random() + Math.random() + 1.0) * 0.15F;
    double d1 = Math.sqrt(this.xd * this.xd + this.yd * this.yd);
    this.xd = this.xd / d1 * d0 * 0.4F;
    this.yd = this.yd / d1 * d0 * 0.4F + 0.1F;
  }

  public ScreenParticle setPower(float multiplier) {
    this.xd *= multiplier;
    this.yd = (this.yd + 0.1F) * (double) multiplier + 0.1F;
    return this;
  }

  public void setSpeed(double xd, double yd) {
    this.xd = xd;
    this.yd = yd;
  }

  public void setColor(float particleRed, float particleGreen, float particleBlue) {
    this.rCol = particleRed;
    this.gCol = particleGreen;
    this.bCol = particleBlue;
  }

  protected void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public void setLifetime(int particleLifeTime) {
    this.lifetime = particleLifeTime;
  }

  public int getLifetime() {
    return this.lifetime;
  }

  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      // Friction is applied in reverse
      this.yd = this.yd + 0.04 * (double) this.gravity;
      this.x += this.xd;
      this.y += this.yd;

      this.xd = this.xd * (double) this.friction;
      this.yd = this.yd * (double) this.friction;
    }
  }

  public abstract void render(VertexConsumer buffer, float partialTicks);

  public abstract ParticleRenderType getRenderType();

  @Override
  public String toString() {
    return this.getClass().getSimpleName()
        + ", Pos ("
        + this.x
        + ","
        + this.y
        + "), RGBA ("
        + this.rCol
        + ","
        + this.gCol
        + ","
        + this.bCol
        + ","
        + this.alpha
        + "), Age "
        + this.age;
  }

  public void remove() {
    this.removed = true;
  }

  public void setPos(double x, double y) {
    this.x = x;
    this.y = y;
  }

  protected int getLightColor(float partialTick) {
    // TODO:
    return 0xffffff;
  }

  public boolean isAlive() {
    return !this.removed;
  }

  public Vector2d getPos() {
    return new Vector2d(this.x, this.y);
  }
}
