package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.ReputationRanks;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModGroves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ReputationScreen extends RootsScreen {
  protected ReputationScreen() {
    super(Component.translatable("roots.gui.reputation"));
  }

  @Override
  protected void init() {
    super.init();
  }

  private ReputationStorage getStorage() {
    return this.minecraft.player.getData(ModAttachments.REPUTATION_STORAGE.get());
  }

  public static void open() {
    ReputationScreen newScreen = new ReputationScreen();
    Minecraft.getInstance().setScreen(newScreen);
  }

  @Override
  public void drawForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    super.drawForeground(graphics, mouseX, mouseY, partialTicks);
  }

  @Override
  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int uvW, int uvH, int maxW, int maxH) {
  }

  private static final ResourceLocation ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace(
      "container/enchanting_table/enchantment_slot_highlighted"
  );
  private static final ResourceLocation ENCHANTMENT_SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/enchanting_table/enchantment_slot");

  @Override
  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
  }

  @Override
  public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    ReputationStorage rep = getStorage();

    ReputationRanks.Progress fairy = rep.getProgress(ModGroves.FAIRY.get());
    ReputationRanks.Progress wild = rep.getProgress(ModGroves.WILD.get());
    ReputationRanks.Progress sprouting = rep.getProgress(ModGroves.SPROUTING.get());
    ReputationRanks.Progress elemental = rep.getProgress(ModGroves.ELEMENTAL.get());
    ReputationRanks.Progress twilight = rep.getProgress(ModGroves.TWILIGHT.get());
    ReputationRanks.Progress fungal = rep.getProgress(ModGroves.FUNGAL.get());


    int i = (this.width - 176) / 2;
    int j = (this.height - 142) / 2;
    graphics.blit(background, i, j, 0, 0, getBackgroundWidth(), getBackgroundHeight());

    ReputationRanks.Progress[] progresses = new ReputationRanks.Progress[]{fairy, twilight, fungal, elemental, wild, sprouting};
    Grove[] groves = new Grove[]{
        ModGroves.FAIRY.get(), ModGroves.TWILIGHT.get(), ModGroves.FUNGAL.get(), ModGroves.ELEMENTAL.get(), ModGroves.WILD.get(), ModGroves.SPROUTING.get()
    };

    for (int l = 0; l < 6; l++) {
      int i1 = i + 60;
      int j1 = i1 + 5;
      ReputationRanks.Progress prog = progresses[l];
      Grove grove = groves[l];
      String s;
      if (prog.nextRank() == 0) {
        s = prog.progress() + " " + prog.rank();
      } else {
        s = prog.progress() + "/" + prog.nextRank() + " " + prog.rank();
      }
      int i2 = 6839882;
      int j2 = mouseX - (i + 60);
      int k2 = mouseY - (j + 14 + 19 * l);
      RenderSystem.enableBlend();
      if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19) {
        graphics.blitSprite(ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE, i1, j + 14 + 19 * l, 108, 19);
        i2 = 16777088;
      } else {
        graphics.blitSprite(ENCHANTMENT_SLOT_SPRITE, i1, j + 14 + 19 * l, 108, 19);
      }

      RenderSystem.disableBlend();
      graphics.drawString(this.font, grove.getName(), j1, j + 16 + 19 * l, i2);
      i2 = 8453920;

      graphics.drawString(this.font, s, j1 + 86 + 15 - this.font.width(s), j + 16 + 19 * l + 7, i2);
    }
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/reputation.png");

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
