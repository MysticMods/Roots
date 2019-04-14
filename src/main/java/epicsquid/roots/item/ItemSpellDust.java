package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.inventory.SpellHolder;
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
      for(Map.Entry<String, SpellBase> entry : SpellRegistry.spellRegistry.entrySet()){
        ItemStack stack = new ItemStack(this, 1);
        createData(stack, entry.getValue());
        subItems.add(stack);
      }
    }
  }

  public static ItemStack createData(ItemStack stack, SpellBase spell) {
    SpellHolder capability = SpellHolder.fromStack(stack);

    capability.setSpellToSlot(spell);
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }

    tag.setString("spell_name", spell.getName());
    return stack;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    SpellHolder capability = SpellHolder.fromStack(stack);

    SpellBase spell = capability.getSelectedSpell();
    if (spell == null) return;

    spell.addToolTip(tooltip);
  }
}