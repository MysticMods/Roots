package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.util.CycleTimer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public interface IConditionIngredient {
  ItemStack getVisual (CycleTimer timer);
  Component getDecoration (CycleTimer timer);
  Component getTitle (CycleTimer timer);
  Component getTooltip (CycleTimer timer);

  record Multi (List<ItemStack> visual, Optional<Component> decoration, Optional<Component> title, Optional<Component> tooltip) implements IConditionIngredient {
    public static final MapCodec<Multi> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ItemStack.STRICT_SINGLE_ITEM_CODEC
        .listOf().fieldOf("visual")
        .forGetter(Multi::visual), ComponentSerialization.CODEC.optionalFieldOf("decoration")
        .forGetter(Multi::decoration), ComponentSerialization.CODEC.optionalFieldOf("title")
        .forGetter(Multi::title), ComponentSerialization.CODEC.optionalFieldOf("tooltip")
        .forGetter(Multi::tooltip)
    ).apply(instance, Multi::new));
    public static final Codec<Multi> CODEC = MAP_CODEC.codec();
    public static final Codec<List<Multi>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, Multi> STREAM_CODEC = StreamCodec.composite(ItemStack.LIST_STREAM_CODEC, Multi::visual,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, Multi::decoration,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, Multi::title,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, Multi::tooltip, Multi::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<Multi>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    @Override
    public ItemStack getVisual(CycleTimer timer) {
      return timer.getCycled(visual);
    }

    @Override
    public Component getDecoration(CycleTimer timer) {
      return decoration.orElse(CommonComponents.EMPTY);
    }

    @Override
    public Component getTitle(CycleTimer timer) {
      return title.orElseGet(() -> this.getVisual(timer).getDisplayName());
    }

    @Override
    public Component getTooltip(CycleTimer timer) {
      return tooltip.orElse(CommonComponents.EMPTY);
    }
  }

  record Single(ItemStack visual,
                       Optional<Component> decoration,
                       Optional<Component> title,
                       Optional<Component> tooltip) implements IConditionIngredient {
    public static final MapCodec<Single> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ItemStack.STRICT_SINGLE_ITEM_CODEC
        .fieldOf("visual")
        .forGetter(Single::visual), ComponentSerialization.CODEC.optionalFieldOf("decoration")
        .forGetter(Single::decoration), ComponentSerialization.CODEC.optionalFieldOf("title")
        .forGetter(Single::title), ComponentSerialization.CODEC.optionalFieldOf("tooltip")
        .forGetter(Single::tooltip)
    ).apply(instance, Single::new));
    public static final Codec<Single> CODEC = MAP_CODEC.codec();
    public static final Codec<List<Single>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, Single> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC, Single::visual,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, Single::decoration,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, Single::title,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, Single::tooltip, Single::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<Single>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public ItemStack getVisual (CycleTimer timer) {
      return visual;
    }

    public Component getDecoration (CycleTimer timer) {
      return decoration.orElse(CommonComponents.EMPTY);
    }

    public Component getTitle (CycleTimer timer) {
      return title.orElseGet(visual::getDisplayName);
    }

    public Component getTooltip (CycleTimer timer) {
      return tooltip.orElse(CommonComponents.EMPTY);
    }
  }

  record Full (List<IConditionIngredient> list) implements IConditionIngredient {

    @Override
    public ItemStack getVisual(CycleTimer timer) {
      return timer.getCycled(list).getVisual(timer);
    }

    @Override
    public Component getDecoration(CycleTimer timer) {
      return timer.getCycled(list).getDecoration(timer);
    }

    @Override
    public Component getTitle(CycleTimer timer) {
      return timer.getCycled(list).getTitle(timer);
    }

    @Override
    public Component getTooltip(CycleTimer timer) {
      return timer.getCycled(list).getTooltip(timer);
    }
  }
}
