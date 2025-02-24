package mysticmods.roots.client;

public class RootsRenderer {
  public static int getColorARGB(int red, int green, int blue, float alpha) {
    if (alpha < 0) {
      alpha = 0;
    } else if (alpha > 1) {
      alpha = 1;
    }
    int argb = (int) (255 * alpha) << 24;
    argb |= red << 16;
    argb |= green << 8;
    argb |= blue;
    return argb;
  }

  public static float getRed(int color) {
    return (color >> 16 & 0xFF) / 255.0F;
  }

  public static float getGreen(int color) {
    return (color >> 8 & 0xFF) / 255.0F;
  }

  public static float getBlue(int color) {
    return (color & 0xFF) / 255.0F;
  }

  public static float getAlpha(int color) {
    return (color >> 24 & 0xFF) / 255.0F;
  }
}
