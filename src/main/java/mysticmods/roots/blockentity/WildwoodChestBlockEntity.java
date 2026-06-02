package mysticmods.roots.blockentity;

import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

public class WildwoodChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
  private static final int CAPACITY = 9 * 5;
  private NonNullList<ItemStack> items = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);

  private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
    @Override
    protected void onOpen(Level p_155531_, BlockPos p_155532_, BlockState p_155533_) {
      p_155531_.playSound(
          null,
          (double) p_155532_.getX() + 0.5,
          (double) p_155532_.getY() + 0.5,
          (double) p_155532_.getZ() + 0.5,
          SoundEvents.CHEST_OPEN,
          SoundSource.BLOCKS,
          0.5F,
          p_155531_.random.nextFloat() * 0.1F + 0.9F
      );
    }

    @Override
    protected void onClose(Level p_155541_, BlockPos p_155542_, BlockState p_155543_) {
      p_155541_.playSound(
          null,
          (double) p_155542_.getX() + 0.5,
          (double) p_155542_.getY() + 0.5,
          (double) p_155542_.getZ() + 0.5,
          SoundEvents.CHEST_CLOSE,
          SoundSource.BLOCKS,
          0.5F,
          p_155541_.random.nextFloat() * 0.1F + 0.9F
      );
    }

    @Override
    protected void openerCountChanged(Level p_155535_, BlockPos p_155536_, BlockState p_155537_, int p_155538_, int p_155539_) {
      WildwoodChestBlockEntity.this.signalOpenCount(p_155535_, p_155536_, p_155537_, 1, p_155539_);
    }

    @Override
    protected boolean isOwnContainer(Player player) {
      if (!(player.containerMenu instanceof ChestMenu)) {
        return false;
      } else {
        Container container = ((ChestMenu) player.containerMenu).getContainer();
        return container == WildwoodChestBlockEntity.this;
      }
    }
  };

  private final ChestLidController chestLidController = new ChestLidController();

  public WildwoodChestBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.WILDWOOD_CHEST.get(), pos, blockState);
  }

  @Override
  public int getContainerSize() {
    return CAPACITY;
  }

  @Override
  protected Component getDefaultName() {
    return Component.translatable("container.wildwoodchest");
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    if (!this.tryLoadLootTable(tag)) {
      ContainerHelper.loadAllItems(tag, this.items, registries);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    if (!this.trySaveLootTable(tag)) {
      ContainerHelper.saveAllItems(tag, this.items, registries);
    }
  }

  public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, WildwoodChestBlockEntity blockEntity) {
    blockEntity.chestLidController.tickLid();
  }

  @Override
  public boolean triggerEvent(int id, int type) {
    if (id == 1) {
      this.chestLidController.shouldBeOpen(type > 0);
      return true;
    } else {
      return super.triggerEvent(id, type);
    }
  }

  @Override
  public void startOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  public void stopOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  protected NonNullList<ItemStack> getItems() {
    return this.items;
  }

  @Override
  protected void setItems(NonNullList<ItemStack> items) {
    this.items = items;
  }

  @Override
  public float getOpenNess(float partialTicks) {
    return this.chestLidController.getOpenness(partialTicks);
  }

  public static int getOpenCount(BlockGetter level, BlockPos pos) {
    BlockState blockstate = level.getBlockState(pos);
    if (blockstate.hasBlockEntity()) {
      BlockEntity blockentity = level.getBlockEntity(pos);
      if (blockentity instanceof WildwoodChestBlockEntity ww) {
        return ww.openersCounter.getOpenerCount();
      }
    }

    return 0;
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory player) {
    return new ChestMenu(MenuType.GENERIC_9x5, id, player, this, 5);
  }

  public void recheckOpen() {
    if (!this.remove) {
      this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
    Block block = state.getBlock();
    level.blockEvent(pos, block, 1, eventParam);
  }
}
