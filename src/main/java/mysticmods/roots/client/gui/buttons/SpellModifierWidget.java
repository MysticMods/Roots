package mysticmods.roots.client.gui.buttons;

import mysticmods.roots.api.client.ModifierTab;
import mysticmods.roots.api.client.ModifierWidget;
import mysticmods.roots.api.modifier.IModifierNode;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.spell.Spell;

// TODO: Make root modifier not clickable
public class SpellModifierWidget extends ModifierWidget<Spell, SpellModifier> {
  public SpellModifierWidget(ModifierTab<Spell, SpellModifier> tab, IModifierNode<Spell, SpellModifier> node) {
    super(tab, node);
  }

  @Override
  protected void onClick(double mouseX, double mouseY, double button) {

  }
}
