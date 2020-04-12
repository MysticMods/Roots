package epicsquid.roots.library;

import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class StaffSpellInfo extends AbstractSpellInfo<ModifierInstanceList> {
  private ModifierInstanceList modifiers = new ModifierInstanceList();
  private long cooldownStart = -1;

  private StaffSpellInfo() {
  }

  @Nullable
  @Override
  public ModifierInstanceList getModifiers() {
    return modifiers;
  }

  public boolean onCooldown() {
    return cooldownLeft() > 0;
  }

  public long cooldownLeft() {
    if (cooldownStart == -1) {
      return -1;
    }

    return System.currentTimeMillis() - this.cooldownStart;
  }

  public int cooldown() {
    return (int) (cooldownLeft() / 1000);
  }

  public void use() {
    this.cooldownStart = System.currentTimeMillis();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = super.serializeNBT();
    result.setTag("m", modifiers.serializeNBT());
    result.setLong("l", cooldownStart);
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    super.deserializeNBT(nbt);
    this.modifiers = ModifierInstanceList.fromNBT(nbt.getTagList("m", Constants.NBT.TAG_COMPOUND));
    this.cooldownStart = nbt.getLong("l");
  }

  public static StaffSpellInfo fromNBT(NBTTagCompound tag) {
    StaffSpellInfo instance = new StaffSpellInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
