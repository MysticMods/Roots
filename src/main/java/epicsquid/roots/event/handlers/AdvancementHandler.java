package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import epicsquid.roots.util.XPUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID)
@SuppressWarnings("unused")
public class AdvancementHandler {
  public static int FIRST_CRAFT_XP = 4;

  @SubscribeEvent
  public static void onAdvancement (AdvancementEvent event) {
    if (event.getEntityPlayer().world.isRemote) return;

    ResourceLocation adv = event.getAdvancement().getId();
    if (adv.getNamespace().equals(Roots.MODID)) {
      switch (adv.getPath()) {
        case "aubergine":
        case "baffle_cap":
        case "cloud_berry":
        case "dewgonia":
        case "infernal_bulb":
        case "moonglow_leaf":
        case "pereskia":
        case "spirit_herbs":
        case "stalicripe":
        case "terra_moss":
          XPUtil.spawnXP(event.getEntityPlayer().world, event.getEntityPlayer().getPosition(), FIRST_CRAFT_XP);
      }
    }
  }
}
