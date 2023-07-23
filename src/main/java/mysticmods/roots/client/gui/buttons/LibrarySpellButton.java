package mysticmods.roots.client.gui.buttons;

import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.StaffScreen;

public class LibrarySpellButton extends TypedButton<Spell, SpellSupplier<Spell>, StaffScreen> {
  private final boolean transparent;
  public LibrarySpellButton(StaffScreen parentScreen, SpellSupplier<Spell> spellSupplier, int id, int pX, int pY, boolean transparent) {
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
}
