package mysticmods.roots.api.spell;

import it.unimi.dsi.fastutil.objects.*;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.integration.curios.CuriosIntegration;
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

// TODO: Urgent -- CuriosIntegration breaks API
public class Costing {
  private final ISpellInstance spell;

  private final Object2DoubleMap<Herb> totalCosts = new Object2DoubleLinkedOpenHashMap<>();

  private final Object2BooleanMap<SpellModifier> modifierMap = new Object2BooleanLinkedOpenHashMap<>();

  private final Map<Herb, List<HerbEntry>> herbMapCache;

  private CostInstance.ChargeType chargeType;

  private int operationsCount = 0;
  private boolean noCharge = false;
  private boolean foundCreativePouch = false;

  public Costing(ISpellInstance spell) {
    this.spell = spell;
    modifierMap.defaultReturnValue(false);
    herbMapCache = new HashMap<>();
    chargeType = spell.getSpell().getCosts().chargeType();
  }

  public Costing(ISpellInstance spell, Player player) {
    this.spell = spell;
    // base cost is always a cost
    modifierMap.defaultReturnValue(false);
    herbMapCache = herbMap(player);
    chargeType = spell.getSpell().getCosts().chargeType();
  }

  public CostInstance.ChargeType getChargeType() {
    return chargeType;
  }

  public void noCharge() {
    this.noCharge = true;
  }

  public void increment() {
    this.operationsCount++;
  }

  public void operations(int operations) {
    this.operationsCount = operations;
  }

  public void charge(SpellModifier modifier) {
    if (!this.spell.hasModifier(modifier)) {
      throw new IllegalStateException("tried to charge for a modifier (" + modifier + ") in the spell " + this.spell.getSpell() + " when that spell doesn't have that modifier enabled");
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
    List<ItemStack> curios = CuriosIntegration.getPouches(player);
    for (int i = 0; i < curios.size(); i++) {
      ItemStack inSlot = curios.get(i);
      if (inSlot.is(RootsTags.Items.CREATIVE_POUCHES)) {
        foundCreativePouch = true;
        return herbMap;
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
    return herbMap;
  }

  public boolean canAfford(Player player, boolean checkModifiers) {
    if (player.isCreative() || foundCreativePouch) {
      return true;
    }
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
        if (remainder > count) {
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
    if (player.isCreative() || foundCreativePouch) {
      return true;
    }
    if (player.level().isClientSide()) {
      throw new IllegalStateException("Trying to charge '" + player + "' on the client side.");
    }

    if (noCharge) {
      return false;
    }

    if (chargeType == CostInstance.ChargeType.OPERATION && operationsCount == 0) {
      RootsAPI.LOG.error("Charging a spell with operation costs but no operations! {}", spell);
    }

    calculateCosts(true, false, false, tick);

    Inventory playerInventory = player.getInventory();
    List<ItemStack> curios = CuriosIntegration.getPouches(player);
    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

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
              RootsAPI.LOG.error("No capability found for {}", capStack);
              continue;
            }
            ItemStack capItem = thisCap.getStackInSlot(herbEntry.subindex);
            if (capItem.getCount() >= toConsume) {
              thisCap.extractItem(herbEntry.subindex, toConsume, false);
              toConsume = 0;
              herbEntry.count = thisCap.getStackInSlot(herbEntry.subindex).getCount();
              // TODO: All of this should modify in place
/*              playerInventory.setItem(herbEntry.slot, capStack);*/
              break;
            } else {
              thisCap.extractItem(herbEntry.subindex, capItem.getCount(), false);
              toConsume -= capItem.getCount();
              herbEntry.count = capItem.getCount();
/*              playerInventory.setItem(herbEntry.slot, capStack);*/
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
              // TODO: As above, so below
/*              playerInventory.setItem(herbEntry.slot, capStack);*/
              break;
            } else {
              thisCap.extractItem(herbEntry.subindex, capItem.getCount(), false);
              toConsume -= capItem.getCount();
              herbEntry.count = capItem.getCount();
/*              playerInventory.setItem(herbEntry.slot, capStack);*/
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

    Object2DoubleMap<Herb> totals = new Object2DoubleOpenHashMap<>();

    for (Herb herb : totalCosts.keySet()) {
      for (HerbEntry herbEntry : herbMapCache.getOrDefault(herb, List.of())) {
        totals.put(herb, totals.getDouble(herb) + herbEntry.count);
      }
      totals.put(herb, totals.getDouble(herb) + cap.amount(herb));
    }

    RootsAPI.getInstance().syncHerbs(player, totals);

    return true;
  }

  private void calculateCosts(boolean checkModifiers, boolean skipModifiers, boolean maxOperations, boolean tick) {
    totalCosts.clear();
    Map<Herb, List<Cost>> herbCosts = new HashMap<>();
    CostInstance.ChargeType thisType = spell.getSpell().getChargeType();
    for (Cost cost : spell.getSpell().getCosts().costs()) {
      List<Cost> costs = herbCosts.get(cost.getHerb());
      if (costs == null) {
        costs = new ArrayList<>();
        herbCosts.put(cost.getHerb(), costs);
      }
      if (thisType == CostInstance.ChargeType.OPERATION && maxOperations) {
        for (int i = 0; i < spell.getSpell().getMaximumOperations(); i++) {
          costs.add(cost);
        }
      } else if (thisType == CostInstance.ChargeType.OPERATION) {
        for (int i = 0; i < operationsCount; i++) {
          costs.add(cost);
        }
      } else if (thisType == CostInstance.ChargeType.CAST) {
        costs.add(cost);
      }
    }
    if (!skipModifiers) {
      for (SpellModifier modifier : spell.getEnabledModifiers()) {
        thisType = modifier.getChargeType();
        if (!checkModifiers || modifierMap.getBoolean(modifier)) {
          for (Cost cost : modifier.getCosts().costs()) {
            List<Cost> costs = herbCosts.get(cost.getHerb());
            if (costs == null) {
              costs = new ArrayList<>();
              herbCosts.put(cost.getHerb(), costs);
            }
            if (thisType == CostInstance.ChargeType.OPERATION && maxOperations) {
              for (int i = 0; i < spell.getSpell().getMaximumOperations(); i++) {
                costs.add(cost);
              }
            } else if (thisType == CostInstance.ChargeType.OPERATION) {
              for (int i = 0; i < operationsCount; i++) {
                costs.add(cost);
              }
            } else if (thisType == CostInstance.ChargeType.CAST) {
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

      if (tick) {
        total /= 20;
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

  private class HerbEntry {
    private final HerbEntryType type;
    private final Herb herb;
    private final int slot;
    private int count;
    private final int subindex;

    public HerbEntry(HerbEntryType type, Herb herb, int slot, int count, int subindex) {
      this.type = type;
      this.herb = herb;
      this.slot = slot;
      this.count = count;
      this.subindex = subindex;
    }
  }

  private enum HerbEntryType {
    CAPABILITY, INVENTORY, CURIOS_CAPABILITY
  }
}
