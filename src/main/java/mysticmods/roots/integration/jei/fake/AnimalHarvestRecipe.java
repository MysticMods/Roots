package mysticmods.roots.integration.jei.fake;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.integration.jei.categories.AnimalHarvestCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.validation.DirectoryValidator;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record AnimalHarvestRecipe(EntityType<?> entity, List<LootTable> additionalLootTables) {
  public static List<AnimalHarvestRecipe> getRecipes () {

    ReloadableResourceManager manager = new ReloadableResourceManager(PackType.SERVER_DATA);
    List<PackResources> packs = new ArrayList<>();
    packs.add(ServerPacksSource.createVanillaPackSource());

    HolderLookup.Provider provider = Minecraft.getInstance().level.registryAccess();
    var lootTables = provider.lookupOrThrow(Registries.LOOT_TABLE);

    List<AnimalHarvestRecipe> recipes = new ArrayList<>();
    for (Holder<EntityType<?>> holder : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(RootsTags.Entities.ANIMAL_HARVEST)) {
      if (holder.is(RootsTags.Entities.ANIMAL_HARVEST_EXCLUDE)) {
        continue;
      }

      EntityType<?> entity = holder.value();

      List<LootTable> additionalLootTables = new ArrayList<>();

      var extra = holder.getData(DataMaps.ADDITIONAL_ANIMAL_HARVEST_LOOT_TABLES);
      if (extra != null) {
        for (ResourceKey<LootTable> additional : extra) {
          lootTables.get(additional).ifPresent(o -> additionalLootTables.add(o.value()));
        }
      }
      recipes.add(new AnimalHarvestRecipe(entity, additionalLootTables));
    }
    return recipes;
  }

  public static class DummyValidator extends DirectoryValidator {
    public DummyValidator() {
      super((o) -> false);
    }

    @Override
    public void validateSymlink(Path directory, List<ForbiddenSymlinkInfo> entries) throws IOException {
    }

    @Override
    public List<ForbiddenSymlinkInfo> validateSymlink(Path directory) throws IOException {
      return Collections.emptyList();
    }

    @Override
    public List<ForbiddenSymlinkInfo> validateDirectory(Path directory, boolean validateSymlinks) throws IOException {
      return Collections.emptyList();
    }

    @Override
    public void validateKnownDirectory(Path directory, List<ForbiddenSymlinkInfo> forbiddenSymlinkInfos) throws IOException {
    }
  }
}
