package mysticmods.roots.client.gui.renderable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class CloudRenderer implements Renderable {
  private static final ResourceLocation[] CLOUDS = {
      RootsAPI.rl("reputation/clouds/clouds_01"),
      RootsAPI.rl("reputation/clouds/clouds_02"),
      RootsAPI.rl("reputation/clouds/clouds_03"),
      RootsAPI.rl("reputation/clouds/clouds_04"),
  };

  private static final int INSET = 0; // no frame in the art any more; clip to the art itself

  private static final int MIN_Y = 141;
  private static final int MAX_Y = 186;

  private static final int START_DROP = 108;
  private static final int END_DROP = 193;
  private static final int MIN_DROPPED_Y = 158;
  private static final int MAX_DROPPED_Y = 197;

  private static final int FADE_START = 69;
  private static final int FADE_END = 153;

  private static final float MAX_ALPHA = 0.9F;
  private static final float MIN_ALPHA = 0.2F;

  private static final int MAX_CLOUDS = 3;
  private static final float MIN_SPEED = 2F;  // pixels per second
  private static final float MAX_SPEED = 4F;
  private static final float MIN_SPAWN_DELAY = 12F; // seconds
  private static final float MAX_SPAWN_DELAY = 25F;

  // Vertical bobbing
  private static final float MIN_BOB_AMPLITUDE = 0.75F; // pixels
  private static final float MAX_BOB_AMPLITUDE = 1.75F;
  private static final float MIN_BOB_PERIOD = 12F;      // seconds per full up-down cycle
  private static final float MAX_BOB_PERIOD = 20F;

  private final int width;
  private final int height;
  private final boolean leftToRight;

  private int left;
  private int top;

  private final RandomSource random = RandomSource.create();
  private final List<Cloud> clouds = new ArrayList<>();
  private long lastNanos = -1L;
  private float spawnTimer;

  public CloudRenderer(int width, int height, boolean leftToRight) {
    this.width = width;
    this.height = height;
    this.leftToRight = leftToRight;

    // One cloud already in flight so the screen doesn't open empty
    this.clouds.add(newCloud(Mth.nextFloat(this.random, 0.1F, 0.6F)));
    this.spawnTimer = nextSpawnDelay();
  }

  /**
   * Call from Screen#init whenever the GUI is (re)laid out.
   */
  public void setPosition(int left, int top) {
    this.left = left;
    this.top = top;
  }

  private float nextSpawnDelay() {
    return Mth.nextFloat(this.random, MIN_SPAWN_DELAY, MAX_SPAWN_DELAY);
  }

  private Cloud newCloud(float progress) {
    ResourceLocation id = CLOUDS[this.random.nextInt(CLOUDS.length)];
    SpriteContents contents = Minecraft.getInstance().getGuiSprites().getSprite(id).contents();
    int w = contents.width();
    int h = contents.height();

    float bobAmplitude = Mth.nextFloat(this.random, MIN_BOB_AMPLITUDE, MAX_BOB_AMPLITUDE);
    float bobFrequency = Mth.TWO_PI / Mth.nextFloat(this.random, MIN_BOB_PERIOD, MAX_BOB_PERIOD);
    float bobPhase = this.random.nextFloat() * Mth.TWO_PI;

    // Whole cloud (top and bottom, including bob) stays inside MIN_Y..MAX_Y
    float y = pickTop(MIN_Y, MAX_Y, h, bobAmplitude, MIN_Y);

    // Same for the dropped band, and never above the cruising height so it only descends
    float dropY = pickTop(MIN_DROPPED_Y, MAX_DROPPED_Y, h, bobAmplitude, y);

    // Positions are relative to the image; left/top are applied at render time.
    // The path runs from just hidden at the entry edge to just hidden past the far edge.
    float startX = this.leftToRight ? INSET - w : this.width - INSET;
    float endX = this.leftToRight ? this.width - INSET : INSET - w;

    float pixelsPerSecond = Mth.nextFloat(this.random, MIN_SPEED, MAX_SPEED);
    float speed = pixelsPerSecond / Math.max(1F, Math.abs(endX - startX));

    return new Cloud(id, w, h, y, dropY, startX, endX, speed, progress, bobAmplitude, bobFrequency, bobPhase);
  }

  private float pickTop(int bandMin, int bandMax, int h, float bob, float notAbove) {
    float low = Math.max(bandMin + bob, notAbove);
    float high = bandMax - h - bob;
    if (low > high) {
      return low > bandMin + bob ? Math.max(high, bandMin + bob) : (bandMin + bandMax - h) / 2F;
    }
    return Mth.nextFloat(this.random, low, high);
  }

  private void update(float dt) {
    this.clouds.removeIf(c -> {
      c.age += dt;
      c.progress += c.speed * dt;
      return c.progress >= 1F;
    });

    // Only count down while there's room, so a freed slot waits a full delay
    if (this.clouds.size() < MAX_CLOUDS) {
      this.spawnTimer -= dt;
      if (this.spawnTimer <= 0F) {
        this.clouds.add(newCloud(0F));
        this.spawnTimer = nextSpawnDelay();
      }
    }
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    long now = Util.getNanos();
    float dt = this.lastNanos < 0L ? 0F : Math.min((now - this.lastNanos) / 1.0E9F, 0.1F);
    this.lastNanos = now;
    update(dt);

    graphics.enableScissor(this.left + INSET, this.top + INSET,
        this.left + this.width - INSET, this.top + this.height - INSET);
    RenderSystem.enableBlend();

    PoseStack pose = graphics.pose();
    for (Cloud c : this.clouds) {
      float relX = Mth.lerp(c.progress, c.startX, c.endX);

      // Leading edge along the direction of travel; right-to-left mirrors the zones
      float along = this.leftToRight ? relX + c.w : this.width - relX;

      float fade = Mth.clamp((along - FADE_START) / (float) (FADE_END - FADE_START), 0F, 1F);
      graphics.setColor(1F, 1F, 1F, Mth.lerp(fade, MAX_ALPHA, MIN_ALPHA));

      float t = Mth.clamp((along - START_DROP) / (float) (END_DROP - START_DROP), 0F, 1F);
      t = t * t * (3F - 2F * t); // ease in and out of the descent

      float baseY = Mth.lerp(t, c.y, c.dropY);
      float bob = c.bobAmplitude * Mth.sin(c.age * c.bobFrequency + c.bobPhase);

      float x = this.left + relX;
      float y = this.top + baseY + bob;

      pose.pushPose();
      pose.translate(x, y, 0F);
      graphics.blitSprite(c.sprite, 0, 0, c.w, c.h);
      pose.popPose();
    }
    graphics.setColor(1F, 1F, 1F, 1F);
    graphics.disableScissor();
  }

  private static final class Cloud {
    final ResourceLocation sprite;
    final int w;
    final int h;
    final float y;
    final float dropY;
    final float startX;
    final float endX;
    final float speed; // progress per second
    final float bobAmplitude;
    final float bobFrequency; // radians per second
    final float bobPhase;
    float progress;
    float age;

    Cloud(ResourceLocation sprite, int w, int h, float y, float dropY, float startX, float endX, float speed,
          float progress, float bobAmplitude, float bobFrequency, float bobPhase) {
      this.sprite = sprite;
      this.w = w;
      this.h = h;
      this.y = y;
      this.dropY = dropY;
      this.startX = startX;
      this.endX = endX;
      this.speed = speed;
      this.progress = progress;
      this.bobAmplitude = bobAmplitude;
      this.bobFrequency = bobFrequency;
      this.bobPhase = bobPhase;
    }
  }
}
