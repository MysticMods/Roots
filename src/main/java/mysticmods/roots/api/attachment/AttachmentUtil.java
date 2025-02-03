package mysticmods.roots.api.attachment;

import mysticmods.roots.network.ISyncPacket;
import mysticmods.roots.network.client.ClientboundDiscardEntityAttachmentPacket;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttachmentUtil {
  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSync (ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer, Function<T, V> packetSupplier) {
    monitorForChange(player, attachment, consumer, (p, t) -> PacketDistributor.sendToPlayer(p, packetSupplier.apply(t)));
  }

  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSync (ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, Function<T, V> packetSupplier) {
    monitorAndSync(player, attachment, null, packetSupplier);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer) {
    monitorForChange(player, attachment, consumer, null);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, @Nullable  BiConsumer<ServerPlayer, T> consumer, @Nullable BiConsumer<ServerPlayer, T> whenDirty) {
    T attachmentInstance = player.getData(attachment.value());
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

  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSyncEntity (LivingEntity player, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, BiConsumer<LivingEntity, T> consumer, BiFunction<T, Integer, V> packetSupplier) {
    monitorEntityForChange(player, attachment, consumer, (p, t) -> PacketDistributor.sendToPlayersTrackingEntity(p, packetSupplier.apply(t, p.getId())));
  }

  public static <T extends ICleanable> void monitorEntityForChange (LivingEntity entity, DeferredHolder<AttachmentType<?>, AttachmentType<T>> attachment, @Nullable BiConsumer <LivingEntity, T> consumer, @Nullable BiConsumer<LivingEntity, T> whenDirty) {
    if (!entity.hasData(attachment.value())) {
      return;
    }
    T attachmentInstance = entity.getData(attachment.value());
    if (attachmentInstance.isEmpty()) {
      entity.removeData(attachment.value());
      PacketDistributor.sendToPlayersTrackingEntity(entity, new ClientboundDiscardEntityAttachmentPacket(attachment.getKey().location().toString(), entity.getId()));
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
