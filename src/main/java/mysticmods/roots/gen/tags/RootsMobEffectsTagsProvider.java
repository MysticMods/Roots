package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class RootsMobEffectsTagsProvider extends TagsProvider<MobEffect> {
  public RootsMobEffectsTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, Registries.MOB_EFFECT, lookupProvider, RootsAPI.MODID, existingFileHelper);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(RootsTags.MobEffects.SUPPRESS_PARTICLES);
    tag(RootsTags.MobEffects.PURITY_FORCE_EXCLUDE);
    tag(RootsTags.MobEffects.PURITY_FORCE_INCLUDE);
    tag(RootsTags.MobEffects.GEAS).add(ModEffects.GEAS.getKey());
    tag(RootsTags.MobEffects.INSTANT_CANCEL_EFFECT).add(ModEffects.SKY_SOARER.getKey());
    tag(RootsTags.MobEffects.DELAYED_CANCEL_EFFECT).add(ModEffects.LIGHT_DRIFTER.getKey());
    tag(RootsTags.MobEffects.CANCELLABLE_EFFECTS).addTags(RootsTags.MobEffects.INSTANT_CANCEL_EFFECT, RootsTags.MobEffects.DELAYED_CANCEL_EFFECT);
  }
}
