package mysticmods.roots.api.recipe.crafting;

import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

import javax.annotation.Nullable;

public interface IRootsTileCrafting<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity> extends IRootsCrafting<H> {
  @Nullable
  T getBlockEntity();
}
