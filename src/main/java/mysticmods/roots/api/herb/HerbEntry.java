package mysticmods.roots.api.herb;

public class HerbEntry {
  public final HerbEntryType type;
  public final Herb herb;
  public final int slot;
  public int count;
  public final int subindex;

  public HerbEntry(HerbEntryType type, Herb herb, int slot, int count, int subindex) {
    this.type = type;
    this.herb = herb;
    this.slot = slot;
    this.count = count;
    this.subindex = subindex;
  }

  public HerbEntryType getType() {
    return type;
  }

  public Herb getHerb() {
    return herb;
  }

  public int getSlot() {
    return slot;
  }

  public int getCount() {
    return count;
  }

  public int getSubindex() {
    return subindex;
  }
}
