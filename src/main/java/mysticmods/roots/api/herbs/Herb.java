package mysticmods.roots.api.herbs;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class Herb extends ForgeRegistryEntry<IHerb> implements IHerb {
  private final Supplier<? extends IItemProvider> item;
  private final Supplier<? extends IItemProvider> seed;
  private final BlockEntry<? extends Block> crop;

  public Herb(Supplier<? extends IItemProvider> item, Supplier<? extends IItemProvider> seed, BlockEntry<? extends Block> crop) {
    this.item = item;
    this.seed = seed;
    this.crop = crop;
  }

  @Override
  public IItemProvider getItem() {
    return item.get();
  }

  @Override
  public IItemProvider getSeed() {
    return seed.get();
  }

  @Override
  public Block getCrop() {
    return crop.get();
  }
}
