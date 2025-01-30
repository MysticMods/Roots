package mysticmods.roots.api.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.test.world.PartialBlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record CanonicalRepresentation(List<Either<CanonicalBlock, CanonicalBlockState>> states, List<BlockState> resolvedStates) {
  public static Codec<CanonicalRepresentation> CODEC = Codec.either(CanonicalBlock.CODEC, CanonicalBlockState.CODEC)
      .listOf().xmap(CanonicalRepresentation::new, CanonicalRepresentation::states);
  public static StreamCodec<RegistryFriendlyByteBuf, CanonicalRepresentation> STREAM_CODEC = ByteBufCodecs.either(CanonicalBlock.STREAM_CODEC, CanonicalBlockState.STREAM_CODEC)
      .apply(ByteBufCodecs.list()).map(CanonicalRepresentation::new, CanonicalRepresentation::states);

  public CanonicalRepresentation (Object ... blocks) {
    this(Stream.of(blocks).map(CanonicalRepresentation::of).map(CanonicalBlockOrState::blockOrState).toList());
  }

  private CanonicalRepresentation (List<Either<CanonicalBlock, CanonicalBlockState>> states) {
    this(states, new ArrayList<>());
  }

  public List<BlockState> getStates () {
    if (resolvedStates.isEmpty() || resolvedStates.size() != states.size()) {
      resolvedStates.clear();
      states.forEach(
          either -> either.map(
              canonicalBlock -> resolvedStates.add(canonicalBlock.block().defaultBlockState()),
              canonicalBlockState -> resolvedStates.add(canonicalBlockState.state().build())
          )
      );
    }

    return resolvedStates;
  }

  public boolean place (Level level, BlockPos pos) {
    List<BlockState> states = getStates();

    if (pos.getY() >= level.getMaxBuildHeight() || pos.getY() + states.size() >= level.getMaxBuildHeight()) {
      return false;
    }

    BlockState state = level.getBlockState(pos);
    if (!state.isAir() && !state.canBeReplaced()) {
      return false;
    }

    for (int i = 0; i < states.size(); i++) {
      state = level.getBlockState(pos.above(i));
      if (!state.isAir() && !state.canBeReplaced()) {
        return false;
      }
    }

    int i = 0;
    for (BlockState blockState : states) {
      level.setBlock(pos.above(i), blockState, 3);
      i++;
    }

    return true;
  }

  private sealed interface CanonicalBlockOrState permits CanonicalBlock, CanonicalBlockState {
    @Nullable
    default Block block () {
      return null;
    }

    @Nullable
    default PartialBlockState state () {
      return null;
    }

    default Either<CanonicalBlock, CanonicalBlockState> blockOrState() {
      if (block() != null) {
        return Either.left((CanonicalBlock) this);
      } else {
        return Either.right((CanonicalBlockState) this);
      }
    }
  }

  private record CanonicalBlock(Block block) implements CanonicalBlockOrState{
    public static Codec<CanonicalBlock> CODEC = Block.CODEC.xmap(CanonicalBlock::new, CanonicalBlock::block).codec();
    public static StreamCodec<RegistryFriendlyByteBuf, CanonicalBlock> STREAM_CODEC = ByteBufCodecs.registry(Registries.BLOCK)
        .map(CanonicalBlock::new, CanonicalBlock::block);
  }

  private record CanonicalBlockState(PartialBlockState state) implements CanonicalBlockOrState {
    public static Codec<CanonicalBlockState> CODEC = PartialBlockState.CODEC.xmap(CanonicalBlockState::new, CanonicalBlockState::state);
    public static StreamCodec<RegistryFriendlyByteBuf, CanonicalBlockState> STREAM_CODEC = PartialBlockState.STREAM_CODEC.map(CanonicalBlockState::new, CanonicalBlockState::state);
  }

  private static CanonicalBlockOrState of (Object object) {
    if (object instanceof Block block) {
      return new CanonicalBlock(block);
    } else if (object instanceof PartialBlockState state) {
      return new CanonicalBlockState(state);
    } else {
      throw new IllegalArgumentException("Invalid object type");
    }
  }
}
