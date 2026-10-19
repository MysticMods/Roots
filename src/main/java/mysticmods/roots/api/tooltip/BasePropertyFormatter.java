package mysticmods.roots.api.tooltip;

import mysticmods.roots.api.property.IPropertyDataMapped;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

public class BasePropertyFormatter<T, S extends BasePropertyFormatter<T, S>> {
  protected IPropertyDataMapped source;

  public S self () {
    //noinspection unchecked
    return (S) this;
  }

  public S spell(ResourceKey<Spell> spell) {
    this.source = RootsRegistries.SPELLS.get(spell);
    if (this.source == null) {
      throw new NullPointerException(spell + " could not be found!");
    }
    return self();
  }

  public S spell(Holder<Spell> spell) {
    this.source = spell.value();
    return self();
  }

  public S spell(Spell spell) {
    this.source = spell;
    return self();
  }

  public S ritual(ResourceKey<Ritual> ritual) {
    this.source = RootsRegistries.RITUALS.get(ritual);
    if (this.source == null) {
      throw new NullPointerException(ritual + " could not be found!");
    }
    return self();
  }

  public S ritual(Holder<Ritual> ritual) {
    this.source = ritual.value();
    return self();
  }

  public S ritual(Ritual ritual) {
    this.source = ritual;
    return self();
  }
}
