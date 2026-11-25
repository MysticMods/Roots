package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.block.StateProperties;
import mysticmods.roots.api.condition.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PillarCondition(TagKey<Block> tag, TagKey<Item> itemTag) implements ILevelCondition {
  public static final MapCodec<PillarCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(PillarCondition::tag), TagKey.codec(Registries.ITEM).fieldOf("itemTag").forGetter(PillarCondition::itemTag)).apply(instance, PillarCondition::new));

  private static final StateProperties.Part[] PARTS_TOP_DOWN = {StateProperties.Part.TOP, StateProperties.Part.MIDDLE, StateProperties.Part.BOTTOM};

  public PillarCondition(PillarType type) {
    this(type.pillar(), type.itemTag());
  }

  @Nullable
  private static StateProperties.Part getPart(BlockState state) {
    return state.hasProperty(StateProperties.GroveStone.PART) ? state.getValue(StateProperties.GroveStone.PART) : null;
  }

  @Override
  public boolean place(Level level, BlockPos position) {
    RandomSource random = level.getRandom();
    Block block = BuiltInRegistries.BLOCK.getTag(tag)
        .map(o -> o.getRandomElement(random).map(Holder::value).orElse(Blocks.AIR)).orElse(Blocks.AIR);
    if (block == Blocks.AIR) {
      throw new IllegalStateException("Couldn't resolve any block from the tag '" + tag.location() + "'!");
    }
    BlockState base = block.defaultBlockState();
    if (!base.hasProperty(StateProperties.GroveStone.PART)) {
      throw new IllegalStateException("The resolved block for the tag '" + tag.location() + "' (" + block.builtInRegistryHolder()
          .getKey().location() + ") doesn't have the PART property!");
    }
    level.setBlock(position, base.setValue(StateProperties.GroveStone.PART, StateProperties.Part.BOTTOM), 3);
    level.setBlock(position.above(), base.setValue(StateProperties.GroveStone.PART, StateProperties.Part.MIDDLE), 3);
    level.setBlock(position.above().above(), base.setValue(StateProperties.GroveStone.PART, StateProperties.Part.TOP), 3);
    return true;
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player) {
    Set<BlockPos> result = new HashSet<>();
    BlockPos cursor = pos.immutable();
    for (StateProperties.Part part : PARTS_TOP_DOWN) {
      BlockState blockState = level.getBlockState(cursor);
      if (!blockState.is(tag) || getPart(blockState) != part) {
        return Collections.emptySet();
      }
      result.add(cursor);
      cursor = cursor.below();
    }
    return result;
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public List<ConditionIngredient> getIngredient(Holder<ICondition> condition) {
    return ConditionIngredient.fromTag(itemTag, condition);
  }
}
