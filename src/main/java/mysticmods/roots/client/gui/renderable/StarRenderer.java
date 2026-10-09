package mysticmods.roots.client.gui.renderable;

import com.mojang.blaze3d.systems.RenderSystem;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.util.CycleTimer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class StarRenderer implements Renderable {
  private static final List<ResourceLocation> STAR_SPRITES = List.of(
      RootsAPI.rl("reputation/stars/small_star_1"),
      RootsAPI.rl("reputation/stars/small_star_2"));

  private static final int STAR_SIZE = 3;

  private static final int[][] REGION_BANDS = {
      {0, 39, 29},
      {39, 95, 74},
      {95, 141, 97},
      {141, 163, 119},
  };
  private static final int REGION_LEFT = 29;
  private static final int REGION_RIGHT = 255;
  private static final int REGION_TOP = 0;
  private static final int REGION_BOTTOM = 163;

  // Stars painted into reputation_background.png, as pixel positions in the 256x256 texture
  private static final int[][] PAINTED_STARS = {
      {236, 0}, {51, 1}, {63, 1}, {85, 1}, {21, 2}, {254, 3}, {3, 4}, {69, 4},
      {42, 5}, {52, 6}, {65, 6}, {30, 7}, {242, 7}, {32, 8}, {13, 9}, {247, 13},
      {47, 14}, {255, 14}, {5, 15}, {21, 18}, {31, 20}, {250, 23}, {7, 26}, {2, 47},
      {18, 58}, {2, 69}, {253, 71}, {125, 74}, {4, 75}, {141, 77}, {251, 78}, {98, 79},
      {235, 80}, {102, 83}, {224, 84}, {255, 85}, {89, 88}, {139, 90}, {210, 90}, {82, 91},
      {92, 93}, {223, 93}, {249, 93}, {176, 94}, {198, 94}, {239, 94}, {207, 97}, {157, 98},
      {190, 98}, {105, 99}, {80, 100}, {146, 100}, {108, 103}, {251, 103}, {102, 104}, {115, 104},
      {95, 105}, {150, 105}, {123, 106}, {91, 107}, {93, 107}, {224, 107}, {106, 109}, {203, 109},
      {236, 110}, {92, 111}, {3, 113}, {255, 117}, {148, 120}, {200, 125}, {224, 125}, {106, 126},
      {137, 126}, {235, 128}, {209, 130}, {154, 135}, {194, 136}, {254, 136}, {108, 137}, {117, 138},
      {210, 138}, {244, 138}, {166, 141}, {179, 143}, {217, 144}, {248, 144}, {197, 145}, {150, 147},
      {118, 150}, {175, 150}, {136, 151}, {185, 151}, {228, 151}, {162, 152}, {209, 152}, {253, 152},
      {241, 155}, {151, 157}, {129, 160}, {199, 160}, {171, 166}, {215, 168}, {237, 168}, {149, 171},
      {187, 177}, {182, 190},
  };

  // Spacing between generated stars
  private static final int MIN_PADDING = 16;
  private static final int MAX_PADDING = 73;
  private static final int MAX_ATTEMPTS = 5000;

  // Clearance from painted stars, measured from the new star's centre
  private static final int PAINTED_PADDING = 9;

  // Alpha: brightest in the dark upper-right, faintest toward the bright lower-left
  private static final float MAX_ALPHA = 0.55F;
  private static final float MIN_ALPHA = 0.1F;
  private static final float LEFT_WEIGHT = 0.35F;
  private static final float BOTTOM_WEIGHT = 0.65F;

  private final RandomSource random = RandomSource.create();
  private final List<Star> stars = new ArrayList<>();

  private int left;
  private int top;

  public StarRenderer() {
    generate();
  }

  /**
   * Call from Screen#init whenever the GUI is (re)laid out.
   */
  public void setPosition(int left, int top) {
    this.left = left;
    this.top = top;
  }

  private void generate() {
    int starCount = 40 + this.random.nextInt(10) + this.random.nextInt(3) * 4;
    int minSq = MIN_PADDING * MIN_PADDING;
    int maxSq = MAX_PADDING * MAX_PADDING;

    for (int attempts = 0; this.stars.size() < starCount && attempts < MAX_ATTEMPTS; attempts++) {
      int x = Mth.nextInt(this.random, REGION_LEFT, REGION_RIGHT - STAR_SIZE);
      int y = Mth.nextInt(this.random, REGION_TOP, REGION_BOTTOM - STAR_SIZE);

      if (!inRegion(x, y) || nearPaintedStar(x, y)) {
        continue;
      }

      boolean tooClose = false;
      boolean nearEnough = this.stars.isEmpty(); // first star can go anywhere

      for (Star other : this.stars) {
        int dx = other.x - x;
        int dy = other.y - y;
        int distSq = dx * dx + dy * dy;
        if (distSq < minSq) {
          tooClose = true;
          break;
        }
        if (distSq <= maxSq) {
          nearEnough = true;
        }
      }

      if (!tooClose && nearEnough) {
        CycleTimer timer = CycleTimer.create(18 + this.random.nextInt(4) * 13);
        this.stars.add(new Star(x, y, alphaAt(x, y), timer));
      }
    }
  }

  private static int leftEdgeAt(int y) {
    for (int[] band : REGION_BANDS) {
      if (y >= band[0] && y < band[1]) {
        return band[2];
      }
    }
    return -1; // outside the region vertically
  }

  private static boolean inRegion(int x, int y) {
    int bottom = y + STAR_SIZE - 1;
    if (y < REGION_TOP || bottom >= REGION_BOTTOM || x + STAR_SIZE > REGION_RIGHT) {
      return false;
    }
    // The staircase steps right as it goes down, so the bottom row is the binding one
    int edge = leftEdgeAt(bottom);
    return edge >= 0 && x >= edge;
  }

  private static boolean nearPaintedStar(int x, int y) {
    int cx = x + STAR_SIZE / 2;
    int cy = y + STAR_SIZE / 2;
    int padSq = PAINTED_PADDING * PAINTED_PADDING;
    for (int[] p : PAINTED_STARS) {
      int dx = p[0] - cx;
      int dy = p[1] - cy;
      if (dx * dx + dy * dy < padSq) {
        return true;
      }
    }
    return false;
  }

  private static float alphaAt(int x, int y) {
    float nx = (x - REGION_LEFT) / (float) (REGION_RIGHT - REGION_LEFT);  // 0 = left, 1 = right
    float ny = (y - REGION_TOP) / (float) (REGION_BOTTOM - REGION_TOP);   // 0 = top, 1 = bottom
    float glow = Mth.clamp((1F - nx) * LEFT_WEIGHT + ny * BOTTOM_WEIGHT, 0F, 1F);
    return Mth.lerp(glow, MAX_ALPHA, MIN_ALPHA);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    RenderSystem.enableBlend();
    RenderSystem.enableDepthTest();

    for (Star star : this.stars) {
      graphics.setColor(1F, 1F, 1F, star.alpha);
      graphics.blitSprite(star.timer.getCycled(STAR_SPRITES),
          this.left + star.x, this.top + star.y, STAR_SIZE, STAR_SIZE);
    }

    graphics.setColor(1F, 1F, 1F, 1F);
  }

  private record Star(int x, int y, float alpha, CycleTimer timer) {
  }
}