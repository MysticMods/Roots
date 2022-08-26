package epicsquid.roots.block;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumPlantType;

public class BlockStalicripeCrop extends BlockCropBase {

  public BlockStalicripeCrop(@Nonnull String name, @Nonnull EnumPlantType plantType) {
    super(name, plantType);
  }

  /**
   * Gets the seed to drop for the crop
   */
  @Override
  @Nonnull
  public Item getSeed() {
    return ModItems.stalicripe;
  }

  /**
   * Gets the crop to drop for the plant
   */
  @Override
  @Nonnull
  public Item getCrop() {
    return ModItems.stalicripe;
  }

}
