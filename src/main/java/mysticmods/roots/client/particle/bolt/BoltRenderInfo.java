package mysticmods.roots.client.particle.bolt;

import mysticmods.roots.client.particle.Color;

public class BoltRenderInfo {

  public static final BoltRenderInfo DEFAULT = new BoltRenderInfo();
  public static final BoltRenderInfo ELECTRICITY = electricity();
  public static final BoltRenderInfo VINES = vines();
  public static final BoltRenderInfo SHATTER = shatter();

  /**
   * How much variance is allowed in segment lengths (parallel to straight line).
   */
  public float parallelNoise = 0.1F;
  /**
   * How much variance is allowed perpendicular to the straight line vector. Scaled by distance and spread function.
   */
  public float spreadFactor = 0.1F;

  /**
   * The chance of creating an additional branch after a certain segment.
   */
  public float branchInitiationFactor = 0.0F;
  /**
   * The chance of a branch continuing (post-initiation).
   */
  public float branchContinuationFactor = 0.0F;

  public Color color = Color.rgbad(0.45F, 0.45F, 0.5F, 0.8F);

  public RandomFunction randomFunction = RandomFunction.GAUSSIAN;
  public SpreadFunction spreadFunction = SpreadFunction.SINE;
  public SegmentSpreader segmentSpreader = SegmentSpreader.NO_MEMORY;

  public static BoltRenderInfo electricity() {
    return new BoltRenderInfo().color(Color.rgbad(0.54F, 0.91F, 1F, 0.8F)).noise(0.2F, 0.2F).branching(0.1F, 0.6F)
        .spreader(SegmentSpreader.memory(0.9F));
  }

  public static BoltRenderInfo vines() {
    return new BoltRenderInfo().color(Color.rgbad(0.34f, 0.8f, 0.3f, 0.5f)).noise(0f, 0.1f).branching(0.4f, 0f)
        .spreader(SegmentSpreader.memory(0.7f)).spreadFunction(SpreadFunction.LINEAR_ASCENT)
        .randomFunction(RandomFunction.UNIFORM);
  }

  public static BoltRenderInfo shatter() {
    return new BoltRenderInfo().color(Color.rgbad(0.64f, 0.5f, 0.4f, 1f)).noise(0.1f, 0.05f).branching(0.5f, 0f)
        .spreader(SegmentSpreader.memory(0.5f)).spreadFunction(SpreadFunction.SINE)
        .randomFunction(RandomFunction.GAUSSIAN);
  }

  public BoltRenderInfo noise(float parallelNoise, float spreadFactor) {
    this.parallelNoise = parallelNoise;
    this.spreadFactor = spreadFactor;
    return this;
  }

  public BoltRenderInfo branching(float branchInitiationFactor, float branchContinuationFactor) {
    this.branchInitiationFactor = branchInitiationFactor;
    this.branchContinuationFactor = branchContinuationFactor;
    return this;
  }

  public BoltRenderInfo spreader(SegmentSpreader segmentSpreader) {
    this.segmentSpreader = segmentSpreader;
    return this;
  }

  public BoltRenderInfo randomFunction(RandomFunction randomFunction) {
    this.randomFunction = randomFunction;
    return this;
  }

  public BoltRenderInfo spreadFunction(SpreadFunction spreadFunction) {
    this.spreadFunction = spreadFunction;
    return this;
  }

  public BoltRenderInfo color(Color color) {
    this.color = color;
    return this;
  }
}
