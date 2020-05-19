package epicsquid.roots.spell.info;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.modifiers.modifier.ModifierList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Objects;

public class StaffSpellInfo extends AbstractSpellModifiers<ModifierInstanceList> {
  public static StaffSpellInfo EMPTY = new StaffSpellInfo();
  private long cooldownStart = -1;

  public StaffSpellInfo() {
    modifiers = new ModifierInstanceList();
  }

  public StaffSpellInfo(SpellBase spell) {
    super(spell);
    modifiers = new ModifierInstanceList(spell);
  }

  @Nullable
  @Override
  public ModifierInstanceList getModifiers() {
    return modifiers;
  }

  public void setModifiers(ModifierInstanceList modifiers) {
    this.modifiers = modifiers;
  }

  public void setModifiers (ModifierList modifiers) {
    this.modifiers = new ModifierInstanceList(modifiers);
  }

  public boolean onCooldown() {
    return cooldown() > 0;
  }

  public int cooldownLeft() {
    if (cooldownStart == -1) {
      return -1;
    }

    int internal = (int) ((System.currentTimeMillis() - this.cooldownStart) / 1000);

    if (internal > spell.getCooldown() || internal < 0) {
      this.cooldownStart = -1;
      return -1;
    } else {
      return internal;
    }
  }

  public int cooldown () {
    return spell.getCooldown();
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

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  public static StaffSpellInfo fromNBT(NBTTagCompound tag) {
    StaffSpellInfo instance = new StaffSpellInfo();
    instance.deserializeNBT(tag);
    return instance;
  }

  @Nullable
  public static StaffSpellInfo fromRegistry (String name) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    SpellBase spell = SpellRegistry.getSpell(rl);
    if (spell == null) {
      return null;
    }
    return new StaffSpellInfo(spell);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StaffSpellInfo that = (StaffSpellInfo) o;
    return cooldownStart == that.cooldownStart &&
        getModifiers() != null && getModifiers().equals(that.getModifiers());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getModifiers(), cooldownStart);
  }
}
