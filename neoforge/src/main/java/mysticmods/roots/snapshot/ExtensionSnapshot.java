package mysticmods.roots.snapshot;

import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

public class ExtensionSnapshot extends Snapshot {
  private int radiusZX, radiusY;
  private AABB aabb;

  public ExtensionSnapshot(Player player, int radiusZX, int radiusY) {
    super(player);
    this.radiusZX = radiusZX;
    this.radiusY = radiusY;
  }

  public ExtensionSnapshot(int timestamp) {
    super(timestamp);
  }

  public int getRadiusZX() {
    return radiusZX;
  }

  public int getRadiusY() {
    return radiusY;
  }

  public AABB getAABB() {
    if (aabb == null) {
      aabb = new AABB(-radiusZX, -radiusY, -radiusZX, radiusZX, radiusY, radiusZX);
    }
    return aabb;
  }

  @Override
  public SnapshotSerializer<?> getSerializer() {
    return ModSerializers.EXTENSION.get();
  }

  public static class Serializer extends SnapshotSerializer<ExtensionSnapshot> {
    public Serializer(Builder<ExtensionSnapshot> builder) {
      super(builder);
    }

    @Override
    protected void updateFromTag(ExtensionSnapshot snapshot, CompoundTag tag) {
      snapshot.radiusY = tag.getInt("radiusY");
      snapshot.radiusZX = tag.getInt("radiusZX");
    }

    @Override
    protected void updateToTag(ExtensionSnapshot snapshot, CompoundTag tag) {
      tag.putInt("radiusY", snapshot.radiusY);
      tag.putInt("radiusZX", snapshot.radiusZX);
    }

    @Override
    public int getDecay() {
      return ModSpells.EXTENSION_SENSE_DANGER_DURATION.get().getValue() * 3;
    }
  }
}
