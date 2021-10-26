package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import mysticmods.roots.blocks.entities.CatalystPlateBlockEntity;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlockEntities {
  public static final TileEntityEntry<CatalystPlateBlockEntity> CATALYST_PLATE = REGISTRATE.tileEntity("catalyst_plate", CatalystPlateBlockEntity::new).validBlock(ModBlocks.CATALYST_PLATE).register();

  public static void load () {
  }
}
