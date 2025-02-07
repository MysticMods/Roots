package mysticmods.roots.api.test.world;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TagMatchWorldTest extends WorldTest {
  public static final MapCodec<TagMatchWorldTest> CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag")
      .xmap(TagMatchWorldTest::new, test -> test.tag);
  public static final StreamCodec<ByteBuf, TagMatchWorldTest> STREAM_CODEC = ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC.map(TagMatchWorldTest::new, o -> o.tag);
  public static final ResourceKey<WorldTestType<?>> TAG_MATCH_TEST_KEY = ResourceKey.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.rl("tag_match_test"));
  private final TagKey<Block> tag;

  public TagMatchWorldTest(TagKey<Block> tag) {
    this.tag = tag;
  }

  public TagKey<Block> getTag() {
    return tag;
  }

  @Override
  public boolean test(BlockState state, RandomSource random) {
    return state.is(tag);
  }

  @Override
  protected WorldTestType<?> getType() {
    return RootsRegistries.WORLD_TEST_TYPES.get(TAG_MATCH_TEST_KEY);
  }

  public static class Type implements WorldTestType<TagMatchWorldTest> {

    @Override
    public MapCodec<TagMatchWorldTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TagMatchWorldTest> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
