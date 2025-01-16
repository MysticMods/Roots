package mysticmods.roots.util;

/*
public class TooltipUtil {
  public static void spellStaffTooltip(List<Component> pTooltipComponents, ItemStack pStack, TooltipFlag flag) {
    OldSpellStorage storage = OldSpellStorage.fromItem(pStack);
    if (storage != null) {
      pTooltipComponents.add(Component.translatable("roots.tooltip.staff.selected", storage.getSlot() + 1));
      SpellInstance spell = storage.getSpell();
      pTooltipComponents.add(Component.literal(""));
      if (spell != null) {
        TooltipUtil.spellInstanceTooltip(pTooltipComponents, spell, flag);
      } else {
        pTooltipComponents.add(Component.translatable("roots.tooltip.staff.no_spell"));
      }
      pTooltipComponents.add(Component.literal(""));
      if (RootsAPI.getInstance().isShiftKeyDown()) {
        for (OldSpellStorage.Entry entry : storage.entryList()) {
          pTooltipComponents.add(Component.translatable("roots.tooltip.staff.spell_in_slot", entry.slot() + 1, entry.spell() == null ? Component.translatable("roots.tooltip.staff.no_spell") : entry.spell().getStyledName(), entry.slot() == storage.getSlot() ? Component.translatable("roots.tooltip.staff.is_selected") : Component.literal("")));
        }
      } else {
        // TODO:
        //pTooltipComponents.add(ModLang.holdShift());
      }
    }
  }

  public static void spellInstanceTooltip(List<Component> result, SpellInstance spell, TooltipFlag flag) {
    result.add(spell.getStyledName());
    result.add(Component.empty());
    spellCostTooltip(result, spell, flag);
  }

  public static void spellCostTooltip(List<Component> result, SpellInstance spell, TooltipFlag flag) {
    Costing cos = new Costing(spell);
    for (Object2DoubleMap.Entry<Herb> entry : cos.getMinimumCost().object2DoubleEntrySet()) {
      Herb herb = entry.getKey();
      String herbCost = String.format("%.4f", entry.getDoubleValue());
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
    }
  }
}
*/
