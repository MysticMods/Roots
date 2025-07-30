package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsDamageTagsProvider extends DamageTypeTagsProvider {
  public RootsDamageTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(RootsTags.DamageTypes.IS_LAVA).add(DamageTypes.LAVA);
    tag(RootsTags.DamageTypes.PETAL_SHELL_IGNORES).add(DamageTypes.CACTUS, DamageTypes.CAMPFIRE, DamageTypes.HOT_FLOOR, DamageTypes.SWEET_BERRY_BUSH);

  }

  @Override
  public String getName() {
    return "Roots Damage Type Tags";
  }
}
