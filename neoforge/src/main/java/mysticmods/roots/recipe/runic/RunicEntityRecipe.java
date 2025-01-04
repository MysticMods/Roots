package mysticmods.roots.recipe.runic;

import mysticmods.roots.api.recipe.EntityRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RunicEntityRecipe extends EntityRecipe<RunicEntityCrafting> {
  private int cooldown;
  private int durabilityCost = 1;

  public RunicEntityRecipe() {
    super();
  }

  @Override
  public void modifyEntity(RunicEntityCrafting pContainer, HolderLookup.Provider provider) {

  }

  public int getCooldown() {
    return cooldown;
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }

  public void setDurabilityCost(int durabilityCost) {
    this.durabilityCost = durabilityCost;
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
    //return ModSerializers.RUNIC_ENTITY.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.RUNIC_ENTITY.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.RUNIC_ENTITY_RECIPE_GROUP;
  }
}
