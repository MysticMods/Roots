package mysticmods.roots.api.herb;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// Growth Infusion - 2 wildroot
// Modifier 1 - additive 4 wildewheet
// Modifier 2 - negate base cost, additive 3 spirit leaf
// Modifier 3 - multiplicative total wildewheet +1.5
// Modifier 4 - negative wildewheet
// Modifier 5 - 50% reduction spirit leaf

// Source=SPELL, Herb=Wildroot, Value=2, Type=ADDITIVE
// Source=MODIFIER, Herb=Wildewheet, Value=4, Type=ADDITIVE
// Source=MODIFIER, Herb=???, Value=0, Type=NEGATIVE_BASE
// Source=MODIFIER, Herb=Spirit Leaf, Value=3, Type=ADDITIVE
// Source=MODIFIER, Herb=Wildewheet, Value=1.5, Type=MULTIPLICATIVE_TOTAL
// Source=MODIFIER, Herb=Wildewheet, Value=0, Type=NEGATIVE
// Source=MODIFIER, Herb=Spirit Leaf, Value=0.5, Type=MULTIPLICATIVE_TOTAL

// Spell -> Modifier
// Modifier - Additive
// Modifier -

// Wildroot: 2
// Wildroot: 2, Wildewheet: 4
// Wildroot: 0, Wildewheet: 4
// Wildroot: 0, Wildewheet: 4, Spirit Leaf: 3
// Wildroot: 0, Wildewheet: 6, Spirit Leaf: 3
// Wildroot: 0, Wildewheet: 0, Spirit Leaf: 3
// Wildroot: 0, Wildewheet: 0, Spirit Leaf: 1.5



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
