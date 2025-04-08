package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Set;

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
      int tempSlot = 0;
      for (SpellStorage.SpellSlot entry : storage.getSpells()) {
        // TODO: Spell data
        int slotId = tempSlot + 1;
        Component spellName = entry == null ? Component.translatable("roots.tooltip.staff.no_spell") : entry.spell()
            .getStyledName();
        Component selected = tempSlot == storage.currentSlot() ? Component.translatable("roots.tooltip.staff.is_selected") : Component.literal("");
        Component cd = entry == null ? Component.literal("") : entry.cooldown() > 0 ? Component.translatable("roots.tooltip.staff.cooldown", entry.cooldown() / 20) : Component.literal("");
        pTooltipComponents.add(Component.translatable("roots.tooltip.staff.spell_in_slot", slotId, spellName, selected, cd));
        tempSlot++;
      }
    }
  }

  public static void spellInstanceTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    result.add(spell.getStyledName());
    spellDataTooltip(context, result, spell, flag);
    result.add(Component.empty());
    spellCostTooltip(context, result, spell, flag);
  }

  public static void spellDataTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance instance, TooltipFlag flag) {
    Spell spell = instance.getSpell();
    Set<String> keys = instance.getSpell().getTooltipDataKeys();
/*    if (!keys.isEmpty()) {
      result.add(Component.empty());
    }*/
    for (String key : keys) {
      int index = spell.getDataIndex(key);
      // TODO: Clean this up
      Component keyC = Component.translatable(spell.getOrCreateDescriptionId() + ".data." + key + ".name");
      Component valC;
      if (index == 0) {
        valC = Component.translatable(spell.getOrCreateDescriptionId() + ".data." + spell.getDataKey(spell.getDataValue(instance, key)) + ".name");
      } else {
        valC = Component.literal(String.valueOf(Math.max(1, spell.getDataValue(instance, key))));
      }
      result.add(Component.translatable("roots.tooltip.staff.data", keyC, valC));
    }
  }

  public static void spellCostTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    Costing cos = new Costing(spell);
    for (Object2DoubleMap.Entry<Herb> entry : cos.getMinimumCost().object2DoubleEntrySet()) {
      Herb herb = entry.getKey();
      String herbCost = String.format("%.4f", entry.getDoubleValue());
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
    }
    result.add(Component.translatable("roots.tooltip.cost.charge_type", Component.translatable("roots.tooltip.cost.charge_type." + cos.getChargeType()
        .name().toLowerCase())));
  }
}

