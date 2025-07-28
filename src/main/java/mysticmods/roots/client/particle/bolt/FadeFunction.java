package mysticmods.roots.client.particle.bolt;

/**
 * A bolt's fade function allows one to define lower and upper bounds on the bolt segments rendered based on lifespan. This allows for dynamic 'fade-in' and
 * 'fade-out' effects.
 *
 * @author aidancbrady
 */
public interface FadeFunction {

  /**
   * No fade; render the bolts entirely throughout their lifespan.
   */
  FadeFunction NONE = (totalBolts, lifeScale) -> new RenderBounds(0, totalBolts);

  /**
   * Render bolts with a segment-by-segment 'fade' in and out, with a specified fade duration (applied to start and finish).
   */
  static FadeFunction fade(float fade) {
    return (totalBolts, lifeScale) -> {
      int start = lifeScale > (1 - fade) ? (int) (totalBolts * (lifeScale - (1 - fade)) / fade) : 0;
      int end = lifeScale < fade ? (int) (totalBolts * (lifeScale / fade)) : totalBolts;
      return new RenderBounds(start, end);
    };
  }

  static FadeFunction fade(float fadeIn, float fadeOut) {
    return (totalBolts, lifeScale) -> {
      int start, end;

      if (lifeScale < fadeIn) {
        float t = lifeScale / fadeIn;
        end = (int) (totalBolts * t);
        start = 0;
      } else if (lifeScale > (1 - fadeOut)) {
        float t = (lifeScale - (1 - fadeOut)) / fadeOut;
        start = (int) (totalBolts * t);
        end = totalBolts;
      } else {
        start = 0;
        end = totalBolts;
      }

      return new RenderBounds(Math.max(0, start), Math.min(totalBolts, end));
    };
  }

  static FadeFunction outFade(float fade) {
    return (totalBolts, lifeScale) -> {
      if (lifeScale < (1 - fade)) {
        return new RenderBounds(0, totalBolts);
      }
      float t = (lifeScale - (1 - fade)) / fade;
      int visible = (int) (totalBolts * (1 - t));
      int start = Math.max(0, totalBolts - visible);
      return new RenderBounds(start, totalBolts);
    };
  }

  static FadeFunction pulseFromCenter(float width) {
    return (totalBolts, lifeScale) -> {
      float half = width / 2.0f;
      float center = lifeScale;

      int start = (int) ((center - half) * totalBolts);
      int end = (int) ((center + half) * totalBolts);

      return new RenderBounds(
          Math.max(0, start),
          Math.min(totalBolts, end)
      );
    };
  }

  static FadeFunction pingPong(float width) {
    return (totalBolts, lifeScale) -> {
      float half = width / 2.0f;
      float center = 1.0f - Math.abs(1.0f - 2.0f * lifeScale);

      int start = (int) ((center - half) * totalBolts);
      int end = (int) ((center + half) * totalBolts);

      return new RenderBounds(
          Math.max(0, start),
          Math.min(totalBolts, end)
      );
    };
  }

  RenderBounds getRenderBounds(int totalBolts, float lifeScale);

  record RenderBounds(int start, int end) {
  }
}
