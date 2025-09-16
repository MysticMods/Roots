package mysticmods.roots.integration.jei.ingredient.dimension;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.datamap.DataMaps;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record DimensionType (ResourceKey<Level> dimension, ItemStack icon){
  public static final Codec<DimensionType> CODEC = ResourceKey.codec(Registries.DIMENSION).xmap(DimensionType::new, DimensionType::dimension);

  public DimensionType(ResourceKey<Level> dimension) {
    this(dimension, DataMaps.getDimensionItem(dimension));
  }

  public DimensionType (ResourceLocation location) {
    this(ResourceKey.create(Registries.DIMENSION, location));
  }
}
