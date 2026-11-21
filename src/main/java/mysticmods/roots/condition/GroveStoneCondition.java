package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.block.StateProperties;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.condition.ILevelCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record GroveStoneCondition(TagKey<Block> tag, GroveStoneState state)
    implements ILevelCondition {
  public static final MapCodec<GroveStoneCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.BLOCK)
              .fieldOf("tag").forGetter(GroveStoneCondition::tag),
          GroveStoneState.CODEC.fieldOf("state").forGetter(GroveStoneCondition::state))
      .apply(instance, GroveStoneCondition::new));

  private static final StateProperties.Part[] PARTS_TOP_DOWN = {StateProperties.Part.TOP, StateProperties.Part.MIDDLE, StateProperties.Part.BOTTOM};

  public GroveStoneCondition (GroveType grove, GroveStoneState state) {
    this(grove.tag(), state);
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }

  @Nullable
  private static StateProperties.Part getPart(BlockState state) {
    return state.hasProperty(StateProperties.GroveStone.PART) ? state.getValue(StateProperties.GroveStone.PART) : null;
  }

  private static boolean isActive(BlockState state) {
    return state.hasProperty(StateProperties.ACTIVE) && state.getValue(StateProperties.ACTIVE);
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player) {
    Set<BlockPos> result = new HashSet<>();
    int active = 0;
    BlockPos cursor = pos.immutable();
    for (StateProperties.Part part : PARTS_TOP_DOWN) {
      BlockState blockState = level.getBlockState(cursor);
      if (!blockState.is(tag) || getPart(blockState) != part) {
        return Collections.emptySet();
      }
      if (isActive(blockState)) {
        active++;
      }
      result.add(cursor);
      cursor = cursor.below();
    }

    return switch (state) {
      case ANY -> result;
      case ACTIVE -> active == PARTS_TOP_DOWN.length ? result : Collections.emptySet();
      case INACTIVE -> active == 0 ? result : Collections.emptySet();
    };
  }

  public enum GroveStoneState implements StringRepresentable {
    ANY("any"),
    ACTIVE("active"),
    INACTIVE("inactive");

    public static final Codec<GroveStoneState> CODEC = StringRepresentable.fromEnum(GroveStoneState::values);

    private final String name;

    GroveStoneState(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return name;
    }
  }
}
