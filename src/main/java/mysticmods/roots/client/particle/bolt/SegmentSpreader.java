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

  static SegmentSpreader memoryMinimumAngle (float memoryFactor, double minimumAngle) {
    double minRadians = Math.cos(Math.toRadians(minimumAngle));

    return (perpendicularDist, randVec, maxDiff, spreadScale, progress, rand) -> {
      double nextDiff = maxDiff * (1 - memoryFactor) * rand;
      Vec3 cur = randVec.scale(nextDiff);

      if (!perpendicularDist.equals(Vec3.ZERO)) {
        double dot = perpendicularDist.normalize().dot(cur.normalize());

        if (dot > minRadians) {
          cur = cur.scale(-1);
        }
      }

      Vec3 next = perpendicularDist.scale(memoryFactor).add(cur);

      double length = next.length();
      if (length > maxDiff) {
        next = next.scale(maxDiff / length);
      }

      return next;
    };
  }

  Vec3 getSegmentAdd(Vec3 perpendicularDist, Vec3 randVec, float maxDiff, float scale, float progress, double rand);
}
