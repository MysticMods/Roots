package epicsquid.roots.entity.spell;

import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellWildfire;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public abstract class EntitySpellModifiable<T extends SpellBase> extends Entity {
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityFireJet.class, DataSerializers.VARINT);
  protected UUID playerId = null;
  protected double amplifier;
  protected double speedy;
  protected T instance;
  protected StaffModifierInstanceList modifiers;

  public EntitySpellModifiable(World worldIn, T instance) {
    super(worldIn);
    this.instance = instance;
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, 12);
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

  public double getAmplifier() {
    return amplifier;
  }

  @Override
  public void onUpdate() {
    //super.onUpdate();
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) <= 0) {
      setDead();
    }
  }

  public void setAmplifier(double amplifier) {
    this.amplifier = amplifier;
  }

  public double getSpeedy() {
    return speedy;
  }

  public void setSpeedy(double speedy) {
    this.speedy = speedy;
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.playerId = compound.getUniqueId("playerId");
    if (compound.hasKey("modifiers", Constants.NBT.TAG_COMPOUND)) {
      this.modifiers = StaffModifierInstanceList.fromNBT(compound.getCompoundTag("modifiers"));
    } else {
      this.modifiers = null;
    }
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setUniqueId("playerId", playerId);
    if (this.modifiers != null) {
      compound.setTag("modifiers", this.modifiers.serializeNBT());
    }
  }
}
