package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.block.StateProperties;
import mysticmods.roots.api.condition.*;
import mysticmods.roots.api.grove.Rank;
import mysticmods.roots.block.GroveStoneBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record GroveStoneCondition(TagKey<Block> tag, GroveStoneState state, TagKey<Item>
                                  itemTag, Optional<Rank> minimumRank)
    implements ILevelCondition {
  public static final MapCodec<GroveStoneCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.BLOCK)
              .fieldOf("tag").forGetter(GroveStoneCondition::tag),
          GroveStoneState.CODEC.fieldOf("state").forGetter(GroveStoneCondition::state),
          TagKey.codec(Registries.ITEM).fieldOf("itemTag").forGetter(GroveStoneCondition::itemTag),
      Rank.CODEC.optionalFieldOf("minimumRank").forGetter(GroveStoneCondition::minimumRank))
      .apply(instance, GroveStoneCondition::new));

  private static final StateProperties.Part[] PARTS_TOP_DOWN = {StateProperties.Part.TOP, StateProperties.Part.MIDDLE, StateProperties.Part.BOTTOM};

  public GroveStoneCondition (GroveType grove, GroveStoneState state) {
    this(grove.tag(), state, grove.itemTag(), Optional.empty());
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public boolean place(Level level, BlockPos position) {
    RandomSource random = level.getRandom();
    Block block = BuiltInRegistries.BLOCK.getTag(tag).map(o -> o.getRandomElement(random).map(Holder::value).orElse(Blocks.AIR)).orElse(Blocks.AIR);
    if (block == Blocks.AIR) {
      throw new IllegalStateException("Couldn't resolve any block from the tag '" + tag.location() + "'!");
    }
    BlockState base = block.defaultBlockState();
    if (!base.hasProperty(GroveStoneBlock.PART)) {
      throw new IllegalStateException("The resolved block for the tag '" + tag.location() + "' (" + block.builtInRegistryHolder().getKey().location() + ") doesn't have the PART property!");
    }
    if (minimumRank.isPresent() && !base.hasProperty(GroveStoneBlock.RANK)) {
      throw new IllegalStateException("The resolved block for the tag '" + tag.location() + "' (" + block.builtInRegistryHolder().getKey().location() + ") doesn't have the RANK property while the condition specifies a minumum rank!");
    } else if (minimumRank.isPresent()) {
      final BlockState original = base;
      base = minimumRank.map(o -> original.setValue(GroveStoneBlock.RANK, o)).orElse(base);
    }
    level.setBlock(position, base.setValue(GroveStoneBlock.PART, StateProperties.Part.BOTTOM), 3);
    level.setBlock(position.above(), base.setValue(GroveStoneBlock.PART, StateProperties.Part.MIDDLE), 3);
    level.setBlock(position.above().above(), base.setValue(GroveStoneBlock.PART, StateProperties.Part.TOP), 3);
    return true;
  }

  @Override
  public @Nullable IConditionIngredient.Single getIngredient(Holder<ICondition> condition) {
    Component.translatable("").withStyle()
    return null;
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
      if (minimumRank().map(o -> blockState.getValue(GroveStoneBlock.RANK).compareTo(o) < 0).orElse(false)) {
        return Collections.emptySet();
      }
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
