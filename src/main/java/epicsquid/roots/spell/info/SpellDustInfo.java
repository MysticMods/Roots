package epicsquid.roots.spell.info;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SpellDustInfo extends AbstractSpellInfo {
  public static SpellDustInfo EMPTY = new SpellDustInfo();

  public SpellDustInfo() {
  }

  public SpellDustInfo(SpellBase spell) {
    super(spell);
  }

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  @Override
  public ItemStack asStack() {
    ItemStack stack = new ItemStack(ModItems.spell_icon);
    DustSpellStorage storage = DustSpellStorage.fromStack(stack);
    storage.addSpell(this);
    return stack;
  }

  public StaffSpellInfo toStaff() {
    SpellBase spell = getSpell();
    if (spell != null) {
      return new StaffSpellInfo(spell);
    } else {
      return StaffSpellInfo.EMPTY;
    }
  }

  public static SpellDustInfo fromNBT(NBTTagCompound tag) {
    SpellDustInfo instance = new SpellDustInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
