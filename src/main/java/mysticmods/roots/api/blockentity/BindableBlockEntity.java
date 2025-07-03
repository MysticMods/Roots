package mysticmods.roots.api.blockentity;

import net.minecraft.core.BlockPos;

public interface BindableBlockEntity {
  BlockPos getBoundPosition ();
  void setBoundPosition (BlockPos position);
}
