package mysticmods.roots.api.herbs;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

// TODO: This relies on registrate?
public class Herb extends ForgeRegistryEntry<IHerb> implements IHerb {
  private final Supplier<? extends ItemLike> item;
  private final Supplier<? extends ItemLike> seed;
  private final BlockEntry<? extends Block> crop;

  public Herb(Supplier<? extends ItemLike> item, Supplier<? extends ItemLike> seed, BlockEntry<? extends Block> crop) {
    this.item = item;
    this.seed = seed;
    this.crop = crop;
  }

  @Override
  public ItemLike getItem() {
    return item.get();
  }

  @Override
  public ItemLike getSeed() {
    return seed.get();
  }

  @Override
  public Block getCrop() {
    return crop.get();
  }
}
