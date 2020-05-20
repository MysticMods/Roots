package epicsquid.roots.spell.info;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.modifiers.modifier.ModifierList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Objects;

public class StaffSpellInfo extends AbstractSpellModifiers<ModifierInstanceList> {
  public static StaffSpellInfo EMPTY = new StaffSpellInfo();
  private int cooldown = -1;
  private long cooldownStop = -1;

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

  public void tick () {
    if (this.cooldown == -1) {
      return;
    }
    this.cooldown--;
    if (this.cooldown <= 0) {
      this.cooldown = -1;
    }
  }

  public boolean onCooldown() {
    return this.cooldown != -1;
  }

  public int cooldownLeft() {
    return cooldown;
  }

  public int cooldownTotal() {
    return spell.getCooldown();
  }

  public void use(long cd) {
    this.cooldown = cooldownTotal();
    this.cooldownStop = cd + cooldown;
  }

  public void validate (long cd) {
    if (this.cooldown != -1 && cd > this.cooldownStop) {
      this.cooldown = -1;
    }
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
    this.modifiers = ModifierInstanceList.fromNBT(nbt.getTagList("m", Constants.NBT.TAG_COMPOUND));
    this.cooldown = nbt.getInteger("c");
    this.cooldownStop = nbt.getLong("l");
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

  public ItemStack asStack () {
    ItemStack stack = new ItemStack(ModItems.spell_dust);
    NBTTagCompound comp = ItemUtil.getOrCreateTag(stack);
    comp.setBoolean("staff", true);
    StaffSpellStorage storage = StaffSpellStorage.fromStack(stack);
    storage.setSpellToSlot(this);
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
