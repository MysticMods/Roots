package mysticmods.roots.client.blockentity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.concurrent.ExecutionException;

public class ColorHelper {
  private final static Cache<BlockPos, Color> COLOR_CACHE = CacheBuilder.newBuilder().build();
  private final static RandomSource random = RandomSource.create();
  private final static Color DEFAULT_COLOR = new Color(1f, 0.5f, 0.5f);

  public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f);
  public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
  public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);

  public static Color color(BlockPos position) {
    try {
      return COLOR_CACHE.get(position, () -> {
        random.setSeed(position.asLong());
        int color = Mth.hsvToRgb(random.nextFloat(), 0.3f + 0.7f * random.nextFloat(), 0.8f + 0.2f * random.nextFloat());
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        return new Color(r, g, b);
      });
    } catch (ExecutionException e) {
      return DEFAULT_COLOR;
    }
  }

  public record Color(float r, float g, float b, float a) {
    public Color(float r, float g, float b) {
      this(r, g, b, 1.0f);
    }
  }
}
