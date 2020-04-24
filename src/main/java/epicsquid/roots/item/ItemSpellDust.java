package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.library.StaffSpellStorage;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
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
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);

    SpellBase spell = capability.getSelectedSpell();
    if (spell == null) return;

    spell.addToolTip(tooltip);
/*    List<SpellModule> spellModules = capability.getSelectedModules();
    if (!spellModules.isEmpty()) {
      tooltip.add(I18n.format("roots.spell.module.description"));
      String prefix = "roots.spell." + spell.getName();
      for (SpellModule module : spellModules) {
        tooltip.add(module.getFormat() + I18n.format("roots.spell.module." + module.getName() + ".name") + ": " + I18n.format(prefix + "." + module.getName() + ".description"));
      }
    }*/
  }
}