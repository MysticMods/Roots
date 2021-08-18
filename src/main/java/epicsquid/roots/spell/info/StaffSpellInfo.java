package epicsquid.roots.spell.info;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.FakeSpell;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

public class StaffSpellInfo extends AbstractSpellModifiers<StaffModifierInstanceList> {
  public static StaffSpellInfo EMPTY = new StaffSpellInfo(FakeSpell.INSTANCE);
  private int cooldown = -1;
  private long cooldownStop = -1;

  public StaffSpellInfo(SpellBase spell) {
    super(spell);
    modifiers = new StaffModifierInstanceList(spell);
  }

  @Override
  public StaffModifierInstanceList getModifiers() {
    return modifiers;
  }

  @Override
  public void setModifiers(StaffModifierInstanceList modifiers) {
    this.modifiers = modifiers;
  }

  public boolean tick() {
    if (this.cooldown == -1) {
      return false;
    }
    this.cooldown--;
    if (this.cooldown <= 0) {
      this.cooldown = -1;
    }
    return true;
  }

  public boolean onCooldown() {
    return this.cooldown != -1;
  }

  public int cooldownLeft() {
    return cooldown;
  }

  public int cooldownTotal() {
    SpellBase spell = getSpell();
    if (spell != null) {
      return spell.getCooldown();
    } else {
      return 0;
    }
  }

  public void use(long cd) {
    this.cooldown = cooldownTotal();
    this.cooldownStop = cd + cooldown;
  }

  public boolean validate(long cd) {
    if (this.cooldown != -1 && cd > this.cooldownStop) {
      this.cooldown = -1;
      return true;
    }
    return false;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = super.serializeNBT();
    result.setTag("m", modifiers.serializeNBT());
    result.setInteger("c", cooldown);
    result.setLong("l", cooldownStop);
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    super.deserializeNBT(nbt);
    this.modifiers = StaffModifierInstanceList.fromNBT(nbt.getCompoundTag("m"));
    this.cooldown = nbt.getInteger("c");
    this.cooldownStop = nbt.getLong("l");
  }

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  @Nullable
  public static StaffSpellInfo fromNBT(NBTTagCompound tag) {
    SpellBase spell = getSpellFromTag(tag);
    if (spell == null) {
      return null;
    }
    StaffSpellInfo instance = new StaffSpellInfo(spell);
    instance.deserializeNBT(tag);
    return instance;
  }

  @Nullable
  public static StaffSpellInfo fromRegistry(String name) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    SpellBase spell = SpellRegistry.getSpell(rl);
    if (spell == null) {
      return null;
    }
    return new StaffSpellInfo(spell);
  }

  public static StaffSpellInfo fromLibrary(LibrarySpellInfo incoming) {
    SpellBase spell = incoming.getSpell();
    if (spell != null) {
      StaffSpellInfo info = new StaffSpellInfo(spell);
      info.setModifiers(incoming.getModifiers().toStaff());
      return info;
    } else {
      return StaffSpellInfo.EMPTY;
    }
  }

  public static StaffSpellInfo fromSpell(SpellBase spell, boolean modifiers) {
    StaffSpellInfo info = new StaffSpellInfo(spell);
    if (modifiers) {
      for (StaffModifierInstance modifier : info.getModifiers()) {
        modifier.setApplied();
      }
    }
    return info;
  }

  public LibrarySpellInfo toLibrary() {
    SpellBase spell = getSpell();
    if (spell != null) {
      LibrarySpellInfo info = new LibrarySpellInfo(spell);
      info.setObtained();
      info.setModifiers(getModifiers().toLibrary());
      return info;
    } else {
      return LibrarySpellInfo.EMPTY;
    }
  }

  @Override
  public ItemStack asStack() {
    ItemStack stack = new ItemStack(ModItems.spell_icon);
    NBTTagCompound comp = ItemUtil.getOrCreateTag(stack);
    comp.setBoolean("staff", true);
    StaffSpellStorage storage = StaffSpellStorage.fromStack(stack);
    storage.addSpell(this);
    storage.setSelectedSlot(1);
    storage.saveToStack();
    return stack;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StaffSpellInfo that = (StaffSpellInfo) o;
    return cooldown == that.cooldown;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cooldown);
  }
}
