package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.client.gui.buttons.ReputationButton;
import mysticmods.roots.client.gui.renderable.CloudRenderer;
import mysticmods.roots.client.gui.renderable.StarRenderer;
import mysticmods.roots.client.particle.screen.ScreenParticleSupplier;
import mysticmods.roots.client.particle.screen.base.ScreenParticle;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModGroves;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReputationScreen extends RootsScreen implements ScreenParticleSupplier {
  private ReputationButton unity;
  private ReputationButton elemental;
  private ReputationButton cultivation;
  private ReputationButton animate;
  private ReputationButton wild;
  private ReputationButton fungal;
  private ReputationButton twilight;

  private final StarRenderer stars;
  private final CloudRenderer clouds;

  private final Map<ParticleRenderType, List<ScreenParticle>> myParticles = new HashMap<>();

  protected ReputationScreen() {
    super(Component.translatable("roots.gui.reputation"));
    this.clouds = new CloudRenderer(getBackgroundWidth(), getBackgroundHeight(), true);
    this.stars = new StarRenderer();
  }

  @Override
  protected void init() {
    super.init();

    this.unity = new ReputationButton(ModGroves.UNITY, this.leftPos + 119, this.topPos + 4);
    this.animate = new ReputationButton(ModGroves.ANIMATE, this.leftPos + 26, this.topPos + 50);
    this.cultivation = new ReputationButton(ModGroves.CULTIVATION, this.leftPos + 42, this.topPos + 171);
    this.twilight = new ReputationButton(ModGroves.TWILIGHT, this.leftPos + 206, this.topPos + 50);
    this.fungal = new ReputationButton(ModGroves.FUNGAL, this.leftPos + 191, this.topPos + 171);
    this.elemental = new ReputationButton(ModGroves.ELEMENTAL, this.leftPos + 119, this.topPos + 220);
    this.wild = new ReputationButton(ModGroves.WILD, this.leftPos + 119, this.topPos + 112);

    this.stars.setPosition(this.leftPos, this.topPos);
    this.addRenderableOnly(this.stars);
    this.clouds.setPosition(this.leftPos, this.topPos);
    this.addRenderableOnly(this.clouds);
    this.addRenderableOnly(this::drawTreeAndBorder);
    this.addRenderableWidget(this.fungal);
    this.addRenderableWidget(this.unity);
    this.addRenderableWidget(this.animate);
    this.addRenderableWidget(this.cultivation);
    this.addRenderableWidget(this.twilight);
    this.addRenderableWidget(this.elemental);
    this.addRenderableWidget(this.wild);

    //this.addRenderableOnly(this::drawLines);

    updateButtons();
  }

  protected void drawLines(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    graphics.hLine(leftPos - 120, leftPos + 256 + 120, topPos + 81, -1);
    graphics.hLine(leftPos - 120, leftPos + 256 + 120, topPos + 171, -1);
    graphics.hLine(leftPos - 120, leftPos + 256 + 120, topPos + 128, -1);
/*
    graphics.hLine(leftPos + 120, leftPos + 256, 160, -1);
    graphics.hLine(leftPos + 100, leftPos + 120, 140, -1);
    graphics.hLine(leftPos + 80, leftPos + 100, 100, -1);
    graphics.hLine(leftPos + 40, leftPos + 80, 50, -1);
    graphics.vLine(leftPos + 40, topPos, topPos + 60, -1);
    graphics.vLine(leftPos + 80, topPos + 55, topPos + 110, -1);
    graphics.vLine(leftPos + 100, topPos + 100, topPos + 160, -1);
    graphics.vLine(leftPos + 120, topPos + 140, topPos + 180, -1);*/


  }

  protected void updateButtons() {
    ReputationStorage rep = getStorage();

    for (Renderable renderable : this.renderables) {
      if (renderable instanceof ReputationButton repButton) {
        repButton.updateProgress(rep);
      }
    }
  }

  private ReputationStorage getStorage() {
    return this.minecraft.player.getData(ModAttachments.REPUTATION_STORAGE.get());
  }

  public static void open() {
    RootsClientHooks.stopUsingItem(new ReputationScreen());
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/reputation_background.png");
  private static final ResourceLocation foreground = RootsAPI.rl("textures/gui/reputation_foreground.png");
  private static final ResourceLocation border = RootsAPI.rl("textures/gui/reputation_border.png");

  @Override
  public ResourceLocation getBackground() {
    return background;
  }

  protected void drawTreeAndBorder(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    graphics.blit(foreground, leftPos, topPos, 0, 0, getBackgroundWidth(), getBackgroundHeight(), getBackgroundWidth(), getBackgroundHeight());
    graphics.blit(border, leftPos - 16, topPos - 18, 0, 0, 289, 290, 289, 290);
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
