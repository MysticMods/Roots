package epicsquid.roots.block.runes;

import net.minecraft.block.Block;

@SuppressWarnings("deprecation")
public class TrampleBlock extends Block {
  public static int SAFE_RANGE_X = 30;
  public static int SAFE_RANGE_Y = 5;
  public static int SAFE_RANGE_Z = 30;

  public TrampleBlock(Properties properties) {
    super(properties);
  }
}
