package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mysticmods.roots.block.entity.CatalystPlateBlockEntity;
import mysticmods.roots.block.entity.MortarBlockEntity;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlockEntities {
   public static final BlockEntityEntry<CatalystPlateBlockEntity> CATALYST_PLATE = REGISTRATE.blockEntity("catalyst_plate", CatalystPlateBlockEntity::new).validBlock(ModBlocks.CATALYST_PLATE).register();

  public static final BlockEntityEntry<MortarBlockEntity> MORTAR = REGISTRATE.blockEntity("mortar", MortarBlockEntity::new).validBlock(ModBlocks.MORTAR).register();

  public static void load() {
  }
}
