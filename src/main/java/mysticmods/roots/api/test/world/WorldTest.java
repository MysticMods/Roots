package mysticmods.roots.api.test.world;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class WorldTest {
  public static final Codec<WorldTest> CODEC = RootsRegistries.WORLD_TEST_TYPES.byNameCodec()
      .dispatch("type", WorldTest::getType, WorldTestType::codec);
  public static final StreamCodec<RegistryFriendlyByteBuf, WorldTest> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.WORLD_TEST_TYPES)
      .dispatch(WorldTest::getType, WorldTestType::streamCodec);

  public abstract boolean test(BlockState state, RandomSource random);

  public abstract BlockState getBlockState(HolderLookup.Provider provider);

  @Nullable
  public abstract Ingredient getIngredient();

  protected abstract WorldTestType<?> getType();
}
