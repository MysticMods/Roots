package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.reference.Constants;
import mysticmods.roots.blockentity.inventory.LimitedItemStackHandler;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PedestalBlockEntity extends UseDelegatedBlockEntity implements InventoryBlockEntity, ServerTickBlockEntity, ClientTickBlockEntity {
  protected ItemStackHandler inventory;
  protected int limit;

  private ItemStack animationItem = ItemStack.EMPTY;
  private BlockState animationState = Blocks.AIR.defaultBlockState();
  private int animationTicks = 0;

  public float visualAnimationTicks = 0.0f;

  public PedestalBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int limit) {
    super(pType, pWorldPosition, pBlockState);
    this.limit = limit;
    inventory = new LimitedItemStackHandler(1, this::getLimit) {
      @Override
      protected void onContentsChanged(int slot) {
        if (PedestalBlockEntity.this.hasLevel() && !PedestalBlockEntity.this.getLevel().isClientSide()) {
          PedestalBlockEntity.this.setChanged();
          Level level = PedestalBlockEntity.this.getLevel();
          BlockPos pos = PedestalBlockEntity.this.getBlockPos();
          BlockState state = PedestalBlockEntity.this.getBlockState();
          level.sendBlockUpdated(pos, state, state, 8);
          level.invalidateCapabilities(pos);
        }
      }
    };
  }

  public PedestalBlockEntity(BlockPos pWorldPosition, BlockState pBlockState, int limit) {
    this(ModBlockEntities.PEDESTAL.get(), pWorldPosition, pBlockState, limit);
  }

  public PedestalBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    this(pType, pWorldPosition, pBlockState, Item.DEFAULT_MAX_STACK_SIZE);
  }

  public PedestalBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(ModBlockEntities.PEDESTAL.get(), pWorldPosition, pBlockState, Item.DEFAULT_MAX_STACK_SIZE);
  }

  public int getLimit() {
    return limit;
  }

  public void animate() {
    if (!level.isClientSide()) {
      this.animationTicks = Constants.PEDESTAL_ANIMATION_TICKS;
      this.animationItem = inventory.getStackInSlot(0).copy();
      if (this.animationItem.getItem() instanceof BlockItem blockItem) {
        this.animationState = blockItem.getBlock().defaultBlockState();
      }
      setChanged();
      updateViaState();
    }
  }

  public int getAnimateTick() {
    return animationTicks;
  }

  public boolean isAnimating() {
    return this.animationTicks > 0;
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
    return super.getUpdateTag(lookup);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    ItemStack inSlot = inventory.getStackInSlot(0);
    if (inHand.isEmpty() || player.isCrouching()) {
      // This is the worst part tbh
      if (!inSlot.isEmpty()) {
        ItemStack result = inventory.extractItem(0, inSlot.getCount(), false);
        if (!player.isCrouching()) {
          player.setItemInHand(hand, result);
        } else {
          ItemStack leftover = ItemUtil.insertPlayerInventoryStacked(player, result, false);
          if (!leftover.isEmpty()) {
            ItemUtil.Spawn.spawnItem(level, getBlockPos(), leftover);
          }
        }
      }
    } else if (inSlot.isEmpty()) {
      // insert
      ItemStack result = inventory.insertItem(0, inHand, false);
      if (!player.isCreative()) {
        player.setItemInHand(hand, result);
      }
    } else {
      // Are they the same item?
      if (ItemStack.isSameItemSameComponents(inSlot, inHand)) {
        ItemStack inSlot2 = inventory.extractItem(0, inSlot.getCount(), false);
        ItemStack leftover = ItemUtil.insertPlayerInventoryStacked(player, inSlot2, false);
        if (!leftover.isEmpty()) {
          ItemUtil.Spawn.spawnItem(level, getBlockPos(), leftover);
        }
      } else {
        // Are they different items?
        ItemStack inSlot2 = inventory.extractItem(0, inSlot.getCount(), false);
        ItemStack leftover = inventory.insertItem(0, inHand, false);
        if (!leftover.isEmpty()) {
          player.setItemInHand(hand, leftover);
          // Try to merge the rest
          if (!inSlot2.isEmpty()) {
            ItemStack stackedResult = ItemUtil.insertPlayerInventoryStacked(player, inSlot2, false);
            if (!stackedResult.isEmpty()) {
              ItemUtil.Spawn.spawnItem(level, getBlockPos(), stackedResult);
            }
          }
        } else {
          player.setItemInHand(hand, inSlot2);
        }
      }
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pProvider) {
    super.saveAdditional(pTag, pProvider);
    if (!animationItem.isEmpty()) {
      pTag.put("animationItem", animationItem.save(pProvider));
    }
    pTag.putInt("animationTicks", animationTicks);
    pTag.put("inventory", inventory.serializeNBT(pProvider));
    pTag.putInt("limit", limit);
  }

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("inventory"));
    }
    if (pTag.contains("limit", Tag.TAG_INT)) {
      limit = pTag.getInt("limit");
    } else {
      limit = 64;
    }
    if (pTag.contains("animationItem", Tag.TAG_COMPOUND)) {
      animationItem = ItemStack.parse(provider, pTag.getCompound("animationItem")).orElse(ItemStack.EMPTY);
    } else {
      animationItem = ItemStack.EMPTY;
    }
    if (this.animationItem.getItem() instanceof BlockItem blockItem) {
      this.animationState = blockItem.getBlock().defaultBlockState();
    }
    this.animationTicks = pTag.getInt("animationTicks");
    if (this.animationTicks == Constants.PEDESTAL_ANIMATION_TICKS) {
      this.visualAnimationTicks = 0.0f;
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
    if (this.animationTicks > 0 && !this.animationItem.isEmpty()) {
      return this.animationItem;
    }
    return inventory.getStackInSlot(0);
  }

  @Nullable
  public BlockState getHeldBlockState () {
    if (this.animationTicks > 0 && !this.animationState.isAir()) {
      return this.animationState;
    }

    return null;
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
      } else if (this.getBlockState().is(RootsTags.Blocks.GROVE_PEDESTALS) || this.getBlockState()
          .is(RootsTags.Blocks.DISPLAY_PEDESTALS)) {
        offset = 0.95;
      }
    }
    return offset;
  }

  // TODO: Block ticker
  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    if (this.animationTicks > 0) {
      this.animationTicks--;
      if (this.animationTicks == 0) {
        this.animationItem = ItemStack.EMPTY;
      }
      setChanged();
      updateViaState();
    }
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (ConfigManager.DISABLE_PATICLES.get()) {
      return;
    }
    if (this.animationTicks > 0) {
      this.visualAnimationTicks += 1.0f;

      float progress = Mth.clamp(this.visualAnimationTicks / (float) Constants.PEDESTAL_ANIMATION_TICKS, 0.0f, 1.0f);

      if (progress < 0.65f) {
        float y = pPos.getY() + (float) offset();

        if (progress <= 0.25f) {
          float liftProgress = progress / 0.25f;
          y += Mth.lerp(liftProgress, 0.0f, 0.6f) + 0.1f;
        } else {
          y += 0.6f;
        }

        for (int i = 0; i < 5; i++) {
          double x = pPos.getX() + 0.5 + (pLevel.random.nextDouble() - 0.5) * 0.2;
          double z = pPos.getZ() + 0.5 + (pLevel.random.nextDouble() - 0.5) * 0.2;

          // Spawn particles at the pedestal
          pLevel.addParticle(
              RootsParticleOptions.builder(ModParticles.GROVE_CRAFTER).color(ModSpells.WILDFIRE).build(),
              x, y, z,
              pLevel.getRandom().nextDouble() * 0.01, (pLevel.random.nextDouble() * 0.05), pLevel.getRandom()
                  .nextDouble() * 0.01
          );
        }
      }
    }
  }

  public void dropContents() {
    ItemStack inSlot = inventory.extractItem(0, inventory.getSlotLimit(0), false);
    if (!inSlot.isEmpty()) {
      ItemUtil.Spawn.spawnItem(getLevel(), getBlockPos(), inSlot);
    }
  }
}
