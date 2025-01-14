package mysticmods.roots.api.attachment;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class EntityCooldowns {
  public static void setExpiresAt(@Nonnull Entity entity, Supplier<AttachmentType<Integer>> attachmentType, int tickCount) {
    entity.setData(attachmentType, tickCount);
  }

  public static boolean hasExpired(@Nonnull Entity entity, Supplier<AttachmentType<Integer>> attachmentType) {
    int expiresAt = entity.getData(attachmentType);
    return expiresAt == -1 || ServerLifecycleHooks.getCurrentServer().getTickCount() >= expiresAt;
  }
}
