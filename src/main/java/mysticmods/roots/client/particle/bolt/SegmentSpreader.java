package mysticmods.roots.client.particle.bolt;

import net.minecraft.world.phys.Vec3;

/**
 * A SegmentSpreader defines how successive bolt segments are arranged in the bolt generation calculation, based on previous state.
 *
 * @author aidancbrady
 */
public interface SegmentSpreader {

  /**
   * Don't remember where the last segment left off, just randomly move from the straight-line vector.
   */
  SegmentSpreader NO_MEMORY = (perpendicularDist, randVec, maxDiff, scale, progress, rand) -> randVec.scale(maxDiff * rand);

  // Segment spreader a la ChatGPT
  SegmentSpreader JAGGED = (perp, randVec, maxDiff, scale, progress, rand) -> randVec.scale(maxDiff);

  /**
   * Move from where the previous segment ended by a certain memory factor. Higher memory will restrict perpendicular movement.
   */
  static SegmentSpreader memory(float memoryFactor) {
    return (perpendicularDist, randVec, maxDiff, spreadScale, progress, rand) -> {
      double nextDiff = maxDiff * (1 - memoryFactor) * rand;
      Vec3 cur = randVec.scale(nextDiff);
      perpendicularDist = perpendicularDist.add(cur);
      double length = perpendicularDist.length();
      if (length > maxDiff) {
        perpendicularDist = perpendicularDist.scale(maxDiff / length);
      }
      return perpendicularDist.add(cur);
    };
  }

  Vec3 getSegmentAdd(Vec3 perpendicularDist, Vec3 randVec, float maxDiff, float scale, float progress, double rand);
}
