package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.client.gui.buttons.ReputationButton;
import mysticmods.roots.client.gui.renderable.CloudRenderer;
import mysticmods.roots.client.gui.renderable.StarRenderer;
import mysticmods.roots.client.particle.screen.ScreenParticleSupplier;
import mysticmods.roots.client.particle.screen.base.ScreenParticle;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModGroves;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReputationScreen extends RootsScreen implements ScreenParticleSupplier {
  private ReputationButton fairy;
  private ReputationButton elemental;
  private ReputationButton cultivation;
  private ReputationButton pastoral;
  private ReputationButton wild;
  private ReputationButton fungal;
  private ReputationButton twilight;
  private final List<Vector2i> stars = new ArrayList<>();

  private final CloudRenderer clouds;

  private final Map<ParticleRenderType, List<ScreenParticle>> myParticles = new HashMap<>();

  protected ReputationScreen() {
    super(Component.translatable("roots.gui.reputation"));
    this.clouds = new CloudRenderer(256, 256, true);

    int starStartX = 106;
    int starStopX = 220;
    int starStartY = 60;
    int starStopY = 140;

    int minPadding = 20;
    int maxPadding = 65;

    RandomSource random = RandomSource.create();

    int starCount = 20 + random.nextInt(10) + random.nextInt(3) * 4;

    int minSq = minPadding * minPadding;
    int maxSq = maxPadding * maxPadding;

    for (int attempts = 0; stars.size() < starCount && attempts < 1000; attempts++) {
      int x = Mth.nextInt(random, starStartX, starStopX);
      int y = Mth.nextInt(random, starStartY, starStopY);

      boolean tooClose = false;
      boolean nearEnough = stars.isEmpty(); // first star can go anywhere

      for (Vector2i other : stars) {
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
        stars.add(new Vector2i(x, y));
      }
    }
  }

  @Override
  protected void init() {
    super.init();

    this.fairy = new ReputationButton(this.leftPos + 118, this.topPos + 20, GroveType.FAIRY);
    this.pastoral = new ReputationButton(this.leftPos + 48, this.topPos + 81, GroveType.PASTORAL);
    this.cultivation = new ReputationButton(this.leftPos + 48, this.topPos + 171, GroveType.CULTIVATION);
    this.twilight = new ReputationButton(this.leftPos + /*206*/ 188, this.topPos + 81, GroveType.TWILIGHT);
    this.fungal = new ReputationButton(this.leftPos + /*191*/ 188, this.topPos + 171, GroveType.FUNGAL);
    this.elemental = new ReputationButton(this.leftPos + 118, this.topPos + 208, GroveType.ELEMENTAL);

    for (Vector2i star : stars) {
      this.addRenderableOnly(new StarRenderer(this.leftPos + star.x, this.topPos + star.y));
    }

    this.clouds.setPosition(this.leftPos, this.topPos);
    this.addRenderableOnly(this.clouds);
    this.addRenderableOnly(this::drawTree);
    this.addRenderableWidget(this.fungal);
    this.addRenderableWidget(this.fairy);
    this.addRenderableWidget(this.pastoral);
    this.addRenderableWidget(this.cultivation);
    this.addRenderableWidget(this.twilight);
    this.addRenderableWidget(this.elemental);

    updateButtons();
  }

  protected void updateButtons() {
    ReputationStorage rep = getStorage();

    this.fairy.setProgress(rep.getProgress(ModGroves.FAIRY));
    this.elemental.setProgress(rep.getProgress(ModGroves.ELEMENTAL));
    this.twilight.setProgress(rep.getProgress(ModGroves.TWILIGHT));
    this.pastoral.setProgress(rep.getProgress(ModGroves.PASTORAL));
    this.cultivation.setProgress(rep.getProgress(ModGroves.CULTIVATION));
    this.fungal.setProgress(rep.getProgress(ModGroves.FUNGAL));
  }

  private ReputationStorage getStorage() {
    return this.minecraft.player.getData(ModAttachments.REPUTATION_STORAGE.get());
  }

  public static void open() {
    RootsClientHooks.stopUsingItem(new ReputationScreen());
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/reputation_background.png");
  private static final ResourceLocation foreground = RootsAPI.rl("textures/gui/reputation_foreground.png");

  @Override
  public ResourceLocation getBackground() {
    return background;
  }

  protected void drawTree(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    graphics.blit(foreground, leftPos, topPos, 0, 0, getBackgroundWidth(), getBackgroundHeight(), getBackgroundWidth(), getBackgroundHeight());
  }

  @Override
  public int getBackgroundWidth() {
    return 256;
  }

  @Override
  public int getBackgroundHeight() {
    return 256;
  }

  @Override
  public Map<ParticleRenderType, List<ScreenParticle>> getParticles() {
    return myParticles;
  }
}
