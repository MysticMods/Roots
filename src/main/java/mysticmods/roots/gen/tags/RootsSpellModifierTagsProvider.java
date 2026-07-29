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
    this.tag(RootsTags.SpellModifiers.SHATTER_FORTUNE).add(
        ModModifiers.SHATTER_FORTUNE_I.value(), ModModifiers.SHATTER_FORTUNE_II.value(), ModModifiers.SHATTER_FORTUNE_III.value()
    );
    this.tag(RootsTags.SpellModifiers.SMELTS).add(ModModifiers.SHATTER_SMELTING.value());
    this.tag(RootsTags.SpellModifiers.INCREASES_FORTUNE).addTag(RootsTags.SpellModifiers.SHATTER_FORTUNE);
    this.tag(RootsTags.SpellModifiers.INCREASES_LOOTING);
    this.tag(RootsTags.SpellModifiers.SILK_TOUCH).add(ModModifiers.SHATTER_SILK_TOUCH.value());
    this.tag(RootsTags.SpellModifiers.SHEARING).add(ModModifiers.SHATTER_SILK_TOUCH.value());
    this.tag(RootsTags.SpellModifiers.MAGNETISM).add(ModModifiers.SHATTER_MAGNETISM.value());
    this.tag(RootsTags.SpellModifiers.DANDELION_WINDS_INCREASES_DURATION).add(ModModifiers.DANDELION_WINDS_DURATION_1.value(), ModModifiers.DANDELION_WINDS_DURATION_2.value(), ModModifiers.DANDELION_WINDS_DURATION_3.value(), ModModifiers.DANDELION_WINDS_DURATION_4.value(), ModModifiers.DANDELION_WINDS_DURATION_5.value());
    this.tag(RootsTags.SpellModifiers.DANDELION_WINDS_INCREASES_CHANCE).add(ModModifiers.DANDELION_WINDS_CHANCE_1.value(), ModModifiers.DANDELION_WINDS_CHANCE_2.value(), ModModifiers.DANDELION_WINDS_CHANCE_3.value(), ModModifiers.DANDELION_WINDS_CHANCE_4.value());
    this.tag(RootsTags.SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_DECREASE).add(ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_1.value(), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_2.value(), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_3.value(), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_4.value(), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_5.value());
    this.tag(RootsTags.SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_DECREASE).add(ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_1.value(), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_2.value(), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_3.value(), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_4.value(), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_5.value());
  }

  @Override
  public String getName() {
    return "Roots Spell Modifier Tags";
  }
}
