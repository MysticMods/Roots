package mysticmods.roots.client.gui.buttons;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.StaffScreen;

public class LibrarySpellButton extends TypedButton<Spell, SpellSupplier<Spell>, StaffScreen> {
  public LibrarySpellButton(StaffScreen parentScreen, SpellSupplier<Spell> spellSupplier, int pX, int pY) {
    super(parentScreen, spellSupplier, pX, pY, 16, 16, spellSupplier.get().getName(), parentScreen::onLibrarySpellClick);
  }

  public Spell getSpell() {
    return spellSupplier.get();
  }
}
