package mysticmods.roots.api.attachment;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class AttachmentUtil {

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, Supplier<AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer) {
    monitorForChange(player, attachment, consumer, null);
  }

  public static <T extends ICleanable> void monitorForChange(ServerPlayer player, Supplier<AttachmentType<T>> attachment, BiConsumer<ServerPlayer, T> consumer, BiConsumer<ServerPlayer, T> whenDirty) {
    T attachmentInstance = player.getData(attachment);
    consumer.accept(player, attachmentInstance);
    if (attachmentInstance.isDirty()) {
      if (whenDirty != null) {
        whenDirty.accept(player, attachmentInstance);
      }
      attachmentInstance.setDirty(false);
      player.setData(attachment, attachmentInstance);
    }
  }
}
