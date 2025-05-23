package mysticmods.roots.client.particle.bolt;

import mysticmods.roots.client.particle.Color;
import net.minecraft.SharedConstants;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DynamicBoltEffect implements IBoltEffect {

  private final RandomSource random = RandomSource.create();

  private final BoltRenderInfo renderInfo;

  private final PositionProvider provider;

  private final int segments;

  private final long seed;


  private int count = 1;
  private float size = 0.1F;

  private int lifespan = SharedConstants.TICKS_PER_SECOND + (SharedConstants.TICKS_PER_SECOND / 2);

  private SpawnFunction spawnFunction = SpawnFunction.delay(3 * SharedConstants.TICKS_PER_SECOND);
  private FadeFunction fadeFunction = FadeFunction.fade(0.5F);

  public DynamicBoltEffect(BoltRenderInfo info, PositionProvider provider, int segments) {
    this.renderInfo = info;
    this.provider = provider;
    this.segments = segments;
    this.seed = random.nextLong();
  }

  /**
   * Set the amount of bolts to render for this single bolt instance.
   *
   * @param count amount of bolts to render
   * @return this
   */
  @Override
  public DynamicBoltEffect count(int count) {
    this.count = count;
    return this;
  }

  /**
   * Set the starting size (or width) of bolt segments.
   *
   * @param size starting size of bolt segments
   * @return this
   */
  public DynamicBoltEffect size(float size) {
    this.size = size;
    return this;
  }

  /**
   * Define the {@link SpawnFunction} for this bolt effect.
   *
   * @param spawnFunction spawn function to use
   * @return this
   */
  public DynamicBoltEffect spawn(SpawnFunction spawnFunction) {
    this.spawnFunction = spawnFunction;
    return this;
  }

  /**
   * Define the {@link FadeFunction} for this bolt effect.
   *
   * @param fadeFunction fade function to use
   * @return this
   */
  public DynamicBoltEffect fade(FadeFunction fadeFunction) {
    this.fadeFunction = fadeFunction;
    return this;
  }

  /**
   * Define the lifespan (in ticks) of this bolt, at the end of which the bolt will expire.
   *
   * @param lifespan lifespan to use in ticks
   * @return this
   */
  public DynamicBoltEffect lifespan(int lifespan) {
    this.lifespan = lifespan;
    return this;
  }

  public int getLifespan() {
    return lifespan;
  }

  public SpawnFunction getSpawnFunction() {
    return spawnFunction;
  }

  public FadeFunction getFadeFunction() {
    return fadeFunction;
  }

  public Color getColor() {
    return renderInfo.color;
  }

  public List<BoltQuads> generate(float partialTicks) {
    RandomSource random = RandomSource.create(this.seed);
    Vec3 start = this.provider.getStart(partialTicks);
    Vec3 end = this.provider.getStop(partialTicks);

    List<BoltQuads> quads = new ArrayList<>();
    Vec3 diff = end.subtract(start);
    float totalDistance = (float) diff.length();
    for (int i = 0; i < count; i++) {
      Queue<BoltInstructions> drawQueue = new LinkedList<>();
      drawQueue.add(new BoltInstructions(start, 0, Vec3.ZERO, false));
      while (!drawQueue.isEmpty()) {
        BoltInstructions data = drawQueue.poll();
        Vec3 perpendicularDist = data.perpendicularDist;
        float noise = (random.nextFloat() - 0.5F) * 2F * renderInfo.parallelNoise;
        float step = (1F / segments) * (1F + noise);
        float progress = Math.min(1F, data.progress + step);
        Vec3 segmentEnd;
        float segmentDiffScale = renderInfo.spreadFunction.getMaxSpread(progress);
        if (progress >= 1 && segmentDiffScale <= 0) {
          segmentEnd = end;
        } else {
          float maxDiff = renderInfo.spreadFactor * segmentDiffScale * totalDistance;
          Vec3 randVec = findRandomOrthogonalVector(diff, random);
          double rand = renderInfo.randomFunction.getRandom(random);
          perpendicularDist = renderInfo.segmentSpreader.getSegmentAdd(perpendicularDist, randVec, maxDiff, segmentDiffScale, progress, rand);
          // new vector is original + current progress through segments + perpendicular change
          segmentEnd = start.add(diff.scale(progress)).add(perpendicularDist);
        }
        float boltSize = size * (0.5F + (1 - progress) * 0.5F);
        BoltQuads quadData = createSegmentQuads(data.start, segmentEnd, boltSize);
        quads.add(quadData);

        if (progress >= 1) {
          break; // break if we've reached the defined end point
        } else if (!data.isBranch) {
          // continue the bolt if this is the primary (non-branch) segment
          drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, false));
        } else if (random.nextFloat() < renderInfo.branchContinuationFactor) {
          // branch continuation
          drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, true));
        }

        while (random.nextFloat() < renderInfo.branchInitiationFactor * (1 - progress)) {
          // branch initiation (probability decreases as progress increases)
          drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, true));
        }
      }
    }
    return quads;
  }

  @Override
  public int getCount() {
    return count;
  }

  private static Vec3 findRandomOrthogonalVector(Vec3 vec, RandomSource rand) {
    Vec3 orthogonal;
    do {
      Vec3 newVec = new Vec3(-0.5 + rand.nextDouble(), -0.5 + rand.nextDouble(), -0.5 + rand.nextDouble());
      orthogonal = vec.cross(newVec);
    } while (orthogonal.lengthSqr() < 1e-6);
    return orthogonal.normalize();
  }

  private static final Vec3 MIDDLE_BLOCK_OFFSET = new Vec3(0.5, 0.5, 0.5);

  private BoltQuads createSegmentQuads(Vec3 startPos, Vec3 end, float size) {
    Vec3 diff = end.subtract(startPos);
    Vec3 rightAdd = diff.cross(MIDDLE_BLOCK_OFFSET).normalize().scale(size);
    Vec3 backAdd = diff.cross(rightAdd).normalize().scale(size), rightAddSplit = rightAdd.scale(0.5F);

    Vec3 startRight = startPos.add(rightAdd);
    Vec3 startBack = startPos.add(rightAddSplit).add(backAdd);
    Vec3 endRight = end.add(rightAdd), endBack = end.add(rightAddSplit).add(backAdd);

    BoltQuads quads = new BoltQuads();
    quads.addQuad(startPos, end, endRight, startRight);
    quads.addQuad(startRight, endRight, end, startPos);

    quads.addQuad(startRight, endRight, endBack, startBack);
    quads.addQuad(startBack, endBack, endRight, startRight);

    return quads;
  }

  protected record BoltInstructions(Vec3 start, float progress, Vec3 perpendicularDist,
                                    boolean isBranch) {
  }
}
