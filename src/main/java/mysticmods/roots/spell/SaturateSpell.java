package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayList;
import java.util.List;

public class SaturateSpell extends Spell {
  private float saturationMultiplier, foodMultiplier;

  public SaturateSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xe134f6, 0x05e82a);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.SATURATE_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.saturationMultiplier = ModSpells.SATURATE_SATURATION_MULTIPLIER.get().getValue();
    this.foodMultiplier = ModSpells.SATURATE_FOOD_MULTIPLIER.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    FoodData data = pPlayer.getFoodData();
    int currentFood = data.getFoodLevel();
    float currentSaturation = data.getSaturationLevel();
    if (currentFood >= 20 && currentSaturation >= 20) {
      costs.noCharge();
      return;
    }

    float newSat = currentSaturation;
    int newFood = currentFood;

    Object2IntLinkedOpenHashMap<ItemStack> foodsToSlots = new Object2IntLinkedOpenHashMap<>();
    Object2IntLinkedOpenHashMap<ItemStack> usedAmounts = new Object2IntLinkedOpenHashMap<>();
    pPlayer.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> {
      for (int i = 0; i < cap.getSlots(); i++) {
        ItemStack stack = cap.getStackInSlot(i);
        if (stack.isEdible() && !stack.is(RootsTags.Items.SKIPPED_FOODS)) {
          FoodProperties props = stack.getFoodProperties(pPlayer);
          if (props == null || !props.getEffects().isEmpty()) {
            continue;
          }
          foodsToSlots.put(stack, i);
        }
      }
    });

    if (foodsToSlots.isEmpty()) {
      costs.noCharge();
      return;
    }

    List<ItemStack> sortedFoods = foodsToSlots.keySet().stream().sorted((o1, o2) -> Float.compare(saturation(o1, pPlayer, instance), saturation(o2, pPlayer, instance))).toList();

    for (ItemStack stack : sortedFoods) {
      float thisSaturation = saturation(stack, pPlayer, instance);
      float thisFood = food(stack, pPlayer, instance);
      int used = 0;
      for (int i = 0; i < stack.getCount(); i++) {
        newSat += thisSaturation;
        newFood += thisFood;
        used++;
        if (newSat >= 20 && newFood >= 20) {
          break;
        }
      }
      if (used > 0) {
        usedAmounts.put(stack, used);
      }
      if (newSat >= 20 && newFood >= 20) {
        break;
      }
    }

    if ((newSat <= currentSaturation && newFood <= currentFood) || usedAmounts.isEmpty()) {
      costs.noCharge();
      return;
    }

    List<ItemStack> consumedItems = new ArrayList<>();
    pPlayer.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      for (Object2IntMap.Entry<ItemStack> entry : usedAmounts.object2IntEntrySet()) {
        int usedAmount = entry.getIntValue();
        int index = foodsToSlots.getInt(entry.getKey());
        ItemStack result = handler.extractItem(index, usedAmount, false);
        if (!result.isEmpty()) {
          consumedItems.add(result);
        }
      }
    });

    if (consumedItems.isEmpty()) {
      costs.noCharge();
      return;
    }

    if (data.saturationLevel < newSat) {
      data.setSaturation(Math.min(20, newSat));
    }
    if (data.getFoodLevel() < newFood) {
      data.setFoodLevel(Math.min(20, newFood));
    }

    for (ItemStack stack : consumedItems) {
      ItemStack result = stack.finishUsingItem(pLevel, pPlayer);
      if (result.isEmpty()) {
        continue;
      }
      if (result.hasCraftingRemainingItem()) {
        pPlayer.getInventory().placeItemBackInInventory(result.getCraftingRemainingItem());
      } else {
        pPlayer.getInventory().placeItemBackInInventory(result);
      }
    }
  }

  private float saturation(ItemStack stack, Player pPlayer, SpellInstance spell) {
    if (!stack.isEdible()) {
      return 0;
    }

    FoodProperties props = stack.getFoodProperties(pPlayer);
    if (props == null) {
      return 0;
    }

    return (props.getSaturationModifier() * props.getNutrition() * 2) * saturationMultiplier;
  }

  private float food(ItemStack stack, Player pPlayer, SpellInstance spell) {
    if (!stack.isEdible()) {
      return 0;
    }

    FoodProperties props = stack.getFoodProperties(pPlayer);
    if (props == null) {
      return 0;
    }

    return props.getNutrition() * foodMultiplier;
  }
}
