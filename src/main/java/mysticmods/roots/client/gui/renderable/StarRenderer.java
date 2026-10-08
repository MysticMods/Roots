package mysticmods.roots.client.gui.renderable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.util.CycleTimer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Locale;

public class StarRenderer implements Renderable {
  private final int height;
  private final int width;
  private final int x;
  private final int y;
  private final CycleTimer cycleTimer;
  private static final List<ResourceLocation> STAR_SPRITES = List.of(RootsAPI.rl("reputation/stars/small_star_1"), RootsAPI.rl("reputation/stars/small_star_2"));
  private static final RandomSource random = RandomSource.create();

  public StarRenderer(int x, int y) {
    this.cycleTimer = CycleTimer.create(18 + random.nextInt(4) * 13);

    this.height = 3;
    this.width = 3;
    this.x = x;
    this.y = y;
  }

  @Override
  public void render(GuiGraphics guiGraphics, int i, int i1, float v) {
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.4F);
    PoseStack pose = guiGraphics.pose();
    pose.pushPose();
    RenderSystem.enableBlend();
    RenderSystem.enableDepthTest();

    guiGraphics.blitSprite(cycleTimer.getCycled(STAR_SPRITES), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    pose.popPose();
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }
}
