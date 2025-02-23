package mysticmods.roots.block.crop;

import com.google.common.base.Suppliers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeetrootBlock;

import java.util.function.Supplier;

public class ThreeStageCropBlock extends BeetrootBlock {
  private final Supplier<? extends ItemLike> seedProvider;

  public ThreeStageCropBlock(Supplier<? extends ItemLike> seedProvider, Properties builder) {
    super(builder);
    this.seedProvider = Suppliers.memoize(seedProvider::get);
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return seedProvider.get();
  }
}
