package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class RootsLevelConditionTypeTagsProvider extends IntrinsicHolderTagsProvider<ILevelConditionType<?>> {

  public RootsLevelConditionTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, RootsRegistries.Keys.LEVEL_CONDITIONS, provider, p_256665_ -> RootsRegistries.LEVEL_CONDITIONS.getResourceKey(p_256665_).orElseThrow(), RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.LevelConditionTypes.GROVE_STONE).add(ModConditions.GROVE_STONE_CONDITION_TYPE.get());
    this.tag(RootsTags.LevelConditionTypes.PILLAR).add(ModConditions.PILLAR_CONDITION_TYPE.get());
  }

  @Override
  public String getName() {
    return "Roots Level Condition Tags";
  }
}
