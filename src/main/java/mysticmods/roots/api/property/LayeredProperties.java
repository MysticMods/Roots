package mysticmods.roots.api.property;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.modifier.ModifierSet;

public interface LayeredProperties<PROPERTY extends LayeredProperty<BASE>, BASE, INSTANCE, MODIFIER extends Modifier<BASE, MODIFIER>, MODIFIERSET extends ModifierSet<BASE, MODIFIER, MODIFIERSET>> {
  PROPERTY get(INSTANCE instance);

  PROPERTY get(MODIFIERSET modifiers);
}
