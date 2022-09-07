package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

import javax.annotation.Nullable;

public abstract class RootsTileCrafting<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity> extends RootsCrafting<H> implements IRootsTileCrafting<H, T> {
  protected T blockEntity;

  public RootsTileCrafting(H handler, T blockEntity, @Nullable Player player) {
    super(handler, player);
    this.blockEntity = blockEntity;
  }

  @Nullable
  @Override
  public Level getLevel() {
    return this.blockEntity.getLevel();
  }

  @Nullable
  @Override
  public T getBlockEntity() {
    return this.blockEntity;
  }
}
