package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.client.gui.buttons.ReputationButton;
import mysticmods.roots.client.particle.screen.ScreenParticleEngine;
import mysticmods.roots.client.particle.screen.ScreenParticleSupplier;
import mysticmods.roots.client.particle.screen.base.ScreenParticle;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModGroves;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

  private final Map<ParticleRenderType, List<ScreenParticle>> myParticles = new HashMap<>();

  protected ReputationScreen() {
    super(Component.translatable("roots.gui.reputation"));
  }

  @Override
  protected void init() {
    super.init();

    this.fairy = new ReputationButton(this.leftPos + 120, this.topPos + 20, GroveType.FAIRY);
    this.pastoral = new ReputationButton(this.leftPos + 26, this.topPos + 81, GroveType.PASTORAL);
    this.cultivation = new ReputationButton(this.leftPos + 42, this.topPos + 171, GroveType.CULTIVATION);
    this.twilight = new ReputationButton(this.leftPos + 206, this.topPos + 81, GroveType.TWILIGHT);
    this.fungal = new ReputationButton(this.leftPos + 191, this.topPos + 171, GroveType.FUNGAL);
    this.elemental = new ReputationButton(this.leftPos + 117, this.topPos + 208, GroveType.ELEMENTAL);

    this.addRenderableWidget(this.fungal);
    this.addRenderableWidget(this.fairy);
    this.addRenderableWidget(this.pastoral);
    this.addRenderableWidget(this.cultivation);
    this.addRenderableWidget(this.twilight);
    this.addRenderableWidget(this.elemental);

    updateButtons();

    this.addRankParticle(this.fungal);
    this.addRankParticle(this.fairy);
    this.addRankParticle(this.pastoral);
    this.addRankParticle(this.cultivation);
    this.addRankParticle(this.twilight);
    this.addRankParticle(this.elemental);
  }

  private void addRankParticle (ReputationButton button) {
    int rank = button.getRank();

    if (rank == 0) {
      return;
    }

    this.addContainerParticle(RootsParticleOptions.builder(ModParticles.RANK).build(), button.getX(), button.getY(), button.getX(), button.getY());
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
