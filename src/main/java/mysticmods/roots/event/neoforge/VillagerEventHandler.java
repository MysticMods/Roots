package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class VillagerEventHandler {
  @SubscribeEvent
  public static void onVillagerTrades(VillagerTradesEvent event) {
    if (event.getType() == VillagerProfession.BUTCHER) {
      // Novice
      event.getTrades().get(1).add(
          new BasicItemListing(new ItemStack(ModItems.VENISON.get(), 7), new ItemStack(Items.EMERALD), 16, 2, 0.05f)
      );
      // Apprentice
      event.getTrades().get(1).add(
          new BasicItemListing(1, new ItemStack(ModItems.COOKED_VENISON.get(), 5), 16, 5, 0.05f)
      );
      event.getTrades().get(1).add(
          new BasicItemListing(1, new ItemStack(ModItems.COOKED_SQUID.get(), 4), 16, 5, 0.05f)
      );
      // Journeyman
      event.getTrades().get(3).add(
          new BasicItemListing(new ItemStack(ModItems.RAW_SQUID.get(), 5), new ItemStack(Items.EMERALD), 12, 5, 0.05f)
      );
      // Expert
      event.getTrades().get(4).add(
          new BasicItemListing(18, new ItemStack(ModItems.ANTLERS.get()), 12, 30, 0.05f)
      );
    } else if (event.getType() == VillagerProfession.FARMER) {
      // Novice
      event.getTrades().get(1).add(
          new BasicItemListing(new ItemStack(ModItems.AUBERGINE.get(), 15), new ItemStack(Items.EMERALD), 16, 2, 0.05f)
      );
    } else if (event.getType() == VillagerProfession.CLERIC) {
      // Novice
      event.getTrades().get(2).add(
          new BasicItemListing(new ItemStack(ModItems.SILVER_INGOT.get(), 3), new ItemStack(Items.EMERALD), 12, 10, 0.05f)
      );
      // Expert
      event.getTrades().get(4).add(
          new BasicItemListing(new ItemStack(ModItems.CARAPACE.get(), 18), new ItemStack(Items.EMERALD), 12, 30, 0.05f)
      );
    } else if (event.getType() == VillagerProfession.LEATHERWORKER) {
      // Journeyman
      event.getTrades().get(3).add(
          new BasicItemListing(new ItemStack(ModItems.PELT.get(), 9), new ItemStack(Items.EMERALD), 12, 20, 0.05f)
      );
    }
  }

  @SubscribeEvent
  public static void onWanderingTrade(WandererTradesEvent event) {
    event.getRareTrades().add(
        new BasicItemListing(18, new ItemStack(ModItems.ALERTNESS_CHARM), 1, 0, 0.05f)
    );
  }
}
