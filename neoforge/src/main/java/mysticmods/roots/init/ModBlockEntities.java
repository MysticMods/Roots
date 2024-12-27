package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlockEntities {
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RootsAPI.MODID);

  public static final Holder<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, ModBlocks.RITUAL_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL, ModBlocks.REINFORCED_RITUAL_PEDESTAL, ModBlocks.GROVE_PEDESTAL).build(null));


  public static final BlockEntityEntry<PedestalBlockEntity> PEDESTAL = REGISTRATE.blockEntity("pedestal", PedestalBlockEntity::new).validBlocks(ModBlocks.RITUAL_PEDESTAL, ModBlocks.WILDWOOD_PEDESTAL, ModBlocks.REINFORCED_RITUAL_PEDESTAL, ModBlocks.GROVE_PEDESTAL).register();

  public static final BlockEntityEntry<MortarBlockEntity> MORTAR = REGISTRATE.blockEntity("mortar", MortarBlockEntity::new).validBlock(ModBlocks.MORTAR).register();

  public static final BlockEntityEntry<GroveCrafterBlockEntity> GROVE_CRAFTER = REGISTRATE.blockEntity("grove_crafter", GroveCrafterBlockEntity::new).validBlock(ModBlocks.GROVE_CRAFTER).register();

  public static final BlockEntityEntry<PyreBlockEntity> PYRE = REGISTRATE.blockEntity("pyre", PyreBlockEntity::new).validBlocks(ModBlocks.PYRE, ModBlocks.DECORATIVE_PYRE, ModBlocks.REINFORCED_PYRE).register();

  public static void register (IEventBus bus) {
    BLOCK_ENTITIES.register(bus);
  }

  public static void load() {
  }
}
