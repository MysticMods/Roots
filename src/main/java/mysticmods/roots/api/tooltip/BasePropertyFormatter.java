package mysticmods.roots.api.tooltip;

import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.IPropertyDataMapped;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BasePropertyFormatter<T, S extends BasePropertyFormatter<T, S>> {
  protected IPropertyDataMapped source;
  protected Supplier<Number> groupValue = null;

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

  public S modifier (SpellModifier modifier) {
    this.groupValue = modifier::getGroupValue;
    return self();
  }

  public S modifier (Holder<SpellModifier> modifier) {
    this.groupValue = () -> modifier.value().getGroupValue();
    return self();
  }

  public S modifier (ResourceKey<SpellModifier> modifierKey) {
    SpellModifier modifier = RootsRegistries.SPELL_MODIFIERS.get(modifierKey);
    if (modifier == null) {
      throw new NullPointerException("Missing modifier for: " + modifierKey);
    }
    return modifier(modifier);
  }
}
