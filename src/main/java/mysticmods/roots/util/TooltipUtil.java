package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TooltipUtil {
  public static void spellStaffTooltip(Item.TooltipContext context, List<Component> pTooltipComponents, ItemStack pStack, TooltipFlag flag) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage != null) {
      pTooltipComponents.add(Component.translatable("roots.tooltip.staff.selected", (storage.currentSlot()) + 1));
      ISpellInstance spell = storage.getCurrentSpell();
      pTooltipComponents.add(Component.literal(""));
      if (spell != null) {
        TooltipUtil.spellInstanceTooltip(context, pTooltipComponents, spell, flag);
      } else {
        pTooltipComponents.add(Component.translatable("roots.tooltip.staff.no_spell"));
      }
      pTooltipComponents.add(Component.literal(""));
      if (RootsAPI.getInstance().isShiftKeyDown()) {
        int tempSlot = 0;
        for (SpellStorage.SpellSlot entry : storage.getSpells()) {
          // TODO: Include cooling down
          pTooltipComponents.add(Component.translatable("roots.tooltip.staff.spell_in_slot", tempSlot + 1, entry == null ? Component.translatable("roots.tooltip.staff.no_spell") : entry.spell()
              .getStyledName(), tempSlot == storage.currentSlot() ? Component.translatable("roots.tooltip.staff.is_selected") : Component.literal("")));
          tempSlot++;
        }
      } else {
        // TODO:
        pTooltipComponents.add(RootsAPI.holdShift());
      }
    }
  }

  public static void spellInstanceTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    result.add(spell.getStyledName());
    result.add(Component.empty());
    spellCostTooltip(context, result, spell, flag);
  }

  public static void spellCostTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    Costing cos = new Costing(spell);
    for (Object2DoubleMap.Entry<Herb> entry : cos.getMinimumCost().object2DoubleEntrySet()) {
      Herb herb = entry.getKey();
      String herbCost = String.format("%.4f", entry.getDoubleValue());
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
    }
    result.add(Component.translatable("roots.tooltip.cost.charge_type", Component.translatable("roots.tooltip.cost.charge_type." + cos.getChargeType().name().toLowerCase())));
  }
}

