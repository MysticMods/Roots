package mysticmods.roots.api.herbs;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class Herb extends DescribedRegistryEntry<Herb> {
  private final Supplier<? extends ItemLike> item;

  public Herb(Supplier<? extends ItemLike> item) {
    this.item = item;
  }

  public ItemLike getItem() {
    return item.get();
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.HERB_REGISTRY.get().getKey(this);
  }

  @Override
  protected String getDescriptor() {
    return "herb";
  }
}
