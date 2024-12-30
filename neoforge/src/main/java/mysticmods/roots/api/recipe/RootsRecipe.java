package mysticmods.roots.api.recipe;

import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.RootsCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class RootsRecipe<H extends IItemHandler, W extends RootsCrafting<H>> implements IRootsRecipe<W> {
  protected NonNullList<Ingredient> ingredients = NonNullList.create();
  protected List<LevelCondition> levelConditions = new ArrayList<>();
  protected List<PlayerCondition> playerConditions = new ArrayList<>();
  protected ItemStack result;
  protected List<ChanceOutput> chanceOutputs = new ArrayList<>();
  protected List<Grant> grants = new ArrayList<>();

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  @Override
  public void setLevelConditions(List<LevelCondition> levelConditions) {
    this.levelConditions = levelConditions;
  }

  @Override
  public void setPlayerConditions(List<PlayerCondition> playerConditions) {
    this.playerConditions = playerConditions;
  }

  @Override
  public List<LevelCondition> getLevelConditions() {
    return this.levelConditions;
  }

  @Override
  public List<PlayerCondition> getPlayerConditions() {
    return this.playerConditions;
  }

  @Override
  public void setResultItem(ItemStack result) {
    this.result = result;
  }

  @Override
  public void setChanceOutputs(List<ChanceOutput> chanceOutputs) {
    this.chanceOutputs = chanceOutputs;
  }

  @Override
  public List<ChanceOutput> getChanceOutputs() {
    return this.chanceOutputs;
  }

  @Override
  public void setGrants(List<Grant> grants) {
    this.grants = grants;
  }

  @Override
  public List<Grant> getGrants() {
    return this.grants;
  }

  @Override
  public boolean matches(W arg, Level arg2) {
    return false;
  }

  @Override
  public ItemStack assemble(W arg, HolderLookup.Provider arg2) {


    return null;
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return false;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider arg) {
    return null;
  }
}
