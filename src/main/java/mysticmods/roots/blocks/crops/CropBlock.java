package mysticmods.roots.blocks.crops;

import net.minecraft.util.IItemProvider;
import noobanidus.libs.noobutil.block.BaseBlocks;

import java.util.function.Supplier;

public class CropBlock extends BaseBlocks.CropsBlock {
  private final Supplier<? extends IItemProvider> seedProvider;

  public CropBlock(Properties builder, Supplier<? extends IItemProvider> seedProvider) {
    super(builder);
    this.seedProvider = seedProvider;
  }

  @Override
  protected IItemProvider getBaseSeedId() {
    return seedProvider.get();
  }
}
