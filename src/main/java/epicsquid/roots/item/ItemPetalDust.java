package epicsquid.roots.item;

import java.util.List;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPetalDust extends ItemBase {
  public ItemPetalDust(String name) {
    super(name);
    this.hasSubtypes = true;
    this.setHasSubtypes(true);
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
    if (tab == this.getCreativeTab()) {
      for (int i = 0; i < SpellRegistry.spellRegistry.size(); i++) {
        ItemStack stack = new ItemStack(this, 1);
        createData(stack, SpellRegistry.spellRegistry.keySet().toArray(new String[SpellRegistry.spellRegistry.size()])[i]);
        subItems.add(stack);
      }
    }
  }

  public static ItemStack createData(ItemStack stack, String spellName) {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    stack.getTagCompound().setString("spell", spellName);
    return stack;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    if (!stack.hasTagCompound()) {
      tooltip.add(I18n.format("roots.tooltip.petaldust.notag"));
    } else {
      SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell")).addToolTip(tooltip);
    }
  }
}