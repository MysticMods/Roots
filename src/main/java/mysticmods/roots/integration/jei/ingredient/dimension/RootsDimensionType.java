package mysticmods.roots.integration.jei.ingredient.dimension;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.datamap.DataMaps;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record RootsDimensionType(ResourceKey<Level> dimension, ItemStack icon) {
  public static final Codec<RootsDimensionType> CODEC = ResourceKey.codec(Registries.DIMENSION)
      .xmap(RootsDimensionType::new, RootsDimensionType::dimension);

  public RootsDimensionType(ResourceKey<Level> dimension) {
    this(dimension, DataMaps.getDimensionItem(dimension));
  }

  public RootsDimensionType(ResourceLocation location) {
    this(ResourceKey.create(Registries.DIMENSION, location));
  }

  public Component getName() {
    return Component.translatable("dimension." + dimension.location().toString().replace(':', '.'));
  }
}
