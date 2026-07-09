package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.modifier.ModifierTrees;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.blockentity.WildwoodChestBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.network.client.ClientboundAnimalHarvestSyncPacket;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import net.neoforged.neoforge.registries.callback.BakeCallback;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataEventHandler {
  @SubscribeEvent
  public static void modifyRegistries(ModifyRegistriesEvent event) {
    event.getRegistry(RootsRegistries.Keys.SPELL_MODIFIERS)
        .addCallback((BakeCallback<SpellModifier>) registry -> ModifierTrees.initialize());
  }

  @SubscribeEvent
  public static void registerDataMaps(RegisterDataMapTypesEvent event) {
    event.register(DataMaps.SPELL_COST_DATA);
    event.register(DataMaps.SPELL_MODIFIER_COST_DATA);
    event.register(DataMaps.RITUAL_MODIFIER_COST_DATA);
    event.register(DataMaps.HERB_ITEM_DATA);
    event.register(DataMaps.SPELL_PROPERTY_DATA);
    event.register(DataMaps.RITUAL_PROPERTY_DATA);
    event.register(DataMaps.SPROUT_BREEDING_ITEM_CHANCE);
    event.register(DataMaps.GROWTH_RECORDS);
    event.register(DataMaps.HARVEST_RECORDS);
    event.register(DataMaps.STEM_BLOCKS);
    event.register(DataMaps.GROVE_ACTION_REPUTATIONS);
    event.register(DataMaps.GROVE_RANKS);
    event.register(DataMaps.HARVEST_SEED_TO_CROP);
    event.register(DataMaps.GROWTH_SEED_TO_CROP);
    event.register(DataMaps.GROVE_POWER_GENERATORS);
    event.register(DataMaps.GROVE_GENERATION_ENTRIES);
    event.register(DataMaps.ADDITIONAL_ANIMAL_HARVEST_LOOT_TABLES);
    event.register(DataMaps.GROVE_ACTION_ICONS);
    event.register(DataMaps.RITUAL_MODIFIER_ICONS);
    event.register(DataMaps.RITUAL_MODIFIER_RESTRICTED);
    event.register(DataMaps.SPELL_MODIFIER_RESTRICTED);
    event.register(DataMaps.AUGMENTATION_DATA);
    event.register(DataMaps.EXTRA_CROP_DATA);
    event.register(DataMaps.EXTRA_CROP_CHANCE);
    event.register(DataMaps.DIMENSION_ITEM);
    event.register(DataMaps.ENTITY_AUGMENTATION_DATA);
    event.register(DataMaps.SHATTER_COST_MULTIPLIERS);
  }

  private static void callInit(Holder<?> holder) {
    if (holder.value() instanceof IDataMapInitialize<?> init) {
      init.performInit(holder);
    }
  }

  @SubscribeEvent
  public static void onDataReloaded(DataMapsUpdatedEvent event) {
    var reference = event.getRegistry().getAny().orElse(null);
    if (reference == null) {
      return;
    }

    if (event.getRegistry().key().equals(Registries.ITEM)) {
      DataMaps.DIMENSION_LOOKUP.clear();
    }

    if (reference.value() instanceof IDataMapInitialize<?>) {
      event.getRegistry().holders().forEach(DataEventHandler::callInit);
    }
  }

  @SubscribeEvent
  public static void onTagSync(TagsUpdatedEvent event) {
    if (event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
      AnimalHarvestRecipe.cached = AnimalHarvestRecipe.getServerRecipes(event.getRegistryAccess().asGetterLookup());
    }
  }

  @SubscribeEvent
  public static void onDataPackSync(OnDatapackSyncEvent event) {
    if (AnimalHarvestRecipe.cached != null) {
      var cache = AnimalHarvestRecipe.cached;
      if (!cache.recipes().isEmpty()) {
        ClientboundAnimalHarvestSyncPacket packet = new ClientboundAnimalHarvestSyncPacket(cache.recipes());

        event.getRelevantPlayers().forEach(o -> {
          if (ConfigManager.DEBUG_JEI.getAsBoolean()) {
            RootsAPI.LOG.error("Sending animal harvest sync packet to player: {}", o.getGameProfile().getName());
          }
          o.connection.send(packet);
        });
      } else {
        if (ConfigManager.DEBUG_JEI.getAsBoolean()) {
          // TODO: "Empty recipes" sync packet
          RootsAPI.LOG.error("Animal harvest recipe cache is empty at datapack sync event, no sync packet sent");
        }
      }
    } else {
      if (ConfigManager.DEBUG_JEI.getAsBoolean()) {
        RootsAPI.LOG.error("Animal harvest recipe cache is null on datapack sync event");
      }
    }
  }

  @SubscribeEvent
  public static void onBrewingRecipe(RegisterBrewingRecipesEvent event) {
    event.getBuilder().addContainerRecipe(Items.SPLASH_POTION, ModItems.MOONGLOW.get(), Items.LINGERING_POTION);
    event.getBuilder().addMix(Potions.WATER, ModItems.BAFFLECAP.get(), Potions.AWKWARD);
    event.getBuilder().addStartMix(ModItems.CLOUD_BERRY.get(), Potions.LONG_WATER_BREATHING);
    event.getBuilder().addStartMix(ModItems.DEWGONIA.get(), Potions.LONG_SWIFTNESS);
    event.getBuilder().addStartMix(ModItems.INFERNO_BULB.get(), Potions.FIRE_RESISTANCE);
    event.getBuilder().addStartMix(ModItems.PERESKIA.get(), Potions.STRONG_HEALING);
    event.getBuilder().addStartMix(ModItems.STONEPETAL.get(), Potions.LEAPING);
    event.getBuilder().addStartMix(ModItems.SPIRITLEAF.get(), Potions.REGENERATION);
    event.getBuilder().addStartMix(ModItems.STALICRIPE.get(), Potions.STRENGTH);
    event.getBuilder().addStartMix(ModItems.WILDEWHEET.get(), Potions.NIGHT_VISION);
  }


  @SubscribeEvent
  public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, context) -> {
      if (blockEntity instanceof InventoryBlockEntity ibe) {
        return ibe.getInventory();
      }

      return null;
    }, ModBlocks.DISPLAY_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get(), ModBlocks.GROVE_PEDESTAL.get(), ModBlocks.RITUAL_PEDESTAL.get(), ModBlocks.FUNGAL_TRANSMUTER.get());
    event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, context) -> {
      if (blockEntity instanceof WildwoodChestBlockEntity wbe) {
        return new InvWrapper(wbe);
      }

      return null;
    }, ModBlocks.WILDWOOD_CHEST.get());

    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.HERB_POUCH_CONTENTS.get(), 9), ModItems.HERB_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.COMPONENT_POUCH_CONTENTS.get(), 18), ModItems.COMPONENT_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.APOTHECARY_POUCH_CONTENTS.get(), 27), ModItems.APOTHECARY_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.SYLVAN_POUCH_CONTENTS.get(), 36), ModItems.SYLVAN_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.QUIVER_CONTENTS.get(), 6), ModItems.WILDWOOD_QUIVER.get());
  }
}
