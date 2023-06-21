package mysticmods.roots.gen;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;

public class RootsDataProviderTypes {
  //public static final ProviderType<RegistrateTagsProvider<Ritual>> RITUAL_TAGS = ProviderType.register("tags/rituals", type -> (p, e) -> new RegistrateTagsProvider<Ritual>(p, type, "rituals", e.getGenerator(), Registries.RITUAL_REGISTRY.get(), e.getExistingFileHelper()));
}
