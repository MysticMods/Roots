package mysticmods.roots.api.recipe;

import mysticmods.roots.api.capability.Unlock;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class RootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> implements IRootsRecipe<W> {
  protected final BaseRecipeData data = new BaseRecipeData();

  public RootsRecipe() {
  }

  public RootsRecipe(BaseRecipeData data) {
    this.data.updateFrom(data);
  }

  protected BaseRecipeData getData() {
    return data;
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
    this.data.ingredients = ingredients;
  }

  @Override
  public void setLevelConditions(List<LevelCondition> levelConditions) {
    this.data.levelConditions = levelConditions;
  }

  @Override
  public void setPlayerConditions(List<PlayerCondition> playerConditions) {
    this.data.playerConditions = playerConditions;
  }

  @Override
  public List<LevelCondition> getLevelConditions() {
    return this.data.levelConditions;
  }

  @Override
  public List<PlayerCondition> getPlayerConditions() {
    return this.data.playerConditions;
  }

  @Override
  public void setResultItem(ItemStack result) {
    this.data.result = result;
  }

  @Override
  public void setChanceOutputs(List<ChanceOutput> chanceOutputs) {
    this.data.chanceOutputs = chanceOutputs;
  }

  @Override
  public List<ChanceOutput> getChanceOutputs() {
    return this.data.chanceOutputs;
  }

  public void setUnlocks(List<Unlock<?>> grants) {
    this.data.unlocks = grants;
  }

  @Override
  public List<Unlock<?>> getUnlocks() {
    return this.data.unlocks;
  }

  @Override
  public boolean matches(W arg, Level arg2) {
    List<ItemStack> inputs = new ArrayList<>();
    for (int i = 0; i < arg.size(); i++) {
      ItemStack stack = arg.getItem(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }
    return RecipeMatcher.findMatches(inputs, getIngredients()) != null;
  }

  @Override
  public ItemStack assemble(W arg, HolderLookup.Provider arg2) {
    Player player = arg.getPlayer();
    if (player != null) {
      for (Unlock<?> unlock : getUnlocks()) {
        // TODO: Do unlocks
      }
    }

    return getResultItem(arg2).copy();
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return true;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider arg) {
    if (data.result == null) {
      return ItemStack.EMPTY;
    }
    return data.result;
  }
}
