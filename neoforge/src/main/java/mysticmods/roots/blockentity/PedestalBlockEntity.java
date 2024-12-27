package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import noobanidus.libs.noobutil.util.ItemUtil;

public class PedestalBlockEntity extends UseDelegatedBlockEntity implements InventoryBlockEntity {
  private final ItemStackHandler inventory = new ItemStackHandler(1) {
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

  public PedestalBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
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
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
    pTag.put("inventory", inventory.serializeNBT());
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(pTag.getCompound("inventory"));
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      load(tag);
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
