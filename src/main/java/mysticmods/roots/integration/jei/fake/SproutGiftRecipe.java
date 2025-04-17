package mysticmods.roots.integration.jei.fake;

import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record SproutGiftRecipe(EntityType<?> sprout, List<ChanceOutput> outputs) {
  public static List<SproutGiftRecipe> getRecipes() {
    Map<EntityType<?>, Set<Gift>> aggregate = new HashMap<>();

  }

  private record Gift (Item item, int chance) {

  }
}
