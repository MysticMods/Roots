package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.grove.ReputationRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class ReputationButton extends Button {
  private static final ResourceLocation BUTTON_BASE = RootsAPI.rl("reputation/base_rank");

  private static ResourceLocation baseFromRank(int rank) {
    return switch (rank) {
      case 2 -> BUTTON_BASE.withSuffix("_2");
      case 3 -> BUTTON_BASE.withSuffix("_3");
      case 4 -> BUTTON_BASE.withSuffix("_4");
      default -> BUTTON_BASE;
    };
  }

  private static ResourceLocation iconFromRank(GroveType grove, int rank) {
    ResourceLocation base = RootsAPI.rl("reputation/" + grove.name());
    return switch (rank) {
      case 1, 2, 3, 4 -> base.withSuffix("_glow");
      default -> base.withSuffix("_base");
    };
  }

  private static ResourceLocation progress(GroveType grove) {
    return RootsAPI.rl("reputation/" + grove.name() + "_progress");
  }

  private final GroveType grove;
  private ReputationRanks.Progress progress;
  protected boolean wasHovered = false;
  private AnimationState state = AnimationState.NORMAL;
  private float animationStart = -1f;
  private float animationStartScale = 1f;

  public ReputationButton(int x, int y, GroveType grove) {
    super(x, y, 32, 32, CommonComponents.EMPTY, (v) -> {
    }, DEFAULT_NARRATION);
    this.grove = grove;
  }

  public void setProgress(ReputationRanks.Progress progress) {
    this.progress = progress;
  }

  public int getRank() {
    if (this.progress == null) {
      return 0;
    }

    return this.progress.rank();
  }

  public float getProgress() {
    if (this.progress == null) {
      return 0f;
    }

    int current = progress.progress();
    int max = progress.nextRank();

    return max <= 0 ? 0F : Mth.clamp((float) current / (float) max, 0F, 1F);
  }

  @Override
  protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    float currentTick = Minecraft.getInstance().levelRenderer.getTicks() + partialTick;

    if (this.isHovered != this.wasHovered) {
      // Start from wherever we currently are, so reversing mid-animation is smooth
      this.animationStartScale = getScale(currentTick);
      this.animationStart = currentTick;
      this.state = this.isHovered ? AnimationState.HOVER_START : AnimationState.HOVER_STOP;
    }
    this.wasHovered = this.isHovered;

    float scale = getScale(currentTick);

    // Once the shrink has finished, we're back to resting
    if (this.state == AnimationState.HOVER_STOP && currentTick - this.animationStart >= 5f) {
      this.state = AnimationState.NORMAL;
    }

    PoseStack pose = guiGraphics.pose();
    pose.pushPose();
    RenderSystem.enableBlend();
    RenderSystem.enableDepthTest();

/*    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.1f);

    guiGraphics.blitSprite(DROP_SHADOW, this.getX() - 16, this.getY() - 16, 64, 64);*/

    guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);

    if (scale != 1f) {
      // Snap the scaled button to whole screen pixels so its edges never fall between pixels;
      // otherwise the edge column can sample the neighbouring sprite in the GUI atlas
      float guiScale = (float) Minecraft.getInstance().getWindow().getGuiScale();
      float sizePx = Math.round(this.getWidth() * scale * guiScale);
      float snapped = sizePx / (this.getWidth() * guiScale);

      float cx = this.getX() + this.getWidth() / 2f;
      float cy = this.getY() + this.getHeight() / 2f;
      float leftPx = Math.round((cx - this.getWidth() * snapped / 2f) * guiScale);
      float topPx = Math.round((cy - this.getHeight() * snapped / 2f) * guiScale);

      pose.translate(leftPx / guiScale, topPx / guiScale, 0f);
      pose.scale(snapped, snapped, 1f);
      pose.translate(-this.getX(), -this.getY(), 0f);
    }


    guiGraphics.blitSprite(baseFromRank(getRank()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    guiGraphics.blitSprite(iconFromRank(grove, getRank()), this.getX(), this.getY(), this.getWidth(), this.getHeight());

    if (this.progress != null) {
      float sweep = this.progress.rank() == 4 ? Mth.TWO_PI : Mth.clamp(getProgress(), 0F, 1F) * Mth.TWO_PI;
      if (sweep > 0F) {
        renderProgress(guiGraphics, sweep);
      }
    }

    pose.popPose();

    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  private float getScale(float currentTick) {
    if (this.state == AnimationState.NORMAL) {
      return 1f;
    }

    float target = this.state == AnimationState.HOVER_START ? 1.1f : 1f;
    float t = Mth.clamp((currentTick - this.animationStart) / 5f, 0f, 1f);
    t = t * t * (3f - 2f * t); // smoothstep easing
    return Mth.lerp(t, this.animationStartScale, target);
  }

  private void renderProgress(GuiGraphics guiGraphics, float sweep) {
    TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(progress(grove));
    RenderSystem.setShaderTexture(0, sprite.atlasLocation());
    RenderSystem.setShader(GameRenderer::getPositionTexShader);

    Matrix4f matrix = guiGraphics.pose().last().pose();
    BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

    float hw = this.getWidth() / 2F;
    float hh = this.getHeight() / 2F;
    float cx = this.getX() + hw;
    float cy = this.getY() + hh;

    for (int q = 0; q < 4; q++) {
      float start = q * Mth.HALF_PI;
      if (sweep <= start) {
        break;
      }
      float end = Math.min(sweep, start + Mth.HALF_PI);

      float halfStart = (q % 2 == 0) ? hh : hw;
      float halfNext = (q % 2 == 0) ? hw : hh;
      float cornerAngle = start + (float) Math.atan2(halfNext, halfStart);

      float[] s = edgePoint(start, hw, hh);
      float[] e = edgePoint(end, hw, hh);
      float[] c;
      if (end > cornerAngle) {
        float[] n = edgePoint(start + Mth.HALF_PI, hw, hh);
        c = new float[]{s[0] + n[0], s[1] + n[1]};
      } else {
        c = e;
      }

      vertex(buffer, matrix, sprite, cx, cy, hw, hh, 0F, 0F);
      vertex(buffer, matrix, sprite, cx, cy, hw, hh, e[0], e[1]);
      vertex(buffer, matrix, sprite, cx, cy, hw, hh, c[0], c[1]);
      vertex(buffer, matrix, sprite, cx, cy, hw, hh, s[0], s[1]);
    }

    MeshData mesh = buffer.build();
    if (mesh != null) {
      BufferUploader.drawWithShader(mesh);
    }
  }

  private static float[] edgePoint(float angle, float hw, float hh) {
    float dx = (float) -Math.sin(angle);
    float dy = (float) Math.cos(angle);
    float tx = Math.abs(dx) > 1.0E-6F ? hw / Math.abs(dx) : Float.MAX_VALUE;
    float ty = Math.abs(dy) > 1.0E-6F ? hh / Math.abs(dy) : Float.MAX_VALUE;
    float t = Math.min(tx, ty);
    return new float[]{dx * t, dy * t};
  }

  private static void vertex(BufferBuilder buffer, Matrix4f matrix, TextureAtlasSprite sprite,
                             float cx, float cy, float hw, float hh, float ox, float oy) {
    float u = Mth.lerp((ox + hw) / (2F * hw), sprite.getU0(), sprite.getU1());
    float v = Mth.lerp((oy + hh) / (2F * hh), sprite.getV0(), sprite.getV1());
    buffer.addVertex(matrix, cx + ox, cy + oy, 0F).setUv(u, v);
  }

  private enum AnimationState {
    HOVER_START,
    HOVER_STOP,
    NORMAL;
  }
}
