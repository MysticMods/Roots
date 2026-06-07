package mysticmods.roots.api.spell;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.herb.*;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedChild;
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

  private final Object2DoubleMap<Herb> totalCosts = new Object2DoubleOpenHashMap<>();

  private final Object2BooleanMap<ICosted> modifierMap = new Object2BooleanOpenHashMap<>();

  private Map<Herb, List<HerbEntry>> herbMapCache;

  private final ParentChargeType chargeType;

  private int operationsCount = 0;
  private double discount = 0;
  private boolean noCharge = false;
  private boolean foundCreativePouch = false;

  // TODO: Modified spells in spell library do not correctly count modifiers (in tooltip) in costs
  //  nor does it show modifiers
  // TODO: Modifier list concatenates weirdly
  // TODO: Hold shift for more details on staff
  // TODO: Sort modifiers sequentially

  // Herb costs can be as follows:
  // (Per spell)    (Herb + double + additive/multiplicative) +
  //                ((Herb + double + additive/multiplicative) (per charged modifier))
  //                * (charge == operation ? op_count : 1)

  // Costings:
  // Per herb costs from base
  // Per herb costs from modifier
  // Discounts

  enum CostSource {
    SPELL, // always additive
    MODIFIER, // multiplicative or additive
    DISCOUNT // always multiplicative
  }

  public record CostSegment(double amount, CostType type, CostSource source) {
    public static CostSegment spell (double amount) {
      return new CostSegment(amount, CostType.ADDITIVE, CostSource.SPELL);
    }

    public static CostSegment discount (double amount) {
      return new CostSegment(amount, CostType.MULTIPLICATIVE_TOTAL, CostSource.DISCOUNT);
    }

    public static CostSegment modifier (double amount, CostType type) {
      return new CostSegment(amount, type, CostSource.MODIFIER);
    }
  }

  // First, sort by cost type: additive then multiplicative (compareTo?)
  // Iterate over and have two running totals:
  // Total
  // Total without modifiers

  // Validate that all CostSources.SPELL = CostType.ADDITIVE

  // TODO: Base spells can never be multiplicative

  // Sky Soarer
  // cloud_berry: +1.250
  // Friendly Earth
  // stalicripe: +0.125
  // Amplifier 1:
  // cloud_berry: +0.125
  // Amplifier 2:
  // cloud_berry +0.125
  // Speedy 1:
  // cloud_berry: +0.125
  // Speedy 2:
  // cloud_berry: *1.05

  // cloud_berry: (+1.625) *1.05 -> [1.70625] * 0.98 -> 1.672125
  // stalicripe: +0.125 -> 0.1225
  // double: total cost   +1.70625    -> 0.45625
  // double: total cost without modifiers +1.225    -> 0.447125
  // double: total cost without discounts +1.70625

  // Cloud Berry: +1.706 [±0.456]
  // Stalicripe: +0.125

  public static final class CostChain {
    private final List<CostSegment> segments;


    public CostChain(List<CostSegment> segments) {
      this.segments = segments;
      this.segments.sort(Comparator.comparing(CostSegment::type));
    }

    public List<CostSegment> segments() {
      return segments;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      var that = (CostChain) obj;
      return Objects.equals(this.segments, that.segments);
    }

    @Override
    public int hashCode() {
      return Objects.hash(segments);
    }

    @Override
    public String toString() {
      return "CostChain[" +
          "segments=" + segments + ']';
    }

  }

  public Costing(ICostedParent parent) {
    this.parent = parent;
    modifierMap.defaultReturnValue(false);
    chargeType = parent.getChargeType();
    for (ICostedChild modifier : parent.getChildren()) {
      if (modifier.getChargeType() == ChildChargeType.ALWAYS) {
        charge(modifier);
      }
    }
  }

  public Costing(ICostedParent parent, Player player) {
    this(parent);
    herbMapCache = herbMap(player);
  }

  public ParentChargeType getChargeType() {
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

  public void charge(ICostedChild modifier) {
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

    if (chargeType == ParentChargeType.OPERATION && operationsCount == 0) {
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
    ParentChargeType thisType = getChargeType();
    for (Cost cost : parent.getCosts().costs()) {
      List<Cost> costs = herbCosts.get(cost.getHerb());
      if (costs == null) {
        costs = new ArrayList<>();
        herbCosts.put(cost.getHerb(), costs);
      }
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
    }
    if (!skipModifiers) {
      // TODO: Does this actually work to calculate modifiers?
      for (ICostedChild modifier : parent.getChildren()) {
        if (checkModifiers && modifierMap.getBoolean(modifier) || maxOperations) {
          for (Cost cost : modifier.getCosts().costs()) {
            List<Cost> costs = herbCosts.get(cost.getHerb());
            if (costs == null) { // TODO: Is this actually faster?
              costs = new ArrayList<>();
              herbCosts.put(cost.getHerb(), costs);
            }
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
          }
        }
      }
    }
    for (Map.Entry<Herb, List<Cost>> entry : herbCosts.entrySet()) {
      double total = 0;
      for (Cost cost : entry.getValue()) {
        if (cost.getType().isMultiplicative()) {
          continue;
        }

        total += cost.getValue();
      }
      for (Cost cost : entry.getValue()) {
        if (cost.getType().isAdditive()) {
          continue;
        }

        // TODO: Handle multiplicative base

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

  // TODO: Tooltip cost
  public Object2DoubleMap<Herb> getMinimumCost() {
    int ops = this.operationsCount;
    this.operationsCount = 1;
    calculateCosts(false, true, false, false);
    this.operationsCount = ops;
    return new Object2DoubleOpenHashMap<>(totalCosts);
  }

  public Object2DoubleMap<Herb> getTooltipCost() {
    int ops = this.operationsCount;
    this.operationsCount = 1;
    calculateCosts(true, false, false, false);
    this.operationsCount = ops;
    return new Object2DoubleOpenHashMap<>(totalCosts);
  }

  public Object2DoubleMap<Herb> getMaximumCost() {
    calculateCosts(false, false, true, false);
    return new Object2DoubleOpenHashMap<>(totalCosts);
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
