package mysticmods.roots.gen;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.init.ModRituals;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public class RootsRitualTagsProvider extends IntrinsicHolderTagsProvider<Ritual> {


  public RootsRitualTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.RITUALS, provider, p_256665_ -> p_256665_.builtInRegistryHolder().getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Rituals.NYI).add(ModRituals.BLOOMING.get(), ModRituals.FIRE_STORM.get(), ModRituals.FROST_LANDS.get(), ModRituals.GATHERING.get(), ModRituals.GERMINATION.get(), ModRituals.HEALING_AURA.get(), ModRituals.HEAVY_STORMS.get(), ModRituals.OVERGROWTH.get(), ModRituals.PROTECTION.get(), ModRituals.PURITY.get(), ModRituals.SPREADING_FOREST.get(), ModRituals.SUMMON_CREATURES.get(), ModRituals.TRANSMUTATION.get(), ModRituals.WARDING.get(), ModRituals.WILDROOT_GROWTH.get(), ModRituals.WINDWALL.get());
    this.tag(RootsTags.Rituals.PRIMAL_OPPOSED);
    this.tag(RootsTags.Rituals.PRIMAL_ALIGNED);
    this.tag(RootsTags.Rituals.FAIRY_OPPOSED);
    this.tag(RootsTags.Rituals.FAIRY_ALIGNED);
    this.tag(RootsTags.Rituals.TWILIGHT_OPPOSED);
    this.tag(RootsTags.Rituals.TWILIGHT_ALIGNED);
    this.tag(RootsTags.Rituals.FUNGAL_OPPOSED);
    this.tag(RootsTags.Rituals.FUNGAL_ALIGNED);
    this.tag(RootsTags.Rituals.SPROUT_OPPOSED);
    this.tag(RootsTags.Rituals.SPROUT_ALIGNED);
    this.tag(RootsTags.Rituals.ELEMENTAL_OPPOSED);
    this.tag(RootsTags.Rituals.ELEMENTAL_ALIGNED);
    this.tag(RootsTags.Rituals.WILD_OPPOSED);
    this.tag(RootsTags.Rituals.WILD_ALIGNED);
  }

  @Override
  public String getName() {
    return "Roots Ritual Tags";
  }
}
