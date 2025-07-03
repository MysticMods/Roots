package mysticmods.roots.blockentity.template;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.Bounded;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public abstract class BaseBlockEntity extends BlockEntity implements Bounded {
  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected BlockCapabilityCache<IItemHandler, @org.jetbrains.annotations.Nullable Direction> lastOutput;
  protected Object particleHolder;

  public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public RandomSource getRandom () {
    if (getLevel() == null) {
      return RandomSource.create();
    }

    return getLevel().getRandom();
  }

  @Nullable
  public Object getParticleHolder () {
    return particleHolder;
  }

  public void setParticleHolder(Object particleHolder) {
    this.particleHolder = particleHolder;
  }

  protected boolean canOutputTo(BlockState state, BlockPos pos) {
    return true;
  }

  protected BoundingBox getPedestalBoundingBox () {
    return getBoundingBox();
  }

  // TODO: Some sort of caching
  public List<Pair<BlockPos, PedestalBlockEntity>> pedestals(TagKey<Block> include, TagKey<Block> exclude) {
    List<Pair<BlockPos, PedestalBlockEntity>> pedestalPositions = new ArrayList<>();
    if (getPedestalBoundingBox() != null) {
      BlockPos start = getBlockPos();
      BoundingBox box = getPedestalBoundingBox().moved(start.getX(), start.getY(), start.getZ());
      BlockPos.betweenClosedStream(box).forEach(pos -> {
        BlockState state = getLevel().getBlockState(pos);
        if (state.is(include) && !state.is(exclude)) {
          if (getLevel().getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
            // Already checks for empty
            if (!pedestal.getHeldItem().isEmpty()) {
              pedestalPositions.add(new Pair<>(pos.immutable(), pedestal));
            }
          }
        }
      });
    }
    return pedestalPositions;
  }

  public void updateViaState() {
    setChanged();
    ClientboundBlockEntityDataPacket packet = getUpdatePacket();
    if (packet == null) {
      return;
    }
    BlockPos pos = getBlockPos();
    ((ServerLevel) level).getServer().getPlayerList().broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64, level.dimension(), packet);
  }

  @Nullable
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
    CompoundTag pTag = new CompoundTag();
    saveAdditional(pTag, lookup);
    return pTag;
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.saveAdditional(pTag, lookup);
  }

  protected void resetBounds () {
    boundingBox = null;
    aabb = null;
  }

  // TODO: This really should accept some sort of enum for variable bounding boxes for the same block entity
  @SuppressWarnings("deprecation")
  @Override
  public BoundingBox getBoundingBox() {
    if (!isBounded()) {
      return null;
    }
    if (boundingBox == null) {
      boundingBox = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX(), getRadiusY(), getRadiusZ());
    }
    return boundingBox;
  }

  @Override
  public AABB getAABB () {
    if (!isBounded()) {
      return null;
    }
    if (aabb == null) {
      BoundingBox box = getBoundingBox();
      if (box == null) {
        return null;
      }
      aabb = AABB.of(box);
    }

    return aabb;
  }

  public ItemStack outputAdjacent(ItemStack stack) {
    // Unneccessary allocation?
    List<ItemStack> temp = new ArrayList<>();
    temp.add(stack);

    temp = outputAdjacent(temp);
    if (temp.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (temp.size() != 1) {
      RootsAPI.LOG.error("outputAdjacent returned multiple stacks, this is not supported");
    }

    return temp.getFirst();
  }

  public List<ItemStack> outputAdjacent(List<ItemStack> stacks) {
    if (lastOutput != null) {
      IItemHandler output = lastOutput.getCapability();
      if (output != null) {
        stacks = outputAdjacent(stacks, output);
      }
    }

    if (stacks.isEmpty()) {
      return stacks;
    }

    for (Direction direction : Direction.values()) {
      if (direction == Direction.DOWN) { // You can have any direction unless it's DOWN
        continue;
      }
      if (stacks.isEmpty()) {
        break;
      }
      BlockPos pos = getBlockPos().relative(direction);
      BlockState state = getLevel().getBlockState(pos);
      if (!canOutputTo(state, pos)) {
        continue;
      }
      IItemHandler output = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, pos, direction.getOpposite());
      if (output != null && (lastOutput == null || lastOutput.getCapability() != output)) {
        stacks = outputAdjacent(stacks, output);
        if (stacks.isEmpty()) {
          lastOutput = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) getLevel(), pos, null);
          return stacks;
        }
      }
    }

    return stacks;
  }

  public List<ItemStack> outputAdjacent(List<ItemStack> stacks, IItemHandler handler) {
    List<ItemStack> result = new ArrayList<>();
    for (ItemStack stack : stacks) {
      ItemStack remainder = ItemHandlerHelper.insertItem(handler, stack, false);
      if (!remainder.isEmpty()) {
        result.add(remainder);
      }
    }
    return result;
  }

  public static <T extends BlockEntity> void clientTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (pLevel.isClientSide() && pBlockEntity instanceof ClientTickBlockEntity clientBlockEntity) {
      clientBlockEntity.clientTick(pLevel, pPos, pState);
    }
  }

  public static <T extends BlockEntity> void serverTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (!pLevel.isClientSide() && pBlockEntity instanceof ServerTickBlockEntity serverBlockEntity) {
      serverBlockEntity.serverTick((ServerLevel) pLevel, pPos, pState);
    }
  }
}
