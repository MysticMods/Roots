package mysticmods.roots.client.gui.buttons;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class StaffSpellButton extends TypedButton<SpellInstance, SpellSupplier<SpellInstance>, StaffScreen> {

  public StaffSpellButton(StaffScreen parentScreen, SpellSupplier<SpellInstance> spellGetter, int id, int pX, int pY) {
    super(parentScreen, spellGetter, id, pX, pY, 16, 16, parentScreen::buttonClicked, DEFAULT_NARRATION);
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/staff_spell_slot.png");
  private static final ResourceLocation highlight = RootsAPI.rl("textures/gui/staff_spell_slot_highlight.png");

  public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    StaffScreen.drawFromTexture(graphics, background, getX() - 2, getY() - 2, 0, 0, 20, 20, 20, 20, graphics.pose());
    super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
    if (parentScreen.isSelected(this) && visible) {
      StaffScreen.drawFromTexture(graphics, highlight, getX() - 1, getY() - 1, 0, 0, 18, 18, 18, 18, graphics.pose());
    }
  }

  public SpellInstance getSpellInstance() {
    return spellSupplier.get();
  }
}
