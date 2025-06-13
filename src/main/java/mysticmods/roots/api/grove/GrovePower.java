package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiFunction;

public interface GrovePower {
  int getMaxPower();

  int getUsedPower();

  void generateTick(ServerLevel level, BlockPos pos, BlockState state);
  void consumeTick (ServerLevel level, BlockPos pos, BlockState state);

  @SuppressWarnings("SpellCheckingInspection")
  sealed interface Congen permits Generator, Consumer {
    TagKey<Grove> tag();

    int value();

    static <T extends Congen> MapCodec<T> codec(BiFunction<TagKey<Grove>, Integer, T> builder) {
      return RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(RootsRegistries.Keys.GROVES)
              .fieldOf("tag").forGetter(Congen::tag),
          Codec.INT.fieldOf("value").forGetter(Congen::value)).apply(instance, builder));
    }

    static <T extends Congen> StreamCodec<ByteBuf, T> streamCodec(BiFunction<TagKey<Grove>, Integer, T> builder) {
      return StreamCodec.composite(ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), Congen::tag, ByteBufCodecs.VAR_INT, Congen::value, builder);
    }
  }

  record Generator(TagKey<Grove> tag, int value) implements Congen {
    public static final MapCodec<Generator> MAP_CODEC = Congen.codec(Generator::new);
    public static final Codec<Generator> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, Generator> STREAM_CODEC = Congen.streamCodec(Generator::new);
    public static final Codec<List<Generator>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<ByteBuf, List<Generator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public int generate(IGroveInstance grove, BlockPos pos) {
      if (!grove.asGrove().is(tag)) {
        return 0;
      }

      return value * grove.getRank();
    }
  }

  record Consumer(TagKey<Grove> tag, int value) implements Congen {
    public static final MapCodec<Consumer> MAP_CODEC = Congen.codec(Consumer::new);
    public static final Codec<Consumer> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, Consumer> STREAM_CODEC = Congen.streamCodec(Consumer::new);
    public static final Codec<List<Consumer>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<ByteBuf, List<Consumer>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
  }

  class BlockTracker {
    private final Object2IntMap<Block> countMap = new Object2IntOpenHashMap<>();
    private final int maxCount;

    private BlockTracker(int maxCount) {
      this.maxCount = maxCount;
    }

    public boolean count (Block block) {
      if (countMap.containsKey(block)) {
        int currentValue = countMap.getInt(block);
        if (currentValue >= maxCount) {
          return false;
        }
        countMap.put(block, currentValue + 1);
      } else {
        countMap.put(block, 1);
      }
      return true;
    }

    public static BlockTracker create (int maxCount) {
      return new BlockTracker(maxCount);
    }
  }

  static Set<Block> getAllBlocks (TagKey<Block> tag) {
    Set<Block> blocks = new ObjectOpenHashSet<>();
    for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
      blocks.add(holder.value());
    }
    return blocks;
  }

  enum Symmetry implements StringRepresentable {
    NONE,
    RADIAL_SAME_BLOCK,
    RADIAL_SAME_BLOCK_OR_TAG,
    RADIAL_DIFFERENT_SAME_TAG,
    RADIAL_NOT_MATCHING;

    public static Codec<Symmetry> CODEC = StringRepresentable.fromEnum(Symmetry::values);

    @Nullable
    public BlockPos getPaired(BlockPos start, BlockPos center) {
      if (this == NONE) {
        return null;
      }

      int y = start.getY();

      start = new BlockPos(start.getX(), 0, start.getZ());
      center = new BlockPos(center.getX(), 0, center.getZ());

      BlockPos newPos = start.subtract(center).multiply(-1).offset(center);

      return new BlockPos(newPos.getX(), y, newPos.getZ());
    }

    public boolean matches (Level level, TagKey<Block> tag, BlockPos start, BlockPos center) {
      BlockState state = level.getBlockState(start);
      if (!state.is(tag)) {
        return false;
      }

      BlockPos newPos = getPaired(start, center);
      if (newPos == null) {
        return true;
      }

      BlockState newState = level.getBlockState(newPos);
      if (this == RADIAL_SAME_BLOCK) {
        return newState.is(state.getBlock());
      } else if (this == RADIAL_SAME_BLOCK_OR_TAG) {
        return newState.is(tag);
      } else if (this == RADIAL_DIFFERENT_SAME_TAG) {
        return newState.is(tag) && !newState.is(state.getBlock());
      }

      return false;
    }

    @Override
    public String getSerializedName() {
      return name().toLowerCase(Locale.ROOT);
    }
  }

  record GenerationEntry(TagKey<Block> tag, int maxCount, Symmetry symmetry) {
    public static final MapCodec<GenerationEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(GenerationEntry::tag),
        Codec.INT.fieldOf("max_count").forGetter(GenerationEntry::maxCount),
        Symmetry.CODEC.optionalFieldOf("symmetry", Symmetry.NONE).forGetter(GenerationEntry::symmetry)
    ).apply(instance, GenerationEntry::new));
    public static final Codec<GenerationEntry> CODEC = MAP_CODEC.codec();
    public static final Codec<List<GenerationEntry>> LIST_CODEC = CODEC.listOf();
  }
}
