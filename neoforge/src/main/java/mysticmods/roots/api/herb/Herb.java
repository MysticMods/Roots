package mysticmods.roots.api.herb;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Herb extends StyledRegistryEntry<Herb> {
  private static final Map<Item, Herb> herbCache = new HashMap<>();
  private final Supplier<? extends ItemLike> item;
  private final TagKey<Item> tag;

  private final Holder.Reference<Herb> builtInRegistryHolder = RootsRegistries.HERBS.createIntrusiveHolder(this);

  @Deprecated
  public Holder.Reference<Herb> builtInRegistryHolder() {
    return this.builtInRegistryHolder;
  }

  public Herb(Supplier<? extends ItemLike> item, TagKey<Item> tag, ChatFormatting color) {
    this.item = item;
    this.tag = tag;
    this.color = color;
  }

  /*@Nullable
  // TODO: this should go somewhere else
  public static Herb getHerb(ItemStack stack) {
    if (!stack.is(RootsTags.Items.HERBS)) {
      return null;
    }
    Herb potential = herbCache.get(stack.getItem());
    if (potential != null) {
      return potential;
    }
    for (Herb herb : RootsRegistries.HERBS.get().getValues()) {
      if (stack.is(herb.getTag())) {
        herbCache.put(stack.getItem(), herb);
        return herb;
      }
    }

    return null;
  }*/

  public ItemLike getItem() {
    return item.get();
  }

  public TagKey<Item> getTag() {
    return tag;
  }

  public boolean is(ResourceLocation location) {
    return builtInRegistryHolder().is(location);
  }

  public boolean is(ResourceKey<Herb> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Herb>> predicate) {
    return builtInRegistryHolder().is(predicate);
  }

  public boolean is(TagKey<Herb> tag) {
    return builtInRegistryHolder().is(tag);
  }

  @Override
  public ResourceLocation getKey() {
    return builtInRegistryHolder.getKey().location();
  }

  @Override
  protected String getDescriptor() {
    return "herb";
  }
}
