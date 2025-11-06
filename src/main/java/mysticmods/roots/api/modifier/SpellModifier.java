package mysticmods.roots.api.modifier;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

public class SpellModifier extends Modifier<Spell, SpellModifier> {
  public SpellModifier(ResourceKey<Grove> grove, @NotNull ResourceKey<mysticmods.roots.api.modifier.SpellModifier> parent, ResourceKey<Spell> applicable) {
    super(grove, parent, applicable);
  }

  public SpellModifier(ResourceKey<Grove> grove, ResourceKey<Spell> applicable) {
    super(grove, applicable);
  }

  @Override
  public Holder<mysticmods.roots.api.modifier.SpellModifier> builtInRegistryHolder() {
    return RootsRegistries.SPELL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "spell_modifier";
  }

  @Override
  public void init(Holder<SpellModifier> holder) {

  }
}
