package epicsquid.roots.event;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class MappingsEvent {
  @SubscribeEvent
  public static void onMissingBlockMappings(RegistryEvent.MissingMappings<Block> event) {
    for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "thatch":
            mapping.remap(ModBlocks.thatch);
            break;
          case "grove_crafter":
            mapping.remap(epicsquid.roots.init.ModBlocks.fey_crafter);
            break;
          case "fey_light":
            mapping.ignore();
            break;
          case "bonfire":
            mapping.remap(epicsquid.roots.init.ModBlocks.pyre);
            break;
          case "decorative_bonfire":
            mapping.remap(epicsquid.roots.init.ModBlocks.decorative_pyre);
            break;
          case "reinforced_bonfire":
            mapping.remap(epicsquid.roots.init.ModBlocks.reinforced_pyre);
            break;
          case "offertory_plate":
            mapping.remap(epicsquid.roots.init.ModBlocks.offering_plate);
            break;
        }
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  public static void onMissingItemMappings(RegistryEvent.MissingMappings<Item> event) {
    for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "thatch":
            mapping.remap(((BlockBase) ModBlocks.thatch).getItemBlock());
            break;
          case "grove_crafter":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.fey_crafter).getItemBlock());
            break;
          case "fey_light":
            mapping.ignore();
            break;
          case "assorted_seeds":
/*            mapping.remap(ModItems.seeds);*/
            break;
          case "cooked_seeds":
/*            mapping.remap(ModItems.cooked_seeds);*/
            break;
          case "bonfire":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.pyre).getItemBlock());
            break;
          case "decorative_bonfire":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.decorative_pyre).getItemBlock());
            break;
          case "reinforced_bonfire":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.reinforced_pyre).getItemBlock());
            break;
          case "offertory_plate":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.offering_plate).getItemBlock());
            break;
        }
        if (missing.getPath().startsWith("runic_")) {
          ResourceLocation replacement = new ResourceLocation(Roots.MODID, missing.getPath().replace("runic_", "runed_"));
          Item item = ForgeRegistries.ITEMS.getValue(replacement);
          if (replacement == null) {
            throw new NullPointerException(replacement.toString() + " replacement for " + missing.toString() + " is null!");
          }
          mapping.remap(item);
        }
      }
    }
  }
}
