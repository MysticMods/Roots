package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.capability.spell.ISpellHolderCapability;
import epicsquid.roots.capability.spell.SpellHolderCapabilityProvider;
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

import javax.annotation.Nullable;
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
    ISpellHolderCapability capability = stack.getCapability(SpellHolderCapabilityProvider.ENERGY_CAPABILITY, null);
    ItemStaff.updateCapability(stack, capability);
    if (capability == null) return stack;

    capability.setSpellToSlot(spell);
    return stack;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    ISpellHolderCapability capability = stack.getCapability(SpellHolderCapabilityProvider.ENERGY_CAPABILITY, null);
    ItemStaff.updateCapability(stack, capability);
    if (capability == null) return;

    SpellBase spell = capability.getSelectedSpell();
    if (spell == null) return;

    spell.addToolTip(tooltip);
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  @Nullable
  @Override
  public NBTTagCompound getNBTShareTag(ItemStack stack) {
    NBTTagCompound result = super.getNBTShareTag(stack);

    if (result == null) result = new NBTTagCompound();

    ISpellHolderCapability cap = stack.getCapability(SpellHolderCapabilityProvider.ENERGY_CAPABILITY, null);
    if (cap != null) {
      NBTTagCompound cap_tag = cap.getData();
      result.setTag("spell_capability", cap_tag);
    }

    return result;
  }
}