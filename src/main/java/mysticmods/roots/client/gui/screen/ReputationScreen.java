package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.client.gui.buttons.ReputationButton;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModGroves;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ReputationScreen extends RootsScreen {
  private ReputationButton fairy;
  private ReputationButton elemental;
  private ReputationButton cultivation;
  private ReputationButton pastoral;
  private ReputationButton wild;
  private ReputationButton fungal;
  private ReputationButton twilight;

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
}
