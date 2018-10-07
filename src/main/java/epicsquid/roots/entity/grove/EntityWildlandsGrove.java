package epicsquid.roots.entity.grove;

import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWildlandsGrove extends Entity {

  public EntityWildlandsGrove(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
  }

  @Override
  protected void entityInit() {

  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (world.isRemote) {
      ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 100, 255, 100, 0.2f, 20.0f, 40);
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {

  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {

  }


}
