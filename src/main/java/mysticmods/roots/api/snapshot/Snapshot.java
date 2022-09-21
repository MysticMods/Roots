package mysticmods.roots.api.snapshot;

import net.minecraft.world.entity.player.Player;

public abstract class Snapshot {
  protected int timestamp;
  public Snapshot(Player player) {
    this.timestamp = getExpiry(player);
  }

  public Snapshot(int timestamp) {
    this.timestamp = timestamp;
  }

  public int getTimestamp() {
    return timestamp;
  }

  public abstract SnapshotSerializer<?> getSerializer();

  public int getExpiry(Player player) {
    return player.tickCount + getSerializer().getDecay();
  }

  public boolean isExpired(Player player) {
    return player.tickCount >= timestamp;
  }
}
