package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsStructureTagsProvider extends StructureTagsProvider {
  public RootsStructureTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, completableFuture, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
  }

  @Override
  public String getName() {
    return "Roots Structure Tags";
  }
}