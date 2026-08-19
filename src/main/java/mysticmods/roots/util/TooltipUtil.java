package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellSlot;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostType;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;
import java.util.stream.Collectors;

// Beware: this is triggered on both the server and the client so never reference client-only things
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
        int slotId = tempSlot + 1;
        boolean isModified = entry != null && !entry.getEnabledModifiers().isEmpty();
        Component spellName = entry == null ? Component.translatable("roots.tooltip.staff.no_spell") : entry.getStyledName();
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
    spellModeTooltip(context, result, spell, flag);
    fullSpellCostTooltip(context, result, spell, flag);
    addModifierList(context, result, spell, flag);
  }

  private static List<List<SpellModifier>> segmentModifiers(ISpellInstance spell) {
    SpellModifierSet modifiers = spell.getEnabledModifiers();
    if (modifiers.isEmpty()) {
      return List.of();
    } else if (modifiers.size() == 1) {
      if (modifiers.firstElement() != null && modifiers.firstElement()
          .is(RootsTags.SpellModifiers.SKIPPED_IN_TOOLTIPS)) {
        return List.of();
      }
      return List.of(modifiers.stream().toList());
    }

    List<List<SpellModifier>> result = new ArrayList<>();

    Map<GroupId, List<SpellModifier>> grouped = new HashMap<>();

    for (SpellModifier modifier : modifiers) {
      if (!modifier.is(RootsTags.SpellModifiers.SKIPPED_IN_TOOLTIPS)) {
        if (modifier.canGroup()) {
          grouped.computeIfAbsent(modifier.getGroupKey(), k -> new ArrayList<>()).add(modifier);
        } else {
          result.add(List.of(modifier));
        }
      }
    }

    for (Map.Entry<GroupId, List<SpellModifier>> entry : grouped.entrySet()) {
      List<SpellModifier> entryValues = entry.getValue();
      if (entryValues.isEmpty()) {
        RootsAPI.LOG.error("Empty group SOMEHOW??? {}", entry);
        continue;

      }
      Set<ResourceKey<SpellModifier>> parents = entry.getValue().stream().map(SpellModifier::getParent)
          .filter(Objects::nonNull).collect(Collectors.toSet());
      List<SpellModifier> newResult = new ArrayList<>();
      for (SpellModifier modifier : entry.getValue()) {
        if (!parents.contains(modifier.getSelf())) {
          newResult.addFirst(modifier);
        } else {
          newResult.addLast(modifier);
        }
      }
      result.add(newResult);
    }

    return result;
  }

  public static void addModifierList(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    List<List<SpellModifier>> modifiers = segmentModifiers(spell);
    if (!modifiers.isEmpty()) {
      if (flag.hasShiftDown()) {
        for (List<SpellModifier> modifierGroup : modifiers) {
          SpellModifier first = modifierGroup.getFirst();
          if (first.is(RootsTags.SpellModifiers.SKIPPED_IN_TOOLTIPS)) {
            continue;
          }
          Component name = modifierGroup.size() == 1 ? first.getName() : first.getGroupName(modifierGroup.size());
          result.add(Component.translatable("roots.tooltip.spell.modifier_description", name, TooltipUtil.describeModifier(context, result, first, flag)));
          result.add(CommonComponents.EMPTY);
          TooltipUtil.baseModifierCostTooltip(context, result, modifierGroup, flag);
        }
      } else {
        result.add(CommonComponents.EMPTY);
        MutableComponent modifierNames = Component.empty();
        boolean first = true;
        for (List<SpellModifier> modifierGroup : modifiers) {
          if (modifierGroup.getFirst().is(RootsTags.SpellModifiers.SKIPPED_IN_TOOLTIPS)) {
            continue;
          }
          if (!first) {
            modifierNames.append(", ");
          }
          first = false;
          var firstModifer = modifierGroup.getFirst();
          modifierNames.append(modifierGroup.size() == 1 ? firstModifer.getName() : firstModifer.getGroupName(modifierGroup.size()));
        }
        result.add(Component.translatable("roots.tooltip.spell.modifiers", modifierNames));
      }
    }
  }

  public static void spellInstanceTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance spell, TooltipFlag flag) {
    spellInstanceTooltip(context, result, spell, flag, true);
  }

  public static void spellModeTooltip(Item.TooltipContext context, List<Component> result, ISpellInstance instance, TooltipFlag flag) {
    if (instance.getCycleComponent() != null) {
      var mode = instance.getSpellData(instance.getCycleComponent());
      result.add(Component.translatable("roots.tooltip.staff.data", Component.translatable("roots.spell_mode.mode"), mode.getStyledName()));
      result.add(CommonComponents.EMPTY);
    }
  }

  private record CostKey (Holder<Herb> herb, CostType type) {
    private CostKey fromCost (Cost cost) {
      return new CostKey(cost.getHolder(), type);
    }
  }

  public static List<Cost> collateCosts (List<Cost> incoming) {
    Object2DoubleMap<CostKey> map = new Object2DoubleOpenHashMap<>();
    for (Cost cost : incoming) {
      var key = new CostKey(cost.getHolder(), cost.getType());
      var current = map.getOrDefault(key, 0);
      map.put(key, current + cost.getValue());
    }
    List<Cost> result = new ArrayList<>();
    for (Object2DoubleMap.Entry<CostKey> entry : map.object2DoubleEntrySet()) {
      result.add(new Cost(entry.getKey().type(), entry.getKey().herb(), entry.getDoubleValue()));
    }
    return result;
  }

  public static void baseModifierCostTooltip(Item.TooltipContext context, List<Component> result, List<SpellModifier> modifierList, TooltipFlag flag) {
    List<Cost> modifierCosts = new ArrayList<>();
    for (SpellModifier modifier : modifierList) {
      modifierCosts.addAll(modifier.getCosts().costs());
    }

    var negated = modifierCosts.removeIf(o -> o.getType().isNegative());

    if (negated) {
      var baseCosts = modifierList.getFirst().getApplicableHolder().value().getCosts();
      for (Cost cost : baseCosts.costs()) {
        modifierCosts.addFirst(Cost.negate(cost));
      }
    }

    for (Cost cost : collateCosts(modifierCosts)) {
      Herb herb = cost.getHerb();
      String herbCost = cost.getType()
          .isMultiplicative() ? String.format("%.1f", (cost.getValue()) * 100) : String.format("%.4f", Math.abs(cost.getValue())); // TODO: This doesn't work properly for reductions
      String amountKey = switch (cost.getType()) {
        case ADDITIVE -> "roots.tooltip.cost.cost_amount";
        case MULTIPLICATIVE_BASE -> "roots.tooltip.cost.cost_multiply_base";
        case MULTIPLICATIVE_TOTAL -> "roots.tooltip.cost.cost_multiply_total";
        default -> "roots.tooltip.cost.cost_cancel";
      };
      result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable(amountKey, herbCost)));
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
    for (Map.Entry<Herb, Costing.HerbCost> entry : cos.getTooltipCost().entrySet()) {
      Herb herb = entry.getKey();
      Costing.HerbCost cost = entry.getValue();
      if (cost.total() <= 0) {
        continue;
      }
      String herbCost = String.format("%.4f", cost.total());
      if (cost.modifiers() != 0) {
        String herbModifierCost = String.format("%.4f", Math.abs(cost.modifiers()));
        result.add(Component.translatable("roots.tooltip.cost.herb_cost_full", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost), Component.translatable("roots.tooltip.cost.herb_cost_modified", herbModifierCost)));
      } else {
        result.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
      }
    }
    addChargeType(context, result, spell.getChargeType(), flag);
  }

  public static void describeSpell(Item.TooltipContext context, List<Component> tooltipComponents, ISpellInstance spell, TooltipFlag tooltipFlag) {
    if (tooltipFlag.hasShiftDown() || tooltipFlag.hasAltDown() || tooltipFlag.hasControlDown()) {
      tooltipComponents.add(spell.asSpell().getTooltipExtendedDescription(spell));
    } else {
      tooltipComponents.add(spell.asSpell().getTooltipDescription(spell));
    }
  }

  public static void describeSpell(Item.TooltipContext context, List<Component> tooltipComponents, Spell spell, TooltipFlag tooltipFlag) {
    if (tooltipFlag.hasShiftDown() || tooltipFlag.hasAltDown() || tooltipFlag.hasControlDown()) {
      tooltipComponents.add(spell.getTooltipExtendedDescription());
    } else {
      tooltipComponents.add(spell.getTooltipDescription());
    }
  }

  public static Component describeModifier(Item.TooltipContext context, List<Component> tooltipComponents, SpellModifier spellModifier, TooltipFlag tooltipFlag) {
    if (tooltipFlag.hasAltDown() || tooltipFlag.hasControlDown()) {
      return spellModifier.getTooltipExtendedDescription();
    } else {
      return spellModifier.getTooltipDescription();
    }
  }
}

