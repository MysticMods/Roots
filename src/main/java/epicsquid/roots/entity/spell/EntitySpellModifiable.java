package epicsquid.roots.entity.spell;

import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class EntitySpellModifiable<T extends SpellBase> extends Entity {
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntitySpellModifiable.class, DataSerializers.VARINT);
  protected UUID playerId = null;
  protected T instance;
  protected ISnapshot modifiers;

  public EntitySpellModifiable(World worldIn, T instance, int duration) {
    super(worldIn);
    this.instance = instance;
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, duration);
  }

  public void setModifiers(StaffModifierInstanceList modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  @Override
  protected void entityInit() {
  }

  @Override
  public void onUpdate() {
    //super.onUpdate();
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) <= 0) {
      onDeath();
      setDead();
    }
  }

  public void onDeath () {
  }

  public int getLifetime() {
    return getDataManager().get(lifetime);
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.playerId = compound.getUniqueId("playerId");
    this.modifiers = new ModifierSnapshot(compound.getIntArray("modifiers"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setUniqueId("playerId", playerId);
    compound.setIntArray("modifiers", this.modifiers.toArray());
  }
}
