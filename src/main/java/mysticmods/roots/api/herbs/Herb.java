package mysticmods.roots.api.herbs;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class Herb extends ForgeRegistryEntry<IHerb> implements IHerb {
  private final Supplier<? extends ItemLike> item;

  public Herb(Supplier<? extends ItemLike> item) {
    this.item = item;
  }

  @Override
  public ItemLike getItem() {
    return item.get();
  }
}
