package mysticmods.roots.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import noobanidus.libs.noobutil.advancement.IGenericPredicate;

import javax.annotation.Nullable;

public class CraftingPredicate implements IGenericPredicate<GroveRecipe> {
  private GroveRecipe recipe;

  public CraftingPredicate() {
  }

  public CraftingPredicate(GroveRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public boolean test(ServerPlayer player, GroveRecipe condition) {
    return recipe.equals(condition);
  }

  @Override
  public IGenericPredicate<GroveRecipe> deserialize(@Nullable JsonElement element) {
    if (element != null) {
      JsonObject object = element.getAsJsonObject();
      if (GsonHelper.isStringValue(object, "recipe")) {
        ResourceLocation rl = new ResourceLocation(GsonHelper.getAsString(object, "recipe"));
        GroveRecipe recipe = ResolvedRecipes.GROVE.getRecipe(rl);
        if (recipe != null) {
          return new CraftingPredicate(recipe);
        } else {
          throw new JsonSyntaxException("Unknown recipe name: '" + rl + "'");
        }
      }
    }
    return new CraftingPredicate(null);
  }
}
