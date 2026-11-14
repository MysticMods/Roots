package mysticmods.roots.api.attachment;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record KeybindsNotified(ImmutableSet<Item> notifiedItems) {
  public static final KeybindsNotified EMPTY = new KeybindsNotified(ImmutableSet.of());

  public static final Codec<KeybindsNotified> CODEC = BuiltInRegistries.ITEM.byNameCodec().listOf()
      .xmap(KeybindsNotified::new, o -> o.notifiedItems()
          .asList());
  public static final StreamCodec<RegistryFriendlyByteBuf, KeybindsNotified> STREAM_CODEC = ByteBufCodecs.registry(Registries.ITEM)
      .apply(ByteBufCodecs.list()).map(
          KeybindsNotified::new, o -> o.notifiedItems()
              .asList());

  public KeybindsNotified(List<Item> items) {
    this(ImmutableSet.copyOf(items));
  }

  public static KeybindsNotified include(Item item) {
    return new KeybindsNotified(ImmutableSet.of(item));
  }

  public static KeybindsNotified add(KeybindsNotified existing, Item newItem) {
    return new KeybindsNotified(ImmutableSet.<Item>builder().addAll(existing.notifiedItems()).add(newItem).build());
  }

  public KeybindsNotified add(Item newItem) {
    return add(this, newItem);
  }

  public KeybindsNotified add(ItemStack item) {
    return add(this, item.getItem());
  }

  public boolean has(ItemStack stack) {
    return has(stack.getItem());
  }

  public boolean has(Item item) {
    return notifiedItems.contains(item);
  }
}
