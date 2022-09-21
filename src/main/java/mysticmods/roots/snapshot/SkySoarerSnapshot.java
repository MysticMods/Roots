package mysticmods.roots.snapshot;

import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SkySoarerSnapshot extends Snapshot {
  private Vec3 originalMovement;

  public SkySoarerSnapshot(Player player, Vec3 originalMovement) {
    super(player);
    this.originalMovement = originalMovement;
  }

  public SkySoarerSnapshot(int timestamp) {
    super(timestamp);
  }

  public Vec3 getOriginalMovement() {
    return originalMovement;
  }

  @Override
  public SnapshotSerializer<?> getSerializer() {
    return ModSerializers.SKY_SOARER.get();
  }

  public static class Serializer extends SnapshotSerializer<SkySoarerSnapshot> {
    public Serializer(Builder<SkySoarerSnapshot> builder) {
      super(builder);
    }

    @Override
    protected void updateFromTag(SkySoarerSnapshot snapshot, CompoundTag tag) {
      snapshot.originalMovement = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    @Override
    protected void updateToTag(SkySoarerSnapshot snapshot, CompoundTag tag) {
      tag.putDouble("x", snapshot.originalMovement.x);
      tag.putDouble("y", snapshot.originalMovement.y);
      tag.putDouble("z", snapshot.originalMovement.z);
    }

    @Override
    public int getDecay() {
      // TODO: max duration of the spell???
      return 120;
    }
  }
}
