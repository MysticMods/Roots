package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.grove.IGroveConsumer;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.network.client.fx.GrowthAmplifierFXPacket;
import mysticmods.roots.ritual.BloomingRitual;
import mysticmods.roots.util.PositionCache;
import mysticmods.roots.util.SimpleNoise;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class AmplifierBlockEntity extends UseDelegatedBlockEntity implements InventoryBlockEntity, ServerTickBlockEntity, ClientTickBlockEntity {
  public float ticks;
  public float rotationAccumulator;

  private PositionCache pCache;

  private boolean powered = false;

  private int poweredTicks = 0;

  protected ItemStackHandler inventory;

  public AmplifierBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.GROWTH_AMPLIFIER.get(), pWorldPosition, pBlockState);
    inventory = new ItemStackHandler(1) {
      @Override
      protected void onContentsChanged(int slot) {
        if (AmplifierBlockEntity.this.hasLevel() && !AmplifierBlockEntity.this.getLevel().isClientSide()) {
          AmplifierBlockEntity.this.setChanged();
          Level level = AmplifierBlockEntity.this.getLevel();
          BlockPos pos = AmplifierBlockEntity.this.getBlockPos();
          BlockState state = AmplifierBlockEntity.this.getBlockState();
          level.sendBlockUpdated(pos, state, state, 8);
          level.invalidateCapabilities(pos);
        }
      }
    };
  }

  @Override
  public int getRadiusX() {
    return ConfigManager.GROWTH_AMPLIFIER_BOUNDS_X.get();
  }

  @Override
  public int getRadiusY() {
    return ConfigManager.GROWTH_AMPLIFIER_BOUNDS_Y.get();
  }

  @Override
  public int getRadiusZ() {
    return ConfigManager.GROWTH_AMPLIFIER_BOUNDS_Z.get();
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
    return super.getUpdateTag(lookup);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
/*
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
*/

    return InteractionResult.SUCCESS;
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pProvider) {
    super.saveAdditional(pTag, pProvider);
    pTag.put("inventory", inventory.serializeNBT(pProvider));
    pTag.putBoolean("powered", this.powered);
    pTag.putInt("poweredTicks", this.poweredTicks);
  }

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("inventory"));
    }
    if (pTag.contains("powered")) {
      this.powered = pTag.getBoolean("powered");
    }
    if (pTag.contains("poweredTicks")) {
      this.poweredTicks = pTag.getInt("poweredTicks");
    } else {
      this.poweredTicks = 0;
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
    return offset;
  }

  // TODO: Block ticker
  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
/*    if (isPowered()) {
      if (poweredTicks < 80) {
        poweredTicks++;
        return;
      }

      Item item = TagUtil.getRandomElement(pLevel, RootsTags.Items.GROWTH_AMPLIFIER_GRASSES);
      if (!(item instanceof BlockItem flowerToPlace)) {
        return;
      }

      if (pCache == null) {
        pCache = new PositionCache(pPos, getBoundingBox().moved(pPos.getX(), pPos.getY(), pPos.getZ()));
      }

      RandomSource random = pLevel.getRandom();
      pCache.initCache(level, List.of(BloomingRitual.TWO_AIR_ABOVE));
      BlockPos chosen = pCache.random(BloomingRitual.TWO_AIR_ABOVE, random);
      if (chosen == null) {
        return;
      }
      Vec3 center = Vec3.atCenterOf(chosen);

      BlockPlaceContext context = new BlockPlaceContext(pLevel, null, InteractionHand.MAIN_HAND, new ItemStack(flowerToPlace), new BlockHitResult(center, Direction.UP, chosen, false));
      if (flowerToPlace.place(context).consumesAction()) {
        PacketDistributor.sendToPlayersTrackingChunk(pLevel, new ChunkPos(pPos), new GrowthAmplifierFXPacket(center, getBlockPos()));
        poweredTicks = 0;
      }
    }*/
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    this.ticks += 1f;

    float time = ticks * 0.02f;
    float baseSpeed = 0.65f;
    float speedMod = SimpleNoise.noise(time * 0.4f) * 0.8f + 1.0f;

    this.rotationAccumulator += baseSpeed * speedMod;
  }

/*  @Override
  public boolean isPowered() {
    return powered;
  }

  @Override
  public void markPowered(IGroveInstance grove, boolean powered) {
    if (this.powered != powered) {
      this.powered = powered;
      setChanged();
      updateViaState();
    }
  }

  @Override
  public int getRequiredPower(IGroveInstance grove) {
    return 30;
  }*/
}
