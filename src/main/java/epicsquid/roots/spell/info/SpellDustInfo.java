package epicsquid.roots.spell.info;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.spell.info.storage.LibrarySpellStorage;
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
    ItemStack stack = new ItemStack(ModItems.spell_dust);
    DustSpellStorage storage = DustSpellStorage.fromStack(stack);
    storage.setSpellToSlot(this);
    return stack;
  }

  public StaffSpellInfo toStaff () {
    return new StaffSpellInfo(getSpell());
  }

  public static SpellDustInfo fromNBT(NBTTagCompound tag) {
    SpellDustInfo instance = new SpellDustInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
