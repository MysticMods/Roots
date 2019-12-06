package epicsquid.roots.item;

import net.minecraft.item.Item;

// TODO: Rethink this entirely/redo this

public class SpellDustItem extends Item {
  public SpellDustItem(Properties properties) {
    super(properties);
  }
/*  public SpellDustItem(String name) {
    super(name);
    this.hasSubtypes = true;
    this.setHasSubtypes(true);
  }*/

/*  @Override
  public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subItems) {
    if (tab == this.getCreativeTab()) {
      for (SpellBase entry : SpellRegistry.spellRegistry.values()) {
        subItems.add(entry.getResult());
      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    SpellHandler capability = SpellHandler.fromStack(stack);

    SpellBase spell = capability.getSelectedSpell();
    if (spell == null) return;

    spell.addToolTip(tooltip);
    List<SpellModule> spellModules = capability.getSelectedModules();
    if (!spellModules.isEmpty()) {
      tooltip.add(I18n.format("roots.spell.module.description"));
      String prefix = "roots.spell." + spell.getName();
      for (SpellModule module : spellModules) {
        tooltip.add(module.getFormat() + I18n.format("roots.spell.module." + module.getName() + ".name") + ": " + I18n.format(prefix + "." + module.getName() + ".description"));
      }
    }
  }*/
}