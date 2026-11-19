package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.block.StateProperties;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.api.condition.PillarType;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.block.PillarBlock;
import mysticmods.roots.init.ModConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

public record PillarCondition(PillarType pillarType) implements ILevelCondition {
  public static final Codec<PillarCondition> CODEC = PillarType.CODEC.xmap(PillarCondition::new, PillarCondition::pillarType);
  public static final MapCodec<PillarCondition> MAP_CODEC = CODEC.fieldOf("pillar_type");
  public static final StreamCodec<RegistryFriendlyByteBuf, PillarCondition> STREAM_CODEC = PillarType.STREAM_CODEC.map(PillarCondition::new, PillarCondition::pillarType);

  @Nullable
  private StateProperties.Part getPart(BlockState state) {
    if (!state.hasProperty(StateProperties.GroveStone.PART)) {
      return null;
    } else {
      return state.getValue(StateProperties.GroveStone.PART);
    }
  }

  @Override
  public CanonicalRepresentation getRepresentation() {
    var tag = BuiltInRegistries.BLOCK.getTag(pillarType.pillar());
    if (tag.isEmpty()) {
      throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the grove type tag " + pillarType + " is empty");
    }
    BlockState state = tag.get().get(0).value().defaultBlockState();
    return fromBlockState(state);
  }

  public static CanonicalRepresentation fromBlockState(BlockState state) {
    BlockState bottom = state.setValue(PillarBlock.PART, StateProperties.Part.BOTTOM);
    BlockState middle = state.setValue(PillarBlock.PART, StateProperties.Part.MIDDLE);
    BlockState top = state.setValue(PillarBlock.PART, StateProperties.Part.TOP);
    //noinspection rawtypes
    Property[] properties = new Property[]{StateProperties.GroveStone.PART};
    return new CanonicalRepresentation(new PartialBlockState(bottom, properties), new PartialBlockState(middle, properties), new PartialBlockState(top, properties));
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
    BlockState initial = level.getBlockState(pos);
    // If the initial position isn't the capstone, we don't care
    if (!initial.is(pillarType.pillar()) && getPart(initial) != StateProperties.Part.TOP) {
      return Collections.emptySet();
    }

    // Keep a note of which blockpositions are part of this pillar
    Set<BlockPos> result = new HashSet<>();
    result.add(pos.immutable());
    BlockPos pPos = pos.below();

    initial = level.getBlockState(pPos);
    if (!initial.is(pillarType.pillar()) && getPart(initial) != StateProperties.Part.MIDDLE) {
      return Collections.emptySet();
    }

    result.add(pPos.immutable());

    pPos = pPos.below();
    initial = level.getBlockState(pPos);
    if (!initial.is(pillarType.pillar()) && getPart(initial) != StateProperties.Part.BOTTOM) {
      return Collections.emptySet();
    }

    result.add(pPos.immutable());

    return result;
  }

  @Override
  public ILevelConditionType<?> type() {
    return ModConditions.PILLAR_CONDITION_TYPE.get();
  }

  @Override
  public String getName() {
    if (!pillarType.equals(PillarType.ANY)) {
      return pillarType.name().toLowerCase(Locale.ROOT);
    }

    return "any_pillar";
  }

  public static class Type implements ILevelConditionType<PillarCondition> {

    @Override
    public Codec<PillarCondition> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<PillarCondition> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, PillarCondition> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
