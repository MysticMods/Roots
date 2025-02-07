package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, ModBlocks.RITUAL_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get(), ModBlocks.REINFORCED_RITUAL_PEDESTAL.get(), ModBlocks.DISPLAY_PEDESTAL.get(), ModBlocks.GROVE_PEDESTAL.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MortarBlockEntity>> MORTAR = BLOCK_ENTITIES.register("mortar", () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GroveCrafterBlockEntity>> GROVE_CRAFTER = BLOCK_ENTITIES.register("grove_crafter", () -> BlockEntityType.Builder.of(GroveCrafterBlockEntity::new, ModBlocks.GROVE_CRAFTER.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PyreBlockEntity>> PYRE = BLOCK_ENTITIES.register("pyre", () -> BlockEntityType.Builder.of(PyreBlockEntity::new, ModBlocks.PYRE.get(), ModBlocks.REINFORCED_PYRE.get(), ModBlocks.SOUL_PYRE.get(), ModBlocks.REINFORCED_SOUL_PYRE.get())
      .build(null));

  public static void register(IEventBus bus) {
    BLOCK_ENTITIES.register(bus);
  }
}
