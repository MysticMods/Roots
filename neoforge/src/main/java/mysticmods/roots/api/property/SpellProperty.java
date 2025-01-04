package mysticmods.roots.api.property;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SpellProperty<V> extends Property<V> implements IDescribedRegistryEntry {
  private String descriptionId;
  protected Supplier<Spell> spell;

  private final Holder.Reference<SpellProperty<?>> builtInRegistryHolder = RootsRegistries.SPELL_PROPERTIES.createIntrusiveHolder(this);

  public SpellProperty(Supplier<Spell> spell, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.spell = Suppliers.memoize(spell::get);
  }

  public Spell getSpell() {
    return spell.get();
  }

  public Holder.Reference<SpellProperty<?>> builtInRegistryHolder() {
    return builtInRegistryHolder;
  }

  @Override
  public ResourceLocation getKey() {
    return builtInRegistryHolder().getKey().location();
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell_property", getKey());
    }

    return this.descriptionId;
  }
}
