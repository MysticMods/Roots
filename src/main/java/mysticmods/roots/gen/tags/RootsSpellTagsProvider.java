package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class RootsSpellTagsProvider extends IntrinsicHolderTagsProvider<Spell> {

  public RootsSpellTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.SPELLS, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Spells.WIP)
        .add(ModSpells.ACID_CLOUD.get(), ModSpells.AQUA_BUBBLE.get(), ModSpells.MAGNETISM.get(), ModSpells.DANDELION_WINDS.get(), ModSpells.DESATURATE.get(), ModSpells.DISARM.get(), ModSpells.EXTENSION.get(), ModSpells.NONDETECTION.get(), ModSpells.GEAS.get(), ModSpells.RAMPANT_GROWTH.get(), ModSpells.HARVEST.get(), ModSpells.LIFE_DRAIN.get(), ModSpells.ROSE_THORNS.get(), ModSpells.SANCTUARY.get(), ModSpells.SHATTER.get(), ModSpells.JAUNT.get(), ModSpells.TEMPORAL_MORASS.get(), ModSpells.WILDFIRE.get(), ModSpells.SATURATE.get(), ModSpells.LIGHT_DRIFTER.get());
    this.tag(RootsTags.Spells.NYI)
        .add(ModSpells.SUMMON_UNDEAD.get(), ModSpells.RADIANCE.get(), ModSpells.STORM_CLOUD.get(), ModSpells.DECAY.get());
    this.tag(RootsTags.Spells.ADJUSTABLE_SPELL)
        .add(ModSpells.SHATTER.get(), ModSpells.RAMPANT_GROWTH.get(), ModSpells.HARVEST.get());

    this.tag(RootsTags.Spells.FAIRY)
        .add(ModSpells.SYLVAN_LIGHT.get(), ModSpells.PETAL_SHELL.get(), ModSpells.ROSE_THORNS.get(), ModSpells.SANCTUARY.get());
    this.tag(RootsTags.Spells.FUNGAL)
        .add(ModSpells.ACID_CLOUD.get(), ModSpells.DISARM.get(), ModSpells.GEAS.get(), ModSpells.SUMMON_UNDEAD.get());
    this.tag(RootsTags.Spells.ELEMENTAL)
        .add(ModSpells.AQUA_BUBBLE.get(), ModSpells.DANDELION_WINDS.get(), ModSpells.RADIANCE.get(), ModSpells.SHATTER.get(), ModSpells.STORM_CLOUD.get(), ModSpells.SKY_SOARER.get(), ModSpells.WILDFIRE.get());
    this.tag(RootsTags.Spells.SPROUTING)
        .add(ModSpells.DESATURATE.get(), ModSpells.SATURATE.get(), ModSpells.GROWTH_INFUSION.get(), ModSpells.RAMPANT_GROWTH.get(), ModSpells.HARVEST.get());
    this.tag(RootsTags.Spells.PRIMAL).add(ModSpells.MAGNETISM.get(), ModSpells.EXTENSION.get());
    this.tag(RootsTags.Spells.TWILIGHT)
        .add(ModSpells.LIGHT_DRIFTER.get(), ModSpells.LIFE_DRAIN.get(), ModSpells.JAUNT.get(), ModSpells.TEMPORAL_MORASS.get());
    this.tag(RootsTags.Spells.WILD).add(ModSpells.NONDETECTION.get());
    this.tag(RootsTags.Spells.HOLLOW);

    this.tag(RootsTags.Spells.GEAS_ACTION).add(ModSpells.GEAS.get());

    this.tag(RootsTags.Spells.BLOCKS_OFF_HAND_EATING).add(ModSpells.RAMPANT_GROWTH.get(), ModSpells.GROWTH_INFUSION.get());
  }

  @Override
  public String getName() {
    return "Roots Spell Tags";
  }
}
