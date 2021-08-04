package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.spell.info.storage.LibrarySpellStorage;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (entry.isDisabled()) {
          continue;
        }
        subItems.add(entry.getResult());
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
    if (tag.hasKey("staff") && tag.getBoolean("staff")) {
      StaffSpellStorage storage = StaffSpellStorage.fromStack(stack);
      if (storage == null) {
        return;
      }
      StaffSpellInfo info = storage.getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell == null) {
        return;
      }

      spell.addToolTip(tooltip, info.getModifiers());
    } else if (tag.hasKey("library") && tag.getBoolean("library")) {
      LibrarySpellStorage storage = LibrarySpellStorage.fromStack(stack);
      if (storage == null) {
        return;
      }
      LibrarySpellInfo info = storage.getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell == null) {
        return;
      }

      spell.addToolTip(tooltip, info.getModifiers());
    } else {
      DustSpellStorage storage = DustSpellStorage.fromStack(stack);
      if (storage == null) {
        return;
      }
      SpellDustInfo info = storage.getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell == null) {
        return;
      }

      spell.addToolTip(tooltip, (StaffModifierInstanceList) null);
    }
  }
}