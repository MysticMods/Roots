package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public class RootsSpellTagsProvider extends IntrinsicHolderTagsProvider<Spell> {


  public RootsSpellTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.SPELLS, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Spells.NYI)
        .add(ModSpells.ACID_CLOUD.get(), ModSpells.AQUA_BUBBLE.get(), ModSpells.AUGMENT.get(), ModSpells.LIGHT_DRIFTER.get(), ModSpells.MAGNETISM.get(), ModSpells.DANDELION_WINDS.get(), ModSpells.DESATURATE.get(), ModSpells.DISARM.get(), ModSpells.EXTENSION.get(), ModSpells.NONDETECTION.get(), ModSpells.GEAS.get(), ModSpells.CONTROL_UNDEAD.get(), ModSpells.RAMPANT_GROWTH.get(), ModSpells.HARVEST.get(), ModSpells.LIFE_DRAIN.get(), ModSpells.RADIANCE.get(), ModSpells.ROSE_THORNS.get(), ModSpells.SANCTUARY.get(), ModSpells.SHATTER.get(), ModSpells.JAUNT.get(), ModSpells.STORM_CLOUD.get(), ModSpells.TIME_STOP.get(), ModSpells.WILDFIRE.get());
    this.tag(RootsTags.Spells.PRIMAL_OPPOSED);
    this.tag(RootsTags.Spells.PRIMAL_ALIGNED);
    this.tag(RootsTags.Spells.FAIRY_OPPOSED);
    this.tag(RootsTags.Spells.FAIRY_ALIGNED);
    this.tag(RootsTags.Spells.TWILIGHT_OPPOSED);
    this.tag(RootsTags.Spells.TWILIGHT_ALIGNED);
    this.tag(RootsTags.Spells.FUNGAL_OPPOSED);
    this.tag(RootsTags.Spells.FUNGAL_ALIGNED);
    this.tag(RootsTags.Spells.SPROUT_OPPOSED);
    this.tag(RootsTags.Spells.SPROUT_ALIGNED);
    this.tag(RootsTags.Spells.ELEMENTAL_OPPOSED);
    this.tag(RootsTags.Spells.ELEMENTAL_ALIGNED);
    this.tag(RootsTags.Spells.WILD_OPPOSED);
    this.tag(RootsTags.Spells.WILD_ALIGNED);

    this.tag(RootsTags.Spells.ADJUSTABLE_SPELL).add(ModSpells.SHATTER.get());
  }

  @Override
  public String getName() {
    return "Roots Spell Tags";
  }
}
