package mysticmods.roots.api.spell;

import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Costing {
  private final ISpellInstance spell;

  private final Object2DoubleMap<Herb> totalCosts = new Object2DoubleLinkedOpenHashMap<>();

  private final Object2BooleanMap<SpellModifier> modifierMap = new Object2BooleanLinkedOpenHashMap<>();

  private boolean noCharge = false;

  public Costing(ISpellInstance spell) {
    this.spell = spell;
    // base cost is always a cost
    modifierMap.defaultReturnValue(false);
  }

  // TODO: IF you weren't charged should it trigger a cooldown?
  public void noCharge() {
    this.noCharge = true;
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
    // TODO: make this a function?
    Map<Herb, List<HerbEntry>> herbMap = new HashMap<>();
    for (int i = 0; i < playerInventory.getContainerSize(); i++) {
      ItemStack inSlot = playerInventory.getItem(i);
      Herb herb = Herb.getHerb(inSlot);
      if (herb != null) {
        herbMap.computeIfAbsent(herb, k -> new ArrayList<>()).add(new HerbEntry(HerbEntryType.INVENTORY, herb, i, inSlot.getCount(), -1));
        // TODO: Pouches are just a cap?
        /*      } else if (inSlot.is(RootsTags.Items.POUCHES)) {*/
      } else {
        IItemHandler cap = inSlot.getCapability(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.ITEM, null);
        if (cap != null) {
          // TODO: check the item cap
          for (int j = 0; j < cap.getSlots(); j++) {
            ItemStack inSlot2 = cap.getStackInSlot(j);
            Herb herb2 = Herb.getHerb(inSlot2);
            if (herb2 != null) {
              herbMap.computeIfAbsent(herb2, k -> new ArrayList<>()).add(new HerbEntry(HerbEntryType.POUCH, herb2, i, inSlot2.getCount(), j));
            }
          }
        } else {
/*          CompoundTag tag = inSlot.getTag();
          // PROBLEMATIC ASSUMPTIONS HERE
          if (tag != null && tag.contains("Items", Tag.TAG_LIST)) {
            // TODO: try to read it like a shulker box
            ListTag itemListTag;
            try {
              itemListTag = tag.getList("Items", Tag.TAG_COMPOUND);
            } catch (ClassCastException exception) {
              continue;
            }
            NonNullList<ItemStack> items = NonNullList.withSize(itemListTag.size(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, items);
            for (int j = 0; j < items.size(); j++) {
              ItemStack inSlot2 = items.get(j);
              Herb herb2 = Herb.getHerb(inSlot2);
              if (herb2 != null) {
                herbMap.computeIfAbsent(herb2, k -> new ArrayList<>()).add(new HerbEntry(HerbEntryType.POUCH, herb2, i, inSlot2.getCount(), j));
              }
            }
          }*/
        }
      }
    }
    return herbMap;
  }

  public boolean canAfford(Player player, boolean checkModifiers) {
    if (player.isCreative()) {
      return true;
    }
    calculateCosts(checkModifiers, false);


    Inventory playerInventory = player.getInventory();
    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

    //Map<Herb, List<HerbEntry>> herbMap = herbMap(player);

    for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
      double remainder = cap.drain(entry.getKey(), entry.getDoubleValue(), true);
      if (remainder != 0) {
        double count = 0;
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
          ItemStack stack = playerInventory.getItem(i);
          if (stack.is(entry.getKey().getTag())) {
            count += stack.getCount();
          }
          // TODO: Item capabilities, shulker boxes, pouches
        }
        if (remainder > count) {
          return false;
        }
      }
    }

    return true;
  }

  // herbs can be directly in the inventory
  // herbs can be in an item capability in the inventory
  // herbs can be in a shulker box in the inventory
  // herbs can be in a pouch in the inventory
  private record HerbEntry(HerbEntryType type, Herb herb, int slot, int count, int subindex) {
  }

  private enum HerbEntryType {
    POUCH, SHULKER, CAPABILITY, INVENTORY
  }

  // NOTE: THIS DOES NOT CHECK AMOUNTS, MERELY CHARGES
  public boolean charge(Player player) {
    if (player.isCreative()) {
      return false;
    }
    if (player.level().isClientSide()) {
      throw new IllegalStateException("Trying to charge '" + player + "' on the client side.");
    }
    calculateCosts(true, false);

    // TODO: ???
    if (noCharge) {
      return false;
    }

    Inventory playerInventory = player.getInventory();
    HerbStorage cap = player.getData(ModAttachments.HERB_STORAGE);

    //Map<Herb, List<HerbEntry>> herbMap = herbMap(player);

    for (Object2DoubleMap.Entry<Herb> entry : totalCosts.object2DoubleEntrySet()) {
      double remainder = cap.drain(entry.getKey(), entry.getDoubleValue(), false);
      if (remainder != 0) {
        int toConsume = Mth.ceil(remainder);
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
          ItemStack stack = playerInventory.getItem(i);
          if (stack.is(entry.getKey().getTag())) {
            if (stack.getCount() >= toConsume) {
              /*RootsAPI.LOG.info("Shrunk stack of {} by {}", stack, toConsume);*/
              stack.shrink(toConsume);
              toConsume = 0;
              break;
            } else {
              /*RootsAPI.LOG.info("Shrunk stack of {} by {} to 0", stack, stack.getCount());*/
              toConsume -= stack.getCount();
              stack.setCount(0);
            }
            if (toConsume <= 0) {
              break;
            }
          }
        }
        if (toConsume > 0) {
          // HOUSTON WE HAVE A PROBLEM
          RootsAPI.LOG.info("Remainder left over! OH NO! {}", toConsume);
        }
        cap.fill(entry.getKey(), (double) Mth.ceil(remainder) - remainder);
        playerInventory.setChanged();
      }
    }

    return true;
  }

  // TODO: Really need to come up with a cleaner way of doing this
  private void calculateCosts(boolean checkModifiers, boolean skipModifiers) {
    totalCosts.clear();
    Map<Herb, List<Cost>> herbCosts = new HashMap<>();
    for (Cost cost : spell.getSpell().getDefaultCosts()) {
      herbCosts.computeIfAbsent(cost.getHerb(), k -> new ArrayList<>()).add(cost);
    }
    if (!skipModifiers) {
      for (SpellModifier modifier : spell.getEnabledModifiers()) {
        if (!checkModifiers || modifierMap.getBoolean(modifier)) {
          for (Cost cost : modifier.getDefaultCosts()) {
            herbCosts.computeIfAbsent(cost.getHerb(), k -> new ArrayList<>()).add(cost);
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

      totalCosts.put(entry.getKey(), total);
    }
  }

  public Object2DoubleMap<Herb> getMinimumCost() {
    calculateCosts(false, true);
    return new Object2DoubleLinkedOpenHashMap<>(totalCosts);
  }

  public Object2DoubleMap<Herb> getMaximumCost() {
    calculateCosts(false, false);
    return new Object2DoubleLinkedOpenHashMap<>(totalCosts);
  }
}
