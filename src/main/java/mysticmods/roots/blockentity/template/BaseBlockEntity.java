package mysticmods.roots.blockentity.template;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mysticmods.roots.api.blockentity.BoundedBlockEntity;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseBlockEntity extends BlockEntity implements BoundedBlockEntity {
  private static final AABB singleBlock = AABB.ofSize(Vec3.ZERO, 1, 1, 1);
  protected AABB singleBlockBoundingBox;
  protected BoundingBox boundingBox;
  protected BlockPos lastOutputPos = null;

  public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public void updateViaState() {
    setChanged();
    ClientboundBlockEntityDataPacket packet = getUpdatePacket();
    if (packet == null) {
      return;
    }
    ChunkPos chunkPos = new ChunkPos(getBlockPos());
    for (ServerPlayer player : ((ServerLevel)level).getChunkSource().chunkMap.getPlayers(chunkPos, false)) {
      player.connection.send(packet);
    }
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

  @Override
  public BoundingBox getBoundingBox() {
    if (!isBounded()) {
      return null;
    }
    if (boundingBox == null) {
      boundingBox = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX(), getRadiusY(), getRadiusZ()).move(getBlockPos());
    }
    return boundingBox;
  }

  private AABB clientBounds;

  @Nullable
  public AABB getRenderBoundingBox() {
    if (!isBounded()) {
      return null;
    }

    if (clientBounds == null) {
      BoundingBox box = getBoundingBox();
      if (box != null) {
        clientBounds = AABB.of(box);
      }
    }

    return clientBounds;
  }

  public AABB getSingleBlockBoundingBox() {
    if (singleBlockBoundingBox == null) {
      singleBlockBoundingBox = singleBlock.move(getBlockPos());
    }

    return singleBlockBoundingBox;
  }

  public ItemStack outputAdjacent (ItemStack stack) {
    if (lastOutputPos != null) {
      IItemHandler output = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, lastOutputPos, (Direction) null);
      ItemStack result = ItemHandlerHelper.insertItem(output, stack, false);
      if (result.isEmpty()) {
        return ItemStack.EMPTY;
      }

      stack = result;
    }

    for (Direction direction : Direction.values()) {
      if (direction == Direction.DOWN) { // You can have any direction unless it's DOWN
        continue;
      }
      BlockPos pos = getBlockPos().relative(direction);
      if (lastOutputPos != null && lastOutputPos.equals(pos)) {
        continue;
      }
      IItemHandler output = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, pos, direction.getOpposite());
      if (output != null) {
        lastOutputPos = pos;
        ItemStack result = ItemHandlerHelper.insertItem(output, stack, false);
        if (result.isEmpty()) {
          return ItemStack.EMPTY;
        }
        stack = result;
      }
    }

    return stack;
  }

  public List<ItemStack> outputAdjacent (List<ItemStack> stacks) {
    if (lastOutputPos != null) {
      IItemHandler output = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, lastOutputPos, (Direction) null);
      if (output != null) {
        return outputAdjacent(stacks, output);
      }
    }

    for (Direction direction : Direction.values()) {
      if (direction == Direction.DOWN) { // You can have any direction unless it's DOWN
        continue;
      }
      if (stacks.isEmpty()) {
        break;
      }
      BlockPos pos = getBlockPos().relative(direction);
      if (lastOutputPos != null && lastOutputPos.equals(pos)) {
        continue;
      }
      IItemHandler output = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, pos, direction.getOpposite());
      if (output != null) {
        lastOutputPos = pos;
        stacks = outputAdjacent(stacks, output);
      }
    }

    return stacks;
  }

  public List<ItemStack> outputAdjacent (List<ItemStack> stacks, IItemHandler handler) {
    List<ItemStack> result = new ArrayList<>();
    for (ItemStack stack : stacks) {
      ItemStack remainder = ItemHandlerHelper.insertItem(handler, stack, false);
      if (!remainder.isEmpty()) {
        result.add(remainder);
      }
    }
    return result;
  }

  public void refillRecipe (ServerPlayer player, RecipeHolder<? extends RootsTileRecipe<?, ?, ?>> recipe, RecipeInventory inventory) {
    PlayerMainInvWrapper inv = new PlayerMainInvWrapper(player.getInventory());
    if (player.isCreative()) {
      for (Ingredient ingredient : recipe.value().getIngredients()) {
        inventory.insert(ingredient.getItems()[0]);
      }
    } else {
      Int2IntOpenHashMap counts = new Int2IntOpenHashMap();
      boolean foundOuter = true;
      outer: for (Ingredient ingredient : recipe.value().getIngredients()) {
        for (int i = 0; i < inv.getSlots(); i++) {
          ItemStack stack = inv.getStackInSlot(i);
          if (ingredient.test(stack)) {
            counts.put(i, counts.get(i) + 1);
            continue outer;
          }
        }
        foundOuter = false;
        break;
      }
      if (foundOuter) {
        for (Int2IntMap.Entry entry : counts.int2IntEntrySet()) {
          for (int i = 0; i < entry.getIntValue(); i++) {
            ItemStack thisStack = inv.extractItem(entry.getIntKey(), 1, false);
            if (!inventory.insert(thisStack).isEmpty()) {
              inv.insertItem(entry.getIntKey(), thisStack, false);
            }
          }
        }
      }
    }
  }

  public static <T extends BlockEntity> void clientTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (pBlockEntity instanceof ClientTickBlockEntity clientBlockEntity) {
      clientBlockEntity.clientTick(pLevel, pPos, pState);
    }
  }

  public static <T extends BlockEntity> void serverTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (pBlockEntity instanceof ServerTickBlockEntity serverBlockEntity) {
      serverBlockEntity.serverTick(pLevel, pPos, pState);
    }
  }
}
