package mysticmods.roots.client.particle.world;

import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.VecUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RootsItemParticle extends RootsParticle {
  private final Vec3 origin, destination, control1, control2;
  private final float uo, vo;

  protected RootsItemParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack item) {
    super(level, x, y, z);
    Vec3 start = new Vec3(x, y, z);
    this.lifetime = 20 * 5;
    this.quadSize = 0.025f + random.nextFloat() * 0.01f;
    this.origin = new Vec3(x + (random.nextDouble() - 0.5) * 0.4, y + (random.nextDouble() - 0.5) * 0.4, z + (random.nextDouble() - 0.5) * 0.4);
    this.destination = new Vec3(xSpeed, ySpeed, zSpeed);
    this.delayedRender = false;
    if (item == null) {
      throw new IllegalArgumentException("ItemStack cannot be null for particle");
    }
    var model = Minecraft.getInstance().getItemRenderer().getModel(item, level, null, 0);
    this.setSprite(model.getOverrides().resolve(model, item, level, null, 0)
        .getParticleIcon(net.neoforged.neoforge.client.model.data.ModelData.EMPTY));
    this.rollAmount = random.nextFloat() * 0.1f;
    this.defaultMovement = false;
    this.defaultAlpha = false;
    this.defaultColor = false;
    this.uo = this.random.nextFloat() * 3.0F;
    this.vo = this.random.nextFloat() * 3.0F;
    Vec3 diff = this.destination.subtract(this.origin);
    Vec3 up = new Vec3(0, 1, 0);

    double height = Math.max(0.25, diff.length() * 0.2);

    Vec3 randomOffset = new Vec3(
        (random.nextGaussian() - 0.5) * 0.2,
        (random.nextFloat() - 0.5) * 0.1,
        (random.nextGaussian() - 0.5) * 0.2
    );

    this.control1 = origin.add(diff.scale(0.2)).add(up.scale(height * 2.5)).add(randomOffset);
    this.control2 = origin.add(diff.scale(0.6)).add(up.scale(height)).subtract(randomOffset);
    this.defaultLight = false;

    Vec3 motion = origin.subtract(start).normalize().scale(0.05);
    this.xd = motion.x;
    this.yd = motion.y;
    this.zd = motion.z;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.TERRAIN_SHEET;
  }

  @Override
  protected void updateAlpha(float f) {
    super.updateAlpha(f);
  }

  @Override
  protected void updateMovement(float f) {
    if (this.age > (lifetime - 10)) {

    } else {
      f = (float) Math.pow((float) this.age / (this.lifetime - 10), 1.5);
      Vec3 pos = VecUtil.bezier(origin, control1, control2, destination, f);
      this.x = pos.x;
      this.y = pos.y;
      this.z = pos.z;
    }
  }

  @Override
  protected float getU0() {
    return this.sprite.getU((this.uo + 1.0F) / 4.0F);
  }

  @Override
  protected float getU1() {
    return this.sprite.getU(this.uo / 4.0F);
  }

  @Override
  protected float getV0() {
    return this.sprite.getV(this.vo / 4.0F);
  }

  @Override
  protected float getV1() {
    return this.sprite.getV((this.vo + 1.0F) / 4.0F);
  }

  public record Provider() implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new RootsItemParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.item());
    }
  }
}
