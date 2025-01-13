package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsBlockEntityTagsProvider extends IntrinsicHolderTagsProvider<BlockEntityType<?>> {
  public RootsBlockEntityTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, Registries.BLOCK_ENTITY_TYPE, completableFuture, (BlockEntityType<?> arg2) -> arg2.builtInRegistryHolder().key(), RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
  }

  @Override
  public String getName() {
    return "Roots Block Entity Type Tags";
  }
}