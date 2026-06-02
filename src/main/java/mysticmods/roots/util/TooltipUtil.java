package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.datacomponent.SpellSlot;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.herb.ParentChargeType;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TooltipUtil {
  public static void spellStaffTooltip(Item.TooltipContext context, List<Component> pTooltipComponents, ItemStack pStack, TooltipFlag flag) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage != null) {
      pTooltipComponents.add(Component.translatable("roots.tooltip.staff.selected", (storage.currentSlot()) + 1));
      ISpellInstance spell = storage.getCurrentSpell();
      pTooltipComponents.add(CommonComponents.EMPTY);
      if (spell != null) {
        TooltipUtil.spellInstanceTooltip(context, pTooltipComponents, spell, flag);
      } else {
        pTooltipComponents.add(Component.translatable("roots.tooltip.staff.no_spell"));
      }
      pTooltipComponents.add(CommonComponents.EMPTY);
      int tempSlot = 0;
      for (SpellSlot entry : storage.getSpells()) {
        // TODO: Spell data
        int slotId = tempSlot + 1;
        boolean isModified = entry != null && !entry.getEnabledModifiers().isEmpty();
        Component spellName = entry == null ? Component.translatable("roots.tooltip.staff.no_spell") : entry.spell()
            .getStyledName();
        Component selected = tempSlot == storage.currentSlot() ? Component.translatable("roots.tooltip.staff.is_selected") : CommonComponents.EMPTY;

        Component cd;

        if (entry == null) {
          cd = CommonComponents.EMPTY;
        } else {
          if (context.level() != null && context.level().isClientSide()) {
            Player player = PlayerGetter.getPlayer();
            if (!player.hasData(ModAttachments.COOLDOWN_STORAGE)) {
              cd = CommonComponents.EMPTY;
            } else {
              int cooldown = player.getData(ModAttachments.COOLDOWN_STORAGE).getCooldown(entry.spell());
              if (cooldown > 0) {
                cd = Component.translatable("roots.tooltip.staff.cooldown", cooldown / 20);
              } else {
                cd = CommonComponents.EMPTY;
              }
            }
          } else {
            cd = CommonComponents.EMPTY;
          }
        }
        pTooltipComponents.add(Component.translatable("roots.tooltip.staff.spell_in_slot", slotId, spellName, isModified ? MODIFIED_SPELL : CommonComponents.EMPTY, selected, cd));
        tempSlot++;
      }
    }
  }

  public static final Component MODIFIED_SPELL = Component.translatable("roots.tooltip.staff.is_modified");

  public static void spellInstanceTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag, boolean addName) {
    if (addName) {
      result.add(spell.getStyledName());
    }
    spellDataTooltip(context, result, spell, flag);
    fullSpellCostTooltip(context, result, spell, flag);
    addModifierList(context, result, spell, flag);
  }

  public static void addModifierList(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    var modifiers = spell.getEnabledModifiers();
    // TODO: Modifiers should be sorted
    // TODO: Modifier sets ("Amplified 1, Amplified 2") should collapse ("Amplified (2)")
    if (!modifiers.isEmpty()) {
      result.add(CommonComponents.EMPTY);
      var line = Component.translatable("roots.tooltip.spell.modifiers");
      boolean first = true;
      for (SpellModifier modifier : modifiers) {
        if (!first) {
          line.append(Component.literal(", "));
        }
        if (first) {
          first = false;
        }
        line.append(modifier.getName());
      }
      result.add(line);
    }
  }

  public static void spellInstanceTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    spellInstanceTooltip(context, result, spell, flag, true);
  }

  public static void spellDataTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance instance, TooltipFlag flag) {
    Spell spell = instance.getSpell();
    Set<String> keys = instance.getSpell().getTooltipDataKeys();
    boolean added = false;
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
      added = true;
    }
    if (added) {
      result.add(CommonComponents.EMPTY);
    }
  }

  public static void baseModifierCostTooltip(Item.TooltipContext context, List<Component> result, SpellModifier modifier, TooltipFlag flag) {
    for (Cost cost : modifier.getCosts().costs()) {
      Herb herb = cost.getHerb();
      String herbCost = cost.getType() == Cost.CostType.ADDITIVE ? String.format("%.4f", cost.getValue()) : String.format("%.1f", (cost.getValue() - 1) * 100);
      // TODO: Decide how to handle multiplicative costs
      // +5%
      // (cost.getValue() - 1) * 100;
      var amount = cost.getType() == Cost.CostType.ADDITIVE ? "roots.tooltip.cost.cost_amount" : "roots.tooltip.cost.cost_multiplier";
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable(amount, herbCost)));
    }
  }

  // TODO: Handle
  public static void baseSpellCostTooltip(Item.TooltipContext context, List<Component> result, Spell spell, TooltipFlag flag) {
    for (Cost cost : spell.getCosts().costs()) {
      Herb herb = cost.getHerb();
      String herbCost = String.format("%.4f", cost.getValue());
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
    }
    //addChargeType(context, result, spell.getChargeType(), flag);
  }

  public static void addChargeType(Item.TooltipContext context, List<Component> result, ParentChargeType type, TooltipFlag flag) {
    result.add(Component.translatable("roots.tooltip.cost.charge_type", Component.translatable("roots.tooltip.cost.charge_type." + type
        .name().toLowerCase(Locale.ROOT))));
  }

  public static void fullSpellCostTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    Costing cos = new Costing(spell);
    for (Object2DoubleMap.Entry<Herb> entry : cos.getTooltipCost().object2DoubleEntrySet()) {
      Herb herb = entry.getKey();
      String herbCost = String.format("%.4f", entry.getDoubleValue());
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
    }
    //addChargeType(context, result, spell.getChargeType(), flag);
  }
}

