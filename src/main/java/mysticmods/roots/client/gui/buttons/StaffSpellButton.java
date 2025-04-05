package mysticmods.roots.client.gui.buttons;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class StaffSpellButton extends TypedButton<ISpellInstance, StaffScreen> {

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/staff_spell_slot.png");
  private static final ResourceLocation highlight = RootsAPI.rl("textures/gui/staff_spell_slot_highlight.png");
  public StaffSpellButton(StaffScreen parentScreen, @NotNull Supplier<ISpellInstance> spellGetter, int id, int pX, int pY) {
    super(parentScreen, spellGetter, id, pX, pY, 16, 16, parentScreen::buttonClicked);
  }

  public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    int x1 = getX() - 2;
    int y1 = getY() - 2;
    graphics.blit(background, x1, y1, 0, 0, 20, 20, 20, 20);
    super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
    if (parentScreen.isSelected(this) && visible) {
      int x = getX() - 1;
      int y = getY() - 1;
      graphics.blit(highlight, x, y, 0, 0, 18, 18, 18, 18);
    }
  }
}
