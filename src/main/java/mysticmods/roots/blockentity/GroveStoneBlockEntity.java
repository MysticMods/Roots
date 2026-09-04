package mysticmods.roots.blockentity;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.*;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.template.BaseBoundedBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.ritual.GroveSupplicationRitual;
import mysticmods.roots.util.ReputationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// TODO: Handle rank changes
public class GroveStoneBlockEntity extends BaseBoundedBlockEntity implements ServerTickBlockEntity, GrovePowerGenerator, IGroveInstance {
  private int generatedLastTick = 0;
  private int consumedLastTick = 0;
  private int generatedThisTick = 0;
  private int consumedThisTick = 0;

  private static BoundingBox groveStoneBoundingBox = null;
  private static int groveStoneSearchRadius = -1;

  public static BoundingBox getGroveStoneBoundingBox() {
    if (groveStoneBoundingBox == null) {
      groveStoneBoundingBox = new BoundingBox(-ConfigManager.GROVE_STONE_POWER_RANGE_X.getAsInt(),
          -ConfigManager.GROVE_STONE_POWER_RANGE_Y.getAsInt(),
          -ConfigManager.GROVE_STONE_POWER_RANGE_Z.getAsInt(),
          ConfigManager.GROVE_STONE_POWER_RANGE_X.getAsInt(),
          ConfigManager.GROVE_STONE_POWER_RANGE_Y.getAsInt(),
          ConfigManager.GROVE_STONE_POWER_RANGE_Z.getAsInt());
    }
    return groveStoneBoundingBox;
  }

  public static int getGroveStoneSearchRadius() {
    if (groveStoneSearchRadius == -1) {
      groveStoneSearchRadius = ConfigManager.GROVE_STONE_POWER_RANGE_X.getAsInt() *
          ConfigManager.GROVE_STONE_POWER_RANGE_Y.getAsInt() *
          ConfigManager.GROVE_STONE_POWER_RANGE_Z.getAsInt();
    }
    return groveStoneSearchRadius;
  }

  public GroveStoneBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public GroveStoneBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(ModBlockEntities.GROVE_STONE.get(), pWorldPosition, pBlockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!getLevel().isClientSide()) {
      var state = getBlockState();
      if (state.is(RootsTags.Blocks.GROVE_STONE_WILD) && !state.getValue(GroveStoneBlock.ACTIVE)) {
        tryActivating(null, null, null);
      }
    }
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    if (pState.is(RootsTags.Blocks.GROVE_STONE_WILD)) {
      return;
    }

    if (!pState.getValue(GroveStoneBlock.ACTIVE) || getBoundingBox() == null) {
      return;
    }

    generateTick(pLevel, pPos, pState);
    consumeTick(pLevel, pPos, pState);

    // Temporarily disabled for #1194
    /*    if (generatedThisTick != generatedLastTick || consumedThisTick != consumedLastTick) {*/
    setChanged();
    updateViaState();
    /*    }*/
  }

  public void setRank(int rank) {
    this.resetBounds();
  }

  @Override
  public BlockPos getGrovePosition() {
    return getBlockPos();
  }

  private Holder<Grove> getGrove() {
    if (!(getBlockState().getBlock() instanceof GroveStoneBlock groveBlock)) {
      throw new IllegalStateException("GroveStoneBlockEntity must be attached to a GroveStoneBlock");
    }

    return groveBlock.getGrove();
  }

  // TODO: Resetting the bounds
  @Override
  public int getRadiusX() {
    return switch (getBlockState().getValue(GroveStoneBlock.RANK)) {
      case 1 -> ConfigManager.RANK_1_GROVE_BOUNDS_ZX.getAsInt();
      case 2 -> ConfigManager.RANK_2_GROVE_BOUNDS_ZX.getAsInt();
      case 3 -> ConfigManager.RANK_3_GROVE_BOUNDS_ZX.getAsInt();
      case 4 -> ConfigManager.RANK_4_GROVE_BOUNDS_ZX.getAsInt();
      default -> 0;
    };
  }

  @Override
  public int getRadiusY() {
    return switch (getBlockState().getValue(GroveStoneBlock.RANK)) {
      case 1 -> ConfigManager.RANK_1_GROVE_BOUNDS_Y.getAsInt();
      case 2 -> ConfigManager.RANK_2_GROVE_BOUNDS_Y.getAsInt();
      case 3 -> ConfigManager.RANK_3_GROVE_BOUNDS_Y.getAsInt();
      case 4 -> ConfigManager.RANK_4_GROVE_BOUNDS_Y.getAsInt();
      default -> 0;
    };
  }

  @Override
  public int getRadiusZ() {
    return switch (getBlockState().getValue(GroveStoneBlock.RANK)) {
      case 1 -> ConfigManager.RANK_1_GROVE_BOUNDS_ZX.getAsInt();
      case 2 -> ConfigManager.RANK_2_GROVE_BOUNDS_ZX.getAsInt();
      case 3 -> ConfigManager.RANK_3_GROVE_BOUNDS_ZX.getAsInt();
      case 4 -> ConfigManager.RANK_4_GROVE_BOUNDS_ZX.getAsInt();
      default -> 0;
    };
  }

  @Override
  public int getMaxPower() {
    return generatedThisTick;
  }

  @Override
  public int getUsedPower() {
    return consumedThisTick;
  }

  private BoundingBox movedBox = null;
  private List<BlockPos> positions = null;

  public List<BlockPos> getValidPositions(BlockPos pPos) {
    if (positions == null) {
      if (movedBox == null) {
        BoundingBox box = getBoundingBox();
        if (box == null) {
          return Collections.emptyList();
        }
        movedBox = box.moved(pPos.getX(), pPos.getY(), pPos.getZ());
      }
      positions = new ArrayList<>(BlockPos.betweenClosedStream(movedBox).map(BlockPos::immutable).toList());
    }
    return positions;
  }

  public List<GrovePowerGenerator.GenerationEntry> getGenerationEntries() {
    List<GrovePowerGenerator.GenerationEntry> entries = getGrove().getData(DataMaps.GROVE_GENERATION_ENTRIES);
    if (entries == null) {
      return Collections.emptyList();
    }
    return entries;
  }

  public Map<GenerationEntry, BlockTracker> buildTrackers() {
    List<GrovePowerGenerator.GenerationEntry> entries = getGenerationEntries();
    if (entries.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<GenerationEntry, BlockTracker> trackers = new Object2ObjectOpenHashMap<>();
    entries.forEach(o -> trackers.put(o, BlockTracker.create(o.maxCount())));
    return trackers;
  }

  @Override
  public void generateTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    generatedLastTick = generatedThisTick;
    generatedThisTick = 0;

    List<GrovePowerGenerator.GenerationEntry> entries = getGenerationEntries();
    if (entries.isEmpty()) {
      return;
    }

    List<BlockPos> generatorPositions = getValidPositions(pPos);
    if (generatorPositions.isEmpty()) {
      return;
    }

    Map<GenerationEntry, BlockTracker> trackers = buildTrackers();
    if (trackers.isEmpty()) {
      return;
    }

    for (BlockPos pos : generatorPositions) {
      BlockState stateAt = pLevel.getBlockState(pos);
      if (stateAt.isAir()) {
        continue;
      }
      if (stateAt.is(RootsTags.Blocks.GROVE_CONSUMERS)) {
        continue;
      }
      List<Generator> generators = stateAt.getBlockHolder().getData(DataMaps.GROVE_POWER_GENERATORS);
      if (generators == null) {
        continue;
      }
      for (GenerationEntry entry : entries) {
        Symmetry sym = entry.symmetry();
        if (sym.matches(pLevel, entry.tag(), pos, pPos)) {
          for (Generator generator : generators) {
            int willGenerate = generator.generate(this, pos);
            if (willGenerate != 0) {
              BlockTracker tracker = trackers.get(entry);
              if (tracker.count(stateAt.getBlock())) {
                generatedThisTick += willGenerate;
              }
            }
          }
        }
      }
    }
  }


  @Override
  public void consumeTick(ServerLevel level, BlockPos pPos, BlockState state) {
    consumedLastTick = consumedThisTick;
    consumedThisTick = 0;

    if (generatedThisTick == 0) {
      return;
    }

    int available = generatedThisTick;

    long tick = level.getGameTime();

    List<BlockPos> consumerPositions = level.getData(ModAttachments.GROVE_CONSUMERS)
        .query(pPos, getGroveStoneSearchRadius());

    for (BlockPos pos : consumerPositions) {
      if (available <= 0) {
        break;
      }
      BlockState stateAt = level.getBlockState(pos);
      if (stateAt.isAir()) {
        continue;
      }
      if (stateAt.is(RootsTags.Blocks.GROVE_CONSUMERS)) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IGroveConsumer consumer) {
          PowerTicket ticket = consumer.getTicketForTick(tick);
          available = ticket.supply(this, available);
        }
      }
    }

    consumedThisTick = generatedThisTick - available;
  }

  @Override
  public Grove asGrove() {
    return getGrove().value();
  }

  @Override
  public int getRank() {
    return getBlockState().getValue(GroveStoneBlock.RANK);
  }

  private int maxRank = -1;

  @Override
  public int getMaxRank() {
    if (maxRank == -1) {
      maxRank = Collections.max(GroveStoneBlock.RANK.getPossibleValues());
    }
    return maxRank;
  }

  @Override
  public GrovePowerGenerator getPower() {
    return this;
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.generatedLastTick = tag.getInt("generatedLastTick");
    this.consumedLastTick = tag.getInt("consumedLastTick");
    this.generatedThisTick = tag.getInt("generatedThisTick");
    this.consumedThisTick = tag.getInt("consumedThisTick");
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.saveAdditional(pTag, lookup);
    pTag.putInt("generatedLastTick", generatedLastTick);
    pTag.putInt("consumedLastTick", consumedLastTick);
    pTag.putInt("generatedThisTick", generatedThisTick);
    pTag.putInt("consumedThisTick", consumedThisTick);
  }

  public boolean tryActivating(@Nullable GroveSupplicationRitual groveSupplicationRitual, @Nullable PyreBlockEntity blockEntity, @Nullable Player player) {
    Level level = getLevel();
    if (level == null || level.isClientSide()) {
      return false;
    }

    BlockPos pos = getBlockPos();
    Grove grove = getGrove().value();
    BlockState state = getBlockState();
    if (player == null) {
      if (grove.is(RootsTags.Groves.WILD)) {
        if (state.getValue(StateProperties.ACTIVE)) {
          return false;
        }
        activate(level, pos, state, 0);

        return true;
        // TODO: Animation
      }

      return false;
    }
    var rank = ReputationHelper.getRank(player, grove);
    if (rank > getRank()) {
      activate(level, pos, state, rank);
      return true;

      // TODO: Animation
    }

    return false;
  }

  public static void activate(Level level, BlockPos pos, BlockState state, int rank) {
    if (state.getValue(StateProperties.GroveStone.PART) != StateProperties.Part.TOP) {
      throw new IllegalStateException("GroveStoneBlockEntity::activate can only be called on the block entity of a grove stone, called on " + pos + " which is " + state.getValue(StateProperties.GroveStone.PART));
    }
    level.setBlockAndUpdate(pos, state.setValue(StateProperties.ACTIVE, true)
        .setValue(StateProperties.GroveStone.RANK, rank));
    BlockState below = level.getBlockState(pos.below());
    level.setBlockAndUpdate(pos.below(), below.setValue(StateProperties.ACTIVE, true)
        .setValue(StateProperties.GroveStone.RANK, rank));
    below = level.getBlockState(pos.below().below());
    level.setBlockAndUpdate(pos.below().below(), below.setValue(StateProperties.ACTIVE, true)
        .setValue(StateProperties.GroveStone.RANK, rank));
  }
}
