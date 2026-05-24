package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public record GroveType(String name, TagKey<Block> tag) {
  public static final GroveType ANY = new GroveType("any", RootsTags.Blocks.GROVE_STONES);
  public static final GroveType PRIMAL = new GroveType("primal", RootsTags.Blocks.GROVE_STONE_PRIMAL);
  public static final GroveType ELEMENTAL = new GroveType("elemental", RootsTags.Blocks.GROVE_STONE_ELEMENTAL);
  public static final GroveType FAIRY = new GroveType("fairy", RootsTags.Blocks.GROVE_STONE_FAIRY);
  public static final GroveType FUNGAL = new GroveType("fungal", RootsTags.Blocks.GROVE_STONE_FUNGAL);
  public static final GroveType CULTIVATION = new GroveType("cultivation", RootsTags.Blocks.GROVE_STONE_CULTIVATION);
  public static final GroveType TWILIGHT = new GroveType("twilight", RootsTags.Blocks.GROVE_STONE_TWILIGHT);
  public static final GroveType WILD = new GroveType("wild", RootsTags.Blocks.GROVE_STONE_WILD);

  public static final MapCodec<GroveType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("name")
          .forGetter(GroveType::name),
      TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(GroveType::tag)
  ).apply(instance, GroveType::new));
  public static final Codec<GroveType> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveType> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, GroveType::name, ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, GroveType::tag, GroveType::new);
}
