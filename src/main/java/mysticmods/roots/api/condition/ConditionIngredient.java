package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ConditionIngredient(ResourceKey<ICondition> key, ItemStack visual,
                                  Optional<Component> decoration,
                                  Optional<Component> title,
                                  Optional<Component> tooltip,
                                  Optional<TagKey<Item>> tag) {
  public static final MapCodec<ConditionIngredient> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      ResourceKey.codec(RootsRegistries.Keys.CONDITIONS).fieldOf("key").forGetter(ConditionIngredient::key),
      ItemStack.STRICT_SINGLE_ITEM_CODEC
      .fieldOf("visual")
      .forGetter(ConditionIngredient::visual), ComponentSerialization.CODEC.optionalFieldOf("decoration")
      .forGetter(ConditionIngredient::decoration), ComponentSerialization.CODEC.optionalFieldOf("title")
      .forGetter(ConditionIngredient::title), ComponentSerialization.CODEC.optionalFieldOf("tooltip")
      .forGetter(ConditionIngredient::tooltip),
      TagKey.codec(Registries.ITEM).optionalFieldOf("tag").forGetter(ConditionIngredient::tag)
  ).apply(instance, ConditionIngredient::new));
  public static final Codec<ConditionIngredient> CODEC = MAP_CODEC.codec();
  public static final Codec<List<ConditionIngredient>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, ConditionIngredient> STREAM_CODEC = StreamCodec.composite(
      ResourceKey.streamCodec(RootsRegistries.Keys.CONDITIONS), ConditionIngredient::key,
      ItemStack.STREAM_CODEC, ConditionIngredient::visual,
      ComponentSerialization.OPTIONAL_STREAM_CODEC, ConditionIngredient::decoration,
      ComponentSerialization.OPTIONAL_STREAM_CODEC, ConditionIngredient::title,
      ComponentSerialization.OPTIONAL_STREAM_CODEC, ConditionIngredient::tooltip,
      ByteBufCodecs.optional(ExtraStreamCodecs.ITEM_TAG_STREAM_CODEC), ConditionIngredient::tag,
      ConditionIngredient::new);
  public static final StreamCodec<RegistryFriendlyByteBuf, List<ConditionIngredient>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public ConditionIngredient(Holder<ICondition> condition, ItemStack itemStack) {
    this(condition, itemStack, null, null);
  }

  public ConditionIngredient(Holder<ICondition> condition, ItemStack itemStack, TagKey<Item> tag) {
    this(condition, itemStack, null, tag);
  }

  public ConditionIngredient(Holder<ICondition> condition, ItemStack itemStack, Component description) {
    this(condition, itemStack, description, null);
  }

  public ConditionIngredient(Holder<ICondition> condition, ItemStack itemStack, @Nullable Component description, @Nullable TagKey<Item> itemTag) {
    this(condition.getKey(), itemStack, Optional.ofNullable(description), Optional.of(ICondition.getName(condition)), Optional.of(ICondition.getDescription(condition)), Optional.ofNullable(itemTag));
  }

/*  public static List<ConditionIngredient> fromItems(List<Item> itemList, Holder<ICondition> condition) {
    return fromItems(itemList, null, condition);
  }

  public static List<ConditionIngredient> fromItems(List<Item> itemList, @Nullable Component description, Holder<ICondition> condition) {
    List<ConditionIngredient> result = new ArrayList<>();
    for (Item item : itemList) {
      result.add(new ConditionIngredient(condition, new ItemStack(item), description, null));
    }
    return result;
  }*/

  public static List<ConditionIngredient> fromTag(TagKey<Item> itemTag, Holder<ICondition> condition) {
    return fromTag(itemTag, null, condition);
  }

  public static List<ConditionIngredient> fromTag(TagKey<Item> itemTag, @Nullable Component description, Holder<ICondition> condition) {
    List<ConditionIngredient> result = new ArrayList<>();
    for (Holder<Item> item : BuiltInRegistries.ITEM.getTagOrEmpty(itemTag)) {
      result.add(new ConditionIngredient(
          condition,
          new ItemStack(item.value()),
          description,
          itemTag
      ));
    }
    return result;
  }

  public static List<ConditionIngredient> fromItemLike(ItemLike item, Holder<ICondition> condition) {
    return fromItemLike(item, null, condition);
  }

  public static List<ConditionIngredient> fromItemLike(ItemLike item, @Nullable Component description, Holder<ICondition> condition) {
    return List.of(new ConditionIngredient(condition.getKey(), new ItemStack(item.asItem()), Optional.ofNullable(description), Optional.of(ICondition.getName(condition)), Optional.of(ICondition.getDescription(condition)), Optional.empty()));
  }

  public static List<ConditionIngredient> fromItemLikes(List<? extends ItemLike> item, Holder<ICondition> condition) {
    return fromItemLikes(item, null, condition);
  }

  public static List<ConditionIngredient> fromItemLikes(List<? extends ItemLike> itemList, @Nullable Component description, Holder<ICondition> condition) {
    List<ConditionIngredient> result = new ArrayList<>();
    for (ItemLike item : itemList) {
      result.add(new ConditionIngredient(condition, new ItemStack(item.asItem()), description, null));
    }
    return result;
  }

  public ResourceKey<ICondition> getKey() {
    return key;
  }

  public ItemStack getVisual() {
    return visual;
  }

  public Component getDecoration() {
    return decoration.orElse(CommonComponents.EMPTY);
  }

  public Component getTitle() {
    return title.orElseGet(visual::getDisplayName);
  }

  public Component getTooltip() {
    return tooltip.orElse(CommonComponents.EMPTY);
  }
}
