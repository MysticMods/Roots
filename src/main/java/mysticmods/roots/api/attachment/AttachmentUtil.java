package mysticmods.roots.api.attachment;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.ISyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AttachmentUtil {
  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSync(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer, Function<T, V> packetSupplier) {
    monitorForChange(player, attachment, consumer, (p, t) -> PacketDistributor.sendToPlayer(p, packetSupplier.apply(t)));
  }

  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSync(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, Function<T, V> packetSupplier) {
    monitorAndSync(player, attachment, null, packetSupplier);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer) {
    monitorForChange(player, attachment, consumer, null);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, @Nullable BiConsumer<ServerPlayer, T> consumer, @Nullable BiConsumer<ServerPlayer, T> whenDirty) {
    T attachmentInstance = player.getData(attachment.value());
    if (attachmentInstance == null) {
      return;
    }
    if (consumer != null) {
      consumer.accept(player, attachmentInstance);
    }
    if (attachmentInstance.isDirty()) {
      if (whenDirty != null) {
        whenDirty.accept(player, attachmentInstance);
      }
      attachmentInstance.setDirty(false);
      player.setData(attachment.value(), attachmentInstance);
    }
  }

  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSyncBlockEntity(BlockEntity blockEntity, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<BlockEntity, T> consumer, BiFunction<T, Long, V> packetSupplier) {
    BlockPos pos = blockEntity.getBlockPos();
    monitorBlockEntityForChange(blockEntity, attachment, consumer, (be, t) -> PacketDistributor.sendToPlayersNear((ServerLevel) blockEntity.getLevel(), null, pos.getX(), pos.getY(), pos.getZ(), 64, packetSupplier.apply(t, blockEntity.getBlockPos()
        .asLong())));
  }

  public static <T extends ICleanable> void monitorBlockEntityForChange(BlockEntity entity, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, @Nullable BiConsumer<BlockEntity, T> consumer, @Nullable BiConsumer<BlockEntity, T> whenDirty) {
    BlockPos pos = entity.getBlockPos();
    if (!entity.hasData(attachment.value())) {
      return;
    }
    T attachmentInstance = entity.getData(attachment.value());
    if (attachmentInstance.isEmpty()) {
      entity.removeData(attachment.value());
      Packet<?> packet = entity.getUpdatePacket();
      if (packet != null) {
        ((ServerLevel) entity.getLevel()).getServer().getPlayerList()
            .broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64, entity.getLevel()
                .dimension(), entity.getUpdatePacket());
      }
      return;
    }
    if (consumer != null) {
      consumer.accept(entity, attachmentInstance);
    }
    if (attachmentInstance.isDirty()) {
      if (whenDirty != null) {
        whenDirty.accept(entity, attachmentInstance);
      }
      attachmentInstance.setDirty(false);
      entity.setData(attachment.value(), attachmentInstance);
    }
  }

  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSyncEntity(Entity player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<Entity, T> consumer, BiFunction<T, Integer, V> packetSupplier) {
    monitorEntityForChange(player, attachment, consumer, (p, t) -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(p, packetSupplier.apply(t, p.getId())));
  }

  public static <T extends ICleanable> void monitorEntityForChange(Entity entity, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, @Nullable BiConsumer<Entity, T> consumer, @Nullable BiConsumer<Entity, T> whenDirty) {
    if (!entity.hasData(attachment.value())) {
      return;
    }
    T attachmentInstance = entity.getData(attachment.value());
    if (attachmentInstance.isEmpty()) {
      entity.removeData(attachment.value());
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, RootsAPI.getInstance()
          .getEntityDiscardPacket(attachment.getKey(), entity));
      return;
    }
    if (consumer != null) {
      consumer.accept(entity, attachmentInstance);
    }
    if (attachmentInstance.isDirty()) {
      if (whenDirty != null) {
        whenDirty.accept(entity, attachmentInstance);
      }
      attachmentInstance.setDirty(false);
      entity.setData(attachment.value(), attachmentInstance);
    }
  }
}
