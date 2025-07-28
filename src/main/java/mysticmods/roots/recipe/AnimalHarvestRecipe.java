package mysticmods.roots.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.util.LootTableUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public record AnimalHarvestRecipe(EntityType<?> entity, List<ChanceOutput> loot) {
  public record Cache(List<AnimalHarvestRecipe> recipes) {
  }

  public static final MapCodec<AnimalHarvestRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(AnimalHarvestRecipe::entity),
      ChanceOutput.LIST_CODEC.fieldOf("loot").forGetter(AnimalHarvestRecipe::loot)
  ).apply(instance, AnimalHarvestRecipe::new));
  public static final Codec<AnimalHarvestRecipe> CODEC = MAP_CODEC.codec();
  public static final Codec<AnimalHarvestRecipe.Cache> CACHE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      AnimalHarvestRecipe.CODEC.listOf().fieldOf("recipes").forGetter(AnimalHarvestRecipe.Cache::recipes)
  ).apply(instance, AnimalHarvestRecipe.Cache::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, AnimalHarvestRecipe> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(Registries.ENTITY_TYPE), AnimalHarvestRecipe::entity, ChanceOutput.LIST_STREAM_CODEC, AnimalHarvestRecipe::loot, AnimalHarvestRecipe::new);
  public static final StreamCodec<RegistryFriendlyByteBuf, List<AnimalHarvestRecipe>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public static AnimalHarvestRecipe.Cache getServerRecipes(HolderGetter.Provider provider) {
    var lootTableLookup = provider.lookupOrThrow(Registries.LOOT_TABLE);

    List<AnimalHarvestRecipe> recipes = new ArrayList<>();
    for (Holder<EntityType<?>> holder : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(RootsTags.Entities.ANIMAL_HARVEST)) {
      if (holder.is(RootsTags.Entities.ANIMAL_HARVEST_EXCLUDE)) {
        continue;
      }

      EntityType<?> entity = holder.value();

      List<LootTable> lootTables = new ArrayList<>();

      var extra = holder.getData(DataMaps.ADDITIONAL_ANIMAL_HARVEST_LOOT_TABLES);
      if (extra != null) {
        for (ResourceKey<LootTable> additional : extra) {
          lootTableLookup.get(additional).ifPresent(o -> lootTables.add(o.value()));
        }
      }

      lootTableLookup.get(entity.getDefaultLootTable()).ifPresent(o -> lootTables.add(o.value()));

      List<ChanceOutput> outputs = new ArrayList<>();
      lootTables.forEach(o -> outputs.addAll(LootTableUtil.parseLootTable(o, provider)));
      if (!outputs.isEmpty()) {
        recipes.add(new AnimalHarvestRecipe(entity, outputs));
      }
    }
    return new Cache(recipes);
  }
}
