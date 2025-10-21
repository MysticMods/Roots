package mysticmods.roots.recipe.fake;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.SproutGift;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public record SproutGiftRecipe(EntityType<?> sprout, List<ChanceOutput> outputs) {
  public static List<SproutGiftRecipe> getRecipes() {
    Map<EntityType<?>, Set<Gift>> aggregate = new HashMap<>();
    for (Holder<Item> i : BuiltInRegistries.ITEM.getTag(RootsTags.Items.SPROUT_BREEDING_REWARDS).orElseThrow()) {
      List<SproutGift> itemValues = i.getData(DataMaps.SPROUT_BREEDING_ITEM_CHANCE);
      if (itemValues == null) {
        for (EntityType<?> type : getTypes(RootsTags.Entities.SPROUTS)) {
          Set<Gift> entries = aggregate.computeIfAbsent(type, (o) -> new HashSet<>());
          entries.add(new Gift(i.value(), ConfigManager.SPROUT_BREEDING_REWARDS_DEFAULT_CHANCE.getAsInt()));
        }
      } else {
        for (SproutGift gift : itemValues) {
          for (EntityType<?> type : getTypes(gift.sproutTag())) {
            Set<Gift> entries = aggregate.computeIfAbsent(type, (o) -> new HashSet<>());
            entries.add(new Gift(i.value(), gift.chance()));
          }
        }
      }
    }
    List<SproutGiftRecipe> recipes = new ArrayList<>();
    for (Map.Entry<EntityType<?>, Set<Gift>> i : aggregate.entrySet()) {
      EntityType<?> type = i.getKey();
      Set<Gift> values = i.getValue();
      List<ChanceOutput> outputs = new ArrayList<>();
      int total = values.stream().mapToInt(Gift::chance).sum();
      for (Gift gift : values) {
        float weight = ((float) gift.chance() / (float) total);
        outputs.add(new ChanceOutput(new ItemStack(gift.item()), weight));
      }
      outputs.sort(Comparator.comparingDouble(ChanceOutput::chance));
      recipes.add(new SproutGiftRecipe(type, outputs));
    }
    return recipes;
  }

  private static List<EntityType<?>> getTypes(TagKey<EntityType<?>> tag) {
    List<EntityType<?>> result = new ArrayList<>();
    for (Holder<EntityType<?>> i : BuiltInRegistries.ENTITY_TYPE.getTag(tag).orElseThrow()) {
      result.add(i.value());
    }
    return result;
  }

  private record Gift(Item item, int chance) {
  }
}
