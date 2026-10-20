package mysticmods.roots.blockentity;

import com.google.common.collect.ImmutableList;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.power.PowerTicket;
import mysticmods.roots.api.grove.power.consumer.ISimpleGrovePowerConsumer;
import mysticmods.roots.api.grove.power.consumer.PowerRequest;
import mysticmods.roots.block.FurnaceAmplifierBlock;
import mysticmods.roots.blockentity.template.BaseBoundedBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class FurnaceAmplifierBlockEntity extends BaseBoundedBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, ISimpleGrovePowerConsumer {
  private static final int POWER_REQUIRED = 35;
  private static final TagKey<Grove> GROVE_REQUIRED = RootsTags.Groves.ANY;

  private static final PowerTicket.TicketDefinition TICKET_DEFINITION = new PowerTicket.TicketDefinition(ImmutableList.of(new PowerRequest(RootsTags.Groves.ANY, 35)));

  private PowerTicket ticket;
  private boolean poweredLastTick = false;
  private int poweredTicks;
  private boolean canBePowered = false;

  public FurnaceAmplifierBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public FurnaceAmplifierBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(ModBlockEntities.FURNACE_AMPLIFIER.get(), pWorldPosition, pBlockState);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
    return super.getUpdateTag(lookup);
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pProvider) {
    super.saveAdditional(pTag, pProvider);
    pTag.putBoolean("powered", this.poweredLastTick);
    pTag.putInt("poweredTicks", this.poweredTicks);
  }

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    if (pTag.contains("powered")) {
      this.poweredLastTick = pTag.getBoolean("powered");
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

  @Override
  public void onLoad() {
    super.onLoad();
    if (getLevel() == null) {
      RootsAPI.LOG.error("I feel like this is a broken contract: onLoad called without a level for {}", this);
      return;
    }

    getLevel().getData(ModAttachments.GROVE_CONSUMERS).add(getBlockPos());
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    if (!canBePowered) {
      recheckCanBePowered(pLevel, pPos, pState);
    }
  }

  private void recheckCanBePowered(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    Direction facing = pState.getValue(FurnaceAmplifierBlock.FACING);

    BlockState attachedTo = pLevel.getBlockState(pPos.relative(facing.getOpposite()));

  }


  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {

  }

  @Override
  public PowerTicket getTicketForTick(long tick) {
    if (ticket == null) {
      ticket = TICKET_DEFINITION.create(tick);
      return ticket;
    }

    if (ticket.isValid(tick)) {
      return ticket;
    }

    if (ticket.wasFullfilled()) {
      if (!this.poweredLastTick) {
        this.poweredLastTick = true;
        setChanged();
        updateViaState();
      }
    } else if (this.poweredLastTick) {
      this.poweredLastTick = false;
      setChanged();
      updateViaState();
    }

    ticket = TICKET_DEFINITION.create(tick);
    return ticket;
  }

  @Override
  public boolean wasPoweredLastTick() {
    return poweredLastTick;
  }

  @Override
  public TagKey<Grove> grovePowerTag() {
    return GROVE_REQUIRED;
  }

  @Override
  public int grovePowerRequired() {
    return POWER_REQUIRED;
  }
}
