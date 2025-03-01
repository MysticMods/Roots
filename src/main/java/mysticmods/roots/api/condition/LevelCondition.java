package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.faction.GroveType;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import mysticmods.roots.block.GroveStoneBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class LevelCondition implements IDescribed {
  public static final Codec<LevelCondition> CODEC = RootsRegistries.LEVEL_CONDITIONS.byNameCodec();
  public static final Codec<List<LevelCondition>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, LevelCondition> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.LEVEL_CONDITIONS);
  public static final StreamCodec<RegistryFriendlyByteBuf, List<LevelCondition>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
  protected CanonicalRepresentation representation;
  private String descriptionId;

  public LevelCondition() {
  }

  public Holder<LevelCondition> builtInRegistryHolder() {
    return RootsRegistries.LEVEL_CONDITIONS.wrapAsHolder(this);
  }

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("level_condition", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  protected abstract CanonicalRepresentation getDefaultRepresentation();

  public CanonicalRepresentation getRepresentation() {
    if (representation == null) {
      CanonicalRepresentation canon = builtInRegistryHolder().getData(DataMaps.LEVEL_CONDITION_CANONS);
      if (canon != null) {
        representation = canon;
      } else {
        representation = getDefaultRepresentation();
      }
    }

    return representation;
  }

  public abstract Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player);

  public Set<BlockPos> test(Level level, @Nullable Player player, BoundingBox bounds, BlockPos pos, Set<BlockPos> exclusions) {
    BoundingBox newBounds = bounds.moved(pos.getX(), pos.getY(), pos.getZ());
    for (int x = newBounds.minX(); x < newBounds.maxX(); x++) {
      for (int y = newBounds.minY(); y < newBounds.maxY(); y++) {
        for (int z = newBounds.minZ(); z < newBounds.maxZ(); z++) {
          pos = new BlockPos(x, y, z);
          if (exclusions.contains(pos)) {
            continue;
          }
          Set<BlockPos> result = test(pos, level, player);
          if (!result.isEmpty()) {
            return result;
          }
        }
      }
    }

    return Collections.emptySet();
  }

  public static class BlockStatePropertyCondition extends LevelCondition {
    private final PartialBlockStateMatchWorldTest test;

    public BlockStatePropertyCondition(PartialBlockStateMatchWorldTest test) {
      this.test = test;
    }

    @Override
    protected CanonicalRepresentation getDefaultRepresentation() {
      return new CanonicalRepresentation(test.getPartialBlockState());
    }

    @Override
    public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
      if (test.test(level.getBlockState(pos), level.getRandom())) {
        return Collections.singleton(pos.immutable());
      }

      return Collections.emptySet();
    }
  }

  public static class PillarCondition extends LevelCondition {
    private final TagKey<Block> capstone;
    private final TagKey<Block> pillar;
    private final int heightExcluding;

    public PillarCondition(TagKey<Block> capstone, TagKey<Block> pillar, int height) {
      this.capstone = capstone;
      this.pillar = pillar;
      this.heightExcluding = height;
    }

    @Override
    protected CanonicalRepresentation getDefaultRepresentation() {
      var tag1 = BuiltInRegistries.BLOCK.getTag(capstone);
      if (tag1.isEmpty()) {
        throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the capstone tag " + capstone + " is empty");
      }
      BlockState capstoneState = tag1.get().get(0).value().defaultBlockState();

      var tag2 = BuiltInRegistries.BLOCK.getTag(pillar);
      if (tag2.isEmpty()) {
        throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the pillar tag " + pillar + " is empty");
      }
      BlockState pillarState = tag2.get().get(0).value().defaultBlockState();
      return fromStates(capstoneState, pillarState, heightExcluding);
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
      if (!initial.is(capstone)) {
        return Collections.emptySet();
      }

      // Keep a note of which blockpositions are part of this pillar
      Set<BlockPos> result = new HashSet<>();
      result.add(pos.immutable());
      BlockPos pPos = pos.below();

      // Move downward for each of the height (excluding the capstone)
      for (int i = 0; i < heightExcluding; i++) {
        if (!level.getBlockState(pPos).is(pillar)) {
          // If it isn't a pillar type, just return empty as this isn't valid OR it's a shorter pillar
          return Collections.emptySet();
        }
        result.add(pPos);
        pPos = pPos.below();
      }

      // Check the final underneath block, if it's a pillar or a capstone it's too tall OR it isn't valid
      initial = level.getBlockState(pPos);
      if (initial.is(capstone) || initial.is(pillar)) {
        return Collections.emptySet();
      }

      return result;
    }
  }

  public static class GroveStoneCondition extends LevelCondition {
    private final TagKey<Block> groveType;
    private final boolean requireValid;
    private final boolean requireInvalid;

    public GroveStoneCondition(TagKey<Block> groveType, boolean requireValid, boolean requireInvalid) {
      this.groveType = groveType;
      this.requireValid = requireValid;
      this.requireInvalid = requireInvalid;
      if (requireValid && requireInvalid) {
        throw new IllegalStateException("Cannot require both valid and invalid");
      }
    }

    public GroveStoneCondition(TagKey<Block> groveType, boolean requireValid) {
      this(groveType, requireValid, false);
    }

    @Nullable
    protected StateProperties.Part getPart(BlockState state) {
      if (!state.hasProperty(StateProperties.GroveStone.PART)) {
        return null;
      } else {
        return state.getValue(StateProperties.GroveStone.PART);
      }
    }

    protected boolean getValid(BlockState state) {
      if (!state.hasProperty(StateProperties.ACTIVE)) {
        return false;
      } else {
        return state.getValue(StateProperties.ACTIVE);
      }
    }

    @Override
    protected CanonicalRepresentation getDefaultRepresentation() {
      var tag = BuiltInRegistries.BLOCK.getTag(groveType);
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
      if (!initial.is(groveType) && getPart(initial) != StateProperties.Part.TOP) {
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
      if (!initial.is(groveType) && getPart(initial) != StateProperties.Part.MIDDLE) {
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
      if (!initial.is(groveType) && getPart(initial) != StateProperties.Part.BOTTOM) {
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
  }

  public static LevelCondition.PillarCondition runePillar(int height) {
    return new LevelCondition.PillarCondition(RootsTags.Blocks.RUNE_CAPSTONES, RootsTags.Blocks.RUNE_PILLARS, height);
  }

  public static LevelCondition.PillarCondition logPillar(PillarType type, int height) {
    return new PillarCondition(type.getCapstoneTag(), type.getPillarTag(), height);
  }

  public static LevelCondition.GroveStoneCondition groveStone(GroveType grove, boolean requireValid) {
    return groveStone(grove, requireValid, false);
  }

  public static LevelCondition.GroveStoneCondition groveStone(GroveType grove, boolean requireValid, boolean requireInvalid) {
    return new GroveStoneCondition(grove.getTag(), requireValid, requireInvalid);
  }

  public static LevelCondition.GroveStoneCondition anyGroveStone(boolean requireValid) {
    return new GroveStoneCondition(RootsTags.Blocks.GROVE_STONES, requireValid);
  }

  public enum PillarType {
    ACACIA(RootsTags.Blocks.ACACIA_PILLARS, RootsTags.Blocks.ACACIA_CAPSTONES),
    BIRCH(RootsTags.Blocks.BIRCH_PILLARS, RootsTags.Blocks.BIRCH_CAPSTONES),
    DARK_OAK(RootsTags.Blocks.DARK_OAK_PILLARS, RootsTags.Blocks.DARK_OAK_CAPSTONES),
    JUNGLE(RootsTags.Blocks.JUNGLE_PILLARS, RootsTags.Blocks.JUNGLE_CAPSTONES),
    OAK(RootsTags.Blocks.OAK_PILLARS, RootsTags.Blocks.OAK_CAPSTONES),
    SPRUCE(RootsTags.Blocks.SPRUCE_PILLARS, RootsTags.Blocks.SPRUCE_CAPSTONES),
    CRIMSON(RootsTags.Blocks.CRIMSON_PILLARS, RootsTags.Blocks.CRIMSON_CAPSTONES),
    WARPED(RootsTags.Blocks.WARPED_PILLARS, RootsTags.Blocks.WARPED_CAPSTONES),
    WILDWOOD(RootsTags.Blocks.WILDWOOD_PILLARS, RootsTags.Blocks.WILDWOOD_CAPSTONES),
    MANGROVE(RootsTags.Blocks.MANGROVE_PILLARS, RootsTags.Blocks.MANGROVE_CAPSTONES),
    ANY_LOG(RootsTags.Blocks.LOG_PILLARS, RootsTags.Blocks.LOG_CAPSTONES),
    RUNE(RootsTags.Blocks.RUNE_PILLARS, RootsTags.Blocks.RUNE_CAPSTONES),
    RUNED(RootsTags.Blocks.RUNED_PILLARS, RootsTags.Blocks.RUNED_CAPSTONES),
    ANY_RUNE(RootsTags.Blocks.RUNES_PILLARS, RootsTags.Blocks.RUNES_CAPSTONES),
    ANY(RootsTags.Blocks.PILLARS, RootsTags.Blocks.CAPSTONES);

    private final TagKey<Block> pillarTag;
    private final TagKey<Block> capstoneTag;

    PillarType(TagKey<Block> pillarTag, TagKey<Block> capstoneTag) {
      this.pillarTag = pillarTag;
      this.capstoneTag = capstoneTag;
    }

    public TagKey<Block> getPillarTag() {
      return pillarTag;
    }

    public TagKey<Block> getCapstoneTag() {
      return capstoneTag;
    }
  }
}
