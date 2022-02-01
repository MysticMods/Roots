package mysticmods.roots.api.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

import javax.annotation.Nullable;

public abstract class RootsTileCrafting<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity> extends RootsCrafting<H> implements IRootsTileCrafting<H, T> {
  protected T blockEntity;

  public RootsTileCrafting(H handler, T blockEntity, @Nullable PlayerEntity player) {
    super(handler, player);
    this.blockEntity = blockEntity;
  }

  @Nullable
  @Override
  public World getLevel() {
    return this.blockEntity.getLevel();
  }

  @Nullable
  @Override
  public T getBlockEntity() {
    return this.blockEntity;
  }
}
