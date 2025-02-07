package mysticmods.roots.block.crop;

import com.google.common.base.Suppliers;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeetrootBlock;

import java.util.function.Supplier;

public class ThreeStageCropBlock extends BeetrootBlock {
  private final Supplier<? extends ItemLike> seedProvider;

  public ThreeStageCropBlock(Supplier<? extends ItemLike> seedProvider, Properties builder) {
    super(builder);
    this.seedProvider = Suppliers.memoize(seedProvider::get);
  }

  @Override
  protected int getBonemealAgeIncrease(Level pLevel) {
    return Mth.nextInt(pLevel.random, 2, 5);
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return seedProvider.get();
  }
}
