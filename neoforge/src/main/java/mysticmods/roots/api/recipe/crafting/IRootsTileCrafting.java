package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.level.block.entity.BlockEntity;



import javax.annotation.Nullable;

public interface IRootsTileCrafting<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity> extends IRootsCrafting<H> {
  @Nullable
  T getBlockEntity();
}
