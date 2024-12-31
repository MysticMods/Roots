package mysticmods.roots.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsStructureTagsProvider extends IntrinsicHolderTagsProvider<Structure> {
  public RootsStructureTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, Registries.STRUCTURE, completableFuture, (Structure arg2) -> null, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(RootsTags.Structure.STRUCTURE_BLACKLIST); //.add(BuiltinStructures.DESERT_PYRAMID);
    tag(RootsTags.Structure.STRUCTURE_WHITELIST); //.add(BuiltinStructures.DESERT_PYRAMID);
    tag(RootsTags.Structure.DECAY_STRUCTURES); //.add(BuiltinStructures.DESERT_PYRAMID);
    tag(RootsTags.Structure.REFRESH_STRUCTURES); //.add(BuiltinStructures.JUNGLE_TEMPLE);
  }

  @Override
  public String getName() {
    return "Roots Structure Tags";
  }
}