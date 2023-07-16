package mysticmods.roots.test.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class BlockPropertyMatchTest extends RuleTest {
  public static final Codec<BlockPropertyMatchTest> CODEC = RecordCodecBuilder.create((codec) -> codec.group(BlockState.CODEC.fieldOf("state").forGetter((test) -> test.state), Codec.STRING.fieldOf("property").forGetter((test) -> test.property)).apply(codec, BlockPropertyMatchTest::new));

  private final BlockState state;
  private final String property;

  public BlockPropertyMatchTest(BlockState state, String property) {
    this.state = state;
    this.property = property;
  }

  public BlockPropertyMatchTest(BlockState state, Property<?> property) {
    this(state, property.getName());
  }

  public BlockPropertyMatchTest(Block block, Property<?> property) {
    this(block.defaultBlockState(), property);
  }

  @Override
  public boolean test(BlockState pState, RandomSource pRandom) {
    if (!state.is(pState.getBlock())) {
      return false;
    }

    Property<?> property = state.getBlock().getStateDefinition().getProperty(this.property);
    if (property == null || !pState.hasProperty(property)) {
      return false;
    }

    return compareProperty(state, pState, property);
  }

  private static <T extends Comparable<T>> boolean compareProperty(BlockState pSourceState, BlockState pTargetState, Property<T> pProperty) {
    return pSourceState.getValue(pProperty).equals(pTargetState.getValue(pProperty));
  }

  @Override
  protected RuleTestType<?> getType() {
    return ModFeatures.BLOCK_PROPERTY_MATCH_TEST.get();
  }
}
