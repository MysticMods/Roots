package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PedestalBlockEntity extends UseDelegatedBlockEntity implements InventoryBlockEntity {
  // TODO: Actually limit limitable pedestals
  protected ItemStackHandler inventory;

  public PedestalBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
    inventory = new ItemStackHandler(1) {
      @Override
      protected void onContentsChanged(int slot) {
        if (PedestalBlockEntity.this.hasLevel() && !PedestalBlockEntity.this.getLevel().isClientSide()) {
          PedestalBlockEntity.this.setChanged();
          Level level = PedestalBlockEntity.this.getLevel();
          BlockPos pos = PedestalBlockEntity.this.getBlockPos();
          BlockState state = PedestalBlockEntity.this.getBlockState();
          level.sendBlockUpdated(pos, state, state, 8);
        }
      }
    };
  }

  public PedestalBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(ModBlockEntities.PEDESTAL.get(), pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray) {
    // TODO::
    InteractionHand hand = InteractionHand.MAIN_HAND;
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }
    boolean limited = state.is(RootsTags.Blocks.LIMITED_PEDESTALS);

    // TODO: Swap instead
    ItemStack inHand = player.getItemInHand(hand);
    ItemStack inSlot = inventory.getStackInSlot(0);
    if (inHand.isEmpty()) {
      // extract
      if (!inSlot.isEmpty()) {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        player.setItemInHand(hand, inSlot);
      }
    } else if (inSlot.isEmpty()) {
      // insert
      if (limited && inHand.getCount() > 1) {
        ItemStack copy = inHand.copy();
        copy.setCount(1);
        inHand.shrink(1);
        inventory.setStackInSlot(0, copy);
        player.setItemInHand(hand, inHand);
      } else {
        inventory.setStackInSlot(0, inHand);
        player.setItemInHand(hand, ItemStack.EMPTY);
      }
    } else {
      // swapsies!
      if (limited) {
        ItemStack copy = inHand.copy();
        copy.setCount(1);
        inHand.shrink(1);
        inventory.setStackInSlot(0, copy);
        player.setItemInHand(hand, inHand);
        if (!player.addItem(inSlot)) {
          ItemUtil.Spawn.spawnItem(level, getBlockPos(), inSlot);
        }
      } else {
        inventory.setStackInSlot(0, inHand);
        player.setItemInHand(hand, inSlot);
      }
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pProvider) {
    super.saveAdditional(pTag, pProvider);
    pTag.put("inventory", inventory.serializeNBT(pProvider));
  }

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("inventory"));
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
    super.onDataPacket(net, pkt, provider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, provider);
    }
  }

  public ItemStack getHeldItem() {
    return inventory.getStackInSlot(0);
  }

  @Override
  public ItemStackHandler getInventory() {
    return inventory;
  }

  private double offset = -1;

  public double offset() {
    if (offset == -1) {
      if (this.getBlockState().is(RootsTags.Blocks.RITUAL_PEDESTALS)) {
        offset = 1.4;
      } else if (this.getBlockState().is(RootsTags.Blocks.GROVE_PEDESTALS)) {
        offset = 0.95;
      }
    }
    return offset;
  }
}
