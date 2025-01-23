package mysticmods.roots.client.gui.buttons;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class LibrarySpellButton extends TypedButton<Spell, StaffScreen> {
  private final boolean transparent;

  public LibrarySpellButton(StaffScreen parentScreen, Supplier<Spell> spellSupplier, int id, int pX, int pY, boolean transparent) {
    super(parentScreen, spellSupplier, id, pX, pY, 16, 16, parentScreen::buttonClicked);
    this.transparent = transparent;
  }

  @Override
  public boolean isTransparent() {
    return this.transparent;
  }

  public Spell getSpell() {
    return spellSupplier.get();
  }

  private static final ResourceLocation highlight = RootsAPI.rl("textures/gui/library_spell_slot_highlight.png");

  @Override
  public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
    if (parentScreen.isSelected(this) && visible) {
      // TODO: is htis the correct use of pose?
      int x = getX() - 1;
      int y = getY() - 1;
      graphics.blit(highlight, x, y, 0, 0, 18, 18, 18, 18);
    }
  }
}
