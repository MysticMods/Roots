package mysticmods.roots.api.attachment;

import mysticmods.roots.network.ISyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttachmentUtil {
  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSync (ServerPlayer player, Supplier<AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer, Function<T, V> packetSupplier) {
    monitorForChange(player, attachment, consumer, (p, t) -> PacketDistributor.sendToPlayer(p, packetSupplier.apply(t)));
  }

  public static <T extends ICleanable, V extends ISyncPacket<T>> void monitorAndSync (ServerPlayer player, Supplier<AttachmentType<T>> attachment, Function<T, V> packetSupplier) {
    monitorAndSync(player, attachment, null, packetSupplier);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, Supplier<AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer) {
    monitorForChange(player, attachment, consumer, null);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, Supplier<AttachmentType<T>> attachment, @Nullable  BiConsumer<ServerPlayer, T> consumer, @Nullable BiConsumer<ServerPlayer, T> whenDirty) {
    T attachmentInstance = player.getData(attachment);
    if (consumer != null) {
      consumer.accept(player, attachmentInstance);
    }
    if (attachmentInstance.isDirty()) {
      if (whenDirty != null) {
        whenDirty.accept(player, attachmentInstance);
      }
      attachmentInstance.setDirty(false);
      player.setData(attachment, attachmentInstance);
    }
  }
}
