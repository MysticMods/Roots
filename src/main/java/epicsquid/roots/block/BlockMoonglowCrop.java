package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class BlockMoonglowCrop extends BlockCropBase {

  public BlockMoonglowCrop(@Nonnull String name, @Nonnull PlantType plantType) {
    super(name, plantType);
  }

  /**
   * Gets the seed to drop for the crop
   */
  @Override
  @Nonnull
  public Item getSeed() {
    return ModItems.moonglow_seed;
  }

  /**
   * Gets the crop to drop for the plant
   */
  @Override
  @Nonnull
  public Item getCrop() {
    return ModItems.moonglow_leaf;
  }

}
