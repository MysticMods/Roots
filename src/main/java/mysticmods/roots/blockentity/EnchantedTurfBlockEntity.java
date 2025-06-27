package mysticmods.roots.blockentity;

import com.google.common.collect.ImmutableList;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.grove.GrovePower;
import mysticmods.roots.api.grove.IGroveConsumer;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.grove.PowerTicket;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.network.client.fx.GrowthFXPacket;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class EnchantedTurfBlockEntity extends BaseBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, IGroveConsumer {
  private static final PowerTicket.TicketDefinition TICKET_DEFINITION = new PowerTicket.TicketDefinition(ImmutableList.of(new GrovePower.Consumer(RootsTags.Groves.ANY, 60)));

  private PowerTicket ticket;
  private boolean poweredLastTick = false;
  private int poweredTicks;

  public EnchantedTurfBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.ENCHANTED_TURF.get(), pWorldPosition, pBlockState);
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
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    getTicketForTick(pLevel.getGameTime());

    if (wasPoweredLastTick()) {
      Item item = TagUtil.getRandomElement(pLevel, RootsTags.Items.GROWTH_AMPLIFIER_GRASSES);
      if (!(item instanceof BlockItem flowerToPlace)) {
        return;
      }

      BlockPos above = pPos.above();
      if (!pLevel.isEmptyBlock(above)) {
        return;
      }
      if (!pLevel.isEmptyBlock(above.above())) {
        return;
      }

      if (poweredTicks < ConfigManager.ENCHANTED_TURF_TICKS.get()) {
        poweredTicks++;
        return;
      }

      Vec3 center = Vec3.atCenterOf(above);

      BlockPlaceContext context = new BlockPlaceContext(pLevel, null, InteractionHand.MAIN_HAND, new ItemStack(flowerToPlace), new BlockHitResult(center, Direction.UP, above, false));
      if (flowerToPlace.place(context).consumesAction()) {
        PacketDistributor.sendToPlayersTrackingChunk(pLevel, new ChunkPos(pPos), new GrowthFXPacket(getBlockPos()));
        poweredTicks = 0;
      }
    }
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
}
