package epicsquid.roots.entity.grove;

import epicsquid.roots.grove.GroveType;
import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityGrove extends Entity {

  private float r, g, b;
  private GroveType type;

  public EntityGrove(World worldIn) {
    super(worldIn);
    this.r = 255;
    this.b = 255;
    this.g = 255;
    this.type = GroveType.NATURAL;
  }

  public EntityGrove(World worldIn, float r, float g, float b, GroveType type) {
    super(worldIn);
    this.r = r;
    this.b = b;
    this.g = g;
    this.type = type;
  }

  @Override
  protected void entityInit() {

  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {

  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {

  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (world.isRemote) {
      ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, r, g, b, 0.2f, 20.0f, 40);
    }
  }
}
