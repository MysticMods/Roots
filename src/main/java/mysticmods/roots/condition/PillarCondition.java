package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.api.condition.PillarType;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.init.ModConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public record PillarCondition(PillarType pillar,
                              int heightExcluding) implements ILevelCondition {
  public static final MapCodec<PillarCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      PillarType.CODEC.fieldOf("pillar_type").forGetter(o -> o.pillar),
      Codec.INT.fieldOf("height").forGetter(PillarCondition::heightExcluding)
  ).apply(instance, PillarCondition::new));
  public static final Codec<PillarCondition> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, PillarCondition> STREAM_CODEC = StreamCodec.composite(
      PillarType.STREAM_CODEC, o -> o.pillar,
      ByteBufCodecs.INT, PillarCondition::heightExcluding, PillarCondition::new
  );

  @Override
  public CanonicalRepresentation getRepresentation() {
    var tag1 = BuiltInRegistries.BLOCK.getTag(pillar.capstone());
    if (tag1.isEmpty()) {
      throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the capstone tag " + pillar.capstone() + " is empty");
    }
    BlockState capstoneState = tag1.get().get(0).value().defaultBlockState();

    var tag2 = BuiltInRegistries.BLOCK.getTag(pillar.pillar());
    if (tag2.isEmpty()) {
      throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the pillar tag " + pillar.pillar() + " is empty");
    }
    BlockState pillarState = tag2.get().get(0).value().defaultBlockState();
    return fromStates(capstoneState, pillarState, heightExcluding);
  }

  @Override
  public ILevelConditionType<?> type() {
    return ModConditions.PILLAR_CONDITION_TYPE.get();
  }

  @Override
  public String getName() {
    String req = heightExcluding + "_high_";
    if (pillar == PillarType.ANY) {
      req = req + "any_pillar";
    } else if (pillar == PillarType.ANY_RUNE) {
      req = req + "any_rune_pillar";
    } else {
      req = req + pillar.name().toLowerCase(Locale.ROOT) + "_pillar";
    }
    return req;
  }

  public static CanonicalRepresentation fromStates(BlockState capstone, BlockState pillar, int height) {
    if (capstone.hasProperty(RotatedPillarBlock.AXIS)) {
      capstone = capstone.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
    }
    List<PartialBlockState> states = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      if (capstone.hasProperty(RotatedPillarBlock.AXIS)) {
        states.add(new PartialBlockState(pillar, RotatedPillarBlock.AXIS));
      } else {
        states.add(new PartialBlockState(pillar));
      }
    }
    states.add(new PartialBlockState(capstone));
    return new CanonicalRepresentation(states.toArray());
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
    BlockState initial = level.getBlockState(pos);
    // If the initial position isn't the capstone, we don't care
    if (!initial.is(pillar.capstone())) {
      return Collections.emptySet();
    }

    // Keep a note of which blockpositions are part of this pillar
    Set<BlockPos> result = new HashSet<>();
    result.add(pos.immutable());
    BlockPos pPos = pos.below();

    // Move downward for each of the height (excluding the capstone)
    for (int i = 0; i < heightExcluding; i++) {
      if (!level.getBlockState(pPos).is(pillar.pillar())) {
        // If it isn't a pillar type, just return empty as this isn't valid OR it's a shorter pillar
        return Collections.emptySet();
      }
      result.add(pPos);
      pPos = pPos.below();
    }

    // Check the final underneath block, if it's a pillar or a capstone it's too tall OR it isn't valid
    initial = level.getBlockState(pPos);
    if (initial.is(pillar.capstone()) || initial.is(pillar.pillar())) {
      return Collections.emptySet();
    }

    return result;
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
