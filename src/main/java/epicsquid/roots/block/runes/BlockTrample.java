package epicsquid.roots.block.runes;

import epicsquid.mysticallib.block.BlockTEBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public class BlockTrample extends BlockTEBase {
  public static int SAFE_RANGE_X = 30;
  public static int SAFE_RANGE_Y = 5;
  public static int SAFE_RANGE_Z = 30;

  public BlockTrample(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
  }
}
