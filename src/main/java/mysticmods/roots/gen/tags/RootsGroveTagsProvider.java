package mysticmods.roots.gen.tags;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public class RootsGroveTagsProvider extends IntrinsicHolderTagsProvider<Grove> {


  public RootsGroveTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.GROVES, provider, p_256665_ -> p_256665_.builtInRegistryHolder().getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
/*      this.tag(RootsTags.Groves.PRIMAL_ALIGNED).add(ModGroves.FUNGAL.get(), ModGroves.SPROUT.get(), ModGroves.FAIRY.get(), ModGroves.WILD.get(), ModGroves.TWILIGHT.get(), ModGroves.ELEMENTAL.get());
      this.tag(RootsTags.Groves.PRIMAL_OPPOSED);
      this.tag(RootsTags.Groves.FAIRY_ALIGNED).add(ModGroves.SPROUT.get());
      this.tag(RootsTags.Groves.FAIRY_OPPOSED).add(ModGroves.TWILIGHT.get());
      this.tag(RootsTags.Groves.TWILIGHT_ALIGNED).add(ModGroves.FUNGAL.get());
      this.tag(RootsTags.Groves.TWILIGHT_OPPOSED).add(ModGroves.FAIRY.get());
      this.tag(RootsTags.Groves.FUNGAL_ALIGNED).add(ModGroves.TWILIGHT.get());
      this.tag(RootsTags.Groves.FUNGAL_OPPOSED);
      this.tag(RootsTags.Groves.SPROUT_ALIGNED).add(ModGroves.FAIRY.get(), ModGroves.WILD.get());
      this.tag(RootsTags.Groves.SPROUT_OPPOSED);
      this.tag(RootsTags.Groves.ELEMENTAL_ALIGNED);
      this.tag(RootsTags.Groves.ELEMENTAL_OPPOSED);
      this.tag(RootsTags.Groves.WILD_ALIGNED).add(ModGroves.SPROUT.get());
      this.tag(RootsTags.Groves.WILD_OPPOSED);*/
  }

  @Override
  public String getName() {
    return "Roots Grove Tags";
  }
}
