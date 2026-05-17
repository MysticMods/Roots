package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModModifiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class RootsSpellModifierTagsProvider extends IntrinsicHolderTagsProvider<SpellModifier> {

  public RootsSpellModifierTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.SPELL_MODIFIERS, provider, p_256665_ -> p_256665_.builtInRegistryHolder()
        .getKey(), RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.SpellModifiers.NYI);
    this.tag(RootsTags.SpellModifiers.REQUIRES_UNLOCK);
    this.tag(RootsTags.SpellModifiers.RESTRICTED);

    this.tag(RootsTags.SpellModifiers.SKY_SOARER_AMPLIFIER_INCREASES).add(
        ModModifiers.SKY_SOARER_AMPLIFIED_1.value(), ModModifiers.SKY_SOARER_AMPLIFIED_2.value()
    );
    this.tag(RootsTags.SpellModifiers.SKY_SOARER_DURATION_INCREASES).add(
        ModModifiers.SKY_SOARER_SPEEDY_1.value(), ModModifiers.SKY_SOARER_SPEEDY_2.value()
    );
  }

  @Override
  public String getName() {
    return "Roots Spell Modifier Tags";
  }
}
