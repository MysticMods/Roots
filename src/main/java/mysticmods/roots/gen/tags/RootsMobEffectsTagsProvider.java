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

public class RootsMobEffectsTagsProvider extends TagsProvider<MobEffect> {
  public RootsMobEffectsTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, Registries.MOB_EFFECT, lookupProvider, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(RootsTags.MobEffects.SUPPRESS_PARTICLES);
    tag(RootsTags.MobEffects.PURITY_FORCE_EXCLUDE);
    tag(RootsTags.MobEffects.PURITY_FORCE_INCLUDE);
    tag(RootsTags.MobEffects.GEAS).add(ModEffects.GEAS.getKey());
  }
}
