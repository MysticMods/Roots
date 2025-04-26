package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.init.ModConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public record GroveStoneCondition(GroveType groveType, boolean requireValid,
                                  boolean requireInvalid) implements ILevelCondition {
  public static final MapCodec<GroveStoneCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(GroveType.CODEC.fieldOf("grove_type")
              .forGetter(GroveStoneCondition::groveType),
          Codec.BOOL.optionalFieldOf("require_active", false).forGetter(GroveStoneCondition::requireValid),
          Codec.BOOL.optionalFieldOf("require_inactive", false).forGetter(GroveStoneCondition::requireInvalid))
      .apply(instance, GroveStoneCondition::new));
  public static final Codec<GroveStoneCondition> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveStoneCondition> STREAM_CODEC = StreamCodec.composite(GroveType.STREAM_CODEC, GroveStoneCondition::groveType, ByteBufCodecs.BOOL, GroveStoneCondition::requireValid, ByteBufCodecs.BOOL, GroveStoneCondition::requireInvalid, GroveStoneCondition::new);

  public GroveStoneCondition(GroveType groveType, boolean requireValid) {
    this(groveType, requireValid, false);
  }

  @Nullable
  private StateProperties.Part getPart(BlockState state) {
    if (!state.hasProperty(StateProperties.GroveStone.PART)) {
      return null;
    } else {
      return state.getValue(StateProperties.GroveStone.PART);
    }
  }

  private boolean getValid(BlockState state) {
    if (!state.hasProperty(StateProperties.ACTIVE)) {
      return false;
    } else {
      return state.getValue(StateProperties.ACTIVE);
    }
  }

  @Override
  public CanonicalRepresentation getRepresentation() {
    var tag = BuiltInRegistries.BLOCK.getTag(groveType.tag());
    if (tag.isEmpty()) {
      throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the grove type tag " + groveType + " is empty");
    }
    BlockState state = tag.get().get(0).value().defaultBlockState();
    return fromBlockState(state, requireValid, requireInvalid);
  }

  public static CanonicalRepresentation fromBlockState(BlockState state, boolean requireValid, boolean requireInvalid) {
    BlockState bottom = state.setValue(GroveStoneBlock.PART, StateProperties.Part.BOTTOM)
        .setValue(GroveStoneBlock.ACTIVE, requireValid || !requireInvalid);
    BlockState middle = state.setValue(GroveStoneBlock.PART, StateProperties.Part.MIDDLE)
        .setValue(GroveStoneBlock.ACTIVE, requireValid || !requireInvalid);
    BlockState top = state.setValue(GroveStoneBlock.PART, StateProperties.Part.TOP)
        .setValue(GroveStoneBlock.ACTIVE, requireValid || !requireInvalid);
    //noinspection rawtypes
    Property[] properties = new Property[]{StateProperties.GroveStone.PART, StateProperties.ACTIVE, StateProperties.GroveStone.FACING};
    return new CanonicalRepresentation(new PartialBlockState(bottom, properties), new PartialBlockState(middle, properties), new PartialBlockState(top, properties));
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
    BlockState initial = level.getBlockState(pos);
    // If the initial position isn't the capstone, we don't care
    if (!initial.is(groveType.tag()) && getPart(initial) != StateProperties.Part.TOP) {
      return Collections.emptySet();
    }

    int validCount = 0;
    int invalidCount = 0;
    if (getValid(initial)) {
      validCount++;
    } else {
      invalidCount++;
    }

    // Keep a note of which blockpositions are part of this pillar
    Set<BlockPos> result = new HashSet<>();
    result.add(pos.immutable());
    BlockPos pPos = pos.below();

    initial = level.getBlockState(pPos);
    if (!initial.is(groveType.tag()) && getPart(initial) != StateProperties.Part.MIDDLE) {
      return Collections.emptySet();
    }

    result.add(pPos.immutable());

    if (getValid(initial)) {
      validCount++;
    } else {
      invalidCount++;
    }

    pPos = pPos.below();
    initial = level.getBlockState(pPos);
    if (!initial.is(groveType.tag()) && getPart(initial) != StateProperties.Part.BOTTOM) {
      return Collections.emptySet();
    }

    result.add(pPos.immutable());

    if (getValid(initial)) {
      validCount++;
    } else {
      invalidCount++;
    }

    if (requireInvalid && invalidCount != 3) {
      return Collections.emptySet();
    } else if (!requireInvalid && requireValid && validCount != 3) {
      return Collections.emptySet();
    }

    return result;
  }

  @Override
  public ILevelConditionType<?> type() {
    return ModConditions.GROVE_STONE_CONDITION_TYPE.get();
  }

  @Override
  public String getName() {
    String req;
    if (!requireInvalid && !requireValid) {
      req = "any";
    } else if (requireValid) {
      req = "active";
    } else {
      req = "inactive";
    }
    if (!groveType.equals(GroveType.ANY)) {
      req += "_" + groveType.name().toLowerCase(Locale.ROOT);
    }

    return req + "_grove_stone";
  }

  public static class Type implements ILevelConditionType<GroveStoneCondition> {

    @Override
    public Codec<GroveStoneCondition> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<GroveStoneCondition> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GroveStoneCondition> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
