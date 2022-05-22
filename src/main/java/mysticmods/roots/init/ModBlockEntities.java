package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import mysticmods.roots.block.entity.PedestalBlockEntity;
import mysticmods.roots.block.entity.MortarBlockEntity;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlockEntities {
   public static final BlockEntityEntry<PedestalBlockEntity> PEDESTAL = REGISTRATE.blockEntity("catalyst_plate", PedestalBlockEntity::new).validBlocks(ModBlocks.RITUAL_PEDESTAL, ModBlocks.GROVE_PEDESTAL).register();

  public static final BlockEntityEntry<MortarBlockEntity> MORTAR = REGISTRATE.blockEntity("mortar", MortarBlockEntity::new).validBlock(ModBlocks.MORTAR).register();

  public static void load() {
  }
}
