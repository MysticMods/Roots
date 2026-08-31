package mysticmods.roots.client.particle.world.block;

import mysticmods.roots.api.reference.Constants;
import mysticmods.roots.client.particle.world.RootsItemParticle;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.VecUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

public class GroveCrafterItemParticle extends RootsItemParticle {
  private final Vec3 origin, destination, control1, control2;
  private final int delay;

  protected GroveCrafterItemParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack item, int delay) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, item);
    this.delay = delay;
    Vec3 start = new Vec3(x, y, z);
    this.lifetime = Constants.GROVE_CRAFTING_ANIMATION_TICKS;
    this.quadSize = 0.025f + random.nextFloat() * 0.01f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.origin = new Vec3(x + (random.nextDouble() - 0.5) * 0.08, y + (random.nextDouble() - 0.5) * 0.08, z + (random.nextDouble() - 0.5) * 0.08);
    this.destination = new Vec3(xSpeed, ySpeed, zSpeed);
    this.delayedRender = false;

    this.defaultMovement = false;
    this.defaultAlpha = false;
    this.defaultColor = false;

    Vec3 diff = this.destination.subtract(this.origin);
    Vec3 up = new Vec3(0, 1, 0);

    double height = Math.max(0.25, diff.length() * 0.2);

    // TODO: Gaussians -> weird?
    Vec3 randomOffset = new Vec3(
        (random.nextFloat() - 0.5) * 0.2,
        (random.nextFloat() - 0.5) * 0.1,
        (random.nextFloat() - 0.5) * 0.2
    );

    this.control1 = origin.add(diff.scale(0.2)).add(up.scale(height * 2.5)).add(randomOffset);
    this.control2 = origin.add(diff.scale(0.6)).add(up.scale(height)).subtract(randomOffset);
    this.defaultLight = false;

    Vec3 initMotionDiff = origin.subtract(start);
    Vec3 motion = initMotionDiff.scale(1.0 / 6.0);

    this.xd = motion.x;
    this.yd = motion.y;
    this.zd = motion.z;
    this.alpha = 0f;

    if (item.getItem() instanceof BlockItem blockItem) {
      BlockState state = blockItem.getBlock().defaultBlockState();
      this.rCol = 0.6f;
      this.bCol = 0.6f;
      this.gCol = 0.6f;
      if (IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, level, BlockPos.containing(origin))) {
        int i = Minecraft.getInstance().getBlockColors().getColor(state, level, BlockPos.containing(origin), 0);
        this.rCol *= (float) (i >> 16 & 0xFF) / 255.0F;
        this.gCol *= (float) (i >> 8 & 0xFF) / 255.0F;
        this.bCol *= (float) (i & 0xFF) / 255.0F;
      }
    }
  }

  @Override
  protected void updateAlpha(float f) {
    if (this.age < Constants.GROVE_PARTICLE_PEDESTAL_FADE_IN_START) {
      this.alpha = 0f;
    } else if (this.age <= Constants.GROVE_PARTICLE_PEDESTAL_DELAY) {
      // Fade from 0 to 1 over
      float f2 = (float) (this.age - Constants.GROVE_PARTICLE_PEDESTAL_FADE_IN_START) / Constants.GROVE_PARTICLE_PEDESTAL_FADE_IN_TICKS;
      this.alpha = Mth.lerp(f2, 0f, 1f);
    }
  }

  @Override
  protected void updateMovement(float f) {
    if (this.age < Constants.GROVE_PARTICLE_PEDESTAL_DELAY) {
      // Deliberate pause before expanding from spawn position
    } else if (this.age <= Constants.GROVE_PARTICLE_BEZIER_BEGIN) {
      this.x += this.xd;
      this.y += this.yd;
      this.z += this.zd;
    } else if (this.age <= Constants.GROVE_PARTICLE_BEZIER_BEGIN + this.delay) {
      // Deliberate pause after expanding from spawn position
    } else if (this.age > (lifetime - Constants.GROVE_PARTICLE_PAUSE_TICKS)) {
      // Deliberate pause after arriving at Grove Crafter
    } else {
      int bezierAge = this.age - Constants.GROVE_PARTICLE_BEZIER_BEGIN - delay;
      int bezierDuration = this.lifetime - Constants.GROVE_PARTICLE_BEZIER_SHORTENING - delay;
      float t = (float) bezierAge / bezierDuration;
      f = (float) Math.pow(t, 1.5);
      Vec3 pos = VecUtil.bezier(origin, control1, control2, destination, f);
      this.x = pos.x;
      this.y = pos.y;
      this.z = pos.z;
    }
  }

  public record Provider() implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new GroveCrafterItemParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.item(), type.delay());
    }
  }

}
