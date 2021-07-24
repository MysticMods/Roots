package epicsquid.roots.integration.patchouli;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.CostType;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCost;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SpellModifierProcessor implements IComponentProcessor {
  private SpellBase recipe = null;
  private IModifier modifier = null;
  private ItemStack core = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    recipe = SpellRegistry.getSpell(iVariableProvider.get("spell"));
    if (recipe == null) {
      Roots.logger.error("Invalid spell: " + iVariableProvider.get("spell"));
    }
    modifier = ModifierRegistry.get(new ResourceLocation(iVariableProvider.get("modifier")));
    if (modifier == null) {
      Roots.logger.error("Invalid modifier for spell " + iVariableProvider.get("spell") + ": " + iVariableProvider.get("modifier"));
    }
    core = modifier.getStack();
  }

  @Override
  public String process(String s) {
    if (modifier == null || core == null) {
      return null;
    }
    IModifierCost mainCost = null;
    for (IModifierCost c : modifier.getCosts().values()) {
      if (core.getItem().equals(c.getHerb().getItem())) {
        mainCost = c;
        break;
      }
    }
    switch (s) {
      case "core":
        return ItemStackUtil.serializeStack(core);
      case "name":
        return I18n.format(modifier.getTranslationKey());
      case "description":
        return I18n.format(modifier.getTranslationKey() + ".desc");
      case "cost":
        if (mainCost == null) {
          return "Invalid cost.";
        }
        return String.format("%.03f", mainCost.getValue());
      case "additions":
        StringJoiner result = new StringJoiner("\n");
        for (IModifierCost cost : modifier.getCosts().values()) {
          if (cost == mainCost) {
            continue;
          }
          switch (cost.getCost()) {
            case NO_COST:
              result.add(I18n.format("roots.tooltip.modifier.no_cost"));
              break;
            case ADDITIONAL_COST:
              if (cost.getHerb() == null) {
                throw new NullPointerException("Additional herb modifier cost type but no herb specified.");
              }
              result.add(I18n.format("roots.tooltip.modifier.additional_cost", I18n.format(cost.getHerb().getStack().getTranslationKey() + ".name"), String.format("%.4f", cost.getValue())));
              break;
            case ALL_COST_MULTIPLIER:
              if (cost.getValue() < 0) {
                result.add(I18n.format("roots.tooltip.modifier.decreased_cost", Math.floor(cost.getValue() * 100) + "%"));
              } else {
                result.add(I18n.format("roots.tooltip.modifier.increased_cost", Math.floor(cost.getValue() * 100) + "%"));
              }
              break;
            case SPECIFIC_COST_ADJUSTMENT:
              if (cost.getValue() < 0) {
                result.add(I18n.format("roots.tooltip.modifier.specific_decreased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
              } else {
                result.add(I18n.format("roots.tooltip.modifier.specific_increased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
              }
              break;
            case SPECIFIC_COST_MULTIPLIER:
              if (cost.getValue() < 0) {
                result.add(I18n.format("roots.tooltip.modifier.specific_decreased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
              } else {
                result.add(I18n.format("roots.tooltip.modifier.specific_increased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
              }
              break;
            default:
          }
        }
        return result.toString();
    }

    return null;
  }
}
