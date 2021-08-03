package epicsquid.roots.event;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.compat.OldHerbRegistry;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModVillagers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class MappingsEvent {
  @SubscribeEvent
  public static void onMissingPotionMappings(RegistryEvent.MissingMappings<Potion> event) {
    for (RegistryEvent.MissingMappings.Mapping<Potion> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "invulnerability":
            mapping.remap(ModPotions.nondetection);
            break;
          case "freeze":
            mapping.remap(ModPotions.storm_cloud);
            break;
        }
      }
    }
  }

  @SubscribeEvent
  public static void onMissingProfessionMappings(RegistryEvent.MissingMappings<VillagerRegistry.VillagerProfession> event) {
    for (RegistryEvent.MissingMappings.Mapping<VillagerRegistry.VillagerProfession> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "druid":
            mapping.remap(ModVillagers.mageProfession);
        }
      }
    }
  }

  @SubscribeEvent
  public static void onMissingBlockMappings(RegistryEvent.MissingMappings<Block> event) {
    for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "offering_plate":
            mapping.remap(epicsquid.roots.init.ModBlocks.catalyst_plate);
            break;
          case "reinforced_offering_plate":
            mapping.remap(epicsquid.roots.init.ModBlocks.reinforced_catalyst_plate);
            break;
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
            mapping.remap(epicsquid.roots.init.ModBlocks.catalyst_plate);
            break;
          case "wildwood_rune":
            mapping.remap(epicsquid.roots.init.ModBlocks.wildroot_rune);
            break;
          case "structure_marker":
            mapping.ignore();
            break;
          case "incense_burner":
            mapping.ignore();
            break;
          case "fey_fire":
            mapping.remap(epicsquid.roots.init.ModBlocks.wild_fire);
            break;
        }
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  public static void onMissingEntityMappings(RegistryEvent.MissingMappings<EntityEntry> event) {
    for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "entity_ritual_wild_growth":
            mapping.remap(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(Roots.MODID, "entity_ritual_wildroot_growth")));
            break;
        }
      }
    }
  }

  @SubscribeEvent
  public static void onMissingSoundMappings(RegistryEvent.MissingMappings<SoundEvent> event) {
    for (RegistryEvent.MissingMappings.Mapping<SoundEvent> mapping : event.getMappings()) {
      if (mapping.key.getNamespace().equals(Roots.MODID)) {
        mapping.ignore();
      }
    }
  }

  @SubscribeEvent
  public static void onMissingHerbs(RegistryEvent.MissingMappings<OldHerbRegistry.Herb> event) {
    for (RegistryEvent.MissingMappings.Mapping<OldHerbRegistry.Herb> mapping : event.getMappings()) {
      mapping.ignore();
    }
  }

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  public static void onMissingItemMappings(RegistryEvent.MissingMappings<Item> event) {
    for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
      ResourceLocation missing = mapping.key;
      if (missing.getNamespace().equals(Roots.MODID)) {
        switch (missing.getPath()) {
          case "spell_chrysopoeia":
            mapping.ignore();
            break;
          case "offering_plate":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.catalyst_plate).getItemBlock());
            break;
          case "reinforced_offering_plate":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.reinforced_catalyst_plate).getItemBlock());
            break;
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
            mapping.remap(ModItems.seeds);
            break;
          case "cooked_seeds":
            mapping.remap(ModItems.cooked_seeds);
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
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.catalyst_plate).getItemBlock());
            break;
          case "ritual_wild_growth":
            mapping.remap(epicsquid.roots.init.ModItems.ritual_wildroot_growth);
            break;
          case "wildwood_rune":
            mapping.remap(((BlockBase) epicsquid.roots.init.ModBlocks.wildroot_rune).getItemBlock());
            break;
          case "structure_marker":
            mapping.ignore();
            break;
          case "incense_burner":
            mapping.ignore();
            break;
          case "fey_feather":
            mapping.remap(epicsquid.roots.init.ModItems.mystic_feather);
            break;
          case "strange_slime":
            mapping.remap(epicsquid.roots.init.ModItems.strange_ooze);
            break;
          case "golden_eye":
            mapping.ignore();
            break;
          case "fey_fire":
            mapping.remap(epicsquid.roots.init.ModItems.wild_fire);
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
