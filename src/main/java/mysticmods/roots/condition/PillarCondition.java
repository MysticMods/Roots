package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.block.StateProperties;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.PillarType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record PillarCondition(TagKey<Block> tag) implements ILevelCondition {
  public static final MapCodec<PillarCondition> MAP_CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag")
      .xmap(PillarCondition::new, PillarCondition::tag);

  private static final StateProperties.Part[] PARTS_TOP_DOWN = {StateProperties.Part.TOP, StateProperties.Part.MIDDLE, StateProperties.Part.BOTTOM};

  public PillarCondition(PillarType type) {
    this(type.pillar());
  }

  @Nullable
  private static StateProperties.Part getPart(BlockState state) {
    return state.hasProperty(StateProperties.GroveStone.PART) ? state.getValue(StateProperties.GroveStone.PART) : null;
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
}
