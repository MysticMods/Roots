package mysticmods.roots.api.test.world;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

// TODO: BlockState match!
public abstract class WorldTest {
  public static final Codec<WorldTest> CODEC = RootsRegistries.WORLD_TEST_TYPES.byNameCodec()
      .dispatch("type", WorldTest::getType, WorldTestType::codec);
  public static final StreamCodec<RegistryFriendlyByteBuf, WorldTest> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.WORLD_TEST_TYPES)
      .dispatch(WorldTest::getType, WorldTestType::streamCodec);

  public abstract boolean test(BlockState state, RandomSource random);

  protected abstract WorldTestType<?> getType();
}
