package mysticmods.roots.api.herb;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.modifier.ChildChargeType;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.registry.ICostedParent;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Abstract this away from player-based

public class Costing {
  private final ICostedParent parent;

  private final Object2DoubleMap<Herb> totalCosts = new Object2DoubleOpenHashMap<>();
  // Base costs unmodified by operations
  private final Object2DoubleMap<Herb> baseCosts = new Object2DoubleOpenHashMap<>();

  private final Object2BooleanMap<ICosted> modifierMap = new Object2BooleanOpenHashMap<>();

  private HerbMap herbMapCache;

  private final ParentChargeType chargeType;

  private int operationsCount = 0;
  private double discount = 0;
  private boolean noCharge = false;

  // TODO: Hold shift for more details on staff
  // TODO: Base spells can never be multiplicative - enforce this in the CORRECT PLACE

  public Costing(ICostedParent parent) {
    this.parent = parent;
    modifierMap.defaultReturnValue(false);
    chargeType = parent.getChargeType();
    for (ICostedChild modifier : parent.getChildren()) {
      if (modifier.getChargeType() == ChildChargeType.ALWAYS) {
        charge(modifier);
      }
    }
    herbMapCache = null;
  }

  public void updateHerbCache(Player player) {
    this.herbMapCache = new HerbMap(player);
  }

  public ParentChargeType getChargeType() {
    return chargeType;
  }

  public void noCharge() {
    this.noCharge = true;
  }

  public int operations() {
    return this.operationsCount;
  }

  public void operations(int operations) {
    this.operationsCount = operations;
  }

  public void discount(double discount) {
    this.discount = discount;
  }

  public void charge(ICostedChild modifier) {
    if (!this.parent.hasChild(modifier)) {
      throw new IllegalStateException("tried to charge for a modifier (" + modifier + ") in  '" + this.parent + "' when that doesn't have that modifier enabled");
    }

    this.noCharge = false;
    modifierMap.put(modifier, true);
  }

  public boolean canAfford(Player player, boolean checkModifiers) {
    if (herbMapCache == null) {
      updateHerbCache(player);
    }

    boolean creative = player.isCreative() || herbMapCache.foundCreativePouch();
    calculateCosts(checkModifiers, true, false);

    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

    for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
      double remainder = cap.drain(entry.getKey(), entry.getDoubleValue(), true);
      if (remainder != 0) {
        double count = 0;
        for (HerbEntry herbEntry : herbMapCache.entries(entry.getKey())) {
          count += herbEntry.count;
        }
        if (remainder > count && !creative) {
          return false;
        }
      }
    }

    return true;
  }

  public boolean shouldCharge() {
    return !noCharge && operationsCount > 0;
  }

  public boolean charge(Player player) {
    return charge(player, false);
  }

  // NOTE: THIS DOES NOT CHECK AMOUNTS, MERELY CHARGES
  public boolean charge(Player player, boolean tick) {
    boolean isCreative = player.isCreative() || herbMapCache.foundCreativePouch();
    if (player.level().isClientSide()) {
      throw new IllegalStateException("Trying to charge '" + player + "' on the client side.");
    }

    if (noCharge) {
      return false;
    }

    if (chargeType == ParentChargeType.OPERATION && operationsCount == 0) {
      RootsAPI.LOG.error("Charging with operation costs but no operations! {}", parent);
    }

    calculateCosts(true, false, tick);

    Inventory playerInventory = player.getInventory();
    List<ItemStack> curios = RootsAPI.getInstance().getCurios(player, RootsTags.Items.CURIOS_BELTS);
    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

    if (!isCreative) {
      for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
        double remainder = cap.drain(entry.getKey(), entry.getDoubleValue(), false);
        if (remainder != 0) {
          int toConsume = Mth.ceil(remainder);
          for (HerbEntry herbEntry : herbMapCache.entries(entry.getKey())) {
            if (herbEntry.type == HerbEntryType.INVENTORY) {
              ItemStack stack = playerInventory.getItem(herbEntry.slot);
              if (stack.getCount() >= toConsume) {
                stack.shrink(toConsume);
                toConsume = 0;
                playerInventory.setItem(herbEntry.slot, stack);
                herbEntry.count = stack.getCount();
                break;
              } else {
                toConsume -= stack.getCount();
                stack.setCount(0);
                playerInventory.setItem(herbEntry.slot, ItemStack.EMPTY);
                herbEntry.count = 0;
              }
            } else if (herbEntry.type == HerbEntryType.CURIOS_CAPABILITY) {
              ItemStack capStack = curios.get(herbEntry.slot);
              IItemHandler thisCap = capStack.getCapability(Capabilities.ItemHandler.ITEM, null);
              if (thisCap == null) {
                continue;
              }
              ItemStack capItem = thisCap.getStackInSlot(herbEntry.subindex);
              if (capItem.getCount() >= toConsume) {
                thisCap.extractItem(herbEntry.subindex, toConsume, false);
                toConsume = 0;
                herbEntry.count = thisCap.getStackInSlot(herbEntry.subindex).getCount();
                break;
              } else {
                thisCap.extractItem(herbEntry.subindex, capItem.getCount(), false);
                toConsume -= capItem.getCount();
                herbEntry.count = capItem.getCount();
              }
            } else {
              ItemStack capStack = playerInventory.getItem(herbEntry.slot);
              IItemHandler thisCap = capStack.getCapability(Capabilities.ItemHandler.ITEM, null);
              if (thisCap == null) {
                RootsAPI.LOG.error("No capability found for {}", capStack);
                continue;
              }
              ItemStack capItem = thisCap.getStackInSlot(herbEntry.subindex);
              if (capItem.getCount() >= toConsume) {
                thisCap.extractItem(herbEntry.subindex, toConsume, false);
                toConsume = 0;
                herbEntry.count = thisCap.getStackInSlot(herbEntry.subindex).getCount();
                break;
              } else {
                thisCap.extractItem(herbEntry.subindex, capItem.getCount(), false);
                toConsume -= capItem.getCount();
                herbEntry.count = capItem.getCount();
              }
            }
            if (toConsume <= 0) {
              break;
            }
          }
          if (toConsume > 0) {
            RootsAPI.LOG.info("Remainder left over! OH NO! {}", toConsume);
          }
          cap.fill(entry.getKey(), (double) Mth.ceil(remainder) - remainder);
          playerInventory.setChanged();
          player.inventoryMenu.sendAllDataToRemote();
        }
      }
    }

    Object2DoubleMap<Herb> totals = new Object2DoubleOpenHashMap<>();

    boolean sentAlert = false;

    for (Herb herb : totalCosts.keySet()) {
      double total = cap.amount(herb);
      for (HerbEntry herbEntry : herbMapCache.entries(herb)) {
        total += herbEntry.count;
      }

      if (total < ConfigManager.HERB_MINIMUM_ALERT.getAsDouble() && !isCreative && !tick) {
        // TODO: Translation key
        player.displayClientMessage(Component.literal("Low on " + herb.getName() + "!"), !sentAlert);
        sentAlert = true;
      }
      totals.put(herb, total + (isCreative ? totalCosts.getDouble(herb) : 0));
    }

    RootsAPI.getInstance().syncHerbs(player, totals);

    return true;
  }

  private void calculateCosts(boolean checkModifiers, boolean maxOperations, boolean tick) {
    totalCosts.clear();
    baseCosts.clear();
    Map<Herb, List<Cost>> herbCosts = new HashMap<>();
    ParentChargeType thisType = getChargeType();
    for (Cost cost : parent.getCosts().costs()) {
      if (!cost.getType().isAdditive()) {
        throw new IllegalStateException("Only additive costs can be created for parents, cost type '" + cost.getType()
            .toString() + "' is invalid!");
      }

      var cur = baseCosts.getOrDefault(cost.getHerb(), 0.0);

      int totalOperations = thisType == ParentChargeType.OPERATION ? maxOperations ? parent.getMaximumOperations() : operations() : 1;

      baseCosts.put(cost.getHerb(), cur + cost.getValue() * totalOperations);
    }

/*    for (Cost cost : parent.getCosts().costs()) {
      List<Cost> costs = herbCosts.computeIfAbsent(cost.getHerb(), k -> new ArrayList<>());
      if (thisType == ParentChargeType.OPERATION && maxOperations) {
        for (int i = 0; i < parent.getMaximumOperations(); i++) {
          costs.add(cost);
        }
      } else if (thisType == ParentChargeType.OPERATION) {
        for (int i = 0; i < operationsCount; i++) {
          costs.add(cost);
        }
      } else if (thisType == ParentChargeType.INSTANCE) {
        costs.add(cost);
      }
    }*/


    for (ICostedChild modifier : parent.getChildren()) {
      if (checkModifiers && ((modifier.getChargeType() == ChildChargeType.SPECIFIED && modifierMap.getBoolean(modifier)) || modifier.getChargeType() == ChildChargeType.ALWAYS) || maxOperations) {
        for (Cost cost : modifier.getCosts().costs()) {
          List<Cost> costs = herbCosts.computeIfAbsent(cost.getHerb(), k -> new ArrayList<>());
          if (thisType == ParentChargeType.OPERATION) {
            if (cost.getType() == CostType.MULTIPLICATIVE_TOTAL) {
              costs.add(cost); // Totals are only ever applied once
            } else if (cost.getType() == CostType.NEGATE_BASE_COST) {
              baseCosts.clear();
            } else {
              if (maxOperations) {
                for (int i = 0; i < parent.getMaximumOperations(); i++) {
                  costs.add(cost);
                }
              } else {
                for (int i = 0; i < operationsCount; i++) {
                  costs.add(cost);
                }
              }
            }
          } else {
            costs.add(cost);
          }
        }
      }
    }
    totalCosts.putAll(baseCosts);
    for (Map.Entry<Herb, List<Cost>> entry : herbCosts.entrySet()) {
      double total = 0;

      for (Cost cost : entry.getValue()) {
        if (!cost.getType().isAdditive()) {
          continue;
        }

        total += cost.getValue();
      }

      for (Cost cost : entry.getValue()) {
        if (cost.getType() == CostType.MULTIPLICATIVE_BASE) {
          var val = cost.getValue();
          total += (baseCosts.getOrDefault(entry.getKey(), 0) * val);
        }
      }
      for (Cost cost : entry.getValue()) {
        if (cost.getType() != CostType.MULTIPLICATIVE_TOTAL) {
          continue;
        }

        total *= cost.getValue();
      }

      if (total <= 0) {
        continue;
      }

      // TODO: Check this
      if (tick) {
        total /= 20;
      }

      // Apply discounts
      if (discount > 0) {
        total -= total * discount;
      }

      totalCosts.put(entry.getKey(), total);
      baseCosts.putIfAbsent(entry.getKey(), 0);
    }
  }

/*  public Object2DoubleMap<Herb> getMinimumCost() {
    int ops = this.operationsCount;
    this.operationsCount = 1;
    calculateCosts(false, true, false, false);
    this.operationsCount = ops;
    return new Object2DoubleOpenHashMap<>(totalCosts);
  }*/

  public Map<Herb, HerbCost> getTooltipCost() {
    int ops = this.operationsCount;
    this.operationsCount = 1;
    calculateCosts(true, false, false);
    this.operationsCount = ops;
    Map<Herb, HerbCost> result = new HashMap<>();
    for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
      double total = entry.getDoubleValue();
      double base = baseCosts.getOrDefault(entry.getKey(), 0);
      result.put(entry.getKey(), new HerbCost(total, total - base));
    }
    return result;
    /*    return new Object2DoubleOpenHashMap<>(totalCosts);*/
  }

  public record HerbCost(double total, double modifiers) {
  }

/*  public Object2DoubleMap<Herb> getMaximumCost() {
    calculateCosts(false, false, true, false);
    return new Object2DoubleOpenHashMap<>(totalCosts);
  }*/
}
