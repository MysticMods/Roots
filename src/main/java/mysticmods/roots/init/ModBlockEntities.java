package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VisibleBlockEntity>> VISIBLE = BLOCK_ENTITIES.register("visible", () -> BlockEntityType.Builder.of(VisibleBlockEntity::new, ModBlocks.WILD_ROOTS.get(), ModBlocks.HANGING_GROVE_MOSS.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SylvanLightBlockEntity>> SYLVAN_LIGHT = BLOCK_ENTITIES.register("sylvan_light", () -> BlockEntityType.Builder.of(SylvanLightBlockEntity::new, ModBlocks.SYLVAN_LIGHT.get())
      .build(null));

  static {
    BLOCK_ENTITIES.addAlias(RootsAPI.rl("fey_light"), RootsAPI.rl("sylvan_light"));
  }

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, ModBlocks.RITUAL_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get(), ModBlocks.REINFORCED_RITUAL_PEDESTAL.get(), ModBlocks.DISPLAY_PEDESTAL.get(), ModBlocks.GROVE_PEDESTAL.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FungalTransmuterBlockEntity>> FUNGAL_TRANSMUTER = BLOCK_ENTITIES.register("fungal_transmuter", () -> BlockEntityType.Builder.of(FungalTransmuterBlockEntity::new, ModBlocks.FUNGAL_TRANSMUTER.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnchantedTurfBlockEntity>> ENCHANTED_TURF = BLOCK_ENTITIES.register("enchanted_turf", () -> BlockEntityType.Builder.of(EnchantedTurfBlockEntity::new, ModBlocks.ENCHANTED_TURF.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MortarBlockEntity>> MORTAR = BLOCK_ENTITIES.register("mortar", () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GroveCrafterBlockEntity>> GROVE_CRAFTER = BLOCK_ENTITIES.register("grove_crafter", () -> BlockEntityType.Builder.of(GroveCrafterBlockEntity::new, ModBlocks.GROVE_CRAFTER.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PyreBlockEntity>> PYRE = BLOCK_ENTITIES.register("pyre", () -> BlockEntityType.Builder.of(PyreBlockEntity::new, ModBlocks.PYRE.get(), ModBlocks.REINFORCED_PYRE.get(), ModBlocks.SOUL_PYRE.get(), ModBlocks.REINFORCED_SOUL_PYRE.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GroveStoneBlockEntity>> GROVE_STONE = BLOCK_ENTITIES.register("grove_stone", () -> BlockEntityType.Builder.of(GroveStoneBlockEntity::new, ModBlocks.WILD_GROVE_STONE.get(), ModBlocks.FAIRY_GROVE_STONE.get(), ModBlocks.SPROUTING_GROVE_STONE.get(), ModBlocks.TWILIGHT_GROVE_STONE.get(), ModBlocks.FUNGAL_GROVE_STONE.get(), ModBlocks.ELEMENTAL_GROVE_STONE.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FairyHutBlockEntity>> FAIRY_HUT = BLOCK_ENTITIES.register("fairy_hut", () -> BlockEntityType.Builder.of(FairyHutBlockEntity::new, ModBlocks.BAFFLECAP_FAIRY_HUT.get(), ModBlocks.RED_FAIRY_HUT.get(), ModBlocks.WARPED_FAIRY_HUT.get(), ModBlocks.CRIMSON_FAIRY_HUT.get(), ModBlocks.BROWN_FAIRY_HUT.get())
      .build(null));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WildwoodChestBlockEntity>> WILDWOOD_CHEST = BLOCK_ENTITIES.register("wildwood_chest", () -> BlockEntityType.Builder.of(WildwoodChestBlockEntity::new, ModBlocks.WILDWOOD_CHEST.get())
      .build(null));

  public static void register(IEventBus bus) {
    BLOCK_ENTITIES.register(bus);
  }
}
