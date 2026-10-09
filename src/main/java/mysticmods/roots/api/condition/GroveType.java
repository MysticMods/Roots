package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public record GroveType(String name, TagKey<Grove> grove, TagKey<Block> tag) {
  public static final GroveType ANY = new GroveType("any", RootsTags.Groves.ANY, RootsTags.Blocks.GROVE_STONES);
  public static final GroveType PASTORAL = new GroveType("pastoral", RootsTags.Groves.PASTORAL, RootsTags.Blocks.GROVE_STONE_PASTORAL);
  public static final GroveType ELEMENTAL = new GroveType("elemental", RootsTags.Groves.ELEMENTAL, RootsTags.Blocks.GROVE_STONE_ELEMENTAL);
  public static final GroveType FAIRY = new GroveType("fairy", RootsTags.Groves.FAIRY, RootsTags.Blocks.GROVE_STONE_FAIRY);
  public static final GroveType FUNGAL = new GroveType("fungal", RootsTags.Groves.FUNGAL, RootsTags.Blocks.GROVE_STONE_FUNGAL);
  public static final GroveType CULTIVATION = new GroveType("cultivation", RootsTags.Groves.CULTIVATION, RootsTags.Blocks.GROVE_STONE_CULTIVATION);
  public static final GroveType TWILIGHT = new GroveType("twilight", RootsTags.Groves.TWILIGHT, RootsTags.Blocks.GROVE_STONE_TWILIGHT);
  public static final GroveType WILD = new GroveType("wild", RootsTags.Groves.WILD, RootsTags.Blocks.GROVE_STONE_WILD);
  public static final GroveType HOLLOW = new GroveType("hollow", RootsTags.Groves.HOLLOW, RootsTags.Blocks.GROVE_STONE_ELEMENTAL);

  public static final MapCodec<GroveType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("name")
          .forGetter(GroveType::name),
      TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("grove").forGetter(GroveType::grove),
      TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(GroveType::tag)
  ).apply(instance, GroveType::new));
  public static final Codec<GroveType> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveType> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, GroveType::name, ExtraStreamCodecs.GROVE_TAG_STREAM_CODEC, GroveType::grove, ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, GroveType::tag, GroveType::new);
}
