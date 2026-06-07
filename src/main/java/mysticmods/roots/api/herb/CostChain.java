package mysticmods.roots.api.herb;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class CostChain {
  private final List<CostSegment> segments;


  public CostChain(List<CostSegment> segments) {
    this.segments = segments;
    this.segments.sort(Comparator.comparing(CostSegment::type));
  }

  public List<CostSegment> segments() {
    return segments;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (CostChain) obj;
    return Objects.equals(this.segments, that.segments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(segments);
  }

  @Override
  public String toString() {
    return "CostChain[" +
        "segments=" + segments + ']';
  }

}
