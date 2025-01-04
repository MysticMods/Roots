package mysticmods.roots.recipe.mortar;

import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

// TODO: Mixed Mortar Recipe?
public class MortarRecipe extends RootsTileRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  private int times;

  public MortarRecipe() {
    super();
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    //return ModSerializers.MORTAR.get();
    return null;
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.MORTAR.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.MORTAR_RECIPE_GROUP;
  }
}
