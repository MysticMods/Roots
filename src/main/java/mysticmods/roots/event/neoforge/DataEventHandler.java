package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.network.client.ClientboundAnimalHarvestSyncPacket;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataEventHandler {
  @SubscribeEvent
  public static void registerDataMaps(RegisterDataMapTypesEvent event) {
    event.register(DataMaps.SPELL_COST_DATA);
    event.register(DataMaps.SPELL_MODIFIER_COST_DATA);
    event.register(DataMaps.RITUAL_MODIFIER_COST_DATA);
    event.register(DataMaps.HERB_ITEM_DATA);
    event.register(DataMaps.SPELL_PROPERTY_DATA);
    event.register(DataMaps.RITUAL_PROPERTY_DATA);
    event.register(DataMaps.RITUAL_DISPLAY_ITEM);
    event.register(DataMaps.RITUAL_MODIFIER_RITUAL);
    event.register(DataMaps.SPELL_DISPLAY_ITEM);
    event.register(DataMaps.SPELL_MODIFIER_PARENT);
    event.register(DataMaps.SPELL_MODIFIER_SPELL);
    event.register(DataMaps.SPROUT_BREEDING_ITEM_CHANCE);
    event.register(DataMaps.GROWTH_RECORDS);
    event.register(DataMaps.HARVEST_RECORDS);
    event.register(DataMaps.STEM_BLOCKS);
    event.register(DataMaps.GROVE_ACTION_REPUTATIONS);
    event.register(DataMaps.GROVE_RANKS);
    event.register(DataMaps.OPERATION_COST);
    event.register(DataMaps.HARVEST_SEED_TO_CROP);
    event.register(DataMaps.GROWTH_SEED_TO_CROP);
    event.register(DataMaps.GROVE_POWER_GENERATORS);
    event.register(DataMaps.GROVE_GENERATION_ENTRIES);
    event.register(DataMaps.ADDITIONAL_ANIMAL_HARVEST_LOOT_TABLES);
    event.register(DataMaps.GROVE_ACTION_ICONS);
  }

  private static void callInit(Holder<?> holder) {
    if (holder.value() instanceof IDataMapInitialize<?> init) {
      init.performInit(holder);
    }
  }

  @SubscribeEvent
  public static void onDataReloaded(DataMapsUpdatedEvent event) {
    if (event.getCause() == DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD) {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      if (server != null) {
        server.overworld()
            .setData(ModAttachments.ANIMAL_HARVEST_RECIPE_CACHE.get(), AnimalHarvestRecipe.getServerRecipes(server.reloadableRegistries()
                .lookup()));
      }
    }

    var reference = event.getRegistry().getAny().orElse(null);
    if (reference == null) {
      return;
    }

    if (reference.value() instanceof IDataMapInitialize<?>) {
      event.getRegistry().holders().forEach(DataEventHandler::callInit);
    }
  }

  @SubscribeEvent
  public static void onDataPackSync(OnDatapackSyncEvent event) {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    var cache = server.overworld().getData(ModAttachments.ANIMAL_HARVEST_RECIPE_CACHE.get());
    if (cache.recipes().isEmpty()) {
      return;
    }

    ClientboundAnimalHarvestSyncPacket packet = new ClientboundAnimalHarvestSyncPacket(cache.recipes());

    event.getRelevantPlayers().forEach(o -> {
      o.connection.send(packet);
    });
  }

  @SubscribeEvent
  public static void onBrewingRecipe(RegisterBrewingRecipesEvent event) {
    event.getBuilder().addContainerRecipe(Items.SPLASH_POTION, ModItems.BAFFLECAP.get(), Items.LINGERING_POTION);
  }


  @SubscribeEvent
  public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlock(Capabilities.ItemHandler.BLOCK, new IBlockCapabilityProvider<>() {
      @Override
      public @Nullable IItemHandler getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction context) {
        if (blockEntity instanceof InventoryBlockEntity ibe) {
          return ibe.getInventory();
        }

        return null;
      }
    }, ModBlocks.DISPLAY_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get(), ModBlocks.GROVE_PEDESTAL.get(), ModBlocks.RITUAL_PEDESTAL.get(), ModBlocks.FUNGAL_TRANSMUTER.get());

    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.HERB_POUCH_CONTENTS.get(), 9), ModItems.HERB_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.COMPONENT_POUCH_CONTENTS.get(), 18), ModItems.COMPONENT_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.APOTHECARY_POUCH_CONTENTS.get(), 27), ModItems.APOTHECARY_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.SYLVAN_POUCH_CONTENTS.get(), 36), ModItems.SYLVAN_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.QUIVER_CONTENTS.get(), 6), ModItems.WILDWOOD_QUIVER.get());
  }
}
