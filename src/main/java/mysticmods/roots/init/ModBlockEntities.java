package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlockEntities {
  public static final BlockEntityEntry<PedestalBlockEntity> PEDESTAL = REGISTRATE.blockEntity("pedestal", PedestalBlockEntity::new).validBlocks(ModBlocks.RITUAL_PEDESTAL, ModBlocks.WILDWOOD_PEDESTAL, ModBlocks.REINFORCED_RITUAL_PEDESTAL, ModBlocks.GROVE_PEDESTAL).register();

  public static final BlockEntityEntry<MortarBlockEntity> MORTAR = REGISTRATE.blockEntity("mortar", MortarBlockEntity::new).validBlock(ModBlocks.MORTAR).register();

  public static final BlockEntityEntry<GroveCrafterBlockEntity> GROVE_CRAFTER = REGISTRATE.blockEntity("grove_crafter", GroveCrafterBlockEntity::new).validBlock(ModBlocks.GROVE_CRAFTER).register();

  public static final BlockEntityEntry<PyreBlockEntity> PYRE = REGISTRATE.blockEntity("pyre", PyreBlockEntity::new).validBlocks(ModBlocks.PYRE, ModBlocks.DECORATIVE_PYRE, ModBlocks.REINFORCED_PYRE).register();

  public static void load() {
  }
}
