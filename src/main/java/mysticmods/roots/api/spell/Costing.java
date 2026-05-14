package mysticmods.roots.api.spell;

import it.unimi.dsi.fastutil.objects.*;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.herb.ChargeType;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedParent;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.*;

// TODO: Abstract this away from player-based
public class Costing {
  private final ICostedParent parent;

  private final Object2DoubleMap<Herb> totalCosts = new Object2DoubleLinkedOpenHashMap<>();

  private final Object2BooleanMap<ICosted> modifierMap = new Object2BooleanLinkedOpenHashMap<>();

  private Map<Herb, List<HerbEntry>> herbMapCache;

  private final ChargeType chargeType;

  private int operationsCount = 0;
  private double discount = 0;
  private boolean noCharge = false;
  private boolean foundCreativePouch = false;

  public Costing(ICostedParent parent) {
    this.parent = parent;
    modifierMap.defaultReturnValue(false);
    chargeType = parent.getChargeType();
  }

  public Costing(ICostedParent parent, Player player) {
    this(parent);
    herbMapCache = herbMap(player);
  }

  public ChargeType getChargeType() {
    return chargeType;
  }

  public void noCharge() {
    this.noCharge = true;
  }

  public void increment() {
    this.operationsCount++;
  }

  public int operations() {
    return this.operationsCount;
  }

  public void operations(int operations) {
    this.operationsCount = operations;
  }

  public double discount() {
    return this.discount;
  }

  public void discount(double discount) {
    this.discount = discount;
  }

  public void charge(ICosted modifier) {
    if (!this.parent.hasChild(modifier)) {
      throw new IllegalStateException("tried to charge for a modifier (" + modifier + ") in  '" + this.parent + "' when that doesn't have that modifier enabled");
    }

    this.noCharge = false;
    modifierMap.put(modifier, true);
  }

  private Map<Herb, List<HerbEntry>> herbMap(Player player) {
    Inventory playerInventory = player.getInventory();
    Map<Herb, List<HerbEntry>> herbMap = new HashMap<>();
    for (int i = 0; i < playerInventory.getContainerSize(); i++) {
      ItemStack inSlot = playerInventory.getItem(i);
      if (inSlot.is(RootsTags.Items.CREATIVE_POUCHES)) {
        foundCreativePouch = true;
        return herbMap;
      }
      Herb herb = Herb.getHerb(inSlot);
      if (herb != null) {
        List<HerbEntry> entries = herbMap.get(herb);
        if (entries == null) {
          entries = new ArrayList<>();
          herbMap.put(herb, entries);
        }
        entries.add(new HerbEntry(HerbEntryType.INVENTORY, herb, i, inSlot.getCount(), -1));
      } else {
        IItemHandler cap = inSlot.getCapability(Capabilities.ItemHandler.ITEM, null);
        if (cap != null) {
          for (int j = 0; j < cap.getSlots(); j++) {
            ItemStack inSlot2 = cap.getStackInSlot(j);
            Herb herb2 = Herb.getHerb(inSlot2);
            if (herb2 != null) {
              herbMap.computeIfAbsent(herb2, k -> new ArrayList<>())
                  .add(new HerbEntry(HerbEntryType.CAPABILITY, herb2, i, inSlot2.getCount(), j));
            }
          }
        }
      }
    }
    List<ItemStack> curios = RootsAPI.getInstance().getCurios(player, RootsTags.Items.CURIOS_BELTS);
    for (int i = 0; i < curios.size(); i++) {
      ItemStack inSlot = curios.get(i);
      if (inSlot.is(RootsTags.Items.CREATIVE_POUCHES)) {
        foundCreativePouch = true;
        continue;
      }
      IItemHandler cap = inSlot.getCapability(Capabilities.ItemHandler.ITEM, null);
      if (cap != null) {
        for (int j = 0; j < cap.getSlots(); j++) {
          ItemStack inSlot2 = cap.getStackInSlot(j);
          Herb herb2 = Herb.getHerb(inSlot2);
          if (herb2 != null) {
            herbMap.computeIfAbsent(herb2, k -> new ArrayList<>())
                .add(new HerbEntry(HerbEntryType.CURIOS_CAPABILITY, herb2, i, inSlot2.getCount(), j));
          }
        }
      }
    }
    herbMap.forEach((a, b) -> b.sort(Comparator.comparingInt(HerbEntry::getCount)));
    return herbMap;
  }

  public boolean canAfford(Player player, boolean checkModifiers) {
    boolean creative = player.isCreative() || foundCreativePouch;
    this.herbMapCache = herbMap(player);
    calculateCosts(checkModifiers, false, true, false);

    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

    for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
      double remainder = cap.drain(entry.getKey(), entry.getDoubleValue(), true);
      if (remainder != 0) {
        double count = 0;
        List<HerbEntry> entries = herbMapCache.get(entry.getKey());
        if (entries != null) {
          for (HerbEntry herbEntry : entries) {
            count += herbEntry.count;
          }
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
    boolean isCreative = player.isCreative() || foundCreativePouch;
    if (player.level().isClientSide()) {
      throw new IllegalStateException("Trying to charge '" + player + "' on the client side.");
    }

    if (noCharge) {
      return false;
    }

    if (chargeType == ChargeType.OPERATION && operationsCount == 0) {
      RootsAPI.LOG.error("Charging with operation costs but no operations! {}", parent);
    }

    this.herbMapCache = herbMap(player);
    calculateCosts(true, false, false, tick);

    Inventory playerInventory = player.getInventory();
    List<ItemStack> curios = RootsAPI.getInstance().getCurios(player, RootsTags.Items.CURIOS_BELTS);
    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

    if (!isCreative) {
      for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
        double remainder = cap.drain(entry.getKey(), entry.getDoubleValue(), false);
        if (remainder != 0) {
          int toConsume = Mth.ceil(remainder);
          for (HerbEntry herbEntry : herbMapCache.getOrDefault(entry.getKey(), List.of())) {
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
      for (HerbEntry herbEntry : herbMapCache.getOrDefault(herb, List.of())) {
        total += herbEntry.count;
        //totals.put(herb, totals.getDouble(herb) + herbEntry.count);
      }

      // Thus setting the config to 0 disables this
      // Only send alerts on cast success, not ticks
      // TODO: Maybe sent it on ticks?
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

  private void calculateCosts(boolean checkModifiers, boolean skipModifiers, boolean maxOperations, boolean tick) {
    totalCosts.clear();
    Map<Herb, List<Cost>> herbCosts = new HashMap<>();
    ChargeType thisType = getChargeType();
    for (Cost cost : parent.getCosts().costs()) {
      List<Cost> costs = herbCosts.get(cost.getHerb());
      if (costs == null) {
        costs = new ArrayList<>();
        herbCosts.put(cost.getHerb(), costs);
      }
      if (thisType == ChargeType.OPERATION && maxOperations) {
        for (int i = 0; i < parent.getMaximumOperations(); i++) {
          costs.add(cost);
        }
      } else if (thisType == ChargeType.OPERATION) {
        for (int i = 0; i < operationsCount; i++) {
          costs.add(cost);
        }
      } else if (thisType == ChargeType.CAST) {
        costs.add(cost);
      }
    }
    if (!skipModifiers) {
      for (ICosted modifier : parent.getChildren()) {
        thisType = modifier.getChargeType();
        if (!checkModifiers || modifierMap.getBoolean(modifier)) {
          for (Cost cost : modifier.getCosts().costs()) {
            List<Cost> costs = herbCosts.get(cost.getHerb());
            if (costs == null) {
              costs = new ArrayList<>();
              herbCosts.put(cost.getHerb(), costs);
            }
            if (thisType == ChargeType.OPERATION && maxOperations) {
              for (int i = 0; i < parent.getMaximumOperations(); i++) {
                costs.add(cost);
              }
            } else if (thisType == ChargeType.OPERATION) {
              for (int i = 0; i < operationsCount; i++) {
                costs.add(cost);
              }
            } else if (thisType == ChargeType.CAST) {
              costs.add(cost);
            }
          }
        }
      }
    }
    for (Map.Entry<Herb, List<Cost>> entry : herbCosts.entrySet()) {
      double total = 0;
      for (Cost cost : entry.getValue()) {
        if (cost.getType() != Cost.CostType.ADDITIVE) {
          continue;
        }

        total += cost.getValue();
      }
      for (Cost cost : entry.getValue()) {
        if (cost.getType() != Cost.CostType.MULTIPLICATIVE) {
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
    }

  }

  public Object2DoubleMap<Herb> getMinimumCost() {
    int ops = this.operationsCount;
    this.operationsCount = 1;
    calculateCosts(false, true, false, false);
    this.operationsCount = ops;
    return new Object2DoubleLinkedOpenHashMap<>(totalCosts);
  }

  public Object2DoubleMap<Herb> getMaximumCost() {
    calculateCosts(false, false, true, false);
    return new Object2DoubleLinkedOpenHashMap<>(totalCosts);
  }

  private static class HerbEntry {
    public final HerbEntryType type;
    public final Herb herb;
    public final int slot;
    public int count;
    public final int subindex;

    public HerbEntry(HerbEntryType type, Herb herb, int slot, int count, int subindex) {
      this.type = type;
      this.herb = herb;
      this.slot = slot;
      this.count = count;
      this.subindex = subindex;
    }

    public HerbEntryType getType() {
      return type;
    }

    public Herb getHerb() {
      return herb;
    }

    public int getSlot() {
      return slot;
    }

    public int getCount() {
      return count;
    }

    public int getSubindex() {
      return subindex;
    }
  }

  private enum HerbEntryType {
    CAPABILITY, INVENTORY, CURIOS_CAPABILITY
  }
}
