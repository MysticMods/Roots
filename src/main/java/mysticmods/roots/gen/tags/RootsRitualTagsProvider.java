package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.init.ModRituals;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class RootsRitualTagsProvider extends IntrinsicHolderTagsProvider<Ritual> {


  public RootsRitualTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.RITUALS, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Rituals.NYI)
        .add(ModRituals.GERMINATION.get(), ModRituals.SPREADING_FOREST.get());
    this.tag(RootsTags.Rituals.WIP)
        .add(ModRituals.BLOOMING.get(), ModRituals.FIRE_STORM.get(), ModRituals.FROST_LANDS.get(), ModRituals.GATHERING.get(), ModRituals.HEAVY_STORMS.get(), ModRituals.OVERGROWTH.get(), ModRituals.PROTECTION.get(), ModRituals.PURITY.get(), ModRituals.WILDROOT_GROWTH.get(), ModRituals.WINDWALL.get())
        .add(ModRituals.HEALING_AURA.get(), ModRituals.SUMMON_CREATURES.get(), ModRituals.WARDING.get());
    this.tag(RootsTags.Rituals.FUNGAL).add(ModRituals.PURITY.get());
    this.tag(RootsTags.Rituals.ELEMENTAL)
        .add(ModRituals.FIRE_STORM.get(), ModRituals.FROST_LANDS.get(), ModRituals.HEAVY_STORMS.get(), ModRituals.WINDWALL.get());
    this.tag(RootsTags.Rituals.SPROUTING)
        .add(ModRituals.GERMINATION.get(), ModRituals.SPREADING_FOREST.get(), ModRituals.WILDROOT_GROWTH.get());
    this.tag(RootsTags.Rituals.PRIMAL).add(ModRituals.OVERGROWTH.get(), ModRituals.GROVE_SUPPLICATION.get());
    this.tag(RootsTags.Rituals.TWILIGHT).add(ModRituals.HEALING_AURA.get());
    this.tag(RootsTags.Rituals.WILD)
        .add(ModRituals.ANIMAL_HARVEST.get(), ModRituals.SUMMON_CREATURES.get(), ModRituals.GATHERING.get(), ModRituals.AUGMENTATION.get());
    this.tag(RootsTags.Rituals.HOLLOW);
    this.tag(RootsTags.Rituals.FAIRY)
        .add(ModRituals.BLOOMING.get(), ModRituals.PROTECTION.get(), ModRituals.WARDING.get());
    this.tag(RootsTags.Rituals.SUMMON_CREATURES).add(ModRituals.SUMMON_CREATURES.get());
  }

  @Override
  public String getName() {
    return "Roots Ritual Tags";
  }
}
