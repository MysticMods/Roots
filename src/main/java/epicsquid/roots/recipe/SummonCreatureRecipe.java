package epicsquid.roots.recipe;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SummonCreatureRecipe extends RegistryItem {
  private Class<? extends Entity> clazz;
  private List<Ingredient> ingredients;

  public SummonCreatureRecipe (ResourceLocation resource, Class<? extends Entity> clazz, Ingredient ... ingredients) {
    this.setRegistryName(resource);
    this.clazz = clazz;
    this.ingredients = Lists.newArrayList(ingredients);
  }

  public Class<? extends Entity> getClazz() {
    return clazz;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public boolean matches (List<ItemStack> values) {
    return ListUtil.matchesIngredients(values, getIngredients());
  }
}
