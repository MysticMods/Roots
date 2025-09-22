package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModHerbs;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class RootsHerbTagsProvider extends IntrinsicHolderTagsProvider<Herb> {
  public RootsHerbTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.HERBS, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Herbs.FIRE).add(ModHerbs.INFERNO_BULB.get());
    this.tag(RootsTags.Herbs.AIR).add(ModHerbs.CLOUD_BERRY.get());
    this.tag(RootsTags.Herbs.WATER).add(ModHerbs.DEWGONIA.get());
    this.tag(RootsTags.Herbs.EARTH).add(ModHerbs.STALICRIPE.get());
    this.tag(RootsTags.Herbs.ELEMENTAL)
        .addTags(RootsTags.Herbs.AIR, RootsTags.Herbs.EARTH, RootsTags.Herbs.FIRE, RootsTags.Herbs.WATER);
    this.tag(RootsTags.Herbs.PRIMAL).add(ModHerbs.GROVE_MOSS.get(), ModHerbs.WILDROOT.get());
    this.tag(RootsTags.Herbs.FAIRY).add(ModHerbs.PERESKIA.get());
    this.tag(RootsTags.Herbs.FUNGAL).add(ModHerbs.BAFFLECAP.get());
    this.tag(RootsTags.Herbs.SPROUTING).add(ModHerbs.WILDEWHEET.get());
    this.tag(RootsTags.Herbs.TWILIGHT).add(ModHerbs.MOONGLOW.get());
    this.tag(RootsTags.Herbs.WILD).add(ModHerbs.SPIRITLEAF.get());
  }

  @Override
  public String getName() {
    return "Roots Herb Tags";
  }
}
