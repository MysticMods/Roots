package mysticmods.roots.integration;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.integration.curios.CuriosIntegration;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class IntegrationUtil {
  public static List<ItemStack> getCharms(Player player) {
    List<ItemStack> charms = new ArrayList<>();

    if (ModList.get().isLoaded("curios")) {
      charms.addAll(CuriosIntegration.getCharms(player));
    }

    ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);

    if (mainHand.is(RootsTags.Items.CURIOS_CHARMS)) {
      charms.add(mainHand);
    }

    mainHand = player.getItemInHand(InteractionHand.OFF_HAND);
    if (mainHand.is(RootsTags.Items.CURIOS_CHARMS)) {
      charms.add(mainHand);
    }

    return charms;
  }

  public static void init (IEventBus bus) {
    CuriosIntegration.init(bus);
  }
}
