package mysticmods.roots.snapshot;

import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SkySoarerSnapshot extends Snapshot {
  private float amplifier;
  private Vec3 originalMovement;

  public SkySoarerSnapshot(Player player, Vec3 originalMovement, float amplifier) {
    super(player);
    this.originalMovement = originalMovement;
    this.amplifier = amplifier;
  }

  public SkySoarerSnapshot(int timestamp) {
    super(timestamp);
  }

  public Vec3 getOriginalMovement() {
    return originalMovement;
  }

  public float getAmplifier() {
    return amplifier;
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
      snapshot.amplifier = tag.getFloat("amplifier");
    }

    @Override
    protected void updateToTag(SkySoarerSnapshot snapshot, CompoundTag tag) {
      tag.putDouble("x", snapshot.originalMovement.x);
      tag.putDouble("y", snapshot.originalMovement.y);
      tag.putDouble("z", snapshot.originalMovement.z);
      tag.putFloat("amplifier", snapshot.amplifier);
    }

    @Override
    public int getDecay() {
      // TODO: max duration of the spell???
      // duration can vary, waah
      return ModSpells.SKY_SOARER_DURATION.get().getValue() * 3;
    }
  }
}
