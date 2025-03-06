package mysticmods.roots.api.test.world;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class PartialBlockStateMatchWorldTest extends WorldTest {
  public static final MapCodec<PartialBlockStateMatchWorldTest> CODEC = PartialBlockState.CODEC.fieldOf("partial_block_state")
      .xmap(PartialBlockStateMatchWorldTest::new, test -> test.partialBlockState);
  public static final StreamCodec<RegistryFriendlyByteBuf, PartialBlockStateMatchWorldTest> STREAM_CODEC = PartialBlockState.STREAM_CODEC.map(PartialBlockStateMatchWorldTest::new, test -> test.partialBlockState);
  public static final ResourceKey<WorldTestType<?>> PARTIAL_BLOCK_STATE_MATCH_TEST_KEY = ResourceKey.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.rl("partial_block_state_match_test"));

  private final PartialBlockState partialBlockState;

  public PartialBlockStateMatchWorldTest(PartialBlockState partialBlockState) {
    this.partialBlockState = partialBlockState;
  }

  public PartialBlockState getPartialBlockState() {
    return partialBlockState;
  }

  @Override
  public boolean test(BlockState state, RandomSource random) {
    return partialBlockState.test(state);
  }

  @Override
  public BlockState getBlockState(HolderLookup.Provider provider) {
    return getPartialBlockState().build();
  }

  @Override
  protected WorldTestType<?> getType() {
    return RootsRegistries.WORLD_TEST_TYPES.get(PARTIAL_BLOCK_STATE_MATCH_TEST_KEY);
  }

  public static class Type implements WorldTestType<PartialBlockStateMatchWorldTest> {
    @Override
    public MapCodec<PartialBlockStateMatchWorldTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, PartialBlockStateMatchWorldTest> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
