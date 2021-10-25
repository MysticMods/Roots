package epicsquid.roots.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.SpectatorGui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFakeSpectator extends SpectatorGui {
  private static GuiFakeSpectator instance = null;
  private static SpectatorGui actualInstance = null;

  private static void init() {
    if (instance == null) {
      instance = new GuiFakeSpectator(Minecraft.getMinecraft());
    }
    if (actualInstance == null) {
      actualInstance = Minecraft.getMinecraft().ingameGUI.getSpectatorGui();
    }
  }

  public static void setFake() {
    init();
    Minecraft.getMinecraft().ingameGUI.spectatorGui = instance;
  }

  public static void setReal() {
    init();
    Minecraft.getMinecraft().ingameGUI.spectatorGui = actualInstance;
  }

  private GuiFakeSpectator(Minecraft mcIn) {
    super(mcIn);
  }

  @Override
  public void onHotbarSelected(int p_175260_1_) {
  }

  @Override
  public void renderTooltip(ScaledResolution res, float partialTicks) {
  }

  @Override
  protected void renderPage(ScaledResolution res, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails spectator) {
  }

  @Override
  public void renderSelectedItem(ScaledResolution p_175263_1_) {
  }

  @Override
  public void onSpectatorMenuClosed(SpectatorMenu menu) {
    super.onSpectatorMenuClosed(menu);
  }

  @Override
  public boolean isMenuActive() {
    return false;
  }

  @Override
  public void onMouseScroll(int p_175259_1_) {
  }

  @Override
  public void onMiddleClick() {
  }
}
