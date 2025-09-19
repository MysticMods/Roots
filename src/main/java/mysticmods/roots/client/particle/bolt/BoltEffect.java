package mysticmods.roots.client.particle.bolt;

import mysticmods.roots.client.particle.Color;
import net.minecraft.SharedConstants;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BoltEffect implements IBoltEffect {

  private final RandomSource random = RandomSource.create();

  private final BoltRenderInfo renderInfo;

  private final Vec3 start;
  private final Vec3 end;

  private final int segments;

  private int count = 1;
  private float size = 0.1F;

  private int lifespan = SharedConstants.TICKS_PER_SECOND + (SharedConstants.TICKS_PER_SECOND / 2);

  private SpawnFunction spawnFunction = SpawnFunction.delay(3 * SharedConstants.TICKS_PER_SECOND);
  private FadeFunction fadeFunction = FadeFunction.fade(0.5F);

  public BoltEffect(Vec3 start, Vec3 end) {
    this(BoltRenderInfo.DEFAULT, start, end, (int) (Math.sqrt(start.distanceTo(end) * 100)));
  }

  public BoltEffect(BoltRenderInfo info, Vec3 start, Vec3 end, int segments) {
    this.renderInfo = info;
    this.start = start;
    this.end = end;
    this.segments = segments;
  }

  /**
   * Set the amount of bolts to render for this single bolt instance.
   *
   * @param count amount of bolts to render
   * @return this
   */
  @Override
  public IBoltEffect count(int count) {
    this.count = count;
    return this;
  }

  /**
   * Set the starting size (or width) of bolt segments.
   *
   * @param size starting size of bolt segments
   * @return this
   */
  @Override
  public BoltEffect size(float size) {
    this.size = size;
    return this;
  }

  /**
   * Define the {@link SpawnFunction} for this bolt effect.
   *
   * @param spawnFunction spawn function to use
   * @return this
   */
  @Override
  public BoltEffect spawn(SpawnFunction spawnFunction) {
    this.spawnFunction = spawnFunction;
    return this;
  }

  /**
   * Define the {@link FadeFunction} for this bolt effect.
   *
   * @param fadeFunction fade function to use
   * @return this
   */
  @Override
  public IBoltEffect fade(FadeFunction fadeFunction) {
    this.fadeFunction = fadeFunction;
    return this;
  }

  /**
   * Define the lifespan (in ticks) of this bolt, at the end of which the bolt will expire.
   *
   * @param lifespan lifespan to use in ticks
   * @return this
   */
  @Override
  public BoltEffect lifespan(int lifespan) {
    this.lifespan = lifespan;
    return this;
  }

  @Override
  public int getLifespan() {
    return lifespan;
  }

  @Override
  public SpawnFunction getSpawnFunction() {
    return spawnFunction;
  }

  @Override
  public FadeFunction getFadeFunction() {
    return fadeFunction;
  }

  @Override
  public Color getColor() {
    return renderInfo.color;
  }

  @Override
  public List<BoltQuads> generate(float partialTicks) {
    List<BoltQuads> quads = new ArrayList<>();
    Vec3 diff = end.subtract(start);
    float totalDistance = (float) diff.length();
    for (int i = 0; i < count; i++) {
      Queue<BoltInstructions> drawQueue = new LinkedList<>();
      drawQueue.add(new BoltInstructions(start, 0, new Vec3(0, 0, 0), null, false));
      while (!drawQueue.isEmpty()) {
        BoltInstructions data = drawQueue.poll();
        Vec3 perpendicularDist = data.perpendicularDist;
        float progress = data.progress + (1F / segments) * (1 - renderInfo.parallelNoise + random.nextFloat() * renderInfo.parallelNoise * 2);
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
        BoltQuadData quadData = createQuads(data.cache, data.start, segmentEnd, boltSize);
        quads.add(quadData.quads());

        if (progress >= 1) {
          break; // break if we've reached the defined end point
        } else if (!data.isBranch) {
          // continue the bolt if this is the primary (non-branch) segment
          drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.cache(), false));
        } else if (random.nextFloat() < renderInfo.branchContinuationFactor) {
          // branch continuation
          drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.cache(), true));
        }

        while (random.nextFloat() < renderInfo.branchInitiationFactor * (1 - progress)) {
          // branch initiation (probability decreases as progress increases)
          drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.cache(), true));
        }
      }
    }
    return quads;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public RenderPreset getRenderType() {
    return renderInfo.renderPreset;
  }

  private static Vec3 findRandomOrthogonalVector(Vec3 vec, RandomSource rand) {
    Vec3 newVec = new Vec3(-0.5 + rand.nextDouble(), -0.5 + rand.nextDouble(), -0.5 + rand.nextDouble());
    return vec.cross(newVec).normalize();
  }

  private BoltQuadData createQuads(QuadCache cache, Vec3 startPos, Vec3 end, float size) {
    Vec3 diff = end.subtract(startPos);
    Vec3 rightAdd = diff.cross(new Vec3(0.5, 0.5, 0.5)).normalize().scale(size);
    Vec3 backAdd = diff.cross(rightAdd).normalize().scale(size), rightAddSplit = rightAdd.scale(0.5F);

    Vec3 start = cache == null ? startPos : cache.prevEnd;
    Vec3 startRight = cache == null ? start.add(rightAdd) : cache.prevEndRight;
    Vec3 startBack = cache == null ? start.add(rightAddSplit).add(backAdd) : cache.prevEndBack;
    Vec3 endRight = end.add(rightAdd), endBack = end.add(rightAddSplit).add(backAdd);

    BoltQuads quads = new BoltQuads();
    quads.addQuad(start, end, endRight, startRight);
    quads.addQuad(startRight, endRight, end, start);

    quads.addQuad(startRight, endRight, endBack, startBack);
    quads.addQuad(startBack, endBack, endRight, startRight);

    return new BoltQuadData(quads, new QuadCache(end, endRight, endBack));
  }

  private record QuadCache(Vec3 prevEnd, Vec3 prevEndRight, Vec3 prevEndBack) {
  }

  private record BoltQuadData(BoltQuads quads, QuadCache cache) {
  }

  protected record BoltInstructions(Vec3 start, float progress, Vec3 perpendicularDist, QuadCache cache,
                                    boolean isBranch) {
  }

}
