package mysticmods.roots.api.herbs;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Herb extends StyledRegistryEntry<Herb> {
  private static final Map<Item, Herb> herbCache = new HashMap<>();
  private final Supplier<? extends ItemLike> item;
  private final TagKey<Item> tag;

  public Herb(Supplier<? extends ItemLike> item, TagKey<Item> tag, ChatFormatting color) {
    this.item = item;
    this.tag = tag;
    this.color = color;
  }

  @Nullable
  // TODO: this should go somewhere else
  public static Herb getHerb(ItemStack stack) {
    if (!stack.is(RootsAPI.Tags.Items.Herbs.HERBS)) {
      return null;
    }
    Herb potential = herbCache.get(stack.getItem());
    if (potential != null) {
      return potential;
    }
    for (Herb herb : Registries.HERB_REGISTRY.get().getValues()) {
      if (stack.is(herb.getTag())) {
        herbCache.put(stack.getItem(), herb);
        return herb;
      }
    }

    return null;
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
