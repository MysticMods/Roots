package mysticmods.roots.blockentity;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.*;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// TODO: Handle rank changes
public class GroveStoneBlockEntity extends BaseBlockEntity implements ServerTickBlockEntity, GrovePower, IGroveInstance {
  private int generatedLastTick = 0;
  private int consumedLastTick = 0;
  private int generatedThisTick = 0;
  private int consumedThisTick = 0;

  private static BoundingBox groveStoneBoundingBox = null;

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

  public GroveStoneBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public GroveStoneBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(ModBlockEntities.GROVE_STONE.get(), pWorldPosition, pBlockState);
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    if (!pState.getValue(GroveStoneBlock.ACTIVE) || getBoundingBox() == null) {
      return;
    }

    generateTick(pLevel, pPos, pState);
    consumeTick(pLevel, pPos, pState);

    if (generatedThisTick != generatedLastTick || consumedThisTick != consumedLastTick) {
      setChanged();
      updateViaState();
    }
  }

  public void setRank (int rank) {
    this.resetBounds();
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

  @Override
  public void generateTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    generatedLastTick = generatedThisTick;
    generatedThisTick = 0;
    BoundingBox box = getBoundingBox();
    if (box == null) {
      return;
    }
    box = box.moved(pPos.getX(), pPos.getY(), pPos.getZ());

    List<GrovePower.GenerationEntry> entries = getGrove().getData(DataMaps.GROVE_GENERATION_ENTRIES);
    if (entries == null) {
      return;
    }

    List<BlockPos> generatorPositions = new ArrayList<>(BlockPos.betweenClosedStream(box).map(BlockPos::immutable)
        .toList());
    Map<GenerationEntry, BlockTracker> trackers = new Object2ObjectOpenHashMap<>();
    entries.forEach(o -> trackers.put(o, BlockTracker.create(o.maxCount())));

    for (BlockPos pos : generatorPositions) {
      BlockState stateAt = pLevel.getBlockState(pos);
      if (stateAt.isAir()) {
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

    BoundingBox box = getGroveStoneBoundingBox().moved(pPos.getX(), pPos.getY(), pPos.getZ());

    List<BlockPos> consumerPositions = new ArrayList<>(BlockPos.betweenClosedStream(box).map(BlockPos::immutable)
        .toList());

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
  public GrovePower getPower() {
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
}
