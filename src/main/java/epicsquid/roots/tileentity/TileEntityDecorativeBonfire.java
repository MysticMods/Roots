package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.util.ITickable;

public class TileEntityDecorativeBonfire extends TileBase implements ITickable {

  private boolean doBigFlame = true;

  public TileEntityDecorativeBonfire() {
    super();
  }

  @Override
  public void update() {
    //Spawn the Ignite flame particle
    if (world.isRemote && this.doBigFlame) {
      for (int i = 0; i < 40; i++) {
        ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.125f + 0.75f * Util.rand.nextFloat(), getPos().getY() + 0.75f + 0.5f * Util.rand.nextFloat(),
            getPos().getZ() + 0.125f + 0.75f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(),
            0.03125f * (Util.rand.nextFloat() - 0.5f), 255.0f, 224.0f, 32.0f, 0.75f, 9.0f + 9.0f * Util.rand.nextFloat(), 40);
      }
      doBigFlame = false;
    }
    //Spawn Fire particles
    if (world.isRemote) {
      for (int i = 0; i < 2; i++) {
        ParticleUtil
            .spawnParticleFiery(world, getPos().getX() + 0.3125f + 0.375f * Util.rand.nextFloat(), getPos().getY() + 0.625f + 0.375f * Util.rand.nextFloat(),
                getPos().getZ() + 0.3125f + 0.375f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(),
                0.03125f * (Util.rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 0.75f, 7.0f + 7.0f * Util.rand.nextFloat(), 40);
      }
    }
  }
}