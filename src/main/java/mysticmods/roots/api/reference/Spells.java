package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Spells {
  ResourceKey<Spell> GROWTH_INFUSION = spell("growth_infusion");

  static ResourceKey<Spell> spell(String name) {
    return ResourceKey.create(RootsAPI.SPELL_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
  }
}
