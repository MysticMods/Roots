package mysticmods.roots.api.grove;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.IntFunction;

public enum Symmetry implements StringRepresentable {
  NONE,
  RADIAL_SAME_BLOCK,
  RADIAL_SAME_BLOCK_OR_TAG,
  RADIAL_DIFFERENT_SAME_TAG,
  RADIAL_NOT_MATCHING;

  public static final Codec<Symmetry> CODEC = StringRepresentable.fromEnum(Symmetry::values);
  public static final IntFunction<Symmetry> BY_ID = ByIdMap.continuous(Symmetry::ordinal, Symmetry.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  public static final StreamCodec<ByteBuf, Symmetry> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Symmetry::ordinal);

  @Nullable
  public BlockPos getPairedPosition(BlockPos start, BlockPos center) {
    if (this == NONE) {
      return null;
    }

    int y = start.getY();
    int dx = start.getX() - center.getX();
    int dz = start.getZ() - center.getZ();

    return new BlockPos(center.getX() - dx, y, center.getZ() - dz);
  }

  private boolean matches(Level level, TagKey<Block> tag, BlockState state, BlockPos newPos) {
    BlockState newState = level.getBlockState(newPos);
    if (this == RADIAL_SAME_BLOCK) {
      return newState.is(state.getBlock());
    } else if (this == RADIAL_SAME_BLOCK_OR_TAG) {
      return newState.is(tag);
    } else if (this == RADIAL_DIFFERENT_SAME_TAG) {
      return newState.is(tag) && !newState.is(state.getBlock());
    } else if (this == RADIAL_NOT_MATCHING) {
      return !newState.is(tag);
    }

    return false;
  }

  public boolean matches(Level level, TagKey<Block> tag, BlockPos start, BlockPos center) {
    BlockState state = level.getBlockState(start);
    if (!state.is(tag)) {
      return false;
    }

    BlockPos newPos = getPairedPosition(start, center);
    if (newPos == null) {
      return true;
    }

    return matches(level, tag, state, newPos);
  }

  public Pair<Boolean, BlockPos> matchesWithPair(Level level, TagKey<Block> tag, BlockPos start, BlockPos center) {
    BlockState state = level.getBlockState(start);
    BlockPos paired = getPairedPosition(start, center);

    if (!state.is(tag)) {
      return Pair.of(false, paired);
    }

    if (paired == null) {
      return Pair.of(true, null);
    }

    return Pair.of(matches(level, tag, start, center), paired);
  }

  @Override
  public String getSerializedName() {
    return name().toLowerCase(Locale.ROOT);
  }

  public String getTranslationKey() {
    return "roots.symmetry." + getSerializedName();
  }

  public Component getName() {
    return Component.translatable(getTranslationKey());
  }

  public Component getTooltip() {
    return Component.translatable(getTranslationKey() + ".description");
  }
}
