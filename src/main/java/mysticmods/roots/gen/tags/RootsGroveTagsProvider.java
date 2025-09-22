package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModGroves;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class RootsGroveTagsProvider extends IntrinsicHolderTagsProvider<Grove> {


  public RootsGroveTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.GROVES, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(RootsTags.Groves.PRIMAL).add(ModGroves.PRIMAL.get());
    tag(RootsTags.Groves.FAIRY).add(ModGroves.FAIRY.get());
    tag(RootsTags.Groves.ELEMENTAL).add(ModGroves.ELEMENTAL.get());
    tag(RootsTags.Groves.WILD).add(ModGroves.WILD.get());
    tag(RootsTags.Groves.TWILIGHT).add(ModGroves.TWILIGHT.get());
    tag(RootsTags.Groves.SPROUTING).add(ModGroves.SPROUTING.get());
    tag(RootsTags.Groves.FUNGAL).add(ModGroves.FUNGAL.get());
    //noinspection unchecked
    tag(RootsTags.Groves.ANY).addTags(RootsTags.Groves.FAIRY, RootsTags.Groves.ELEMENTAL, RootsTags.Groves.WILD, RootsTags.Groves.TWILIGHT, RootsTags.Groves.SPROUTING, RootsTags.Groves.FUNGAL);
  }

  @Override
  public String getName() {
    return "Roots Grove Tags";
  }
}
