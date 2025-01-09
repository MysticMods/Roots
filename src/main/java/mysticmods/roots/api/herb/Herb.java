package mysticmods.roots.api.herb;

import mysticmods.roots.api.data.DataMaps;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Herb extends StyledRegistryEntry<Herb> {
  private final Holder<Item> item;
  private final TagKey<Item> tag;
  private String descriptionId;

  public Holder<Herb> builtInRegistryHolder() {
    return RootsRegistries.HERBS.wrapAsHolder(this);
  }

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("herb", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public Herb(Holder<Item> item, TagKey<Item> tag, ChatFormatting color) {
    this.item = item;
    this.tag = tag;
    this.color = color;
  }

  // TODO: ItemStack data map
  @Nullable
  public static Herb getHerb (ItemStack stack) {
    return stack.getItemHolder().getData(DataMaps.HERB_ITEM_DATA);
  }

  public Holder<Item> getItem() {
    return item;
  }

  public TagKey<Item> getTag() {
    return tag;
  }

  public boolean is(ResourceLocation location) {
    return builtInRegistryHolder().is(location);
  }

  public boolean is (Herb herb) {
    return herb == this;
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
  protected String getDescriptor() {
    return "herb";
  }
}
