package epicsquid.roots.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class ModifierInstance implements INBTSerializable<NBTTagCompound> {
  private Modifier modifier;
  private boolean applied;
  private boolean enabled;

  public ModifierInstance(Modifier modifier, boolean applied, boolean enabled) {
    this.modifier = modifier;
    this.applied = applied;
    this.enabled = enabled;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public boolean isApplied() {
    return applied;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("mod", modifier.getRegistryName().toString());
    tag.setBoolean("app", applied);
    tag.setBoolean("en", enabled);
    return tag;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {

  }
}
