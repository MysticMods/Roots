package epicsquid.roots.util;

import epicsquid.mysticallib.util.Util;

import java.awt.*;

public class RgbColorUtil {

  public static RgbColor FLEETNESS = new RgbColor(255, 240, 32);
  public static RgbColor OVERGROWTH = new RgbColor(122, 69, 0);

  public static int generateRandomColor() {
    Color color = new Color(Util.rand.nextInt(256), Util.rand.nextInt(256), Util.rand.nextInt(256));
    return color.getRGB();
  }

}