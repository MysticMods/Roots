package mysticmods.roots.api.test.world;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AlwaysTrueWorldTest extends WorldTest {
  public static final AlwaysTrueWorldTest INSTANCE = new AlwaysTrueWorldTest();
  public static ResourceKey<WorldTestType<?>> ALWAYS_TRUE_TEST_KEY = ResourceKey.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.rl("always_true_test"));
  public static MapCodec<AlwaysTrueWorldTest> CODEC = MapCodec.unit(() -> INSTANCE);
  public static StreamCodec<RegistryFriendlyByteBuf, AlwaysTrueWorldTest> STREAM_CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public boolean test(BlockState state, RandomSource random) {
    return true;
  }

  @Override
  public BlockState getBlockState(HolderLookup.Provider provider) {
    return Blocks.AIR.defaultBlockState();
  }

  @Override
  public @Nullable Ingredient getIngredient() {
    return null;
  }

  @Override
  protected WorldTestType<?> getType() {
    return RootsRegistries.WORLD_TEST_TYPES.get(ALWAYS_TRUE_TEST_KEY);
  }

  public static class Type implements WorldTestType<AlwaysTrueWorldTest> {
    @Override
    public MapCodec<AlwaysTrueWorldTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AlwaysTrueWorldTest> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
