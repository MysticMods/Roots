package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.modifiers.ModifierContext;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.spell.info.storage.LibrarySpellStorage;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSpellDust extends ItemBase {
  public ItemSpellDust(String name) {
    super(name);
    this.hasSubtypes = true;
    this.setHasSubtypes(true);
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
    if (tab == this.getCreativeTab()) {
      for (SpellBase entry : SpellRegistry.spellRegistry.values()) {
        subItems.add(entry.getResult());
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
    if (tag.hasKey("staff") && tag.getBoolean("staff")) {
      StaffSpellInfo info = StaffSpellStorage.fromStack(stack).getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell == null) {
        return;
      }

      spell.addToolTip(tooltip, info.getModifiers());
    } else if (tag.hasKey("library") && tag.getBoolean("library")) {
      LibrarySpellInfo info = LibrarySpellStorage.fromStack(stack).getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell == null) {
        return;
      }

      spell.addToolTip(tooltip, info.getModifiers());
    } else {
      SpellDustInfo info = DustSpellStorage.fromStack(stack).getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell == null) {
        return;
      }

      spell.addToolTip(tooltip, (StaffModifierInstanceList) null);
    }
  }
}