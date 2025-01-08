package mysticmods.roots.recipe.pyre;

import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  private Ritual ritual;

  public PyreRecipe() {
    super();
  }

  public Ritual getRitual() {
    return ritual;
  }

  public void setRitual(Ritual ritual) {
    this.ritual = ritual;
  }


  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
    //return ModSerializers.PYRE.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.PYRE.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.PYRE_RECIPE_GROUP;
  }
}
