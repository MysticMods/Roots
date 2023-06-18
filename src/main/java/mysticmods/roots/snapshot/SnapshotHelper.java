package mysticmods.roots.snapshot;

import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.SnapshotCapability;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public class SnapshotHelper {
    public static <T extends Snapshot> void applyPlayer(LivingEntity entity, SnapshotSerializer<T> serializer, BiConsumer<Player, T> consumer) {
        if (entity instanceof Player player) {
            player.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(cap -> cap.ifPresent(player, serializer, snap -> consumer.accept(player, snap)));
        }
    }

    public static void apply(LivingEntity entity, BiConsumer<LivingEntity, SnapshotCapability> consumer) {
        entity.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(cap -> consumer.accept(entity, cap));
    }
}
