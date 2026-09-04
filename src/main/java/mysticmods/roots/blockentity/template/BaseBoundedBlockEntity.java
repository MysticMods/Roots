package mysticmods.roots.blockentity.template;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.Bounded;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
public abstract class BaseBoundedBlockEntity extends BaseBlockEntity implements Bounded {
  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected BlockCapabilityCache<IItemHandler, @org.jetbrains.annotations.Nullable Direction> lastOutput;
  protected Object particleHolder;

  public BaseBoundedBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public RandomSource getRandom() {
    if (getLevel() == null) {
      return RandomSource.create();
    }

    return getLevel().getRandom();
  }

  @Nullable
  public Object getParticleHolder() {
    return particleHolder;
  }

  public void setParticleHolder(Object particleHolder) {
    this.particleHolder = particleHolder;
  }

  protected boolean canOutputTo(BlockState state, BlockPos pos) {
    return !state.is(RootsTags.Blocks.PEDESTALS) && !state.is(Blocks.DISPENSER);
  }

  protected BoundingBox getPedestalBoundingBox() {
    return getBoundingBox();
  }

  public List<Pair<BlockPos, PedestalBlockEntity>> pedestals(TagKey<Block> include, TagKey<Block> exclude) {
    return pedestals(include, exclude, false);
  }

  // TODO: Some sort of caching
  public List<Pair<BlockPos, PedestalBlockEntity>> pedestals(TagKey<Block> include, TagKey<Block> exclude, boolean allowEmpty) {
    List<Pair<BlockPos, PedestalBlockEntity>> pedestalPositions = new ArrayList<>();
    if (getPedestalBoundingBox() != null) {
      BlockPos start = getBlockPos();
      BoundingBox box = getPedestalBoundingBox().moved(start.getX(), start.getY(), start.getZ());
      BlockPos.betweenClosedStream(box).forEach(pos -> {
        BlockState state = getLevel().getBlockState(pos);
        if (state.is(include) && !state.is(exclude)) {
          if (getLevel().getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
            // Already checks for empty
            if (!pedestal.getHeldItem().isEmpty() || allowEmpty) {
              pedestalPositions.add(new Pair<>(pos.immutable(), pedestal));
            }
          }
        }
      });
    }
    return pedestalPositions;
  }
  
  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
    if (getLevel() != null && getLevel().isClientSide()) {
      resetBounds();
    }
    super.onDataPacket(net, pkt, lookupProvider);
  }

  protected void resetBounds() {
    boundingBox = null;
    aabb = null;
  }

  // TODO: This really should accept some sort of enum for variable bounding boxes for the same block entity
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
  public AABB getAABB() {
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

}
