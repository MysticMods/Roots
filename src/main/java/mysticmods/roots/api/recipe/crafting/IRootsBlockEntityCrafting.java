package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

public interface IRootsBlockEntityCrafting<H extends IItemHandler, T extends BlockEntity> extends IRootsCrafting<H> {
  @Nullable
  T getBlockEntity();
}
