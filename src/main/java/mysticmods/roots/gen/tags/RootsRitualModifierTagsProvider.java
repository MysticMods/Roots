package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.modifier.RitualModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class RootsRitualModifierTagsProvider extends IntrinsicHolderTagsProvider<RitualModifier> {

  public RootsRitualModifierTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.RITUAL_MODIFIERS, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.RitualModifiers.NYI);
    this.tag(RootsTags.RitualModifiers.RESTRICTED);
  }

  @Override
  public String getName() {
    return "Roots Ritual Modifier Tags";
  }
}
