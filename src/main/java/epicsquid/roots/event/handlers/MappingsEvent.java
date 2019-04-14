package epicsquid.roots.event.handlers;

import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class MappingsEvent {
  @SubscribeEvent
  public static void onMissingBlockMappings(RegistryEvent.MissingMappings<Block> event) {
    for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID) && missing.getPath().equals("thatch")) {
        mapping.remap(ModBlocks.thatch);
      }
    }
  }

  @SubscribeEvent
  public static void onMissingItemMappings (RegistryEvent.MissingMappings<Item> event) {
    for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID) && missing.getPath().equals("thatch")) {
        mapping.remap(Item.getItemFromBlock(ModBlocks.thatch));
      }
    }
  }
}
