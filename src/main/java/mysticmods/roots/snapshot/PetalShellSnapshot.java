package mysticmods.roots.snapshot;

import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PetalShellSnapshot extends Snapshot {
  private int count;
  public PetalShellSnapshot(Player player, int count) {
    super(player);
    this.count = count;
  }

  public PetalShellSnapshot(int timestamp) {
    super(timestamp);
  }

  public int getCount() {
    return count;
  }

  @Override
  public SnapshotSerializer<?> getSerializer() {
    return ModSerializers.PETAL_SHELL.get();
  }

  public static class Serializer extends SnapshotSerializer<PetalShellSnapshot> {
    public Serializer(Builder<PetalShellSnapshot> builder) {
      super(builder);
    }

    @Override
    protected void updateFromTag(PetalShellSnapshot snapshot, CompoundTag tag) {
      snapshot.count = tag.getInt("count");
    }

    @Override
    protected void updateToTag(PetalShellSnapshot snapshot, CompoundTag tag) {
      tag.putInt("count", snapshot.count);
    }

    @Override
    public int getDecay() {
      return ModSpells.PETAL_SHELL_COOLDOWN.get().getValue() * 3;
    }
  }
}
