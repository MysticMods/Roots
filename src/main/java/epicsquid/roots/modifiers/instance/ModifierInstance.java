package epicsquid.roots.modifiers.instance;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.modifier.Modifier;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.modifiers.ModifierType;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

public class ModifierInstance implements INBTSerializable<NBTTagCompound>, IModifier {
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
  public String getTranslationKey() {
    return modifier.getTranslationKey();
  }

  @Override
  public ItemStack getItem() {
    return modifier.getItem();
  }

  @Override
  public ItemStack getActualItem() {
    return modifier.getActualItem();
  }

  @Override
  public ModifierType getType () {
    return modifier.getType();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("m", modifier.getRegistryName().toString());
    tag.setBoolean("a", applied);
    tag.setBoolean("e", enabled);
    return tag;
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    this.modifier = ModifierRegistry.get(new ResourceLocation(tag.getString("m")));
    this.applied = tag.getBoolean("a");
    this.enabled = tag.getBoolean("e");
  }

  public static ModifierInstance fromNBT (NBTTagCompound tag) {
    ModifierInstance result = new ModifierInstance(null, false, false);
    result.deserializeNBT(tag);
    return result;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    if (!this.enabled || !this.applied) {
      return costs;
    }

    return modifier.apply(costs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModifierInstance that = (ModifierInstance) o;
    return applied == that.applied &&
        enabled == that.enabled &&
        modifier.equals(that.modifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modifier, applied, enabled);
  }
}
