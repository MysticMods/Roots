package mysticmods.roots.api.herb;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.*;

public class HerbMap {
  private boolean foundCreativePouch = false;

  private Map<Herb, List<HerbEntry>> herbMapCache;

  public HerbMap(Player player) {
    herbMap(player);
  }

  public Iterable<HerbEntry> entries(Herb herb) {
    return herbMapCache.getOrDefault(herb, Collections.emptyList());
  }

  private void herbMap(Player player) {
    Inventory playerInventory = player.getInventory();
    Map<Herb, List<HerbEntry>> herbMap = new HashMap<>();
    for (int i = 0; i < playerInventory.getContainerSize(); i++) {
      ItemStack inSlot = playerInventory.getItem(i);
      if (inSlot.is(RootsTags.Items.CREATIVE_POUCHES)) {
        foundCreativePouch = true;
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
    this.herbMapCache = herbMap;
  }

  public boolean foundCreativePouch() {
    return foundCreativePouch;
  }
}
