package mysticmods.roots.api.test.world;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMatchWorldTest extends WorldTest {
  public static final MapCodec<BlockMatchWorldTest> CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block")
      .xmap(BlockMatchWorldTest::new, test -> test.block);
  public static final StreamCodec<RegistryFriendlyByteBuf, BlockMatchWorldTest> STREAM_CODEC = ByteBufCodecs.registry(Registries.BLOCK)
      .map(BlockMatchWorldTest::new, test -> test.block);
  public static ResourceKey<WorldTestType<?>> BLOCK_MATCH_TEST_KEY = ResourceKey.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.rl("block_match_test"));

  private final Block block;

  public BlockMatchWorldTest(Block block) {
    this.block = block;
  }

  public Block getBlock() {
    return block;
  }

  @Override
  public boolean test(BlockState state, RandomSource random) {
    return state.is(this.block);
  }

  @Override
  protected WorldTestType<?> getType() {
    return RootsRegistries.WORLD_TEST_TYPES.get(BLOCK_MATCH_TEST_KEY);
  }

  public static class Type implements WorldTestType<BlockMatchWorldTest> {
    @Override
    public MapCodec<BlockMatchWorldTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockMatchWorldTest> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
