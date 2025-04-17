package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.Dyeable;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
  public static final ResourceLocation UNDYED = RootsAPI.rl("undyed");

  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      ItemPropertyFunction func = (stack, level, entity, seed) -> {
        Dyeable dyeable = stack.get(ModAttachments.DYEABLE);
        if (dyeable == Dyeable.DEFAULT) {
          return 0;
        }
        return 1;
      };
      ItemProperties.register(ModItems.APOTHECARY_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.COMPONENT_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.HERB_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.SYLVAN_POUCH.get(), UNDYED, func);
    });
  }
}
