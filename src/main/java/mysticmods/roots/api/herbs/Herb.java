package mysticmods.roots.api.herbs;

import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class Herb extends StyledRegistryEntry<Herb> {
  private final Supplier<? extends ItemLike> item;
  private final TagKey<Item> tag;

  public Herb(Supplier<? extends ItemLike> item, TagKey<Item> tag, ChatFormatting color) {
    this.item = item;
    this.tag = tag;
    this.color = color;
  }

  public ItemLike getItem() {
    return item.get();
  }

  public TagKey<Item> getTag() {
    return tag;
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
