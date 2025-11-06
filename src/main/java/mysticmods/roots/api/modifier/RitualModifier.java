package mysticmods.roots.api.modifier;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

public class RitualModifier extends Modifier<Ritual, RitualModifier> {
  public RitualModifier(ResourceKey<Grove> grove, @NotNull ResourceKey<RitualModifier> parent, ResourceKey<Ritual> applicable) {
    super(grove, parent, applicable);
  }

  public RitualModifier(ResourceKey<Grove> grove, ResourceKey<Ritual> applicable) {
    super(grove, applicable);
  }

  @Override
  public Holder<RitualModifier> builtInRegistryHolder() {
    return RootsRegistries.RITUAL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "ritual_modifier";
  }
}
