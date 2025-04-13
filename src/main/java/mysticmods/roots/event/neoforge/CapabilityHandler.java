package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilityHandler {
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
    }, ModBlocks.DISPLAY_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get(), ModBlocks.GROVE_PEDESTAL.get(), ModBlocks.RITUAL_PEDESTAL.get());

    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.HERB_POUCH_CONTENTS.get(), 9), ModItems.HERB_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.COMPONENT_POUCH_CONTENTS.get(), 18), ModItems.COMPONENT_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.APOTHECARY_POUCH_CONTENTS.get(), 27), ModItems.APOTHECARY_POUCH.get());
    event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, ModAttachments.FEY_POUCH_CONTENTS.get(), 27), ModItems.FEY_POUCH.get());
  }
}
